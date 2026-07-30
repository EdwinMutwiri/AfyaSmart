import { useEffect, useState } from "react";
import AppLayout from "../components/layout/AppLayout";

import {
    getDoctorAppointments,
    confirmAppointment,
    completeAppointment,
    cancelAppointment
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

            const response = await getDoctorAppointments(doctorName);

            setAppointments(response.data);

        } catch (err) {

            console.error(err);

        }
    };

    const confirm = async (id) => {

        try {

            await confirmAppointment(id);

            loadAppointments();

        } catch (err) {

            console.error(err);

        }

    };

    const complete = async (id) => {

        try {

            await completeAppointment(id);

            loadAppointments();

        } catch (err) {

            console.error(err);

        }

    };

    const cancel = async (id) => {

        try {

            await cancelAppointment(id);

            loadAppointments();

        } catch (err) {

            console.error(err);

        }

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
                return "bg-gray-100 text-gray-700";

        }

    };

    return (

        <AppLayout>

            <div className="flex justify-between items-center mb-8">

                <div>

                    <h1 className="text-3xl font-bold">

                        Doctor Dashboard

                    </h1>

                    <p className="text-gray-500 mt-1">

                        Welcome Dr. {user.firstName}

                    </p>

                </div>

            </div>

            <div className="bg-white rounded-2xl shadow-lg overflow-hidden">

                <table className="w-full">

                    <thead className="bg-blue-600 text-white">

                        <tr>

                            <th className="p-4 text-left">Doctor</th>

                            <th className="p-4 text-left">Date</th>

                            <th className="p-4 text-left">Time</th>

                            <th className="p-4 text-left">Reason</th>

                            <th className="p-4 text-left">Status</th>

                            <th className="p-4 text-center">Actions</th>

                        </tr>

                    </thead>

                    <tbody>

                        {appointments.length === 0 ? (

                            <tr>

                                <td
                                    colSpan="6"
                                    className="text-center py-10 text-gray-500"
                                >

                                    No appointments available.

                                </td>

                            </tr>

                        ) : (

                            appointments.map((appointment) => (

                                <tr
                                    key={appointment.id}
                                    className="border-b hover:bg-gray-50"
                                >

                                    <td className="p-4">

                                        {appointment.doctorName}

                                    </td>

                                    <td className="p-4">

                                        {appointment.appointmentDate}

                                    </td>

                                    <td className="p-4">

                                        {appointment.appointmentTime}

                                    </td>

                                    <td className="p-4">

                                        {appointment.reason}

                                    </td>

                                    <td className="p-4">

                                        <span
                                            className={`px-3 py-1 rounded-full font-semibold ${badge(appointment.status)}`}
                                        >

                                            {appointment.status}

                                        </span>

                                    </td>

                                    <td className="p-4">

                                        <div className="flex flex-wrap gap-2 justify-center">

                                            {appointment.status === "PENDING" && (

                                                <>
                                                    <button
                                                        onClick={() => confirm(appointment.id)}
                                                        className="bg-green-600 hover:bg-green-700 text-white px-3 py-2 rounded-lg"
                                                    >
                                                        Confirm
                                                    </button>

                                                    <button
                                                        onClick={() => cancel(appointment.id)}
                                                        className="bg-red-600 hover:bg-red-700 text-white px-3 py-2 rounded-lg"
                                                    >
                                                        Cancel
                                                    </button>
                                                </>

                                            )}

                                            {appointment.status === "CONFIRMED" && (

                                                <>
                                                    <button
                                                        onClick={() => complete(appointment.id)}
                                                        className="bg-blue-600 hover:bg-blue-700 text-white px-3 py-2 rounded-lg"
                                                    >
                                                        Complete
                                                    </button>

                                                    <button
                                                        onClick={() => cancel(appointment.id)}
                                                        className="bg-red-600 hover:bg-red-700 text-white px-3 py-2 rounded-lg"
                                                    >
                                                        Cancel
                                                    </button>
                                                </>

                                            )}

                                            {(appointment.status === "COMPLETED" ||
                                                appointment.status === "CANCELLED") && (

                                                <span className="text-gray-500 font-medium">

                                                    No actions available

                                                </span>

                                            )}

                                        </div>

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