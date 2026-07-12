import {
    LineChart,
    Line,
    CartesianGrid,
    XAxis,
    YAxis,
    Tooltip,
    ResponsiveContainer
} from "recharts";

export default function HealthScoreChart({ data }) {

    return (

        <div className="bg-white rounded-2xl shadow-lg p-6">

            <h2 className="text-xl font-bold text-blue-600 mb-6">

                Health Score Trend

            </h2>

            <ResponsiveContainer width="100%" height={300}>

                <LineChart data={data}>

                    <CartesianGrid strokeDasharray="3 3" />

                    <XAxis dataKey="assessmentDate" />

                    <YAxis />

                    <Tooltip />

                    <Line
                        type="monotone"
                        dataKey="healthScore"
                        stroke="#2563eb"
                        strokeWidth={3}
                    />

                </LineChart>

            </ResponsiveContainer>

        </div>

    );

}