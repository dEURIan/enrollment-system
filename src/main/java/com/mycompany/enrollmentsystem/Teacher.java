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
            int newid = 300;
            String idQuery = "SELECT tid FROM teachers ORDER BY tid ASC";
            b.rs = b.st.executeQuery(idQuery);
            while (b.rs.next()) {
                if (b.rs.getInt("tid") == newid) {
                    newid++;
                } else {
                    break;
                }
            }
            String query = "insert into teachers (tid, tname, tdept, tcontact) values(" 
                    + newid + ", '" 
                    + tname + "', '" 
                    + tdept + "', '" 
                    + tcontact + "')";   
            b.st.executeUpdate(query);
            String username = newid + tname;
            String password = String.valueOf(newid);
            try {
                b.st.executeUpdate("CREATE USER IF NOT EXISTS '" + username + "'@'localhost' IDENTIFIED BY '" + password + "'");
                b.st.executeUpdate("GRANT SELECT ON `" + EnrollmentSystem.currentDB + "`.* TO '" + username + "'@'localhost'");
                b.st.executeUpdate("FLUSH PRIVILEGES");
            } catch (Exception e) {
            }
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
                String nameQuery = "select tname from teachers where tid = " + tid;
                b.rs = b.st.executeQuery(nameQuery);
                String tname = "";
                if (b.rs.next()) {
                    tname = b.rs.getString("tname");
                }
                String query = "delete from teachers where tid = " + tid;
                b.st.executeUpdate(query);
                if (tname != null && !tname.isEmpty()) {
                    String username = tid + tname;
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
