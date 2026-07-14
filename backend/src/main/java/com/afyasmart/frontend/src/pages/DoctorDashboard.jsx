import { useEffect, useState } from "react";
import AppLayout from "../components/layout/AppLayout";
import {
    getDoctorAppointments,
    confirmAppointment,
    completeAppointment
} from "../services/appointmentService";

export default function DoctorDashboard() {

    const user = JSON.parse(localStorage.getItem("user"));

    const doctorName = `${user.firstName} ${user.lastName}`;

    const [appointments, setAppointments] = useState([]);

    useEffect(() => {
        loadAppointments();
    }, []);

    const loadAppointments = async () => {

        try {

            const response =
                await getDoctorAppointments(doctorName);

            setAppointments(response.data);

        } catch (err) {

            console.error(err);

        }

    };

    const confirm = async (id) => {

        await confirmAppointment(id);

        loadAppointments();

    };

    const complete = async (id) => {

        await completeAppointment(id);

        loadAppointments();

    };

    const badge = (status) => {

        switch (status) {

            case "PENDING":
                return "bg-yellow-100 text-yellow-700";

            case "CONFIRMED":
                return "bg-green-100 text-green-700";

            case "COMPLETED":
                return "bg-blue-100 text-blue-700";

            case "CANCELLED":
                return "bg-red-100 text-red-700";

            default:
                return "bg-gray-100";

        }

    };

    return (

        <AppLayout>

            <h1 className="text-3xl font-bold mb-8">

                Doctor Dashboard

            </h1>

            <div className="bg-white rounded-2xl shadow-lg overflow-hidden">

                <table className="w-full">

                    <thead className="bg-blue-600 text-white">

                        <tr>

                            <th className="p-4">Date</th>

                            <th className="p-4">Time</th>

                            <th className="p-4">Status</th>

                            <th className="p-4">Action</th>

                        </tr>

                    </thead>

                    <tbody>

                        {appointments.length === 0 ? (

                            <tr>

                                <td
                                    colSpan="4"
                                    className="text-center py-10"
                                >

                                    No appointments available.

                                </td>

                            </tr>

                        ) : (

                            appointments.map((appointment) => (

                                <tr
                                    key={appointment.id}
                                    className="border-b"
                                >

                                    <td className="p-4">

                                        {appointment.appointmentDate}

                                    </td>

                                    <td className="p-4">

                                        {appointment.appointmentTime}

                                    </td>

                                    <td className="p-4">

                                        <span
                                            className={`px-3 py-1 rounded-full ${badge(appointment.status)}`}
                                        >
                                            {appointment.status}
                                        </span>

                                    </td>

                                    <td className="p-4">

                                        {appointment.status === "PENDING" && (

                                            <button
                                                onClick={() => confirm(appointment.id)}
                                                className="bg-green-600 text-white px-4 py-2 rounded-lg"
                                            >
                                                Confirm
                                            </button>

                                        )}

                                        {appointment.status === "CONFIRMED" && (

                                            <button
                                                onClick={() => complete(appointment.id)}
                                                className="ml-2 bg-blue-600 text-white px-4 py-2 rounded-lg"
                                            >
                                                Complete
                                            </button>

                                        )}

                                    </td>

                                </tr>

                            ))

                        )}

                    </tbody>

                </table>

            </div>

        </AppLayout>

    );

}