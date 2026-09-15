package com.afyasmart.backend.service;

/**
 * ============================================================
 * AfyaSmart - Report Service
 * ============================================================
 *
 * Defines the operations available for generating reports.
 *
 * Current reports:
 * - Appointment PDF
 * - Appointment Excel
 * ============================================================
 */
public interface ReportService {

    /**
     * Generates an appointment report in PDF format.
     *
     * @return generated PDF as a byte array
     * @throws Exception if PDF generation fails
     */
    byte[] generateAppointmentPdf() throws Exception;

    /**
     * Generates an appointment report in Excel format.
     *
     * @return generated Excel workbook as a byte array
     * @throws Exception if Excel generation fails
     */
    byte[] generateAppointmentExcel() throws Exception;
}