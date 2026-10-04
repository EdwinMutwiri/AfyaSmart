import axios from "axios";

const API_URL = "http://localhost:8080/api/health-tips";

/**
 * Get a personalized health tip based on
 * the patient's latest health assessment.
 */
export const getRecommendedHealthTip = async (accountId) => {
    const response = await axios.get(
        `${API_URL}/recommend/${accountId}`
    );

    return response.data;
};