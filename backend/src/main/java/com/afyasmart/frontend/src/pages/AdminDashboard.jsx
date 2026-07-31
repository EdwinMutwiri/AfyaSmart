/*
=========================================================
 Admin Dashboard
---------------------------------------------------------
 Purpose:
 Displays system-wide healthcare analytics for
 administrators.

 Features
 ✔ Dashboard summary cards
 ✔ Appointment analytics
 ✔ User analytics
 ✔ Doctor workload
 ✔ Responsive layout
 ✔ Loading state
 ✔ Error handling

 Author: AfyaSmart Project
=========================================================
*/

import { useEffect, useState } from "react";

import AppLayout from "../components/layout/AppLayout";

/*
=========================================================
 Dashboard Components
=========================================================
*/

import DashboardCard from "../components/dashboard/DashboardCard";
import StatusPieChart from "../components/dashboard/StatusPieChart";
import UserRoleChart from "../components/dashboard/UserRoleChart";
import DoctorWorkloadChart from "../components/dashboard/DoctorWorkloadChart";

/*
=========================================================
 Analytics Service
=========================================================
*/

import { getDashboardAnalytics } from "../services/dashboardAnalyticsService";

/*
=========================================================
 Icons
=========================================================
*/

import {

    Users,
    UserRound,
    UserCog,
    ShieldCheck,
    CalendarDays,
    Clock3,
    CheckCircle,
    CheckCheck,
    XCircle

} from "lucide-react";

/*
=========================================================
 Component
=========================================================
*/

export default function AdminDashboard() {

    /*
    -----------------------------------------------------
    Dashboard analytics returned from backend
    -----------------------------------------------------
    */

    const [analytics, setAnalytics] = useState(null);

    /*
    -----------------------------------------------------
    Loading indicator
    -----------------------------------------------------
    */

    const [loading, setLoading] = useState(true);

    /*
    -----------------------------------------------------
    Error message
    -----------------------------------------------------
    */

    const [error, setError] = useState("");

    /*
    -----------------------------------------------------
    Load dashboard immediately after page opens
    -----------------------------------------------------
    */

    useEffect(() => {

        loadDashboard();

    }, []);

    /*
    -----------------------------------------------------
    Fetch analytics from backend
    -----------------------------------------------------
    */

    const loadDashboard = async () => {

        try {

            setLoading(true);

            const response = await getDashboardAnalytics();

            setAnalytics(response.data);

            setError("");

        }

        catch (err) {

            console.error(err);

            setError("Failed to load dashboard analytics.");

        }

        finally {

            setLoading(false);

        }

    };

    /*
    -----------------------------------------------------
    Loading Screen
    -----------------------------------------------------
    */

    if (loading) {

        return (

            <AppLayout>

                <div className="text-center mt-24">

                    <h2 className="text-3xl font-bold">

                        Loading Dashboard...

                    </h2>

                </div>

            </AppLayout>

        );

    }

    /*
    -----------------------------------------------------
    Error Screen
    -----------------------------------------------------
    */

    if (error) {

        return (

            <AppLayout>

                <div className="text-center mt-24">

                    <h2 className="text-red-600 text-2xl font-bold">

                        {error}

                    </h2>

                </div>

            </AppLayout>

        );

    }

    /*
    -----------------------------------------------------
    Dashboard Cards
    -----------------------------------------------------
    */

    const cards = [

        {
            title: "Total Users",
            value: analytics.totalUsers,
            icon: Users,
            color: "bg-blue-600"
        },

        {
            title: "Patients",
            value: analytics.totalPatients,
            icon: UserRound,
            color: "bg-green-600"
        },

        {
            title: "Doctors",
            value: analytics.totalDoctors,
            icon: UserCog,
            color: "bg-purple-600"
        },

        {
            title: "Admins",
            value: analytics.totalAdmins,
            icon: ShieldCheck,
            color: "bg-indigo-600"
        },

        {
            title: "Appointments",
            value: analytics.totalAppointments,
            icon: CalendarDays,
            color: "bg-orange-600"
        },

        {
            title: "Pending",
            value: analytics.pendingAppointments,
            icon: Clock3,
            color: "bg-yellow-500"
        },

        {
            title: "Confirmed",
            value: analytics.confirmedAppointments,
            icon: CheckCircle,
            color: "bg-cyan-600"
        },

        {
            title: "Completed",
            value: analytics.completedAppointments,
            icon: CheckCheck,
            color: "bg-emerald-600"
        },

        {
            title: "Cancelled",
            value: analytics.cancelledAppointments,
            icon: XCircle,
            color: "bg-red-600"
        }

    ];
/* PART 2 OF CODE*/
    /*
    -----------------------------------------------------
    Render Dashboard
    -----------------------------------------------------
    */

    return (

        <AppLayout>

            {/* ============================================
                Dashboard Header
            ============================================ */}

            <div className="mb-10">

                <h1 className="text-4xl font-bold text-gray-800">

                    Admin Analytics Dashboard

                </h1>

                <p className="text-gray-500 mt-2">

                    Welcome to the AfyaSmart Administration Portal.
                    Monitor users, appointments and doctor workload
                    in real time.

                </p>

            </div>

            {/* ============================================
                Summary Cards
            ============================================ */}

            <div className="grid grid-cols-1 md:grid-cols-2 xl:grid-cols-3 2xl:grid-cols-5 gap-6">

                {

                    cards.map((card) => (

                        <DashboardCard

                            key={card.title}

                            title={card.title}

                            value={card.value}

                            icon={card.icon}

                            color={card.color}

                        />

                    ))

                }

            </div>

            {/* ============================================
                Analytics Charts
            ============================================ */}

            <div className="grid grid-cols-1 xl:grid-cols-2 gap-8 mt-10">

                {/* Appointment Status */}

                <StatusPieChart

                    data={analytics.appointmentStatus}

                />

                {/* User Roles */}

                <UserRoleChart

                    data={analytics.userRoles}

                />

            </div>

            {/* ============================================
                Doctor Workload
            ============================================ */}

            <div className="mt-10">

                <DoctorWorkloadChart

                    data={analytics.doctorWorkload}

                />

            </div>

            {/* ============================================
                Quick Statistics
            ============================================ */}

            <div className="bg-white rounded-2xl shadow-lg p-8 mt-10">

                <h2 className="text-2xl font-bold mb-6">

                    System Overview

                </h2>

                <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">

                    <div>

                        <p className="text-gray-500">

                            Active Doctors

                        </p>

                        <h2 className="text-3xl font-bold text-purple-600 mt-2">

                            {analytics.totalDoctors}

                        </h2>

                    </div>

                    <div>

                        <p className="text-gray-500">

                            Registered Patients

                        </p>

                        <h2 className="text-3xl font-bold text-green-600 mt-2">

                            {analytics.totalPatients}

                        </h2>

                    </div>

                    <div>

                        <p className="text-gray-500">

                            Total Appointments

                        </p>

                        <h2 className="text-3xl font-bold text-orange-600 mt-2">

                            {analytics.totalAppointments}

                        </h2>

                    </div>

                    <div>

                        <p className="text-gray-500">

                            Completed Consultations

                        </p>

                        <h2 className="text-3xl font-bold text-emerald-600 mt-2">

                            {analytics.completedAppointments}

                        </h2>

                    </div>

                </div>

            </div>

        </AppLayout>

    );

}