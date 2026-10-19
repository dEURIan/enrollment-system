/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.enrollmentsystem;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Euri
 */
public class GradesForm extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(GradesForm.class.getName());
    String currentSubjId;

    /**
     * Creates new form GradesForm
     */
    public GradesForm() {
        initComponents();
        jLabel1.setText("Teacher Grades Encoding Form: " + EnrollmentSystem.currentDB);
        setTitle("Teacher Grades Encoding Form: " + EnrollmentSystem.currentDB);
        showSubjectRecords();

        String[] gradeOptions = {"", "A", "B+", "B", "C+", "C", "D", "F", "FD"};
        Prelim.setModel(new javax.swing.DefaultComboBoxModel<>(gradeOptions));
        Midterms.setModel(new javax.swing.DefaultComboBoxModel<>(gradeOptions));
        PreFinal.setModel(new javax.swing.DefaultComboBoxModel<>(gradeOptions));
        Final.setModel(new javax.swing.DefaultComboBoxModel<>(gradeOptions));

        SubjectTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                SubjectTableMouseClicked(evt);
            }
        });

        GradesTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                GradesTableMouseClicked(evt);
            }
        });

        setLocationRelativeTo(null);
    }

    public void showSubjectRecords() {
        DefaultTableModel tblmodel = (DefaultTableModel) SubjectTable.getModel();
        tblmodel.setRowCount(0);
        EnrollmentSystem b = new EnrollmentSystem();
        b.DBConnect();

        try {
            String teacherNum = EnrollmentSystem.currentUser.replaceAll("[^0-9]", "");
            String query;
            if (!teacherNum.isEmpty()) {
                int tid = Integer.parseInt(teacherNum);
                query = "SELECT subjid, subjcode, subjdesc, subjunits, subjsched, "
                      + "(SELECT COUNT(*) FROM enroll WHERE enroll.subjid = subjects.subjid) AS students "
                      + "FROM subjects WHERE subjid IN (SELECT SubjID FROM assign WHERE TID = " + tid + ")";
            } else {
                query = "SELECT subjid, subjcode, subjdesc, subjunits, subjsched, "
                      + "(SELECT COUNT(*) FROM enroll WHERE enroll.subjid = subjects.subjid) AS students "
                      + "FROM subjects";
            }
            b.rs = b.st.executeQuery(query);
            while (b.rs.next()) {
                String id = b.rs.getString("subjid");
                String code = b.rs.getString("subjcode");
                String desc = b.rs.getString("subjdesc");
                String units = b.rs.getString("subjunits");
                String sched = b.rs.getString("subjsched");
                String students = b.rs.getString("students");
                String[] items = {id, code, desc, units, sched, students};
                tblmodel.addRow(items);
            }
        } catch (Exception ex) {
            System.out.println("not Success with sql: " + ex);
        }
    }

    private void SubjectTableMouseClicked(java.awt.event.MouseEvent evt) {
        int[] selectRow = SubjectTable.getSelectedRows();
        if (selectRow.length > 0) {
            currentSubjId = SubjectTable.getValueAt(selectRow[0], 0).toString();
            showGrades(currentSubjId);
        }
    }

    private void GradesTableMouseClicked(java.awt.event.MouseEvent evt) {
        int[] selectRow = GradesTable.getSelectedRows();
        if (selectRow.length > 0) {
            String sid = GradesTable.getValueAt(selectRow[0], 0).toString();
            String sname = GradesTable.getValueAt(selectRow[0], 1).toString();
            StudID.setText(sid);
            StudName.setText(sname);

            String p = GradesTable.getValueAt(selectRow[0], 2) != null ? GradesTable.getValueAt(selectRow[0], 2).toString() : "";
            String m = GradesTable.getValueAt(selectRow[0], 3) != null ? GradesTable.getValueAt(selectRow[0], 3).toString() : "";
            String pf = GradesTable.getValueAt(selectRow[0], 4) != null ? GradesTable.getValueAt(selectRow[0], 4).toString() : "";
            String f = GradesTable.getValueAt(selectRow[0], 5) != null ? GradesTable.getValueAt(selectRow[0], 5).toString() : "";

            Prelim.setSelectedItem(p);
            Midterms.setSelectedItem(m);
            PreFinal.setSelectedItem(pf);
            Final.setSelectedItem(f);
        }
    }

    public void showGrades(String subjid) {
        currentSubjId = subjid;
        DefaultTableModel tblmodel = (DefaultTableModel) GradesTable.getModel();
        tblmodel.setRowCount(0);
        EnrollmentSystem b = new EnrollmentSystem();
        b.DBConnect();

        try {
            String query = "SELECT students.studid, students.studname, "
                         + "grades.prelim, grades.midterm, grades.prefinal, grades.final "
                         + "FROM enroll "
                         + "INNER JOIN students ON enroll.studid = students.studid "
                         + "LEFT JOIN grades ON enroll.eid = grades.enroll_eid "
                         + "WHERE enroll.subjid = " + subjid;
            b.rs = b.st.executeQuery(query);
            while (b.rs.next()) {
                String id = b.rs.getString("studid");
                String name = b.rs.getString("studname");
                String prelim = b.rs.getString("prelim");
                if (prelim == null) prelim = "";
                String midterm = b.rs.getString("midterm");
                if (midterm == null) midterm = "";
                String prefinal = b.rs.getString("prefinal");
                if (prefinal == null) prefinal = "";
                String finals = b.rs.getString("final");
                if (finals == null) finals = "";
                String[] items = {id, name, prelim, midterm, prefinal, finals};
                tblmodel.addRow(items);
            }
        } catch (Exception ex) {
            System.out.println("not Success with sql: " + ex);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        SubjectTable = new javax.swing.JTable();
        StudID = new javax.swing.JTextField();
        StudName = new javax.swing.JTextField();
        Prelim = new javax.swing.JComboBox<>();
        Midterms = new javax.swing.JComboBox<>();
        PreFinal = new javax.swing.JComboBox<>();
        Final = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        GradesTable = new javax.swing.JTable();
        SaveBTN = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel1.setText("Teacher Grades Encoding Form: ");

        SubjectTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Subject ID", "Subject Code", "Subject Desc", "Units", "Schedule", "Students"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Integer.class, java.lang.Object.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane1.setViewportView(SubjectTable);

        StudID.setEditable(false);
        StudID.addActionListener(this::StudIDActionPerformed);

        StudName.setEditable(false);

        Prelim.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "", "A", "B+", "B", "C+", "C", "D", "F", "FD" }));

        Midterms.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "", "A", "B+", "B", "C+", "C", "D", "F", "FD" }));

        PreFinal.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "", "A", "B+", "B", "C+", "C", "D", "F", "FD" }));

        Final.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "", "A", "B+", "B", "C+", "C", "D", "F", "FD" }));

        jLabel2.setText("StudID");

        jLabel3.setText("StudName");

        jLabel4.setText("Prelims");

        jLabel5.setText("Midterms");

        jLabel6.setText("PreFinal");

        jLabel7.setText("Finals");

        GradesTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "StudID", "StudName", "Prelims", "Midterms", "PreFinal", "Final"
            }
        ));
        jScrollPane2.setViewportView(GradesTable);

        SaveBTN.setText("Save");
        SaveBTN.addActionListener(this::SaveBTNActionPerformed);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(189, 189, 189)
                        .addComponent(jLabel1))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(73, 73, 73)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane1)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel2)
                                        .addGap(41, 41, 41)
                                        .addComponent(jLabel3))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(StudID, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(StudName, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(18, 18, 18)
                                        .addComponent(Prelim, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(Midterms, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(PreFinal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(Final, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(31, 31, 31)
                                        .addComponent(SaveBTN))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(35, 35, 35)
                                        .addComponent(jLabel4)
                                        .addGap(43, 43, 43)
                                        .addComponent(jLabel5)
                                        .addGap(47, 47, 47)
                                        .addComponent(jLabel6)
                                        .addGap(45, 45, 45)
                                        .addComponent(jLabel7))))
                            .addComponent(jScrollPane2))))
                .addContainerGap(17, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(StudID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(StudName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Prelim, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Midterms, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(PreFinal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Final, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(SaveBTN))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 281, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(56, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void StudIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_StudIDActionPerformed
    }//GEN-LAST:event_StudIDActionPerformed

    private void SaveBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SaveBTNActionPerformed
        String sid = StudID.getText().trim();
        if (sid.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please select a student from the table first.");
            return;
        }
        if (currentSubjId == null || currentSubjId.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please select a subject first.");
            return;
        }

        String p = Prelim.getSelectedItem() != null ? Prelim.getSelectedItem().toString() : "";
        String m = Midterms.getSelectedItem() != null ? Midterms.getSelectedItem().toString() : "";
        String pf = PreFinal.getSelectedItem() != null ? PreFinal.getSelectedItem().toString() : "";
        String f = Final.getSelectedItem() != null ? Final.getSelectedItem().toString() : "";

        EnrollmentSystem b = new EnrollmentSystem();
        b.DBConnect();

        try {
            String eidQuery = "SELECT eid FROM enroll WHERE studid = " + sid + " AND subjid = " + currentSubjId;
            b.rs = b.st.executeQuery(eidQuery);
            if (b.rs.next()) {
                int eid = b.rs.getInt("eid");
                String checkQuery = "SELECT COUNT(*) FROM grades WHERE enroll_eid = " + eid;
                b.rs = b.st.executeQuery(checkQuery);
                boolean exists = false;
                if (b.rs.next() && b.rs.getInt(1) > 0) {
                    exists = true;
                }
                if (exists) {
                    String updateQuery = "UPDATE grades SET prelim = '" + p + "', midterm = '" + m + "', prefinal = '" + pf + "', final = '" + f + "' WHERE enroll_eid = " + eid;
                    b.st.executeUpdate(updateQuery);
                } else {
                    String insertQuery = "INSERT INTO grades (enroll_eid, prelim, midterm, prefinal, final) VALUES (" + eid + ", '" + p + "', '" + m + "', '" + pf + "', '" + f + "')";
                    b.st.executeUpdate(insertQuery);
                }
                JOptionPane.showMessageDialog(null, "Grade saved successfully!");
                showGrades(currentSubjId);
            }
        } catch (Exception ex) {
            System.out.println("not Success with sql: " + ex);
        }
    }//GEN-LAST:event_SaveBTNActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
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
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new GradesForm().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> Final;
    private javax.swing.JTable GradesTable;
    private javax.swing.JComboBox<String> Midterms;
    private javax.swing.JComboBox<String> PreFinal;
    private javax.swing.JComboBox<String> Prelim;
    private javax.swing.JButton SaveBTN;
    private javax.swing.JTextField StudID;
    private javax.swing.JTextField StudName;
    private javax.swing.JTable SubjectTable;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    // End of variables declaration//GEN-END:variables
}
