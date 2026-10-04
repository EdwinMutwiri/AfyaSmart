import api from "./api";

// ============================================================
// MEDICINE CRUD
// ============================================================

// Create a new medicine
export const createMedicine = async (medicine) => {
    const response = await api.post("/medicines", medicine);
    return response.data;
};

// Get one medicine
export const getMedicine = async (medicineId) => {
    const response = await api.get(`/medicines/${medicineId}`);
    return response.data;
};

// Get all medicines belonging to a patient
export const getPatientMedicines = async (accountId) => {
    const response = await api.get(`/medicines/patient/${accountId}`);
    return response.data;
};

// Get only active medicines belonging to a patient
export const getActivePatientMedicines = async (accountId) => {
    const response = await api.get(
        `/medicines/patient/${accountId}/active`
    );

    return response.data;
};

// Update an existing medicine
export const updateMedicine = async (medicineId, medicine) => {
    const response = await api.put(
        `/medicines/${medicineId}`,
        medicine
    );

    return response.data;
};

// Deactivate a medicine
export const deactivateMedicine = async (medicineId) => {
    const response = await api.put(
        `/medicines/${medicineId}/deactivate`
    );

    return response.data;
};

// Delete a medicine
export const deleteMedicine = async (medicineId) => {
    await api.delete(`/medicines/${medicineId}`);
};


// ============================================================
// MEDICINE DOSE TRACKING
// ============================================================

/*
 * Mark ONE SPECIFIC scheduled dose as taken.
 *
 * IMPORTANT:
 * We now use doseId instead of medicineId.
 *
 * Example:
 *
 * POST /api/medicines/doses/15/taken
 *
 * Dose 15 might represent:
 * Paracetamol - 7:00 AM - 29 September 2026
 *
 * Another dose for the same medicine will have a different ID.
 */
export const markDoseAsTaken = async (doseId) => {
    const response = await api.post(
        `/medicines/doses/${doseId}/taken`
    );

    return response.data;
};


/*
 * Get all scheduled medicine doses for TODAY.
 *
 * Example:
 *
 * GET /api/medicines/patient/1/doses/today
 *
 * If a medicine has:
 *
 * 7:00 AM
 * 1:00 PM
 * 7:00 PM
 *
 * the backend will return three separate dose records.
 */
export const getTodayDoses = async (accountId) => {
    const response = await api.get(
        `/medicines/patient/${accountId}/doses/today`
    );

    return response.data;
};


/*
 * Get the complete dose history for one medicine.
 */
export const getMedicineDoseHistory = async (medicineId) => {
    const response = await api.get(
        `/medicines/${medicineId}/dose-history`
    );

    return response.data;
};