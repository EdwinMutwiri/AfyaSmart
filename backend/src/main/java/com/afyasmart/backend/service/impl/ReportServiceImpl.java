package com.afyasmart.backend.service.impl;

import com.afyasmart.backend.entity.Account;
import com.afyasmart.backend.entity.Appointment;
import com.afyasmart.backend.entity.DoctorProfile;
import com.afyasmart.backend.entity.Role;

import com.afyasmart.backend.repository.AccountRepository;
import com.afyasmart.backend.repository.AppointmentRepository;
import com.afyasmart.backend.repository.DoctorProfileRepository;

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
 * Handles generation of administrative reports for:
 *
 * 1. Appointments
 *      - PDF
 *      - Excel
 *
 * 2. Patients
 *      - PDF
 *      - Excel
 *
 * 3. Doctors
 *      - PDF
 *      - Excel
 *
 * The generated files are returned as byte arrays so that
 * ReportController can send them directly to the browser.
 *
 * ============================================================
 */
@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {


    // =========================================================
    // REPOSITORIES
    // =========================================================

    /**
     * Retrieves appointment information.
     */
    private final AppointmentRepository appointmentRepository;


    /**
     * Retrieves patient and account information.
     */
    private final AccountRepository accountRepository;


    /**
     * Retrieves doctor profile information.
     */
    private final DoctorProfileRepository doctorProfileRepository;


    // =========================================================
    // APPOINTMENT PDF REPORT
    // =========================================================

    @Override
    public byte[] generateAppointmentPdf() throws IOException {

        List<Appointment> appointments =
                appointmentRepository
                        .findAllByOrderByAppointmentDateAscAppointmentTimeAsc();

        ByteArrayOutputStream outputStream =
                new ByteArrayOutputStream();

        Document document =
                new Document(PageSize.A4);

        PdfWriter.getInstance(
                document,
                outputStream
        );

        document.open();

        // -----------------------------------------------------
        // TITLE
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
        // APPOINTMENT TABLE
        // -----------------------------------------------------

        PdfPTable table =
                new PdfPTable(7);

        table.setWidthPercentage(100);

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


        Font headerFont =
                new Font(
                        Font.HELVETICA,
                        8,
                        Font.BOLD
                );

        addHeaderCell(table, "Patient", headerFont);
        addHeaderCell(table, "Email", headerFont);
        addHeaderCell(table, "Doctor", headerFont);
        addHeaderCell(table, "Date", headerFont);
        addHeaderCell(table, "Time", headerFont);
        addHeaderCell(table, "Status", headerFont);
        addHeaderCell(table, "Reason", headerFont);


        Font dataFont =
                new Font(
                        Font.HELVETICA,
                        7,
                        Font.NORMAL
                );


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


        document.add(table);


        // -----------------------------------------------------
        // FOOTER
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

        document.close();

        return outputStream.toByteArray();
    }


    // =========================================================
    // APPOINTMENT EXCEL REPORT
    // =========================================================

    @Override
    public byte[] generateAppointmentExcel()
            throws Exception {

        List<Appointment> appointments =
                appointmentRepository
                        .findAllByOrderByAppointmentDateAscAppointmentTimeAsc();

        Workbook workbook =
                new XSSFWorkbook();

        Sheet sheet =
                workbook.createSheet(
                        "Appointments"
                );


        // -----------------------------------------------------
        // TITLE
        // -----------------------------------------------------

        Row titleRow =
                sheet.createRow(0);

        Cell titleCell =
                titleRow.createCell(0);

        titleCell.setCellValue(
                "AfyaSmart Appointment Report"
        );


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
        // HEADERS
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


        org.apache.poi.ss.usermodel.Font headerFont =
                workbook.createFont();

        headerFont.setBold(true);

        CellStyle headerStyle =
                workbook.createCellStyle();

        headerStyle.setFont(
                headerFont
        );


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
        // DATA
        // -----------------------------------------------------

        int rowNumber = 4;

        for (Appointment appointment : appointments) {

            Row row =
                    sheet.createRow(
                            rowNumber++
                    );


            String patientName =
                    appointment.getAccount().getFirstName()
                            + " "
                            + appointment.getAccount().getLastName();


            row.createCell(0)
                    .setCellValue(
                            patientName
                    );


            row.createCell(1)
                    .setCellValue(
                            appointment
                                    .getAccount()
                                    .getEmail()
                    );


            row.createCell(2)
                    .setCellValue(
                            appointment.getDoctorName()
                    );


            row.createCell(3)
                    .setCellValue(
                            appointment.getSpecialization()
                    );


            row.createCell(4)
                    .setCellValue(
                            appointment.getAppointmentDate() != null
                                    ? appointment
                                    .getAppointmentDate()
                                    .toString()
                                    : ""
                    );


            row.createCell(5)
                    .setCellValue(
                            appointment.getAppointmentTime() != null
                                    ? appointment
                                    .getAppointmentTime()
                                    .toString()
                                    : ""
                    );


            row.createCell(6)
                    .setCellValue(
                            appointment.getReason() != null
                                    ? appointment.getReason()
                                    : ""
                    );


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
        // COLUMN WIDTH
        // -----------------------------------------------------

        for (int i = 0; i < headers.length; i++) {

            sheet.autoSizeColumn(i);

        }


        // -----------------------------------------------------
        // CREATE FILE
        // -----------------------------------------------------

        ByteArrayOutputStream outputStream =
                new ByteArrayOutputStream();

        workbook.write(
                outputStream
        );

        workbook.close();

        return outputStream.toByteArray();
    }


    // =========================================================
    // PATIENT PDF REPORT
    // =========================================================

    @Override
    public byte[] generatePatientPdf()
            throws IOException {

        List<Account> patients =
                accountRepository.findByRole(
                        Role.PATIENT
                );


        ByteArrayOutputStream outputStream =
                new ByteArrayOutputStream();

        Document document =
                new Document(PageSize.A4);

        PdfWriter.getInstance(
                document,
                outputStream
        );

        document.open();


        // -----------------------------------------------------
        // TITLE
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


        Font subtitleFont =
                new Font(
                        Font.HELVETICA,
                        14,
                        Font.BOLD
                );

        Paragraph subtitle =
                new Paragraph(
                        "Registered Patients Report",
                        subtitleFont
                );

        subtitle.setAlignment(
                Paragraph.ALIGN_CENTER
        );

        document.add(subtitle);

        document.add(
                new Paragraph(" ")
        );


        // -----------------------------------------------------
        // INFORMATION
        // -----------------------------------------------------

        Font informationFont =
                new Font(
                        Font.HELVETICA,
                        9,
                        Font.NORMAL
                );

        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern(
                        "dd MMMM yyyy, HH:mm"
                );

        document.add(
                new Paragraph(
                        "Generated on: "
                                + LocalDateTime.now()
                                .format(formatter),
                        informationFont
                )
        );

        document.add(
                new Paragraph(
                        "Total Patients: "
                                + patients.size(),
                        informationFont
                )
        );

        document.add(
                new Paragraph(" ")
        );


        // -----------------------------------------------------
        // TABLE
        // -----------------------------------------------------

        PdfPTable table =
                new PdfPTable(4);

        table.setWidthPercentage(100);

        table.setWidths(
                new float[]{
                        1.4f,
                        1.4f,
                        2.0f,
                        1.2f
                }
        );


        Font headerFont =
                new Font(
                        Font.HELVETICA,
                        9,
                        Font.BOLD
                );

        addHeaderCell(
                table,
                "First Name",
                headerFont
        );

        addHeaderCell(
                table,
                "Last Name",
                headerFont
        );

        addHeaderCell(
                table,
                "Email",
                headerFont
        );

        addHeaderCell(
                table,
                "Role",
                headerFont
        );


        Font dataFont =
                new Font(
                        Font.HELVETICA,
                        8,
                        Font.NORMAL
                );


        for (Account patient : patients) {

            addDataCell(
                    table,
                    patient.getFirstName(),
                    dataFont
            );

            addDataCell(
                    table,
                    patient.getLastName(),
                    dataFont
            );

            addDataCell(
                    table,
                    patient.getEmail(),
                    dataFont
            );

            addDataCell(
                    table,
                    patient.getRole() != null
                            ? patient.getRole().name()
                            : "",
                    dataFont
            );
        }


        document.add(table);


        // -----------------------------------------------------
        // FOOTER
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

        document.close();

        return outputStream.toByteArray();
    }


    // =========================================================
    // PATIENT EXCEL REPORT
    // =========================================================

    @Override
    public byte[] generatePatientExcel()
            throws Exception {

        List<Account> patients =
                accountRepository.findByRole(
                        Role.PATIENT
                );


        Workbook workbook =
                new XSSFWorkbook();

        Sheet sheet =
                workbook.createSheet(
                        "Patients"
                );


        // -----------------------------------------------------
        // TITLE
        // -----------------------------------------------------

        Row titleRow =
                sheet.createRow(0);

        Cell titleCell =
                titleRow.createCell(0);

        titleCell.setCellValue(
                "AfyaSmart Registered Patients Report"
        );


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


        sheet.addMergedRegion(
                new CellRangeAddress(
                        0,
                        0,
                        0,
                        3
                )
        );


        // -----------------------------------------------------
        // INFORMATION
        // -----------------------------------------------------

        Row infoRow =
                sheet.createRow(1);

        infoRow.createCell(0)
                .setCellValue(
                        "Total Patients: "
                                + patients.size()
                );


        // -----------------------------------------------------
        // HEADERS
        // -----------------------------------------------------

        Row headerRow =
                sheet.createRow(3);

        String[] headers = {
                "First Name",
                "Last Name",
                "Email",
                "Role"
        };


        org.apache.poi.ss.usermodel.Font headerFont =
                workbook.createFont();

        headerFont.setBold(true);

        CellStyle headerStyle =
                workbook.createCellStyle();

        headerStyle.setFont(
                headerFont
        );


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
        // PATIENT DATA
        // -----------------------------------------------------

        int rowNumber = 4;

        for (Account patient : patients) {

            Row row =
                    sheet.createRow(
                            rowNumber++
                    );


            row.createCell(0)
                    .setCellValue(
                            patient.getFirstName()
                    );


            row.createCell(1)
                    .setCellValue(
                            patient.getLastName()
                    );


            row.createCell(2)
                    .setCellValue(
                            patient.getEmail()
                    );


            row.createCell(3)
                    .setCellValue(
                            patient.getRole() != null
                                    ? patient.getRole().name()
                                    : ""
                    );
        }


        // -----------------------------------------------------
        // AUTO SIZE
        // -----------------------------------------------------

        for (int i = 0; i < headers.length; i++) {

            sheet.autoSizeColumn(i);

        }


        // -----------------------------------------------------
        // CREATE FILE
        // -----------------------------------------------------

        ByteArrayOutputStream outputStream =
                new ByteArrayOutputStream();

        workbook.write(
                outputStream
        );

        workbook.close();

        return outputStream.toByteArray();
    }


    // =========================================================
    // DOCTOR PDF REPORT
    // =========================================================

    @Override
    public byte[] generateDoctorPdf()
            throws IOException {

        List<DoctorProfile> doctors =
                doctorProfileRepository.findAll();


        ByteArrayOutputStream outputStream =
                new ByteArrayOutputStream();

        Document document =
                new Document(PageSize.A4.rotate());

        PdfWriter.getInstance(
                document,
                outputStream
        );

        document.open();


        // -----------------------------------------------------
        // TITLE
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


        Font subtitleFont =
                new Font(
                        Font.HELVETICA,
                        14,
                        Font.BOLD
                );

        Paragraph subtitle =
                new Paragraph(
                        "Registered Doctors Report",
                        subtitleFont
                );

        subtitle.setAlignment(
                Paragraph.ALIGN_CENTER
        );

        document.add(subtitle);

        document.add(
                new Paragraph(" ")
        );


        // -----------------------------------------------------
        // INFORMATION
        // -----------------------------------------------------

        Font informationFont =
                new Font(
                        Font.HELVETICA,
                        9,
                        Font.NORMAL
                );

        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern(
                        "dd MMMM yyyy, HH:mm"
                );

        document.add(
                new Paragraph(
                        "Generated on: "
                                + LocalDateTime.now()
                                .format(formatter),
                        informationFont
                )
        );

        document.add(
                new Paragraph(
                        "Total Doctors: "
                                + doctors.size(),
                        informationFont
                )
        );

        document.add(
                new Paragraph(" ")
        );


        // -----------------------------------------------------
        // DOCTOR TABLE
        // -----------------------------------------------------

        PdfPTable table =
                new PdfPTable(7);

        table.setWidthPercentage(100);


        Font headerFont =
                new Font(
                        Font.HELVETICA,
                        8,
                        Font.BOLD
                );


        addHeaderCell(
                table,
                "Doctor",
                headerFont
        );

        addHeaderCell(
                table,
                "Specialization",
                headerFont
        );

        addHeaderCell(
                table,
                "Hospital",
                headerFont
        );

        addHeaderCell(
                table,
                "License Number",
                headerFont
        );

        addHeaderCell(
                table,
                "Experience",
                headerFont
        );

        addHeaderCell(
                table,
                "Consultation Fee",
                headerFont
        );

        addHeaderCell(
                table,
                "Available",
                headerFont
        );


        Font dataFont =
                new Font(
                        Font.HELVETICA,
                        7,
                        Font.NORMAL
                );


        for (DoctorProfile doctor : doctors) {

            String doctorName = "";

            if (doctor.getAccount() != null) {

                doctorName =
                        doctor.getAccount().getFirstName()
                                + " "
                                + doctor.getAccount().getLastName();
            }


            String specialization =
                    doctor.getSpecialization() != null
                            ? doctor.getSpecialization().getName()
                            : "";


            String hospital =
                    doctor.getHospital() != null
                            ? doctor.getHospital()
                            : "";


            String license =
                    doctor.getLicenseNumber() != null
                            ? doctor.getLicenseNumber()
                            : "";


            String experience =
                    String.valueOf(
                            doctor.getYearsExperience()
                    );


            String consultationFee =
                    doctor.getConsultationFee() != null
                            ? doctor.getConsultationFee().toString()
                            : "";


            String available =
                    doctor.getAvailable() != null
                            && doctor.getAvailable()
                            ? "Yes"
                            : "No";


            addDataCell(
                    table,
                    doctorName,
                    dataFont
            );

            addDataCell(
                    table,
                    specialization,
                    dataFont
            );

            addDataCell(
                    table,
                    hospital,
                    dataFont
            );

            addDataCell(
                    table,
                    license,
                    dataFont
            );

            addDataCell(
                    table,
                    experience,
                    dataFont
            );

            addDataCell(
                    table,
                    consultationFee,
                    dataFont
            );

            addDataCell(
                    table,
                    available,
                    dataFont
            );
        }


        document.add(table);


        // -----------------------------------------------------
        // FOOTER
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

        document.close();

        return outputStream.toByteArray();
    }


    // =========================================================
    // DOCTOR EXCEL REPORT
    // =========================================================

    @Override
    public byte[] generateDoctorExcel()
            throws Exception {

        List<DoctorProfile> doctors =
                doctorProfileRepository.findAll();


        Workbook workbook =
                new XSSFWorkbook();

        Sheet sheet =
                workbook.createSheet(
                        "Doctors"
                );


        // -----------------------------------------------------
        // TITLE
        // -----------------------------------------------------

        Row titleRow =
                sheet.createRow(0);

        Cell titleCell =
                titleRow.createCell(0);

        titleCell.setCellValue(
                "AfyaSmart Registered Doctors Report"
        );


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


        sheet.addMergedRegion(
                new CellRangeAddress(
                        0,
                        0,
                        0,
                        6
                )
        );


        // -----------------------------------------------------
        // INFORMATION
        // -----------------------------------------------------

        Row infoRow =
                sheet.createRow(1);

        infoRow.createCell(0)
                .setCellValue(
                        "Total Doctors: "
                                + doctors.size()
                );


        // -----------------------------------------------------
        // HEADERS
        // -----------------------------------------------------

        Row headerRow =
                sheet.createRow(3);

        String[] headers = {

                "Doctor",
                "Specialization",
                "Hospital",
                "License Number",
                "Years Experience",
                "Consultation Fee",
                "Available"

        };


        org.apache.poi.ss.usermodel.Font headerFont =
                workbook.createFont();

        headerFont.setBold(true);

        CellStyle headerStyle =
                workbook.createCellStyle();

        headerStyle.setFont(
                headerFont
        );


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
        // DOCTOR DATA
        // -----------------------------------------------------

        int rowNumber = 4;

        for (DoctorProfile doctor : doctors) {

            Row row =
                    sheet.createRow(
                            rowNumber++
                    );


            String doctorName = "";

            if (doctor.getAccount() != null) {

                doctorName =
                        doctor.getAccount().getFirstName()
                                + " "
                                + doctor.getAccount().getLastName();
            }


            String specialization =
                    doctor.getSpecialization() != null
                            ? doctor.getSpecialization().getName()
                            : "";


            row.createCell(0)
                    .setCellValue(
                            doctorName
                    );


            row.createCell(1)
                    .setCellValue(
                            specialization
                    );


            row.createCell(2)
                    .setCellValue(
                            doctor.getHospital() != null
                                    ? doctor.getHospital()
                                    : ""
                    );


            row.createCell(3)
                    .setCellValue(
                            doctor.getLicenseNumber() != null
                                    ? doctor.getLicenseNumber()
                                    : ""
                    );


            row.createCell(4)
                    .setCellValue(
                            doctor.getYearsExperience()
                    );


            row.createCell(5)
                    .setCellValue(
                            doctor.getConsultationFee() != null
                                    ? doctor.getConsultationFee()
                                    .doubleValue()
                                    : 0
                    );


            row.createCell(6)
                    .setCellValue(
                            doctor.getAvailable() != null
                                    && doctor.getAvailable()
                                    ? "Yes"
                                    : "No"
                    );
        }


        // -----------------------------------------------------
        // AUTO SIZE
        // -----------------------------------------------------

        for (int i = 0; i < headers.length; i++) {

            sheet.autoSizeColumn(i);

        }


        // -----------------------------------------------------
        // CREATE FILE
        // -----------------------------------------------------

        ByteArrayOutputStream outputStream =
                new ByteArrayOutputStream();

        workbook.write(
                outputStream
        );

        workbook.close();

        return outputStream.toByteArray();
    }


    // =========================================================
    // PDF HEADER CELL HELPER
    // =========================================================

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

    private void addDataCell(
            PdfPTable table,
            String text,
            Font font
    ) {

        PdfPCell cell =
                new PdfPCell(
                        new Phrase(
                                text != null
                                        ? text
                                        : "",
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