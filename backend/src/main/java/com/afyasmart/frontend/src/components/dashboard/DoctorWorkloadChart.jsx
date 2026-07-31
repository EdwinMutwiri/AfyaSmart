/*
=========================================================
 DoctorWorkloadChart Component
---------------------------------------------------------
 Displays the number of appointments assigned to
 each doctor.

 Features
 ✔ Responsive
 ✔ Horizontal Bar Chart
 ✔ Tooltip
 ✔ Professional styling
 ✔ Automatic scaling

 Uses:
 - Recharts

 Author: AfyaSmart Project
=========================================================
*/

import {

    ResponsiveContainer,
    BarChart,
    Bar,
    CartesianGrid,
    XAxis,
    YAxis,
    Tooltip

} from "recharts";

/*
=========================================================
 Component
=========================================================
*/

export default function DoctorWorkloadChart({ data }) {

    /*
    -----------------------------------------------------
    Convert backend Map into an array for Recharts
    -----------------------------------------------------
    */

    const chartData = Object.entries(data).map(

        ([doctor, appointments]) => ({

            doctor,
            appointments

        })

    );

    return (

        <div className="bg-white rounded-2xl shadow-lg p-6">

            <h2 className="text-xl font-bold mb-5">

                Doctor Workload

            </h2>

            <div className="h-96">

                <ResponsiveContainer
                    width="100%"
                    height="100%"
                >

                    <BarChart
                        data={chartData}
                        layout="vertical"
                        margin={{
                            top: 10,
                            right: 20,
                            left: 40,
                            bottom: 10
                        }}
                    >

                        <CartesianGrid strokeDasharray="3 3" />

                        <XAxis
                            type="number"
                            allowDecimals={false}
                        />

                        <YAxis
                            type="category"
                            dataKey="doctor"
                            width={140}
                        />

                        <Tooltip />

                        <Bar
                            dataKey="appointments"
                            fill="#8B5CF6"
                            radius={[0, 8, 8, 0]}
                        />

                    </BarChart>

                </ResponsiveContainer>

            </div>

        </div>

    );

}