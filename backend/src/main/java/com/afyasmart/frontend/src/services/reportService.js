import api from "./api";

/**
 * ============================================================
 * AfyaSmart - Report Service
 * ============================================================
 *
 * This file contains all API functions related to reports.
 *
 * The backend generates the actual report files.
 * The frontend simply requests the file and downloads it.
 *
 * Current reports:
 *
 * 1. Appointment PDF
 * 2. Appointment Excel
 *
 * ============================================================
 */


/**
 * ============================================================
 * Download Appointment PDF
 * ============================================================
 *
 * Calls:
 *
 * GET /api/reports/appointments/pdf
 *
 * The response is returned as a Blob because a PDF is a
 * binary file rather than normal JSON data.
 */
export const downloadAppointmentPdf = async () => {

    const response = await api.get(
        "/reports/appointments/pdf",
        {
            responseType: "blob"
        }
    );

    return response.data;
};


/**
 * ============================================================
 * Download Appointment Excel
 * ============================================================
 *
 * Calls:
 *
 * GET /api/reports/appointments/excel
 *
 * The response is returned as a Blob because an Excel workbook
 * is a binary file.
 */
export const downloadAppointmentExcel = async () => {

    const response = await api.get(
        "/reports/appointments/excel",
        {
            responseType: "blob"
        }
    );

    return response.data;
};