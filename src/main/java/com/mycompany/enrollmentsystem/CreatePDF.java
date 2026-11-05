package com.mycompany.enrollmentsystem;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;

public class CreatePDF {

    private static final Font bfBold12 = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLACK);
    private static final Font bf12 = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.BLACK);

    public static void main(String[] args) {
        new CreatePDF().generateGradeSheet(100, EnrollmentSystem.currentDB.isEmpty() ? "1st_sy2026_2027" : EnrollmentSystem.currentDB);
    }

    public void generateGradeSheet(int studId, String dbName) {
        try {
            File folder = new File("C:\\docs");
            if (!folder.exists()) folder.mkdirs();
            File pdfFile = new File(folder, "report.pdf");

            Document doc = new Document();
            PdfWriter.getInstance(doc, new FileOutputStream(pdfFile));
            doc.open();

            PdfPTable header = new PdfPTable(new float[]{2f, 5f});
            header.setWidthPercentage(90f);
            header.getDefaultCell().setBorder(Rectangle.NO_BORDER);
            header.addCell(Image.getInstance("logo.png"));
            header.addCell(new Phrase("Pinoy Big Brother\nRegistrars Office\nStudent Grade Sheet", bfBold12));
            doc.add(header);

            EnrollmentSystem b = new EnrollmentSystem();
            b.DBConnectUser(EnrollmentSystem.currentUser, EnrollmentSystem.currentPass, dbName);
            String sName = "", yrlvl = "";
            b.rs = b.st.executeQuery("SELECT studname, yrlvl FROM students WHERE studid = " + studId);
            if (b.rs.next()) {
                sName = b.rs.getString("studname");
                yrlvl = b.rs.getString("yrlvl");
            }

            PdfPTable info = new PdfPTable(2);
            info.setWidthPercentage(90f);
            info.setSpacingBefore(10f);
            insertCell(info, "Student ID: " + studId, Element.ALIGN_LEFT, 1, bfBold12, 0, 255, 255, 255);
            insertCell(info, "School Year: " + dbName, Element.ALIGN_LEFT, 1, bfBold12, 0, 255, 255, 255);
            insertCell(info, "Student Name: " + sName, Element.ALIGN_LEFT, 1, bfBold12, 0, 255, 255, 255);
            insertCell(info, "Year Level: " + yrlvl, Element.ALIGN_LEFT, 1, bfBold12, 0, 255, 255, 255);
            doc.add(info);

            PdfPTable table = new PdfPTable(new float[]{2f, 3f, 2f, 2f, 2f, 2f});
            table.setWidthPercentage(90f);
            table.setSpacingBefore(10f);
            insertCell(table, "My Grades", Element.ALIGN_LEFT, 6, bfBold12, 1, 255, 255, 255);

            for (String h : new String[]{"Subject", "Description", "Prelim", "Midterm", "Prefinal", "Final"}) {
                insertCell(table, h, Element.ALIGN_CENTER, 1, bfBold12, 1, 255, 255, 255);
            }

            int count = 0;
            String q = "SELECT s.subjcode, s.subjdesc, g.prelim, g.midterm, g.prefinal, g.final "
                     + "FROM enroll e "
                     + "INNER JOIN subjects s ON e.subjid = s.subjid "
                     + "LEFT JOIN grades g ON e.eid = g.enroll_eid "
                     + "WHERE e.studid = " + studId;
            b.rs = b.st.executeQuery(q);
            while (b.rs.next()) {
                count++;
                insertCell(table, b.rs.getString(1), Element.ALIGN_CENTER, 1, bf12, 1, 255, 255, 255);
                insertCell(table, b.rs.getString(2), Element.ALIGN_LEFT, 1, bf12, 1, 255, 255, 255);
                insertCell(table, b.rs.getString(3) != null ? b.rs.getString(3) : "", Element.ALIGN_CENTER, 1, bf12, 1, 255, 255, 255);
                insertCell(table, b.rs.getString(4) != null ? b.rs.getString(4) : "", Element.ALIGN_CENTER, 1, bf12, 1, 255, 255, 255);
                insertCell(table, b.rs.getString(5) != null ? b.rs.getString(5) : "", Element.ALIGN_CENTER, 1, bf12, 1, 255, 255, 255);
                insertCell(table, b.rs.getString(6) != null ? b.rs.getString(6) : "", Element.ALIGN_CENTER, 1, bf12, 1, 255, 255, 255);
            }
            doc.add(table);

            PdfPTable footer = new PdfPTable(2);
            footer.setWidthPercentage(90f);
            footer.setSpacingBefore(15f);
            insertCell(footer, "Total Subjects Enrolled: " + count, Element.ALIGN_LEFT, 2, bfBold12, 0, 255, 255, 255);
            insertCell(footer, "Student Signature: __________________", Element.ALIGN_LEFT, 1, bf12, 0, 255, 255, 255);
            insertCell(footer, "Registrar: __________________", Element.ALIGN_RIGHT, 1, bf12, 0, 255, 255, 255);
            doc.add(footer);

            doc.close();
            Desktop.getDesktop().open(pdfFile);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error creating PDF: " + e.getMessage());
        }
    }

    public void generateTOR(int studId) {
        try {
            File folder = new File("C:\\docs");
            if (!folder.exists()) folder.mkdirs();
            File pdfFile = new File(folder, "report.pdf");

            Document doc = new Document();
            PdfWriter.getInstance(doc, new FileOutputStream(pdfFile));
            doc.open();

            PdfPTable header = new PdfPTable(new float[]{2f, 5f});
            header.setWidthPercentage(90f);
            header.getDefaultCell().setBorder(Rectangle.NO_BORDER);
            header.addCell(Image.getInstance("logo.png"));
            header.addCell(new Phrase("Pinoy Big Brother\nRegistrars Office\nOfficial Transcript of Records", bfBold12));
            doc.add(header);

            EnrollmentSystem b = new EnrollmentSystem();
            b.DBConnectUser(EnrollmentSystem.currentUser, EnrollmentSystem.currentPass, EnrollmentSystem.currentDB);
            String sName = "", sCrs = "", sYr = "";
            b.rs = b.st.executeQuery("SELECT studname, studcrs, yrlvl FROM students WHERE studid = " + studId);
            if (b.rs.next()) {
                sName = b.rs.getString(1);
                sCrs = b.rs.getString(2);
                sYr = b.rs.getString(3);
            }

            PdfPTable info = new PdfPTable(2);
            info.setWidthPercentage(90f);
            info.setSpacingBefore(10f);
            insertCell(info, "Student ID: " + studId, Element.ALIGN_LEFT, 1, bfBold12, 0, 255, 255, 255);
            insertCell(info, "Course: " + (sCrs != null ? sCrs : ""), Element.ALIGN_LEFT, 1, bfBold12, 0, 255, 255, 255);
            insertCell(info, "Student Name: " + sName, Element.ALIGN_LEFT, 1, bfBold12, 0, 255, 255, 255);
            insertCell(info, "Year Level: " + (sYr != null ? sYr : ""), Element.ALIGN_LEFT, 1, bfBold12, 0, 255, 255, 255);
            doc.add(info);

            List<String> dbs = new ArrayList<>();
            b.rs = b.st.executeQuery("SHOW DATABASES LIKE '%_sy%'");
            while (b.rs.next()) {
                dbs.add(b.rs.getString(1));
            }
            Collections.sort(dbs);

            int totalSubjects = 0;
            for (String db : dbs) {
                EnrollmentSystem semDb = new EnrollmentSystem();
                if (!semDb.DBConnectUser(EnrollmentSystem.currentUser, EnrollmentSystem.currentPass, db)) continue;
                String q = "SELECT s.subjcode, s.subjdesc, s.subjunits, g.prelim, g.midterm, g.prefinal, g.final "
                         + "FROM enroll e "
                         + "INNER JOIN subjects s ON e.subjid = s.subjid "
                         + "LEFT JOIN grades g ON e.eid = g.enroll_eid "
                         + "WHERE e.studid = " + studId;
                semDb.rs = semDb.st.executeQuery(q);

                PdfPTable semTable = new PdfPTable(new float[]{2f, 3f, 1f, 1.5f, 1.5f, 1.5f, 1.5f});
                semTable.setWidthPercentage(90f);
                semTable.setSpacingBefore(10f);
                insertCell(semTable, "Term: " + db, Element.ALIGN_LEFT, 7, bfBold12, 1, 240, 240, 240);

                for (String h : new String[]{"Subject", "Description", "Units", "Prelim", "Midterm", "Prefinal", "Final"}) {
                    insertCell(semTable, h, Element.ALIGN_CENTER, 1, bfBold12, 1, 255, 255, 255);
                }

                boolean hasRows = false;
                while (semDb.rs.next()) {
                    hasRows = true;
                    totalSubjects++;
                    insertCell(semTable, semDb.rs.getString(1), Element.ALIGN_CENTER, 1, bf12, 1, 255, 255, 255);
                    insertCell(semTable, semDb.rs.getString(2), Element.ALIGN_LEFT, 1, bf12, 1, 255, 255, 255);
                    insertCell(semTable, semDb.rs.getString(3), Element.ALIGN_CENTER, 1, bf12, 1, 255, 255, 255);
                    insertCell(semTable, semDb.rs.getString(4) != null ? semDb.rs.getString(4) : "", Element.ALIGN_CENTER, 1, bf12, 1, 255, 255, 255);
                    insertCell(semTable, semDb.rs.getString(5) != null ? semDb.rs.getString(5) : "", Element.ALIGN_CENTER, 1, bf12, 1, 255, 255, 255);
                    insertCell(semTable, semDb.rs.getString(6) != null ? semDb.rs.getString(6) : "", Element.ALIGN_CENTER, 1, bf12, 1, 255, 255, 255);
                    insertCell(semTable, semDb.rs.getString(7) != null ? semDb.rs.getString(7) : "", Element.ALIGN_CENTER, 1, bf12, 1, 255, 255, 255);
                }
                if (hasRows) doc.add(semTable);
            }

            PdfPTable torFooter = new PdfPTable(1);
            torFooter.setWidthPercentage(90f);
            torFooter.setSpacingBefore(12f);
            insertCell(torFooter, "Total Subjects Listed: " + totalSubjects, Element.ALIGN_LEFT, 1, bfBold12, 0, 255, 255, 255);
            insertCell(torFooter, "Grading System: 92-100 (A), 87-91 (B+), 81-86 (B), 76-80 (C+), 70-75 (C), 60-69 (D), Below 60 (F), FD (Failure due to Absences)", Element.ALIGN_LEFT, 1, bf12, 0, 255, 255, 255);
            insertCell(torFooter, "School Seal Validity: Valid only with the official seal of the University and signature of the Registrar.", Element.ALIGN_LEFT, 1, bf12, 0, 255, 255, 255);
            String dateStr = new SimpleDateFormat("MMMM dd, yyyy").format(new Date());
            insertCell(torFooter, "Date Issued: " + dateStr, Element.ALIGN_LEFT, 1, bf12, 0, 255, 255, 255);
            insertCell(torFooter, "Registrar: ____________________________________", Element.ALIGN_RIGHT, 1, bfBold12, 0, 255, 255, 255);
            doc.add(torFooter);

            doc.close();
            Desktop.getDesktop().open(pdfFile);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error creating TOR: " + e.getMessage());
        }
    }

    private static void insertCell(
            PdfPTable table,
            String text,
            int align,
            int colspan,
            Font font,
            int border,
            int r,
            int g,
            int b) {

        PdfPCell cell = new PdfPCell(new Phrase(text.trim(), font));
        cell.setHorizontalAlignment(align);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setColspan(colspan);
        cell.setPadding(5f);

        if (text.trim().isEmpty()) {
            cell.setMinimumHeight(10f);
        }

        if (border == 0) {
            cell.setBorder(Rectangle.NO_BORDER);
        } else {
            cell.setBorder(Rectangle.BOX);
        }

        cell.setBackgroundColor(new BaseColor(r, g, b));
        table.addCell(cell);
    }
}
