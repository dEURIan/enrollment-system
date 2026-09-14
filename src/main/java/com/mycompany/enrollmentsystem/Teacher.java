/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */
package com.mycompany.enrollmentsystem;

/**
 *
 * @author euri
 */
public class Teacher {
        public void newteacher(String tname, String tdept, String tcontact){
        EnrollmentSystem b = new EnrollmentSystem();
        b.DBConnect();

        try{
            String query = "insert into teachers (tname, tdept, tcontact) values('" 
                    + tname + "', '" 
                    + tdept + "', '" 
                    + tcontact + "')";   
            int update = b.st.executeUpdate(query);
            System.out.println("Success with sql!");  

        }catch (Exception ex){
            System.out.print("not Success with sql!");
            ex.printStackTrace();
        }
    }

        public void deleteteacher(int tid){
            EnrollmentSystem b = new EnrollmentSystem();
            b.DBConnect();
            try{
                String query = "delete from teachers where tid = " + tid;
                b.st.executeUpdate(query);
                System.out.println("Deleted successfully!");
            }catch (Exception ex){
                System.out.println("not Success with sql!");
                ex.printStackTrace();
            }
        }

        public void updateteacher(int tid, String tname, String tdept, String tcontact){
            EnrollmentSystem b = new EnrollmentSystem();
            b.DBConnect();
            try{
                String query = "update teachers set tname = '" + tname 
                    + "', tdept = '" + tdept 
                    + "', tcontact = '" + tcontact
                    + "' where tid = " + tid;
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
