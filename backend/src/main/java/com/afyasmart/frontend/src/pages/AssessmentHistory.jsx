import { useEffect, useState } from "react";
import { getAssessmentHistory } from "../services/assessmentService";
import HealthScoreChart from "../components/HealthScoreChart";
import BMIChart from "../components/BMIChart";

export default function AssessmentHistory() {

    const [history, setHistory] = useState([]);

    useEffect(() => {

        const user = JSON.parse(localStorage.getItem("user"));

        if (user) {
            loadHistory(user.id);
        }

    }, []);

    const loadHistory = async (accountId) => {

        try {

            const response = await getAssessmentHistory(accountId);

            const formattedData = response.data.map(item => ({
                ...item,
                assessmentDate: new Date(item.assessmentDate).toLocaleDateString()
            }));

            setHistory(formattedData);

        } catch (error) {

            console.error(error);

        }

    };

    return (

        <div className="min-h-screen bg-slate-100 p-8">

            <h1 className="text-4xl font-bold text-blue-700 mb-8">
                My Health Analytics
            </h1>

            <div className="grid lg:grid-cols-2 gap-8 mb-10">

                <HealthScoreChart data={history} />

                <BMIChart data={history} />

            </div>

            <div className="bg-white rounded-3xl shadow-xl p-8">

                <h2 className="text-2xl font-bold text-blue-600 mb-6">
                    Assessment History
                </h2>

                <table className="w-full">

                    <thead>

                        <tr className="border-b bg-gray-100">

                            <th className="p-4 text-left">Date</th>
                            <th className="p-4 text-left">Health Score</th>
                            <th className="p-4 text-left">BMI</th>
                            <th className="p-4 text-left">Risk</th>

                        </tr>

                    </thead>

                    <tbody>

                        {history.map((item, index) => (

                            <tr key={index} className="border-b hover:bg-gray-50">

                                <td className="p-4">{item.assessmentDate}</td>

                                <td className="p-4 font-bold text-blue-600">
                                    {item.healthScore}
                                </td>

                                <td className="p-4">
                                    {item.bmi}
                                </td>

                                <td className="p-4">
                                    <RiskBadge risk={item.riskLevel} />
                                </td>

                            </tr>

                        ))}

                    </tbody>

                </table>

            </div>

        </div>

    );

}

function RiskBadge({ risk }) {

    let color = "";

    switch (risk) {

        case "LOW":
            color = "bg-green-100 text-green-700";
            break;

        case "MODERATE":
            color = "bg-yellow-100 text-yellow-700";
            break;

        case "HIGH":
            color = "bg-orange-100 text-orange-700";
            break;

        case "CRITICAL":
            color = "bg-red-100 text-red-700";
            break;

        default:
            color = "bg-gray-100 text-gray-700";

    }

    return (

        <span className={`px-3 py-1 rounded-full font-semibold ${color}`}>
            {risk}
        </span>

    );

}