package com.mycompany.enrollmentsystem;

import java.util.Calendar;
import javax.swing.JOptionPane;

public class Login extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Login.class.getName());

    public Login() {
        initComponents();
        Password.setText("");
        jComboBox1.removeAllItems();
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        Username = new javax.swing.JTextField();
        Password = new javax.swing.JPasswordField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        Login = new javax.swing.JButton();
        jComboBox1 = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        Submit = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 18));
        jLabel1.setText("Welcome!");

        Username.addActionListener(this::UsernameActionPerformed);

        Password.setText("jPasswordField1");

        jLabel2.setText("Username");

        jLabel3.setText("Password");

        Login.setText("Login");
        Login.addActionListener(this::LoginActionPerformed);

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox1.addActionListener(this::jComboBox1ActionPerformed);

        jLabel4.setText("School year");

        Submit.setText("Submit");
        Submit.addActionListener(this::SubmitActionPerformed);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(Login, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(125, 125, 125))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(71, 71, 71)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(Password)
                                .addComponent(Username, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(Submit, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(110, 110, 110))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addComponent(jLabel1)
                .addGap(65, 65, 65)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Username, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Password, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(Login)
                .addGap(41, 41, 41)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Submit)
                .addContainerGap(111, Short.MAX_VALUE))
        );

        pack();
    }

    private void UsernameActionPerformed(java.awt.event.ActionEvent evt) {
    }

    public boolean checkLogin(String user, String pass) {
        EnrollmentSystem b = new EnrollmentSystem();
        return b.DBConnectUser(user, pass, "");
    }

    private void LoginActionPerformed(java.awt.event.ActionEvent evt) {
        String user = Username.getText().trim();
        String pass = new String(Password.getPassword()).trim();
        if (user.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter username and password.", "Login", JOptionPane.WARNING_MESSAGE);
            return;
        }
        jComboBox1.removeAllItems();
        boolean loginSuccess = false;
        EnrollmentSystem test = new EnrollmentSystem();
        if (!test.DBConnectUser(user, pass, "")) {
            JOptionPane.showMessageDialog(this, "Invalid username or password!", "Login Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            test.rs = test.st.executeQuery("SHOW DATABASES LIKE '%sy%'");
            while (test.rs.next()) {
                loginSuccess = true;
                jComboBox1.addItem(test.rs.getString(1));
            }
        } catch (Exception e) {
        }
        if (loginSuccess) {
            JOptionPane.showMessageDialog(this, "Login successful! Please select a database and click Submit.", "Login", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "No databases accessible for this user!", "Login", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void SubmitActionPerformed(java.awt.event.ActionEvent evt) {
        if (jComboBox1.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Please click Login first to check accessible databases!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String user = Username.getText().trim();
        String pass = new String(Password.getPassword()).trim();
        String selected = jComboBox1.getSelectedItem().toString();
        String dbName = selected;
        EnrollmentSystem.currentDB = dbName;
        EnrollmentSystem.currentUser = user;
        EnrollmentSystem.currentPass = pass;
        EnrollmentSystem b = new EnrollmentSystem();
        if (b.DBConnectUser(user, pass, dbName)) {
            int userId = -1;
            try {
                userId = Integer.parseInt(user.replaceAll("[^0-9]", ""));
            } catch (Exception e) {
            }

            boolean isAdmin = false;
            if (userId < 100) {
                try {
                    b.rs = b.st.executeQuery("SHOW GRANTS");
                    while (b.rs.next()) {
                        String grant = b.rs.getString(1).toUpperCase();
                        if (grant.contains("ALL PRIVILEGES") || grant.contains("INSERT") || grant.contains("CREATE") || grant.contains("GRANT OPTION")) {
                            isAdmin = true;
                            break;
                        }
                    }
                } catch (Exception e) {
                }
            }

            if (isAdmin) {
                EnrollmentSystem.userRole = "admin";
                StudentsForm a = new StudentsForm();
                a.setVisible(true);
                a.showRecords();
                this.dispose();
            } else if (userId >= 100 && userId < 300) {
                EnrollmentSystem.userRole = "student";
                CreatePDF pdf = new CreatePDF();
                pdf.generateGradeSheet(userId, dbName);
                StudentGrade a = new StudentGrade();
                a.setVisible(true);
                this.dispose();
            } else if (userId >= 300) {
                EnrollmentSystem.userRole = "teacher";
                GradesForm a = new GradesForm();
                a.setSize(750, 650);
                a.setLocationRelativeTo(null);
                a.setVisible(true);
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(null, "Unknown user role!", "Login Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Connection failed to " + dbName, "Login Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {
    }

    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(() -> new Login().setVisible(true));
    }

    private javax.swing.JButton Login;
    private javax.swing.JPasswordField Password;
    private javax.swing.JButton Submit;
    private javax.swing.JTextField Username;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
}
