package com.afyasmart.backend.service;

/**
 * ============================================================
 * AfyaSmart - Report Service
 * ============================================================
 *
 * Defines all report-generation operations used by the
 * AfyaSmart Administration reporting module.
 *
 * Available reports:
 *
 * 1. Appointment Reports
 *    - PDF
 *    - Excel
 *
 * 2. Patient Reports
 *    - PDF
 *    - Excel
 *
 * 3. Doctor Reports
 *    - PDF
 *    - Excel
 *
 * All methods return byte arrays so that the controller can
 * send the generated files directly to the frontend.
 * ============================================================
 */
public interface ReportService {

    // ========================================================
    // APPOINTMENT REPORTS
    // ========================================================

    /**
     * Generates a complete appointment report in PDF format.
     *
     * @return generated PDF as a byte array
     * @throws Exception if PDF generation fails
     */
    byte[] generateAppointmentPdf() throws Exception;

    /**
     * Generates a complete appointment report in Excel format.
     *
     * @return generated Excel workbook as a byte array
     * @throws Exception if Excel generation fails
     */
    byte[] generateAppointmentExcel() throws Exception;


    // ========================================================
    // PATIENT REPORTS
    // ========================================================

    /**
     * Generates a list of registered patients in PDF format.
     *
     * @return generated PDF as a byte array
     * @throws Exception if PDF generation fails
     */
    byte[] generatePatientPdf() throws Exception;

    /**
     * Generates a list of registered patients in Excel format.
     *
     * @return generated Excel workbook as a byte array
     * @throws Exception if Excel generation fails
     */
    byte[] generatePatientExcel() throws Exception;


    // ========================================================
    // DOCTOR REPORTS
    // ========================================================

    /**
     * Generates a list of registered doctors in PDF format.
     *
     * @return generated PDF as a byte array
     * @throws Exception if PDF generation fails
     */
    byte[] generateDoctorPdf() throws Exception;

    /**
     * Generates a list of registered doctors in Excel format.
     *
     * @return generated Excel workbook as a byte array
     * @throws Exception if Excel generation fails
     */
    byte[] generateDoctorExcel() throws Exception;
}