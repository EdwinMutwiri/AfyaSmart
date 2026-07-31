/*
=========================================================
 StatusPieChart Component
---------------------------------------------------------
 Displays the distribution of appointment statuses.

 Features
 ✔ Responsive
 ✔ Interactive Pie Chart
 ✔ Tooltip
 ✔ Legend
 ✔ Professional colours

 Uses:
 - Recharts
=========================================================
*/

import {

    PieChart,
    Pie,
    Cell,
    Tooltip,
    Legend,
    ResponsiveContainer

} from "recharts";

/*
=========================================================
 Chart Colours
=========================================================
*/

const COLORS = [

    "#FACC15",   // Pending
    "#06B6D4",   // Confirmed
    "#10B981",   // Completed
    "#EF4444"    // Cancelled

];

/*
=========================================================
 Component
=========================================================
*/

export default function StatusPieChart({ data }) {

    /*
    ---------------------------------------------
    Convert API object into Recharts array
    ---------------------------------------------
    */

    const chartData = Object.entries(data).map(

        ([name, value]) => ({

            name,
            value

        })

    );

    return (

        <div className="bg-white rounded-2xl shadow-lg p-6">

            <h2 className="text-xl font-bold mb-5">

                Appointment Status

            </h2>

            <div className="h-80">

                <ResponsiveContainer
                    width="100%"
                    height="100%"
                >

                    <PieChart>

                        <Pie

                            data={chartData}

                            dataKey="value"

                            nameKey="name"

                            outerRadius={110}

                            label

                        >

                            {

                                chartData.map((entry, index) => (

                                    <Cell

                                        key={index}

                                        fill={COLORS[index % COLORS.length]}

                                    />

                                ))

                            }

                        </Pie>

                        <Tooltip />

                        <Legend />

                    </PieChart>

                </ResponsiveContainer>

            </div>

        </div>

    );

}