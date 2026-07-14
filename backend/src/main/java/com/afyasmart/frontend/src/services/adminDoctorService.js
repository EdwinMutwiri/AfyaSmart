import api from "./api";

export const createDoctor = async (doctor) => {
    const response = await api.post("/admin/doctors", doctor);
    return response.data;
};