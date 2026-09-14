/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.enrollmentsystem;

import java.sql.DriverManager;

/**
 *
 * @author Euri
 */
public class Enroll {
    static int subjid;

    public static int getSubjid() {
        return subjid;
    }

    public static void setSubjid(int subjid) {
        Enroll.subjid = subjid;
    }
    
    public String enrollStud(int studid){
       DBConnect(); 
       String enrollQuery = "";
    }
    
        public boolean DBConnect(){
 
       try{

            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(
            "jdbc:mysql://localhost:3306/enrollmentsystem?zeroDateTimeBehavior=CONVERT_TO_NULL&useSSL=false&allowPublicKeyRetrieval=true",
             "root", "root"); //(db, user, pass)

            st = con.createStatement();  

            System.out.println("Connected to database!");

        }catch (Exception ex) {
       
            System.out.print(ex);  
            
            System.out.println("Connection failed");
            return false;
        }
         return true;

    }
}
