package com.afyasmart.backend.service.impl;

import com.afyasmart.backend.entity.Appointment;
import com.afyasmart.backend.repository.AppointmentRepository;
import com.afyasmart.backend.service.ReportService;

import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;

import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import lombok.RequiredArgsConstructor;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;

import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


/**
 * ============================================================
 * AfyaSmart - Report Service Implementation
 * ============================================================
 *
 * This class contains the actual implementation for generating
 * AfyaSmart system reports.
 *
 * Current reports:
 *
 * 1. Appointment PDF Report
 * 2. Appointment Excel Report
 *
 * The service retrieves appointment information from the
 * database using AppointmentRepository and converts the
 * information into downloadable report files.
 *
 * Architecture:
 *
 * AppointmentRepository
 *          ↓
 * ReportServiceImpl
 *          ↓
 * PDF / Excel
 *          ↓
 * ReportController
 *          ↓
 * Frontend / Browser
 * ============================================================
 */
@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {


    /*
     * Repository used to retrieve appointment information
     * from the AfyaSmart database.
     */
    private final AppointmentRepository appointmentRepository;


    // =========================================================
    // APPOINTMENT PDF REPORT
    // =========================================================

    /**
     * Generates an appointment report in PDF format.
     *
     * @return generated PDF as byte array
     * @throws IOException if PDF generation fails
     */
    @Override
    public byte[] generateAppointmentPdf() throws IOException {

        /*
         * Retrieve all appointments from the database.
         *
         * The repository method also sorts the appointments
         * according to appointment date and time.
         */
        List<Appointment> appointments =
                appointmentRepository
                        .findAllByOrderByAppointmentDateAscAppointmentTimeAsc();


        /*
         * Create an in-memory output stream.
         *
         * This means we don't need to create a physical
         * temporary PDF file on the server.
         */
        ByteArrayOutputStream outputStream =
                new ByteArrayOutputStream();


        /*
         * Create an A4 PDF document.
         */
        Document document =
                new Document(PageSize.A4);


        /*
         * Connect the PDF writer to our output stream.
         */
        PdfWriter.getInstance(
                document,
                outputStream
        );


        /*
         * Open the document before adding content.
         */
        document.open();


        // -----------------------------------------------------
        // PDF TITLE
        // -----------------------------------------------------

        Font titleFont =
                new Font(
                        Font.HELVETICA,
                        22,
                        Font.BOLD
                );


        Paragraph title =
                new Paragraph(
                        "AFYASMART",
                        titleFont
                );


        title.setAlignment(
                Paragraph.ALIGN_CENTER
        );


        document.add(title);


        // -----------------------------------------------------
        // PDF SUBTITLE
        // -----------------------------------------------------

        Font subtitleFont =
                new Font(
                        Font.HELVETICA,
                        14,
                        Font.BOLD
                );


        Paragraph subtitle =
                new Paragraph(
                        "Appointment Report",
                        subtitleFont
                );


        subtitle.setAlignment(
                Paragraph.ALIGN_CENTER
        );


        document.add(subtitle);


        /*
         * Add spacing after the title.
         */
        document.add(
                new Paragraph(" ")
        );


        // -----------------------------------------------------
        // REPORT INFORMATION
        // -----------------------------------------------------

        Font informationFont =
                new Font(
                        Font.HELVETICA,
                        9,
                        Font.NORMAL
                );


        /*
         * Format the report generation date and time.
         */
        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern(
                        "dd MMMM yyyy, HH:mm"
                );


        String generatedAt =
                LocalDateTime.now()
                        .format(formatter);


        document.add(
                new Paragraph(
                        "Generated on: " + generatedAt,
                        informationFont
                )
        );


        document.add(
                new Paragraph(
                        "Total Appointments: "
                                + appointments.size(),
                        informationFont
                )
        );


        document.add(
                new Paragraph(" ")
        );


        // -----------------------------------------------------
        // PDF APPOINTMENT TABLE
        // -----------------------------------------------------

        /*
         * Create seven columns.
         */
        PdfPTable table =
                new PdfPTable(7);


        table.setWidthPercentage(100);


        /*
         * Configure relative column widths.
         */
        table.setWidths(
                new float[]{
                        1.3f,
                        1.6f,
                        1.3f,
                        1.1f,
                        1.0f,
                        1.0f,
                        1.8f
                }
        );


        // -----------------------------------------------------
        // TABLE HEADERS
        // -----------------------------------------------------

        Font headerFont =
                new Font(
                        Font.HELVETICA,
                        8,
                        Font.BOLD
                );


        addHeaderCell(
                table,
                "Patient",
                headerFont
        );


        addHeaderCell(
                table,
                "Email",
                headerFont
        );


        addHeaderCell(
                table,
                "Doctor",
                headerFont
        );


        addHeaderCell(
                table,
                "Date",
                headerFont
        );


        addHeaderCell(
                table,
                "Time",
                headerFont
        );


        addHeaderCell(
                table,
                "Status",
                headerFont
        );


        addHeaderCell(
                table,
                "Reason",
                headerFont
        );


        // -----------------------------------------------------
        // TABLE DATA
        // -----------------------------------------------------

        Font dataFont =
                new Font(
                        Font.HELVETICA,
                        7,
                        Font.NORMAL
                );


        /*
         * Loop through all appointments.
         */
        for (Appointment appointment : appointments) {

            String patientName =
                    appointment.getAccount().getFirstName()
                            + " "
                            + appointment.getAccount().getLastName();


            String patientEmail =
                    appointment.getAccount().getEmail();


            String doctorName =
                    appointment.getDoctorName();


            String date =
                    appointment.getAppointmentDate() != null
                            ? appointment.getAppointmentDate().toString()
                            : "";


            String time =
                    appointment.getAppointmentTime() != null
                            ? appointment.getAppointmentTime().toString()
                            : "";


            String status =
                    appointment.getStatus() != null
                            ? appointment.getStatus().name()
                            : "";


            String reason =
                    appointment.getReason() != null
                            ? appointment.getReason()
                            : "";


            addDataCell(
                    table,
                    patientName,
                    dataFont
            );


            addDataCell(
                    table,
                    patientEmail,
                    dataFont
            );


            addDataCell(
                    table,
                    doctorName,
                    dataFont
            );


            addDataCell(
                    table,
                    date,
                    dataFont
            );


            addDataCell(
                    table,
                    time,
                    dataFont
            );


            addDataCell(
                    table,
                    status,
                    dataFont
            );


            addDataCell(
                    table,
                    reason,
                    dataFont
            );
        }


        /*
         * Add the completed table to the document.
         */
        document.add(table);


        // -----------------------------------------------------
        // PDF FOOTER
        // -----------------------------------------------------

        document.add(
                new Paragraph(" ")
        );


        Font footerFont =
                new Font(
                        Font.HELVETICA,
                        8,
                        Font.ITALIC
                );


        Paragraph footer =
                new Paragraph(
                        "AfyaSmart Healthcare Management System",
                        footerFont
                );


        footer.setAlignment(
                Paragraph.ALIGN_CENTER
        );


        document.add(footer);


        /*
         * Close the PDF document.
         */
        document.close();


        /*
         * Return the generated PDF.
         */
        return outputStream.toByteArray();
    }


    // =========================================================
    // APPOINTMENT EXCEL REPORT
    // =========================================================

    /**
     * Generates an appointment report in Excel format.
     *
     * @return generated Excel workbook as byte array
     * @throws Exception if Excel generation fails
     */
    @Override
    public byte[] generateAppointmentExcel()
            throws Exception {

        /*
         * Retrieve all appointments from the database.
         */
        List<Appointment> appointments =
                appointmentRepository
                        .findAllByOrderByAppointmentDateAscAppointmentTimeAsc();


        /*
         * Create an Excel workbook using the modern .xlsx
         * format.
         */
        Workbook workbook =
                new XSSFWorkbook();


        /*
         * Create the worksheet.
         */
        Sheet sheet =
                workbook.createSheet(
                        "Appointments"
                );


        // -----------------------------------------------------
        // EXCEL TITLE
        // -----------------------------------------------------

        Row titleRow =
                sheet.createRow(0);


        Cell titleCell =
                titleRow.createCell(0);


        titleCell.setCellValue(
                "AfyaSmart Appointment Report"
        );


        /*
         * IMPORTANT:
         *
         * Apache POI has its own Font class.
         *
         * We intentionally use the fully qualified name here
         * because OpenPDF also has a class called Font.
         *
         * This prevents the "Font is ambiguous" compilation
         * error.
         */
        org.apache.poi.ss.usermodel.Font titleFont =
                workbook.createFont();


        titleFont.setBold(true);


        titleFont.setFontHeightInPoints(
                (short) 16
        );


        CellStyle titleStyle =
                workbook.createCellStyle();


        titleStyle.setFont(titleFont);


        titleCell.setCellStyle(
                titleStyle
        );


        /*
         * Merge the title across all eight columns.
         */
        sheet.addMergedRegion(
                new CellRangeAddress(
                        0,
                        0,
                        0,
                        7
                )
        );


        // -----------------------------------------------------
        // REPORT INFORMATION
        // -----------------------------------------------------

        Row infoRow =
                sheet.createRow(1);


        Cell infoCell =
                infoRow.createCell(0);


        infoCell.setCellValue(
                "Total Appointments: "
                        + appointments.size()
        );


        // -----------------------------------------------------
        // EXCEL TABLE HEADERS
        // -----------------------------------------------------

        Row headerRow =
                sheet.createRow(3);


        String[] headers = {

                "Patient Name",
                "Patient Email",
                "Doctor",
                "Specialization",
                "Appointment Date",
                "Appointment Time",
                "Reason",
                "Status"

        };


        /*
         * Create Excel header font.
         *
         * We use the complete package name to avoid conflict
         * with OpenPDF's Font class.
         */
        org.apache.poi.ss.usermodel.Font headerFont =
                workbook.createFont();


        headerFont.setBold(true);


        CellStyle headerStyle =
                workbook.createCellStyle();


        headerStyle.setFont(
                headerFont
        );


        /*
         * Create all header cells.
         */
        for (int i = 0; i < headers.length; i++) {

            Cell cell =
                    headerRow.createCell(i);


            cell.setCellValue(
                    headers[i]
            );


            cell.setCellStyle(
                    headerStyle
            );
        }


        // -----------------------------------------------------
        // EXCEL APPOINTMENT DATA
        // -----------------------------------------------------

        int rowNumber = 4;


        /*
         * Add every appointment to the Excel sheet.
         */
        for (Appointment appointment : appointments) {

            Row row =
                    sheet.createRow(
                            rowNumber++
                    );


            /*
             * Patient name.
             */
            String patientName =
                    appointment.getAccount().getFirstName()
                            + " "
                            + appointment.getAccount().getLastName();


            row.createCell(0)
                    .setCellValue(
                            patientName
                    );


            /*
             * Patient email.
             */
            row.createCell(1)
                    .setCellValue(
                            appointment
                                    .getAccount()
                                    .getEmail()
                    );


            /*
             * Doctor name.
             */
            row.createCell(2)
                    .setCellValue(
                            appointment.getDoctorName()
                    );


            /*
             * Specialization.
             */
            row.createCell(3)
                    .setCellValue(
                            appointment.getSpecialization()
                    );


            /*
             * Appointment date.
             */
            row.createCell(4)
                    .setCellValue(
                            appointment.getAppointmentDate() != null
                                    ? appointment
                                    .getAppointmentDate()
                                    .toString()
                                    : ""
                    );


            /*
             * Appointment time.
             */
            row.createCell(5)
                    .setCellValue(
                            appointment.getAppointmentTime() != null
                                    ? appointment
                                    .getAppointmentTime()
                                    .toString()
                                    : ""
                    );


            /*
             * Appointment reason.
             */
            row.createCell(6)
                    .setCellValue(
                            appointment.getReason() != null
                                    ? appointment.getReason()
                                    : ""
                    );


            /*
             * Appointment status.
             */
            row.createCell(7)
                    .setCellValue(
                            appointment.getStatus() != null
                                    ? appointment
                                    .getStatus()
                                    .name()
                                    : ""
                    );
        }


        // -----------------------------------------------------
        // AUTOMATIC COLUMN WIDTH
        // -----------------------------------------------------

        /*
         * Automatically resize each column based on its
         * contents.
         */
        for (int i = 0; i < headers.length; i++) {

            sheet.autoSizeColumn(i);
        }


        // -----------------------------------------------------
        // CREATE EXCEL FILE
        // -----------------------------------------------------

        ByteArrayOutputStream outputStream =
                new ByteArrayOutputStream();


        /*
         * Write the workbook into memory.
         */
        workbook.write(
                outputStream
        );


        /*
         * Close the workbook to release resources.
         */
        workbook.close();


        /*
         * Return the Excel file.
         */
        return outputStream.toByteArray();
    }


    // =========================================================
    // PDF HEADER CELL HELPER
    // =========================================================

    /**
     * Adds a formatted header cell to the PDF table.
     */
    private void addHeaderCell(
            PdfPTable table,
            String text,
            Font font
    ) {

        PdfPCell cell =
                new PdfPCell(
                        new Phrase(
                                text,
                                font
                        )
                );


        cell.setHorizontalAlignment(
                PdfPCell.ALIGN_CENTER
        );


        cell.setPadding(5);


        table.addCell(cell);
    }


    // =========================================================
    // PDF DATA CELL HELPER
    // =========================================================

    /**
     * Adds a normal data cell to the PDF table.
     */
    private void addDataCell(
            PdfPTable table,
            String text,
            Font font
    ) {

        PdfPCell cell =
                new PdfPCell(
                        new Phrase(
                                text,
                                font
                        )
                );


        cell.setPadding(4);


        cell.setVerticalAlignment(
                PdfPCell.ALIGN_MIDDLE
        );


        table.addCell(cell);
    }
}