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
        String query = "INSERT INTO students (studname, studadd, studcrs, studgender, yrlvl) "
                     + "VALUES ('" + studname + "', '" + studadd + "', '" + studcrs + "', '" + studgender + "', '" + yrlvl + "')";
        try {
            b.st.executeUpdate(query);
            b.rs = b.st.executeQuery("SELECT LAST_INSERT_ID()");
            int newid = 0;
            if (b.rs.next()) {
                newid = b.rs.getInt(1);
            }
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
            String query = "delete from students where studid = " + studid;
            b.st.executeUpdate(query);
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
