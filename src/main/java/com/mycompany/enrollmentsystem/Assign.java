/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.enrollmentsystem;

/**
 *
 * @author Euri
 */
public class Assign extends EnrollmentSystem {
    static int subjid;
    public static int getSubjid() {
        if (subjid != 0) return subjid;
        return Enroll.getSubjid(); // Uses the subject selected in SubjectsFrame
    }
    public static void setSubjid(int subjid) {
        Assign.subjid = subjid;
    }
    
    public String assignTeacher(int tid){
       DBConnect(); 
       String assignQuery = "insert into assign(SubjID, TID) values(" + getSubjid() + ", " + tid + ")";
       try{
           st.executeUpdate(assignQuery);
       }catch(Exception ex){
           System.out.println("failed to insert: " + ex);
       }
       return "Teacher " + tid + " assigned to Subject " + getSubjid();
    }
    public String unassignTeacher(int tid, int subjid){
       DBConnect(); 
       String unassignQuery = "delete from assign where TID = " + tid + " and SubjID = " + subjid;
       try{
           st.executeUpdate(unassignQuery);
       }catch(Exception ex){
           System.out.println("failed to delete: " + ex);
       }
       return "Subject " + subjid + " unassigned from Teacher " + tid;
    }
}
