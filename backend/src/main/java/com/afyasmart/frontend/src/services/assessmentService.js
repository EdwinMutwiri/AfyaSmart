import api from "./api";

export const analyzeHealth = async (assessmentData) => {
    const response = await api.post("/assessment", assessmentData);
    return response.data;
};

export const getLatestAssessment = async (accountId) => {
    const response = await api.get(`/assessment/latest/${accountId}`);
    return response.data;
};

export const getAssessmentHistory = async (accountId) => {
    const response = await api.get(`/assessment/history/${accountId}`);
    return response.data;
};