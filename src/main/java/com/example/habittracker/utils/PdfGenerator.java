package com.example.habittracker.utils;

import com.example.habittracker.models.Habit;
import com.example.habittracker.models.HabitLog;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class PdfGenerator {

    public static byte[] generateHabitReport(List<Habit> habits) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            Document document = new Document();
            PdfWriter.getInstance(document, outputStream);

            document.open();
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
            Font boldFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
            Font normalFont = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL);

            document.add(new Paragraph("Habit Tracker Report", titleFont));
            document.add(new Paragraph("\n"));

            for (Habit habit : habits) {
                // Add habit details
                document.add(new Paragraph("Habit: " + habit.getName(), boldFont));
                document.add(new Paragraph("Description: " + habit.getDescription(), normalFont));
                document.add(new Paragraph("Start Date: " + habit.getStartDate(), normalFont));
                document.add(new Paragraph("Target Days: " + habit.getTargetDays(), normalFont));
                document.add(new Paragraph("\n"));

                // Create table for logs
                PdfPTable table = new PdfPTable(3); // Columns: Date, Completed, Note
                table.setWidthPercentage(100);
                table.addCell(new PdfPCell(new Phrase("Date", boldFont)));
                table.addCell(new PdfPCell(new Phrase("Completed", boldFont)));
                table.addCell(new PdfPCell(new Phrase("Note", boldFont)));

                List<HabitLog> logs = habit.getHabitLogs();
                if (logs != null && !logs.isEmpty()) {
                    for (HabitLog log : logs) {
                        table.addCell(new PdfPCell(new Phrase(log.getLogDate().toString(), normalFont)));
                        table.addCell(new PdfPCell(new Phrase(log.isCompleted() ? "Yes" : "No", normalFont)));
                        table.addCell(new PdfPCell(new Phrase("", normalFont))); // Placeholder for notes
                    }
                } else {
                    PdfPCell noLogsCell = new PdfPCell(new Phrase("No logs available", normalFont));
                    noLogsCell.setColspan(3);
                    noLogsCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(noLogsCell);
                }

                document.add(table);
                document.add(new Paragraph("\n"));
            }

            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return outputStream.toByteArray();
    }
}
