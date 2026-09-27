import api from "./api";

/**
 * ============================================================
 * AfyaSmart - Medicine Service
 * ============================================================
 *
 * Handles all communication between the React frontend and
 * the Spring Boot Medicine API.
 *
 * Backend base:
 * /api/medicines
 * ============================================================
 */

/**
 * Create a new medicine reminder.
 */
export const createMedicine = async (medicine) => {
    const response = await api.post("/medicines", medicine);
    return response.data;
};

/**
 * Get one medicine by ID.
 */
export const getMedicine = async (medicineId) => {
    const response = await api.get(`/medicines/${medicineId}`);
    return response.data;
};

/**
 * Get all medicines belonging to a patient.
 */
export const getPatientMedicines = async (accountId) => {
    const response = await api.get(
        `/medicines/patient/${accountId}`
    );

    return response.data;
};

/**
 * Get only active medicines belonging to a patient.
 */
export const getActivePatientMedicines = async (accountId) => {
    const response = await api.get(
        `/medicines/patient/${accountId}/active`
    );

    return response.data;
};

/**
 * Update an existing medicine.
 */
export const updateMedicine = async (
    medicineId,
    medicine
) => {
    const response = await api.put(
        `/medicines/${medicineId}`,
        medicine
    );

    return response.data;
};

/**
 * Deactivate a medicine reminder.
 */
export const deactivateMedicine = async (medicineId) => {
    const response = await api.put(
        `/medicines/${medicineId}/deactivate`
    );

    return response.data;
};

/**
 * Permanently delete a medicine.
 */
export const deleteMedicine = async (medicineId) => {
    await api.delete(`/medicines/${medicineId}`);
};