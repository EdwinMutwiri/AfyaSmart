import { useEffect, useState } from "react";
import AppLayout from "../components/layout/AppLayout";
import { getDashboardStatistics } from "../services/adminService";

export default function AdminDashboard() {

    const [stats, setStats] = useState({
        totalPatients: 0,
        totalDoctors: 0,
        totalAppointments: 0,
        totalAssessments: 0
    });

    useEffect(() => {
        loadDashboard();
    }, []);

    const loadDashboard = async () => {
        try {
            const response = await getDashboardStatistics();
            setStats(response.data);
        } catch (error) {
            console.error("Failed to load dashboard:", error);
        }
    };

    return (
        <AppLayout>

            <h1 className="text-4xl font-bold mb-8">
                Admin Dashboard
            </h1>

            <div className="grid md:grid-cols-4 gap-6">

                <Card
                    title="Patients"
                    value={stats.totalPatients}
                />

                <Card
                    title="Doctors"
                    value={stats.totalDoctors}
                />

                <Card
                    title="Appointments"
                    value={stats.totalAppointments}
                />

                <Card
                    title="Assessments"
                    value={stats.totalAssessments}
                />

            </div>

            <div className="mt-10 bg-white rounded-2xl shadow-lg p-8">

                <h2 className="text-2xl font-bold text-blue-600 mb-4">
                    Platform Overview
                </h2>

                <p className="text-gray-600">
                    AfyaSmart administration center.
                    Live statistics are loaded directly from the backend.
                </p>

            </div>

        </AppLayout>
    );
}

function Card({ title, value }) {
    return (
        <div className="bg-white rounded-2xl shadow-lg p-8">

            <h2 className="text-gray-500">
                {title}
            </h2>

            <p className="text-5xl font-bold text-blue-600 mt-4">
                {value}
            </p>

        </div>
    );
}