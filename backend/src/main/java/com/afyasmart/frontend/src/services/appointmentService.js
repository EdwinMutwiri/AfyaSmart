import api from "./api";

export const bookAppointment = async (appointment) => {
    const response = await api.post("/appointments", appointment);
    return response.data;
};

export const getAppointments = async (accountId) => {
    const response = await api.get(`/appointments/${accountId}`);
    return response.data;
};

export const cancelAppointment = async (appointmentId) => {
    const response = await api.put(`/appointments/cancel/${appointmentId}`);
    return response.data;
};