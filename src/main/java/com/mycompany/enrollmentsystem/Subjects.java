/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */
package com.mycompany.enrollmentsystem;

/**
 *
 * @author euri
 */
public class Subjects {
    public void newsubject(String subjcode, String subjdesc, String subjunit, String subjsched){
        EnrollmentSystem b = new EnrollmentSystem();
        b.DBConnect();
        
        try{
            int newid = 200;
            String idQuery = "SELECT subjid FROM subjects ORDER BY subjid ASC";
            b.rs = b.st.executeQuery(idQuery);
            while (b.rs.next()) {
                if (b.rs.getInt("subjid") == newid) {
                    newid++;
                } else {
                    break;
                }
            }
            String query = "insert into subjects (subjid, subjcode, subjdesc, subjunits, subjsched) values(" 
                    + newid + ", '" 
                    + subjcode + "', '" 
                    + subjdesc + "', '" 
                    + subjunit + "', '"
                    + subjsched + "')";   
            int update = b.st.executeUpdate(query);
            System.out.println("Success with sql!");  
            
        }catch (Exception ex){
            System.out.print("not Success with sql!");
            ex.printStackTrace();
        }
    }
    
    public void deleteSubjects(int subjid){
        EnrollmentSystem b = new EnrollmentSystem();
        b.DBConnect();
        try{
            String query = "delete from subjects where subjid = " + subjid;
            b.st.executeUpdate(query);
            System.out.println("Deleted successfully!");
        }catch (Exception ex){
            System.out.println("not Success with sql!");
            ex.printStackTrace();
        }
    }
    
    public void updateSubjects(int subjid, String subjcode, String subjdesc, String subjunit, String subjsched){
        EnrollmentSystem b = new EnrollmentSystem();
        b.DBConnect();
        try{
            String query = "update subjects set subjcode = '" + subjcode 
                + "', subjdesc = '" + subjdesc 
                + "', subjunits = '" + subjunit
                + "', subjsched = '" + subjsched 
                + "' where subjid = " + subjid;
            int rows = b.st.executeUpdate(query);
            if (rows > 0) {
                System.out.println("Updated successfully!");
            } else {
                System.out.println("No matching subject found!");
            }
        }catch (Exception ex){
            System.out.println("not Success with sql!");
            ex.printStackTrace();
        }
    }
}
