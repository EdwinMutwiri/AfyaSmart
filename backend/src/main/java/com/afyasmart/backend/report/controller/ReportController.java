package com.afyasmart.backend.report.controller;

import com.afyasmart.backend.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * ============================================================
 * AfyaSmart - Report Controller
 * ============================================================
 *
 * This controller provides API endpoints for generating and
 * downloading AfyaSmart system reports.
 *
 * Available reports:
 *
 * 1. Appointment PDF Report
 * 2. Appointment Excel Report
 *
 * 3. Patient PDF Report
 * 4. Patient Excel Report
 *
 * 5. Doctor PDF Report
 * 6. Doctor Excel Report
 *
 * Base URL:
 * /api/reports
 *
 * ============================================================
 */
@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ReportController {

    private final ReportService reportService;


    // ============================================================
    // APPOINTMENT REPORTS
    // ============================================================

    /**
     * ========================================================
     * Generate Appointment PDF Report
     * ========================================================
     *
     * Endpoint:
     * GET /api/reports/appointments/pdf
     *
     * @return PDF file as byte array
     */
    @GetMapping("/appointments/pdf")
    public ResponseEntity<byte[]> generateAppointmentPdf()
            throws Exception {

        byte[] pdf =
                reportService.generateAppointmentPdf();

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=AfyaSmart_Appointments_Report.pdf"
                )
                .body(pdf);
    }


    /**
     * ========================================================
     * Generate Appointment Excel Report
     * ========================================================
     *
     * Endpoint:
     * GET /api/reports/appointments/excel
     *
     * @return Excel file as byte array
     */
    @GetMapping("/appointments/excel")
    public ResponseEntity<byte[]> generateAppointmentExcel()
            throws Exception {

        byte[] excel =
                reportService.generateAppointmentExcel();

        return ResponseEntity.ok()
                .contentType(
                        MediaType.parseMediaType(
                                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                        )
                )
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=AfyaSmart_Appointments_Report.xlsx"
                )
                .body(excel);
    }


    // ============================================================
    // PATIENT REPORTS
    // ============================================================

    /**
     * ========================================================
     * Generate Patient PDF Report
     * ========================================================
     *
     * Endpoint:
     * GET /api/reports/patients/pdf
     *
     * This report contains registered patient information
     * available in the AfyaSmart system.
     *
     * @return Patient PDF report
     */
    @GetMapping("/patients/pdf")
    public ResponseEntity<byte[]> generatePatientPdf()
            throws Exception {

        byte[] pdf =
                reportService.generatePatientPdf();

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=AfyaSmart_Patients_Report.pdf"
                )
                .body(pdf);
    }


    /**
     * ========================================================
     * Generate Patient Excel Report
     * ========================================================
     *
     * Endpoint:
     * GET /api/reports/patients/excel
     *
     * This report contains registered patient information
     * in spreadsheet format.
     *
     * @return Patient Excel report
     */
    @GetMapping("/patients/excel")
    public ResponseEntity<byte[]> generatePatientExcel()
            throws Exception {

        byte[] excel =
                reportService.generatePatientExcel();

        return ResponseEntity.ok()
                .contentType(
                        MediaType.parseMediaType(
                                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                        )
                )
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=AfyaSmart_Patients_Report.xlsx"
                )
                .body(excel);
    }


    // ============================================================
    // DOCTOR REPORTS
    // ============================================================

    /**
     * ========================================================
     * Generate Doctor PDF Report
     * ========================================================
     *
     * Endpoint:
     * GET /api/reports/doctors/pdf
     *
     * This report contains doctor profile information
     * registered in the AfyaSmart system.
     *
     * @return Doctor PDF report
     */
    @GetMapping("/doctors/pdf")
    public ResponseEntity<byte[]> generateDoctorPdf()
            throws Exception {

        byte[] pdf =
                reportService.generateDoctorPdf();

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=AfyaSmart_Doctors_Report.pdf"
                )
                .body(pdf);
    }


    /**
     * ========================================================
     * Generate Doctor Excel Report
     * ========================================================
     *
     * Endpoint:
     * GET /api/reports/doctors/excel
     *
     * This report contains doctor profile information
     * in spreadsheet format.
     *
     * @return Doctor Excel report
     */
    @GetMapping("/doctors/excel")
    public ResponseEntity<byte[]> generateDoctorExcel()
            throws Exception {

        byte[] excel =
                reportService.generateDoctorExcel();

        return ResponseEntity.ok()
                .contentType(
                        MediaType.parseMediaType(
                                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                        )
                )
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=AfyaSmart_Doctors_Report.xlsx"
                )
                .body(excel);
    }
}