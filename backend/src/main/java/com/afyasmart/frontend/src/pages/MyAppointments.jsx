import { useEffect, useState } from "react";
import AppLayout from "../components/layout/AppLayout";
import {
    getAppointments,
    cancelAppointment
} from "../services/appointmentService";

export default function MyAppointments() {

    const user = JSON.parse(localStorage.getItem("user"));

    const [appointments, setAppointments] = useState([]);

    useEffect(() => {
        loadAppointments();
    }, []);

    const loadAppointments = async () => {

        try {

            const response = await getAppointments(user.id);

            setAppointments(response.data);

        } catch (error) {

            console.error(error);

        }

    };

    const handleCancel = async (id) => {

        if (!window.confirm("Cancel this appointment?")) return;

        try {

            await cancelAppointment(id);

            loadAppointments();

        } catch (error) {

            alert("Failed to cancel appointment.");

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
                return "bg-gray-100";

        }

    };

    return (

        <AppLayout>

            <h1 className="text-3xl font-bold mb-8">

                My Appointments

            </h1>

            <div className="bg-white rounded-2xl shadow-lg overflow-hidden">

                <table className="w-full">

                    <thead className="bg-blue-600 text-white">

                        <tr>

                            <th className="p-4 text-left">Doctor</th>

                            <th className="p-4 text-left">Specialization</th>

                            <th className="p-4 text-left">Date</th>

                            <th className="p-4 text-left">Time</th>

                            <th className="p-4 text-left">Status</th>

                            <th className="p-4 text-center">Action</th>

                        </tr>

                    </thead>

                    <tbody>

                        {appointments.length === 0 ? (

                            <tr>

                                <td
                                    colSpan="6"
                                    className="text-center py-10 text-gray-500"
                                >

                                    No appointments found.

                                </td>

                            </tr>

                        ) : (

                            appointments.map((appointment) => (

                                <tr
                                    key={appointment.id}
                                    className="border-b"
                                >

                                    <td className="p-4">

                                        {appointment.doctorName}

                                    </td>

                                    <td className="p-4">

                                        {appointment.specialization}

                                    </td>

                                    <td className="p-4">

                                        {appointment.appointmentDate}

                                    </td>

                                    <td className="p-4">

                                        {appointment.appointmentTime}

                                    </td>

                                    <td className="p-4">

                                        <span
                                            className={`px-3 py-1 rounded-full text-sm font-semibold ${badge(appointment.status)}`}
                                        >
                                            {appointment.status}
                                        </span>

                                    </td>

                                    <td className="p-4 text-center">

                                        {appointment.status === "PENDING" && (

                                            <button
                                                onClick={() => handleCancel(appointment.id)}
                                                className="bg-red-600 hover:bg-red-700 text-white px-4 py-2 rounded-lg"
                                            >
                                                Cancel
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