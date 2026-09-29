/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.enrollmentsystem;

import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author ualfante
 */
public class Students {
    public void newstudent(String studname, String studadd, String studcrs, String studgender, String yrlvl) {
        EnrollmentSystem b = new EnrollmentSystem();
        b.DBConnect();
        try {
            int newid = 100;
            String idQuery = "SELECT studid FROM students ORDER BY studid ASC";
            b.rs = b.st.executeQuery(idQuery);
            while (b.rs.next()) {
                if (b.rs.getInt("studid") == newid) {
                    newid++;
                } else {
                    break;
                }
            }
            String query = "INSERT INTO students (studid, studname, studadd, studcrs, studgender, yrlvl) "
                         + "VALUES (" + newid + ", '" + studname + "', '" + studadd + "', '" + studcrs + "', '" + studgender + "', '" + yrlvl + "')";
            b.st.executeUpdate(query);
            String username = newid + studname;
            String password = String.valueOf(newid);
            try {
                b.st.executeUpdate("CREATE USER IF NOT EXISTS '" + username + "'@'localhost' IDENTIFIED BY '" + password + "'");
                b.st.executeUpdate("GRANT SELECT ON `" + EnrollmentSystem.currentDB + "`.* TO '" + username + "'@'localhost'");
                b.st.executeUpdate("FLUSH PRIVILEGES");
            } catch (Exception e) {
            }
            JOptionPane.showMessageDialog(null, "Student added successfully!\nUsername: " + username + "\nPassword: " + password);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error adding student: " + ex.getMessage());
        }
    }
    
    public void deleteStudent(int studid){
        EnrollmentSystem b = new EnrollmentSystem();
        b.DBConnect();
        try{
            String nameQuery = "select studname from students where studid = " + studid;
            b.rs = b.st.executeQuery(nameQuery);
            String studname = "";
            if (b.rs.next()) {
                studname = b.rs.getString("studname");
            }

            java.util.List<String> dbs = new java.util.ArrayList<>();
            try {
                b.rs = b.st.executeQuery("SHOW DATABASES LIKE '%_SY%'");
                while (b.rs.next()) {
                    dbs.add(b.rs.getString(1));
                }
            } catch (Exception e) {
            }
            if (dbs.isEmpty()) {
                int year = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR);
                String schyear = "SY" + year + "_" + (year + 1);
                String[] terms = {"1st", "2nd", "Summer"};
                for (String term : terms) {
                    dbs.add(term + "_" + schyear);
                }
            }
            if (!dbs.contains(EnrollmentSystem.currentDB) && !EnrollmentSystem.currentDB.isEmpty()) {
                dbs.add(EnrollmentSystem.currentDB);
            }

            if (studname.isEmpty()) {
                for (String db : dbs) {
                    try {
                        b.rs = b.st.executeQuery("SELECT studname FROM `" + db + "`.students WHERE studid = " + studid);
                        if (b.rs.next()) {
                            studname = b.rs.getString("studname");
                            break;
                        }
                    } catch (Exception e) {
                    }
                }
            }

            for (String db : dbs) {
                try {
                    b.st.executeUpdate("DELETE FROM `" + db + "`.grades WHERE enroll_eid IN (SELECT eid FROM `" + db + "`.enroll WHERE studid = " + studid + ")");
                    b.st.executeUpdate("DELETE FROM `" + db + "`.enroll WHERE studid = " + studid);
                    b.st.executeUpdate("DELETE FROM `" + db + "`.students WHERE studid = " + studid);
                } catch (Exception e) {
                }
            }

            if (studname != null && !studname.isEmpty()) {
                String username = studid + studname;
                try {
                    b.st.executeUpdate("DROP USER IF EXISTS '" + username + "'@'localhost'");
                    b.st.executeUpdate("FLUSH PRIVILEGES");
                } catch (Exception e) {
                }
            }
            System.out.println("Deleted successfully!");
        }catch (Exception ex){
            System.out.println("not Success with sql!");
            ex.printStackTrace();
        }
    }
    
    public void updateStudent(int studid, String studname, String studadd, String studcrs, String yrlvl){
        EnrollmentSystem b = new EnrollmentSystem();
        b.DBConnect();
        try{
            String query = "update students set studname = '" + studname 
                + "', studadd = '" + studadd 
                + "', studcrs = '" + studcrs
                + "', yrlvl = '" + yrlvl 
                + "' where studid = " + studid;
            int rows = b.st.executeUpdate(query);
            if (rows > 0) {
                System.out.println("Updated successfully!");
            } else {
                System.out.println("No matching student found!");
            }
        }catch (Exception ex){
            System.out.println("not Success with sql!");
            ex.printStackTrace();
        }
    }
}
