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
            JOptionPane.showMessageDialog(null, "Student added successfully!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error adding student: " + ex.getMessage());
        }
    }
    
    public void deleteStudent(int studid){
        EnrollmentSystem b = new EnrollmentSystem();
        b.DBConnect();
        try{
            String query = "delete from students where studid = " + studid;
            b.st.executeUpdate(query);
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
