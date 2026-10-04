import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { getLatestAssessment } from "../services/assessmentService";
import { getRecommendedHealthTip } from "../services/healthTipService";
import AppLayout from "../components/layout/AppLayout";

export default function Dashboard() {

    const [user, setUser] = useState(null);
    const [assessment, setAssessment] = useState(null);
    const [healthTip, setHealthTip] = useState(null);

    useEffect(() => {

        const storedUser = JSON.parse(localStorage.getItem("user"));

        if (!storedUser) return;

        setUser(storedUser);

        loadAssessment(storedUser.id);
        loadHealthTip(storedUser.id);

    }, []);

    const loadAssessment = async (accountId) => {

        try {

            const response = await getLatestAssessment(accountId);

            setAssessment(response.data);

        } catch (e) {

            console.log("No assessment found yet.");

        }

    };

    const loadHealthTip = async (accountId) => {

        try {

            const response = await getRecommendedHealthTip(accountId);

            setHealthTip(response.data);

        } catch (e) {

            console.log("No personalized health tip available yet.");

        }

    };


    return (

        <AppLayout>

            {/* Welcome Header */}
            <div className="bg-blue-600 text-white p-6 rounded-2xl shadow-lg">

                <h1 className="text-3xl font-bold">
                    AfyaSmart Dashboard
                </h1>

                <p className="mt-2">
                    Welcome {user?.firstName} 👋
                </p>

            </div>


            {/* Health Summary Cards */}
            <div className="grid md:grid-cols-4 gap-6 mt-8">

                <Card
                    title="Health Score"
                    value={assessment?.healthScore ?? "--"}
                />

                <Card
                    title="BMI"
                    value={assessment?.bmi ?? "--"}
                />

                <Card
                    title="Risk Level"
                    value={assessment?.riskLevel ?? "--"}
                />

                <Card
                    title="Status"
                    value={
                        assessment
                            ? "Assessment Complete"
                            : "Pending"
                    }
                />

            </div>


            {/* Latest Assessment Recommendation */}
            {assessment && (

                <div className="mt-8 bg-white rounded-2xl shadow-lg p-6">

                    <h2 className="text-xl font-bold text-blue-600">
                        Latest Recommendation
                    </h2>

                    <p className="mt-4 text-gray-700">
                        {assessment.recommendation}
                    </p>

                </div>

            )}


            {/* AI Health Tip */}
            {healthTip && (

                <div className="mt-8 bg-white rounded-2xl shadow-lg p-6 border-l-4 border-purple-600">

                    <div className="flex items-center gap-3">

                        <div className="text-3xl">
                            🤖
                        </div>

                        <div>

                            <h2 className="text-xl font-bold text-purple-600">
                                AI Health Tip
                            </h2>

                            <p className="text-sm text-gray-500">
                                Personalized from your latest health assessment
                            </p>

                        </div>

                    </div>


                    <div className="mt-5">

                        <h3 className="text-lg font-bold text-gray-800">
                            {healthTip.title}
                        </h3>

                        <p className="mt-3 text-gray-700 leading-relaxed">
                            {healthTip.content}
                        </p>

                    </div>


                    <div className="mt-5 flex flex-wrap gap-3">

                        <span className="px-3 py-1 bg-purple-100 text-purple-700 rounded-full text-sm font-medium">
                            {healthTip.category}
                        </span>

                        {healthTip.conditionTag &&
                            healthTip.conditionTag !== "GENERAL" && (

                                <span className="px-3 py-1 bg-blue-100 text-blue-700 rounded-full text-sm font-medium">
                                    {healthTip.conditionTag}
                                </span>

                            )}

                    </div>

                </div>

            )}


            {/* Dashboard Actions */}
            <div className="mt-8 flex flex-wrap gap-4">

                <Link
                    to="/assessment"
                    className="bg-blue-600 hover:bg-blue-700 text-white px-6 py-4 rounded-xl"
                >
                    Start New Health Assessment
                </Link>

                <Link
                    to="/assessment-history"
                    className="bg-green-600 hover:bg-green-700 text-white px-6 py-4 rounded-xl"
                >
                    Assessment History
                </Link>

                <Link
                    to="/appointments"
                    className="bg-purple-600 hover:bg-purple-700 text-white px-6 py-4 rounded-xl"
                >
                    Book Appointment
                </Link>

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

            <p className="text-4xl font-bold text-blue-600 mt-3">
                {value}
            </p>

        </div>

    );

}