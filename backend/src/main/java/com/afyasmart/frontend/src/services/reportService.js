import api from "./api";

/**
 * ============================================================
 * AfyaSmart - Report Service
 * ============================================================
 *
 * Handles downloading PDF and Excel reports from the
 * AfyaSmart backend.
 *
 * Available reports:
 * - Appointments
 * - Patients
 * - Doctors
 * ============================================================
 */

// ============================================================
// APPOINTMENT REPORTS
// ============================================================

export const downloadAppointmentPdf = async () => {
    const response = await api.get(
        "/reports/appointments/pdf",
        {
            responseType: "blob"
        }
    );

    return response.data;
};

export const downloadAppointmentExcel = async () => {
    const response = await api.get(
        "/reports/appointments/excel",
        {
            responseType: "blob"
        }
    );

    return response.data;
};


// ============================================================
// PATIENT REPORTS
// ============================================================

export const downloadPatientPdf = async () => {
    const response = await api.get(
        "/reports/patients/pdf",
        {
            responseType: "blob"
        }
    );

    return response.data;
};

export const downloadPatientExcel = async () => {
    const response = await api.get(
        "/reports/patients/excel",
        {
            responseType: "blob"
        }
    );

    return response.data;
};


// ============================================================
// DOCTOR REPORTS
// ============================================================

export const downloadDoctorPdf = async () => {
    const response = await api.get(
        "/reports/doctors/pdf",
        {
            responseType: "blob"
        }
    );

    return response.data;
};

export const downloadDoctorExcel = async () => {
    const response = await api.get(
        "/reports/doctors/excel",
        {
            responseType: "blob"
        }
    );

    return response.data;
};