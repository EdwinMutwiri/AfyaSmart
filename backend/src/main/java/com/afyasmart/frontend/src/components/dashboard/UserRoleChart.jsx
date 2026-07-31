/*
=========================================================
 UserRoleChart Component
---------------------------------------------------------
 Displays the number of users grouped by role.

 Roles:
 ✔ Patients
 ✔ Doctors
 ✔ Admins

 Features:
 ✔ Responsive
 ✔ Tooltip
 ✔ Legend
 ✔ Professional styling

 Uses:
 - Recharts
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

export default function UserRoleChart({ data }) {

    /*
    -----------------------------------------------------
    Convert the API object into an array that Recharts
    can understand.
    -----------------------------------------------------
    */

    const chartData = Object.entries(data).map(

        ([role, count]) => ({

            role,
            count

        })

    );

    return (

        <div className="bg-white rounded-2xl shadow-lg p-6">

            <h2 className="text-xl font-bold mb-5">

                User Distribution

            </h2>

            <div className="h-80">

                <ResponsiveContainer
                    width="100%"
                    height="100%"
                >

                    <BarChart
                        data={chartData}
                    >

                        <CartesianGrid strokeDasharray="3 3" />

                        <XAxis dataKey="role" />

                        <YAxis allowDecimals={false} />

                        <Tooltip />

                        <Bar
                            dataKey="count"
                            fill="#2563EB"
                            radius={[8, 8, 0, 0]}
                        />

                    </BarChart>

                </ResponsiveContainer>

            </div>

        </div>

    );

}