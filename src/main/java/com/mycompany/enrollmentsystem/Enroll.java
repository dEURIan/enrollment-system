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
public class Enroll extends EnrollmentSystem{
    static int subjid;

    public static int getSubjid() {
        return subjid;
    }

    public static void setSubjid(int subjid) {
        Enroll.subjid = subjid;
    }
    
    public String enrollStud(int studid){
       DBConnect(); 
       String enrollQuery = "insert into enroll(studid, subjid, evaluation) values(" + studid + ", " + subjid + ", '')";
       try{
           st.executeUpdate(enrollQuery);
       }catch(Exception ex){
           System.out.println("failed to insert" + ex);
       }
       return "Student " + studid + " enrolled to " + subjid;
    }
    
    public String dropStud(int studid, int subjid){
       DBConnect(); 
       String dropQuery = "delete from enroll where studid = " + studid + " and subjid = " + subjid;
       try{
           st.executeUpdate(dropQuery);
       }catch(Exception ex){
           System.out.println("failed to drop: " + ex);
       }
       return "Student " + studid + " dropped from subject " + subjid;
    }
   
}
