import { useEffect, useState } from "react";
import AppLayout from "../components/layout/AppLayout";
import { getDashboardStats } from "../services/dashboardService";

import {
    Users,
    UserRound,
    UserCog,
    CalendarDays,
    Clock3,
    CheckCircle,
    CheckCheck,
    XCircle
} from "lucide-react";

export default function AdminDashboard() {

    const [stats, setStats] = useState(null);

    useEffect(() => {
        loadStats();
    }, []);

    const loadStats = async () => {

        try {

            const response = await getDashboardStats();

            setStats(response.data);

        } catch (error) {

            console.error(error);

        }

    };

    if (!stats) {

        return (

            <AppLayout>

                <h2 className="text-2xl font-bold">

                    Loading Dashboard...

                </h2>

            </AppLayout>

        );

    }

    const cards = [

        {
            title: "Total Users",
            value: stats.totalUsers,
            icon: Users,
            color: "bg-blue-600"
        },

        {
            title: "Patients",
            value: stats.totalPatients,
            icon: UserRound,
            color: "bg-green-600"
        },

        {
            title: "Doctors",
            value: stats.totalDoctors,
            icon: UserCog,
            color: "bg-purple-600"
        },

        {
            title: "Appointments",
            value: stats.totalAppointments,
            icon: CalendarDays,
            color: "bg-orange-600"
        },

        {
            title: "Pending",
            value: stats.pendingAppointments,
            icon: Clock3,
            color: "bg-yellow-500"
        },

        {
            title: "Confirmed",
            value: stats.confirmedAppointments,
            icon: CheckCircle,
            color: "bg-cyan-600"
        },

        {
            title: "Completed",
            value: stats.completedAppointments,
            icon: CheckCheck,
            color: "bg-emerald-600"
        },

        {
            title: "Cancelled",
            value: stats.cancelledAppointments,
            icon: XCircle,
            color: "bg-red-600"
        }

    ];

    return (

        <AppLayout>

            <div className="mb-10">

                <h1 className="text-4xl font-bold">

                    Admin Dashboard

                </h1>

                <p className="text-gray-500 mt-2">

                    AfyaSmart Healthcare Management Overview

                </p>

            </div>

            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">

                {cards.map((card) => {

                    const Icon = card.icon;

                    return (

                        <div
                            key={card.title}
                            className="bg-white rounded-2xl shadow-lg p-6"
                        >

                            <div className="flex justify-between items-center">

                                <div>

                                    <p className="text-gray-500">

                                        {card.title}

                                    </p>

                                    <h2 className="text-4xl font-bold mt-3">

                                        {card.value}

                                    </h2>

                                </div>

                                <div
                                    className={`${card.color} text-white p-4 rounded-2xl`}
                                >

                                    <Icon size={30} />

                                </div>

                            </div>

                        </div>

                    );

                })}

            </div>

        </AppLayout>

    );

}