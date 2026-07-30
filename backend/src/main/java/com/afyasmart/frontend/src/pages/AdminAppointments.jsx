import { useEffect, useMemo, useState } from "react";
import AppLayout from "../components/layout/AppLayout";

import {
    getAllAppointments,
    confirmAppointment,
    completeAppointment,
    cancelAppointment
} from "../services/appointmentService";

export default function AdminAppointments() {

    // ==========================
    // State
    // ==========================

    const [appointments, setAppointments] = useState([]);

    const [loading, setLoading] = useState(true);

    const [search, setSearch] = useState("");

    const [statusFilter, setStatusFilter] = useState("ALL");


    // ==========================
    // Load Appointments
    // ==========================

    useEffect(() => {

        loadAppointments();

    }, []);


    const loadAppointments = async () => {

        try {

            setLoading(true);

            const response = await getAllAppointments();

            setAppointments(response.data);

        } catch (error) {

            console.error(error);

        } finally {

            setLoading(false);

        }

    };


    // ==========================
    // Appointment Actions
    // ==========================

    const confirm = async (id) => {

        await confirmAppointment(id);

        loadAppointments();

    };


    const complete = async (id) => {

        await completeAppointment(id);

        loadAppointments();

    };


    const cancel = async (id) => {

        if (!window.confirm("Cancel this appointment?"))
            return;

        await cancelAppointment(id);

        loadAppointments();

    };


    // ==========================
    // Search + Filter
    // ==========================

    const filteredAppointments = useMemo(() => {

        return appointments.filter((appointment) => {

            const keyword = search.toLowerCase();

            const matchesSearch =

                appointment.patientName
                    ?.toLowerCase()
                    .includes(keyword)

                ||

                appointment.patientEmail
                    ?.toLowerCase()
                    .includes(keyword)

                ||

                appointment.doctorName
                    ?.toLowerCase()
                    .includes(keyword);

            const matchesStatus =

                statusFilter === "ALL"

                ||

                appointment.status === statusFilter;

            return matchesSearch && matchesStatus;

        });

    }, [appointments, search, statusFilter]);


    // ==========================
    // Dashboard Statistics
    // ==========================

    const totalAppointments = appointments.length;

    const pendingAppointments = appointments.filter(
        a => a.status === "PENDING"
    ).length;

    const confirmedAppointments = appointments.filter(
        a => a.status === "CONFIRMED"
    ).length;

    const completedAppointments = appointments.filter(
        a => a.status === "COMPLETED"
    ).length;


    // ==========================
    // Status Badge Colors
    // ==========================

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

                        {/* ==========================================
                            Page Header
                        =========================================== */}

                        <div className="flex justify-between items-center mb-8">

                            <div>

                                <h1 className="text-4xl font-bold text-slate-800">

                                    Appointment Management

                                </h1>

                                <p className="text-slate-500 mt-2">

                                    Manage all appointments across the AfyaSmart platform.

                                </p>

                            </div>

                        </div>


                        {/* ==========================================
                            Statistics Cards
                        =========================================== */}

                        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-8">

                            <div className="bg-white rounded-2xl shadow-lg p-6">

                                <h3 className="text-slate-500">

                                    Total

                                </h3>

                                <h2 className="text-4xl font-bold mt-2">

                                    {totalAppointments}

                                </h2>

                            </div>

                            <div className="bg-yellow-50 rounded-2xl shadow-lg p-6">

                                <h3 className="text-yellow-700">

                                    Pending

                                </h3>

                                <h2 className="text-4xl font-bold mt-2">

                                    {pendingAppointments}

                                </h2>

                            </div>

                            <div className="bg-green-50 rounded-2xl shadow-lg p-6">

                                <h3 className="text-green-700">

                                    Confirmed

                                </h3>

                                <h2 className="text-4xl font-bold mt-2">

                                    {confirmedAppointments}

                                </h2>

                            </div>

                            <div className="bg-blue-50 rounded-2xl shadow-lg p-6">

                                <h3 className="text-blue-700">

                                    Completed

                                </h3>

                                <h2 className="text-4xl font-bold mt-2">

                                    {completedAppointments}

                                </h2>

                            </div>

                        </div>


                        {/* ==========================================
                            Search + Filter
                        =========================================== */}

                        <div className="bg-white rounded-2xl shadow-lg p-6 mb-8">

                            <div className="flex flex-col md:flex-row gap-4">

                                <input

                                    type="text"

                                    placeholder="Search patient, email or doctor..."

                                    value={search}

                                    onChange={(e) =>
                                        setSearch(e.target.value)
                                    }

                                    className="flex-1 border rounded-xl p-3"

                                />

                                <select

                                    value={statusFilter}

                                    onChange={(e) =>
                                        setStatusFilter(e.target.value)
                                    }

                                    className="border rounded-xl p-3"

                                >

                                    <option value="ALL">

                                        All Status

                                    </option>

                                    <option value="PENDING">

                                        Pending

                                    </option>

                                    <option value="CONFIRMED">

                                        Confirmed

                                    </option>

                                    <option value="COMPLETED">

                                        Completed

                                    </option>

                                    <option value="CANCELLED">

                                        Cancelled

                                    </option>

                                </select>

                            </div>

                        </div>


                        {/* ==========================================
                            Appointment Table
                        =========================================== */}

                        <div className="bg-white rounded-2xl shadow-lg overflow-hidden">

                            <table className="w-full">

                                <thead className="bg-blue-600 text-white">

                                    <tr>

                                        <th className="p-4 text-left">

                                            Patient

                                        </th>

                                        <th className="p-4 text-left">

                                            Doctor

                                        </th>

                                        <th className="p-4 text-left">

                                            Specialization

                                        </th>

                                        <th className="p-4">

                                            Date

                                        </th>

                                        <th className="p-4">

                                            Time

                                        </th>

                                        <th className="p-4">

                                            Status

                                        </th>

                                        <th className="p-4">

                                            Actions

                                        </th>

                                    </tr>

                                </thead>

                                <tbody>

                                    {loading ? (

                                        <tr>

                                            <td
                                                colSpan="7"
                                                className="text-center py-12"
                                            >

                                                Loading appointments...

                                            </td>

                                        </tr>

                                    ) : filteredAppointments.length === 0 ? (

                                        <tr>

                                            <td
                                                colSpan="7"
                                                className="text-center py-12"
                                            >

                                                No appointments found.

                                            </td>

                                        </tr>

                                    ) : (

                                        filteredAppointments.map((appointment) => (
                                                                            <tr
                                                                                key={appointment.id}
                                                                                className="border-b hover:bg-slate-50 transition"
                                                                            >

                                                                                {/* =========================
                                                                                    Patient
                                                                                ========================== */}

                                                                                <td className="p-4">

                                                                                    <div className="font-semibold">

                                                                                        {appointment.patientName}

                                                                                    </div>

                                                                                    <div className="text-sm text-gray-500">

                                                                                        {appointment.patientEmail}

                                                                                    </div>

                                                                                </td>


                                                                                {/* =========================
                                                                                    Doctor
                                                                                ========================== */}

                                                                                <td className="p-4">

                                                                                    {appointment.doctorName}

                                                                                </td>


                                                                                {/* =========================
                                                                                    Specialization
                                                                                ========================== */}

                                                                                <td className="p-4">

                                                                                    {appointment.specialization}

                                                                                </td>


                                                                                {/* =========================
                                                                                    Date
                                                                                ========================== */}

                                                                                <td className="p-4 text-center">

                                                                                    {appointment.appointmentDate}

                                                                                </td>


                                                                                {/* =========================
                                                                                    Time
                                                                                ========================== */}

                                                                                <td className="p-4 text-center">

                                                                                    {appointment.appointmentTime}

                                                                                </td>


                                                                                {/* =========================
                                                                                    Status
                                                                                ========================== */}

                                                                                <td className="p-4 text-center">

                                                                                    <span
                                                                                        className={`px-3 py-1 rounded-full text-sm font-semibold ${badge(
                                                                                            appointment.status
                                                                                        )}`}
                                                                                    >

                                                                                        {appointment.status}

                                                                                    </span>

                                                                                </td>


                                                                                {/* =========================
                                                                                    Actions
                                                                                ========================== */}

                                                                                <td className="p-4">

                                                                                    <div className="flex flex-wrap gap-2 justify-center">

                                                                                        {appointment.status ===
                                                                                            "PENDING" && (

                                                                                            <>
                                                                                                <button
                                                                                                    onClick={() =>
                                                                                                        confirm(
                                                                                                            appointment.id
                                                                                                        )
                                                                                                    }
                                                                                                    className="bg-green-600 hover:bg-green-700 text-white px-4 py-2 rounded-lg"
                                                                                                >

                                                                                                    Confirm

                                                                                                </button>

                                                                                                <button
                                                                                                    onClick={() =>
                                                                                                        cancel(
                                                                                                            appointment.id
                                                                                                        )
                                                                                                    }
                                                                                                    className="bg-red-600 hover:bg-red-700 text-white px-4 py-2 rounded-lg"
                                                                                                >

                                                                                                    Cancel

                                                                                                </button>
                                                                                            </>
                                                                                        )}

                                                                                        {appointment.status ===
                                                                                            "CONFIRMED" && (

                                                                                            <>
                                                                                                <button
                                                                                                    onClick={() =>
                                                                                                        complete(
                                                                                                            appointment.id
                                                                                                        )
                                                                                                    }
                                                                                                    className="bg-blue-600 hover:bg-blue-700 text-white px-4 py-2 rounded-lg"
                                                                                                >

                                                                                                    Complete

                                                                                                </button>

                                                                                                <button
                                                                                                    onClick={() =>
                                                                                                        cancel(
                                                                                                            appointment.id
                                                                                                        )
                                                                                                    }
                                                                                                    className="bg-red-600 hover:bg-red-700 text-white px-4 py-2 rounded-lg"
                                                                                                >

                                                                                                    Cancel

                                                                                                </button>
                                                                                            </>
                                                                                        )}

                                                                                        {appointment.status ===
                                                                                            "COMPLETED" && (

                                                                                            <span className="text-green-600 font-semibold">

                                                                                                ✓ Completed

                                                                                            </span>

                                                                                        )}

                                                                                        {appointment.status ===
                                                                                            "CANCELLED" && (

                                                                                            <span className="text-red-600 font-semibold">

                                                                                                Cancelled

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