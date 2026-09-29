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

                if (tname.isEmpty()) {
                    for (String db : dbs) {
                        try {
                            b.rs = b.st.executeQuery("SELECT tname FROM `" + db + "`.teachers WHERE tid = " + tid);
                            if (b.rs.next()) {
                                tname = b.rs.getString("tname");
                                break;
                            }
                        } catch (Exception e) {
                        }
                    }
                }

                for (String db : dbs) {
                    try {
                        b.st.executeUpdate("DELETE FROM `" + db + "`.assign WHERE TID = " + tid);
                        b.st.executeUpdate("DELETE FROM `" + db + "`.teachers WHERE tid = " + tid);
                    } catch (Exception e) {
                    }
                }

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
