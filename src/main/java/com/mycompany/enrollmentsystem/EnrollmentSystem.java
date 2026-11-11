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

    public void createConflictProcedure() {
        try {
            st.execute("DROP PROCEDURE IF EXISTS checkScheduleConflict");
            st.execute(
                "CREATE PROCEDURE checkScheduleConflict(IN p_role VARCHAR(10), IN p_id INT, IN p_subjid INT) " +
                "SQL SECURITY INVOKER " +
                "BEGIN " +
                "    DECLARE v_time VARCHAR(50); " +
                "    SELECT SUBSTRING_INDEX(SUBSTRING_INDEX(REPLACE(TRIM(subjsched), ' - ', '-'), ' ', -1), '-', -2) " +
                "    INTO v_time FROM subjects WHERE subjid = p_subjid; " +
                "    IF p_role = 'student' THEN " +
                "        SELECT s.subjid, s.subjcode, s.subjsched FROM enroll e " +
                "        INNER JOIN subjects s ON e.subjid = s.subjid " +
                "        WHERE e.studid = p_id AND v_time != '' " +
                "          AND SUBSTRING_INDEX(SUBSTRING_INDEX(REPLACE(TRIM(s.subjsched), ' - ', '-'), ' ', -1), '-', -2) = v_time; " +
                "    ELSE " +
                "        SELECT s.subjid, s.subjcode, s.subjsched FROM assign a " +
                "        INNER JOIN subjects s ON a.SubjID = s.subjid " +
                "        WHERE a.TID = p_id AND v_time != '' " +
                "          AND SUBSTRING_INDEX(SUBSTRING_INDEX(REPLACE(TRIM(s.subjsched), ' - ', '-'), ' ', -1), '-', -2) = v_time; " +
                "    END IF; " +
                "END"
            );
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    public String checkScheduleConflict(String role, int id, int subjid) {
        DBConnect();
        try {
            try {
                rs = st.executeQuery("CALL checkScheduleConflict('" + role + "', " + id + ", " + subjid + ")");
            } catch (Exception missingProc) {
                createConflictProcedure();
                rs = st.executeQuery("CALL checkScheduleConflict('" + role + "', " + id + ", " + subjid + ")");
            }
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
    