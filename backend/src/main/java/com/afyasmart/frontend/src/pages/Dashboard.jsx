import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { getLatestAssessment } from "../services/assessmentService";

export default function Dashboard() {

    const [user, setUser] =useState(null);
    const [assessment, setAssessment] = useState(null);

    useEffect(() => {

        const storedUser = JSON.parse(localStorage.getItem("user"));

        if (!storedUser) return;

        setUser(storedUser);

        loadAssessment(storedUser.id);

    }, []);

    const loadAssessment = async (accountId) => {

        try {

            const response = await getLatestAssessment(accountId);

            setAssessment(response.data);

        } catch (e) {

            console.log("No assessment found yet.");

        }

    };

    return (

        <div className="min-h-screen bg-slate-100">

            <div className="bg-blue-600 text-white p-6 shadow-lg">

                <h1 className="text-3xl font-bold">
                    AfyaSmart Dashboard
                </h1>

                <p className="mt-2">
                    Welcome {user?.firstName} 👋
                </p>

            </div>

            <div className="grid md:grid-cols-4 gap-6 p-8">

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

            {assessment && (

                <div className="mx-8 bg-white rounded-2xl shadow-lg p-6">

                    <h2 className="text-xl font-bold text-blue-600">

                        Latest Recommendation

                    </h2>

                    <p className="mt-4 text-gray-700">

                        {assessment.recommendation}

                    </p>

                </div>

            )}

            <div className="p-8">

                <Link
                    to="/assessment"
                    className="inline-block bg-blue-600 hover:bg-blue-700 text-white px-6 py-4 rounded-xl"
                >
                    Start New Health Assessment
                </Link>

                <Link
                    to="/assessment-history"
                    className="ml-4 inline-block bg-green-600 hover:bg-green-700 text-white px-6 py-4 rounded-xl"
                >
                    Assessment History
                </Link>

            </div>

        </div>

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