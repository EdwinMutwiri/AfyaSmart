import api from "./api";

export const getAllSpecializations = async () => {
    const response = await api.get("/specializations");
    return response.data;
};