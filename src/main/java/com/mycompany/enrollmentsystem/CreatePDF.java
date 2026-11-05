package com.mycompany.enrollmentsystem;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
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

    private static final Font bfBold14 = new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.BOLD, BaseColor.BLACK);
    private static final Font bfBold12 = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLACK);
    private static final Font bf12 = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.BLACK);
    private static final Font bfBold11 = new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD, BaseColor.BLACK);
    private static final Font bf11 = new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK);
    private static final Font bfBold10 = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD, BaseColor.BLACK);
    private static final Font bf10 = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL, BaseColor.BLACK);
    private static final Font bfBold9 = new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLD, BaseColor.BLACK);
    private static final Font bf9 = new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.NORMAL, BaseColor.BLACK);

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
            header.addCell(new Phrase("Pinoy Big Brother\nRegistrar's Office\nStudent Grade Sheet", bfBold12));
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
                String p = b.rs.getString(3);
                String m = b.rs.getString(4);
                String pf = b.rs.getString(5);
                String f = b.rs.getString(6);
                insertCell(table, (p != null && !p.trim().isEmpty()) ? p : "null", Element.ALIGN_CENTER, 1, bf12, 1, 255, 255, 255);
                insertCell(table, (m != null && !m.trim().isEmpty()) ? m : "null", Element.ALIGN_CENTER, 1, bf12, 1, 255, 255, 255);
                insertCell(table, (pf != null && !pf.trim().isEmpty()) ? pf : "null", Element.ALIGN_CENTER, 1, bf12, 1, 255, 255, 255);
                insertCell(table, (f != null && !f.trim().isEmpty()) ? f : "null", Element.ALIGN_CENTER, 1, bf12, 1, 255, 255, 255);
            }
            if (count == 0) {
                insertCell(table, "null", Element.ALIGN_CENTER, 1, bf12, 1, 255, 255, 255);
                insertCell(table, "null", Element.ALIGN_LEFT, 1, bf12, 1, 255, 255, 255);
                insertCell(table, "null", Element.ALIGN_CENTER, 1, bf12, 1, 255, 255, 255);
                insertCell(table, "null", Element.ALIGN_CENTER, 1, bf12, 1, 255, 255, 255);
                insertCell(table, "null", Element.ALIGN_CENTER, 1, bf12, 1, 255, 255, 255);
                insertCell(table, "null", Element.ALIGN_CENTER, 1, bf12, 1, 255, 255, 255);
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

            Document doc = new Document(com.itextpdf.text.PageSize.LETTER, 36, 36, 36, 36);
            PdfWriter.getInstance(doc, new FileOutputStream(pdfFile));
            doc.open();

            PdfPTable header = new PdfPTable(new float[]{1.5f, 5f});
            header.setWidthPercentage(95f);
            header.getDefaultCell().setBorder(Rectangle.NO_BORDER);

            Image logo = Image.getInstance("logo.png");
            logo.scaleToFit(65f, 65f);
            PdfPCell logoCell = new PdfPCell(logo);
            logoCell.setBorder(Rectangle.NO_BORDER);
            logoCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            logoCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            logoCell.setPaddingRight(12f);
            header.addCell(logoCell);

            PdfPCell textCell = new PdfPCell();
            textCell.setBorder(Rectangle.NO_BORDER);
            textCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            Paragraph pHeader = new Paragraph();
            pHeader.add(new Chunk("Pinoy Big Brother\n", bfBold12));
            pHeader.add(new Chunk("Registrar's Office", bfBold12));
            textCell.addElement(pHeader);
            header.addCell(textCell);

            doc.add(header);

            Paragraph title = new Paragraph("OFFICIAL TRANSCRIPT OF RECORDS", bfBold14);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingBefore(18f);
            title.setSpacingAfter(15f);
            doc.add(title);

            EnrollmentSystem b = new EnrollmentSystem();
            b.DBConnectUser(EnrollmentSystem.currentUser, EnrollmentSystem.currentPass, EnrollmentSystem.currentDB);
            String sName = "", sCrs = "", sGender = "";
            b.rs = b.st.executeQuery("SELECT studname, studcrs, studgender FROM students WHERE studid = " + studId);
            if (b.rs.next()) {
                sName = b.rs.getString(1);
                sCrs = b.rs.getString(2);
                sGender = b.rs.getString(3);
            }

            List<String> dbs = new ArrayList<>();
            try {
                b.rs = b.st.executeQuery("SHOW DATABASES");
                while (b.rs.next()) {
                    String dbName = b.rs.getString(1);
                    if (dbName.toUpperCase().contains("_SY")) {
                        dbs.add(dbName);
                    }
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
            Collections.sort(dbs);

            if (sName.isEmpty()) {
                for (String db : dbs) {
                    try {
                        EnrollmentSystem tempDb = new EnrollmentSystem();
                        if (tempDb.DBConnectUser(EnrollmentSystem.currentUser, EnrollmentSystem.currentPass, db)) {
                            tempDb.rs = tempDb.st.executeQuery("SELECT studname, studcrs, studgender FROM students WHERE studid = " + studId);
                            if (tempDb.rs.next()) {
                                sName = tempDb.rs.getString(1);
                                sCrs = tempDb.rs.getString(2);
                                sGender = tempDb.rs.getString(3);
                                break;
                            }
                        }
                    } catch (Exception e) {
                    }
                }
            }

            PdfPTable info = new PdfPTable(2);
            info.setWidthPercentage(95f);
            info.setSpacingAfter(12f);

            Phrase pId = new Phrase();
            pId.add(new Chunk("Student ID: ", bfBold11));
            pId.add(new Chunk(String.valueOf(studId), bf11));
            insertCell(info, pId, Element.ALIGN_LEFT, 1, 0);

            Phrase pName = new Phrase();
            pName.add(new Chunk("Student Name: ", bfBold11));
            pName.add(new Chunk(sName != null ? sName : "", bf11));
            insertCell(info, pName, Element.ALIGN_LEFT, 1, 0);

            Phrase pCrs = new Phrase();
            pCrs.add(new Chunk("Student Course: ", bfBold11));
            pCrs.add(new Chunk(sCrs != null ? sCrs : "", bf11));
            insertCell(info, pCrs, Element.ALIGN_LEFT, 1, 0);

            Phrase pGender = new Phrase();
            pGender.add(new Chunk("Gender: ", bfBold11));
            pGender.add(new Chunk(sGender != null ? sGender : "", bf11));
            insertCell(info, pGender, Element.ALIGN_LEFT, 1, 0);

            doc.add(info);

            PdfPTable table = new PdfPTable(new float[]{1.5f, 2f, 4.2f, 1.8f, 1.2f});
            table.setWidthPercentage(95f);

            for (String h : new String[]{"SubjID", "Subj Code", "Descriptive Title", "Final", "Credit"}) {
                insertCell(table, h, Element.ALIGN_CENTER, 1, bfBold10, 1, 255, 255, 255);
            }

            int totalSubjects = 0;
            for (String db : dbs) {
                EnrollmentSystem semDb = new EnrollmentSystem();
                if (!semDb.DBConnectUser(EnrollmentSystem.currentUser, EnrollmentSystem.currentPass, db)) continue;

                boolean isEnrolledInSem = false;
                try {
                    semDb.rs = semDb.st.executeQuery("SELECT COUNT(*) FROM students WHERE studid = " + studId);
                    if (semDb.rs.next() && semDb.rs.getInt(1) > 0) {
                        isEnrolledInSem = true;
                    }
                } catch (Exception ex) {
                }

                String q = "SELECT s.subjid, s.subjcode, s.subjdesc, g.final, s.subjunits "
                         + "FROM enroll e "
                         + "INNER JOIN subjects s ON e.subjid = s.subjid "
                         + "LEFT JOIN grades g ON e.eid = g.enroll_eid "
                         + "WHERE e.studid = " + studId;
                semDb.rs = semDb.st.executeQuery(q);

                List<String[]> semRows = new ArrayList<>();
                while (semDb.rs.next()) {
                    String subjid = semDb.rs.getString(1);
                    String subjcode = semDb.rs.getString(2);
                    String subjdesc = semDb.rs.getString(3);
                    String finalGrade = semDb.rs.getString(4);
                    String subjunits = semDb.rs.getString(5);

                    if (finalGrade == null || finalGrade.trim().isEmpty() || finalGrade.trim().equalsIgnoreCase("null")) {
                        finalGrade = "No Grade";
                    }
                    semRows.add(new String[]{
                        subjid != null ? subjid : "",
                        subjcode != null ? subjcode : "",
                        subjdesc != null ? subjdesc : "",
                        finalGrade,
                        subjunits != null ? subjunits : ""
                    });
                }

                if (semRows.isEmpty() && !isEnrolledInSem) {
                    continue;
                }

                String termTitle = db;
                String termPart = "";
                if (db.toLowerCase().contains("1st")) termPart = "1st Semester";
                else if (db.toLowerCase().contains("2nd")) termPart = "2nd Semester";
                else if (db.toLowerCase().contains("summer")) termPart = "Summer";

                String syPart = "";
                int syIndex = db.toUpperCase().indexOf("SY");
                if (syIndex != -1) {
                    String years = db.substring(syIndex + 2).replace("_", "–");
                    syPart = "School Year " + years;
                }
                if (!termPart.isEmpty() && !syPart.isEmpty()) {
                    termTitle = termPart + " | " + syPart;
                } else if (!termPart.isEmpty()) {
                    termTitle = termPart;
                }

                PdfPCell termCell = new PdfPCell(new Phrase(termTitle, bfBold10));
                termCell.setColspan(5);
                termCell.setBorder(Rectangle.NO_BORDER);
                termCell.setPaddingTop(8f);
                termCell.setPaddingBottom(4f);
                termCell.setPaddingLeft(2f);
                table.addCell(termCell);

                for (String[] row : semRows) {
                    totalSubjects++;
                    insertCell(table, row[0], Element.ALIGN_LEFT, 1, bf10, 1, 255, 255, 255);
                    insertCell(table, row[1], Element.ALIGN_LEFT, 1, bf10, 1, 255, 255, 255);
                    insertCell(table, row[2], Element.ALIGN_LEFT, 1, bf10, 1, 255, 255, 255);
                    insertCell(table, row[3], Element.ALIGN_LEFT, 1, bf10, 1, 255, 255, 255);
                    insertCell(table, row[4], Element.ALIGN_LEFT, 1, bf10, 1, 255, 255, 255);
                }
            }
            doc.add(table);

            Paragraph numSubj = new Paragraph("Number of Subjects Listed: " + totalSubjects, bfBold10);
            numSubj.setSpacingBefore(12f);
            numSubj.setSpacingAfter(10f);
            doc.add(numSubj);

            PdfPTable gradingTable = new PdfPTable(2);
            gradingTable.setWidthPercentage(95f);
            gradingTable.setSpacingAfter(8f);

            insertCell(gradingTable, "Grading System up to SY 1981-82", Element.ALIGN_LEFT, 1, bfBold9, 1, 255, 255, 255);
            insertCell(gradingTable, "New Grading System (1st Sem. SY 1982-83 & up)", Element.ALIGN_LEFT, 1, bfBold9, 1, 255, 255, 255);

            String[][] gradingData = {
                {"95-100 = 1.0 = Excellent", "95-100 = 1.0 = Excellent"},
                {"90-94 = 1.5 = Very Good", "90-94 = 1.5 = Very Good"},
                {"85-89 = 2.0 = Good", "85-89 = 2.0 = Good"},
                {"80-84 = 2.5 = Fair", "80-84 = 2.5 = Fair"},
                {"75-79 = 3.0 = Passed", "75-79 = 3.0 = Passed"},
                {"Below 75 = 5.0 = Failed", "Below 75 = 5.0 = Failed"}
            };
            for (String[] gRow : gradingData) {
                insertCell(gradingTable, gRow[0], Element.ALIGN_LEFT, 1, bf9, 1, 255, 255, 255);
                insertCell(gradingTable, gRow[1], Element.ALIGN_LEFT, 1, bf9, 1, 255, 255, 255);
            }
            doc.add(gradingTable);

            Paragraph pFcf = new Paragraph("FOR FCF Grades: O - Outstanding, HS - Highly Satisfactory, MS - Moderately Satisfactory, S - Satisfactory, F - Fair, P - Poor", bf9);
            pFcf.setSpacingBefore(4f);
            pFcf.setSpacingAfter(3f);
            doc.add(pFcf);

            Paragraph pQpe = new Paragraph("Quality Point Equivalent: 1.0 = 4.0, 1.5 = 3.5, 2.0 = 3.0, 2.5 = 2.5, 3.0 = 2.0", bf9);
            pQpe.setSpacingAfter(18f);
            doc.add(pQpe);

            Paragraph pSeal = new Paragraph("NOT VALID WITHOUT SCHOOL SEAL", bfBold11);
            pSeal.setAlignment(Element.ALIGN_CENTER);
            pSeal.setSpacingBefore(12f);
            pSeal.setSpacingAfter(20f);
            doc.add(pSeal);

            PdfPTable sigTable = new PdfPTable(2);
            sigTable.setWidthPercentage(95f);
            sigTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);

            PdfPCell prepCell = new PdfPCell(new Phrase("Prepared By: _____________________", bf10));
            prepCell.setBorder(Rectangle.NO_BORDER);
            prepCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            sigTable.addCell(prepCell);

            PdfPCell checkCell = new PdfPCell(new Phrase("Checked By: _____________________", bf10));
            checkCell.setBorder(Rectangle.NO_BORDER);
            checkCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            sigTable.addCell(checkCell);

            doc.add(sigTable);

            doc.close();
            Desktop.getDesktop().open(pdfFile);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error creating TOR: " + e.getMessage());
        }
    }

    private static void insertCell(
            PdfPTable table,
            Phrase phrase,
            int align,
            int colspan,
            int border) {

        PdfPCell cell = new PdfPCell(phrase);
        cell.setHorizontalAlignment(align);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setColspan(colspan);
        cell.setPadding(4f);
        if (border == 0) {
            cell.setBorder(Rectangle.NO_BORDER);
        } else {
            cell.setBorder(Rectangle.BOX);
        }
        table.addCell(cell);
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

        String cellText = text != null ? text.trim() : "null";
        PdfPCell cell = new PdfPCell(new Phrase(cellText, font));
        cell.setHorizontalAlignment(align);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setColspan(colspan);
        cell.setPadding(4f);

        if (cellText.isEmpty()) {
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
