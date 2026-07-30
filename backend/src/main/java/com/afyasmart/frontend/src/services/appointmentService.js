import api from "./api";

/* =====================================================
   PATIENT APPOINTMENT APIs
   ===================================================== */

/**
 * Book a new appointment
 */
export const bookAppointment = async (appointment) => {
    const response = await api.post("/appointments", appointment);
    return response.data;
};

/**
 * Get appointments for a specific patient
 */
export const getAppointments = async (accountId) => {
    const response = await api.get(`/appointments/${accountId}`);
    return response.data;
};

/**
 * Cancel an appointment
 */
export const cancelAppointment = async (appointmentId) => {
    const response = await api.put(
        `/appointments/cancel/${appointmentId}`
    );
    return response.data;
};


/* =====================================================
   DOCTOR APPOINTMENT APIs
   ===================================================== */

/**
 * Get appointments assigned to a doctor
 */
export const getDoctorAppointments = async (doctorName) => {

    const response = await api.get(
        `/appointments/doctor/${encodeURIComponent(doctorName)}`
    );

    return response.data;
};

/**
 * Confirm an appointment
 */
export const confirmAppointment = async (appointmentId) => {

    const response = await api.put(
        `/appointments/confirm/${appointmentId}`
    );

    return response.data;
};

/**
 * Mark an appointment as completed
 */
export const completeAppointment = async (appointmentId) => {

    const response = await api.put(
        `/appointments/complete/${appointmentId}`
    );

    return response.data;
};


/* =====================================================
   ADMIN APPOINTMENT APIs
   ===================================================== */

/**
 * Get ALL appointments in the system
 */
export const getAllAppointments = async () => {

    const response = await api.get("/appointments");

    return response.data;
};