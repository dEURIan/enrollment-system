package com.mycompany.enrollmentsystem;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class StudentGrade extends javax.swing.JFrame {

    private JComboBox<String> termCombo;
    private JLabel infoLabel;
    private JButton viewBtn;

    public StudentGrade() {
        setTitle("Student Grade");
        setSize(420, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout(FlowLayout.CENTER, 15, 15));

        int userId = -1;
        try {
            userId = Integer.parseInt(EnrollmentSystem.currentUser.replaceAll("[^0-9]", ""));
        } catch (Exception e) {
        }

        infoLabel = new JLabel("Student ID: " + userId + " (" + EnrollmentSystem.currentUser + ")");
        infoLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        add(infoLabel);

        add(new JLabel("Select School Year:"));

        termCombo = new JComboBox<>();
        termCombo.setPreferredSize(new Dimension(220, 28));
        EnrollmentSystem b = new EnrollmentSystem();
        try {
            if (b.DBConnectUser(EnrollmentSystem.currentUser, EnrollmentSystem.currentPass, "")) {
                b.rs = b.st.executeQuery("SHOW DATABASES LIKE '%sy%'");
                while (b.rs.next()) {
                    termCombo.addItem(b.rs.getString(1));
                }
            }
        } catch (Exception ex) {
        }
        if (!EnrollmentSystem.currentDB.isEmpty()) {
            termCombo.setSelectedItem(EnrollmentSystem.currentDB);
        }
        add(termCombo);

        viewBtn = new JButton("View Grade Sheet (PDF)");
        viewBtn.setPreferredSize(new Dimension(220, 32));
        int finalUserId = userId;
        viewBtn.addActionListener(e -> {
            if (termCombo.getSelectedItem() != null) {
                new CreatePDF().generateGradeSheet(finalUserId, termCombo.getSelectedItem().toString());
            }
        });
        add(viewBtn);
    }

    public static void main(String[] args) {
        new StudentGrade().setVisible(true);
    }
}
