import api from "./api";

export const getAllDoctors = async () => {
    const response = await api.get("/doctors");

    console.log(response);

    return response.data;
};

export const getDoctorsBySpecialization = async (specializationId) => {
    const response = await api.get(`/doctors/specialization/${specializationId}`);

    return response.data;
};