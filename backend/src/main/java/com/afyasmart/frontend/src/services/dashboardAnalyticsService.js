import api from "./api";

/*
=========================================
Dashboard Analytics Service
=========================================
*/

export const getDashboardAnalytics = async () => {

    const response = await api.get(
        "/dashboard/analytics"
    );

    return response.data;

};