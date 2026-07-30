import api from "./api";

export const getUsers = async () => {
    const response = await api.get("/users");
    return response.data;
};

export const toggleUserStatus = async (userId) => {
    const response = await api.put(`/users/${userId}/toggle-status`);
    return response.data;
};