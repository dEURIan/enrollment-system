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
    
    public String newdb (String term){
        DBConnect();
        int year = Calendar.getInstance().get(Calendar.YEAR);
        String schyear = "SY" + year+"_"+(year+1);
        String query = "create database if not exists " + term;
        try{
            st.executeUpdate(query);
        }catch(Exception ex){
            System.out.println(ex);
        }
        return term + "_" + schyear;
    }
    
    public static void main(String[] args) { 
      StudentsForm a = new StudentsForm();
      a.setVisible(true);
      a.showRecords();      
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
}
    