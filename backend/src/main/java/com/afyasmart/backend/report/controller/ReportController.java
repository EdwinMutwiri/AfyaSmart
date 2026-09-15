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


    /**
     * ========================================================
     * Generate Appointment PDF Report
     * ========================================================
     *
     * Endpoint:
     * GET /api/reports/appointments/pdf
     *
     * This endpoint retrieves appointment information from the
     * database and generates a downloadable PDF report.
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
     * This endpoint generates an Excel spreadsheet containing
     * appointment information.
     *
     * The Excel file can be opened using Microsoft Excel,
     * LibreOffice Calc, or other spreadsheet applications.
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
}