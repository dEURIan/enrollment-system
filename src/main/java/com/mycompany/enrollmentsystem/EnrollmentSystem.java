/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.enrollmentsystem;

/**
 *
 * @author ualfante
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Calendar;

public class EnrollmentSystem {
    
    Connection con;

    Statement st;

    static ResultSet rs; 
    public static String currentDB = ""; 
    public static String userRole = "admin"; 
    public static String currentUser = "";
    public static String currentPass = ""; 
    
    public String newdb (String term){
        DBConnect();
        int year = Calendar.getInstance().get(Calendar.YEAR);
        String schyear = "SY" + year+"_"+(year+1);
        try{
            String query = "create database if not exists " + term + "_" + schyear;
            st.executeUpdate(query);
            
            String query2 = "use " + term +"_" + schyear;
            st.executeUpdate(query2);
            
            st.executeUpdate("CREATE TABLE IF NOT EXISTS students (" +
                            "studid INT NOT NULL AUTO_INCREMENT," +
                            "studname VARCHAR(100) NOT NULL," +
                            "studadd VARCHAR(255) NULL," +
                            "studcrs VARCHAR(100) NULL," +
                            "studgender VARCHAR(20) NULL," +
                            "yrlvl VARCHAR(20) NULL," +
                            "PRIMARY KEY (studid)) AUTO_INCREMENT = 100;");

            st.executeUpdate("CREATE TABLE IF NOT EXISTS subjects (" +
                            "subjid INT NOT NULL AUTO_INCREMENT," +
                            "subjcode VARCHAR(50) NULL DEFAULT NULL," +
                            "subjdesc VARCHAR(255) NULL DEFAULT NULL," +
                            "subjunits INT NULL DEFAULT NULL," +
                            "subjsched VARCHAR(100) NULL," +
                            "PRIMARY KEY (subjid)) AUTO_INCREMENT = 200;");

            st.executeUpdate("CREATE TABLE IF NOT EXISTS teachers (" +
                            "tid INT NOT NULL AUTO_INCREMENT," +
                            "tname VARCHAR(100) NULL DEFAULT NULL," +
                            "tdept VARCHAR(100) NULL DEFAULT NULL," +
                            "tadd VARCHAR(255) NULL," +
                            "tcontact VARCHAR(50) NULL," +
                            "tstatus VARCHAR(50) NULL," +
                            "PRIMARY KEY (tid)) AUTO_INCREMENT = 300;");

            st.executeUpdate("CREATE TABLE IF NOT EXISTS assign (" +
                            "assignid INT NOT NULL AUTO_INCREMENT," +
                            "SubjID INT NOT NULL UNIQUE," +
                            "TID INT NOT NULL," +
                            "PRIMARY KEY (assignid)," +
                            "FOREIGN KEY (SubjID) REFERENCES subjects(subjid)," +
                            "FOREIGN KEY (TID) REFERENCES teachers(tid));");

            st.executeUpdate("CREATE TABLE IF NOT EXISTS enroll (" +
                            "eid INT NOT NULL AUTO_INCREMENT," +
                            "studid INT NULL DEFAULT NULL," +
                            "subjid INT NULL DEFAULT NULL," +
                            "evaluation VARCHAR(255) DEFAULT NULL," +
                            "PRIMARY KEY (eid)," +
                            "UNIQUE (studid, subjid)," +
                            "FOREIGN KEY (studid) REFERENCES students(studid)," +
                            "FOREIGN KEY (subjid) REFERENCES subjects(subjid));");

            st.executeUpdate("CREATE TABLE IF NOT EXISTS grades (" +
                            "gradeid INT NOT NULL AUTO_INCREMENT," +
                            "enroll_eid INT NOT NULL UNIQUE," +
                            "prelim VARCHAR(10) NULL DEFAULT NULL," +
                            "midterm VARCHAR(10) NULL DEFAULT NULL," +
                            "prefinal VARCHAR(10) NULL DEFAULT NULL," +
                            "final VARCHAR(10) NULL DEFAULT NULL," +
                            "PRIMARY KEY (gradeid)," +
                            "FOREIGN KEY (enroll_eid) REFERENCES enroll(eid));");
            createConflictProcedure();
        }catch(Exception ex){
            System.out.println(ex);
        }
        return term + "_" + schyear;
    }

    private String parseTimeSQL(String tokenExpr) {
        return "COALESCE(STR_TO_DATE(" + tokenExpr + ", '%h:%i%p'), STR_TO_DATE(" + tokenExpr + ", '%H:%i'), STR_TO_DATE(" + tokenExpr + ", '%h%p'), STR_TO_DATE(" + tokenExpr + ", '%H'))";
    }

    public void createConflictProcedure() {
        String timeRegex = "^[0-9]{1,2}(:[0-9]{2})?(AM|PM)?-[0-9]{1,2}(:[0-9]{2})?(AM|PM)?$";
        String cleanV = "REPLACE(REPLACE(REPLACE(UPPER(TRIM(subjsched)), ' AM', 'AM'), ' PM', 'PM'), ' - ', '-')";
        String cleanS = "REPLACE(REPLACE(REPLACE(UPPER(TRIM(s.subjsched)), ' AM', 'AM'), ' PM', 'PM'), ' - ', '-')";
        try {
            st.execute("DROP PROCEDURE IF EXISTS check_student_schedule");
            st.execute("DROP PROCEDURE IF EXISTS check_teacher_schedule");
            st.execute("DROP PROCEDURE IF EXISTS checkScheduleConflict");
            st.execute(
                "CREATE PROCEDURE checkScheduleConflict(IN p_role VARCHAR(10), IN p_id INT, IN p_subjid INT) " +
                "SQL SECURITY INVOKER " +
                "BEGIN " +
                "    DECLARE v_time VARCHAR(50); " +
                "    DECLARE v_days VARCHAR(50); " +
                "    DECLARE v_start TIME; " +
                "    DECLARE v_end TIME; " +
                "    SELECT SUBSTRING_INDEX(SUBSTRING_INDEX(" + cleanV + ", ' ', -1), '-', -2) " +
                "    INTO v_time FROM subjects WHERE subjid = p_subjid; " +
                "    SELECT REPLACE(REPLACE(REPLACE(TRIM(SUBSTRING_INDEX(" + cleanV + ", v_time, 1)), 'SAT', 'S'), 'SUN', 'U'), 'TH', 'R') " +
                "    INTO v_days FROM subjects WHERE subjid = p_subjid; " +
                "    IF v_time REGEXP '" + timeRegex + "' THEN " +
                "        SET v_start = " + parseTimeSQL("SUBSTRING_INDEX(v_time, '-', 1)") + "; " +
                "        IF HOUR(v_start) < 7 AND v_time NOT LIKE '%AM%' THEN SET v_start = ADDTIME(v_start, '12:00:00'); END IF; " +
                "        SET v_end = " + parseTimeSQL("SUBSTRING_INDEX(v_time, '-', -1)") + "; " +
                "        IF HOUR(v_end) < 7 AND v_time NOT LIKE '%AM%' THEN SET v_end = ADDTIME(v_end, '12:00:00'); END IF; " +
                "    END IF; " +
                "    SELECT subjid, subjcode, subjsched FROM ( " +
                "        SELECT subjid, subjcode, subjsched, s_time, " +
                "            REPLACE(REPLACE(REPLACE(TRIM(SUBSTRING_INDEX(s_clean, s_time, 1)), 'SAT', 'S'), 'SUN', 'U'), 'TH', 'R') AS s_days, " +
                "            CASE WHEN s_time REGEXP '" + timeRegex + "' THEN " + parseTimeSQL("SUBSTRING_INDEX(s_time, '-', 1)") + " END AS raw_start, " +
                "            CASE WHEN s_time REGEXP '" + timeRegex + "' THEN " + parseTimeSQL("SUBSTRING_INDEX(s_time, '-', -1)") + " END AS raw_end " +
                "        FROM ( " +
                "            SELECT s.subjid, s.subjcode, s.subjsched, " + cleanS + " AS s_clean, " +
                "                SUBSTRING_INDEX(SUBSTRING_INDEX(" + cleanS + ", ' ', -1), '-', -2) AS s_time " +
                "            FROM subjects s " +
                "            WHERE (p_role = 'student' AND s.subjid IN (SELECT subjid FROM enroll WHERE studid = p_id)) " +
                "               OR (p_role != 'student' AND s.subjid IN (SELECT SubjID FROM assign WHERE TID = p_id)) " +
                "        ) t0 " +
                "    ) t1 " +
                "    WHERE v_start IS NOT NULL AND v_end IS NOT NULL " +
                "      AND raw_start IS NOT NULL AND raw_end IS NOT NULL " +
                "      AND v_start < IF(HOUR(raw_end) < 7 AND s_time NOT LIKE '%AM%', ADDTIME(raw_end, '12:00:00'), raw_end) " +
                "      AND v_end > IF(HOUR(raw_start) < 7 AND s_time NOT LIKE '%AM%', ADDTIME(raw_start, '12:00:00'), raw_start) " +
                "      AND (v_days = '' OR s_days = '' OR " +
                "           (v_days LIKE '%M%' AND s_days LIKE '%M%') OR " +
                "           (v_days LIKE '%T%' AND s_days LIKE '%T%') OR " +
                "           (v_days LIKE '%W%' AND s_days LIKE '%W%') OR " +
                "           (v_days LIKE '%R%' AND s_days LIKE '%R%') OR " +
                "           (v_days LIKE '%F%' AND s_days LIKE '%F%') OR " +
                "           (v_days LIKE '%S%' AND s_days LIKE '%S%') OR " +
                "           (v_days LIKE '%U%' AND s_days LIKE '%U%')); " +
                "END"
            );
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    public String checkScheduleConflict(String role, int id, int subjid) {
        DBConnect();
        createConflictProcedure();
        try {
            rs = st.executeQuery("CALL checkScheduleConflict('" + role + "', " + id + ", " + subjid + ")");
            if (rs.next()) {
                return "Schedule conflict with " + rs.getString("subjcode") + " (" + rs.getString("subjsched") + ")";
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return null;
    }
    
    public static void main(String[] args) { 
      Login a = new Login();
      a.setVisible(true);       
    }
    
    public boolean DBConnect(){
 
       try{

            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(
            "jdbc:mysql://localhost:3306/" + currentDB + "?zeroDateTimeBehavior=CONVERT_TO_NULL&useSSL=false&allowPublicKeyRetrieval=true",
             currentUser, currentPass);

            st = con.createStatement();  

            System.out.println("Connected to database!");

        }catch (Exception ex) {
       
            System.out.print(ex);  
            
            System.out.println("Connection failed");
            return false;
        }
         return true;

    }
    
    public boolean isIdExists(String tableName, String idColumn, int id) {
    DBConnect();
    try {
        String query = "SELECT COUNT(*) FROM " + tableName + " WHERE " + idColumn + " = " + id;
        rs = st.executeQuery(query);
        if (rs.next()) {
            return rs.getInt(1) > 0;
        }
    } catch (Exception ex) {
        ex.printStackTrace();
    }
    return false;
}

    boolean DBConnectUser(String user, String pass, String dbName) {
            try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/" + dbName + "?zeroDateTimeBehavior=CONVERT_TO_NULL&useSSL=false&allowPublicKeyRetrieval=true",
                user,
                pass
            );
            st = con.createStatement();
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
}
    