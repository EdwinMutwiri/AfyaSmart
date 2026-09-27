import {
    LayoutDashboard,
    HeartPulse,
    History,
    CalendarDays,
    UserRound,
    Users,
    UserCog,
    UserPlus,
    FileBarChart,
    Settings,
    LogOut
} from "lucide-react";

import { NavLink, useNavigate } from "react-router-dom";

import { Pill } from "lucide-react";

export default function Sidebar() {

    const navigate = useNavigate();

    const user = JSON.parse(localStorage.getItem("user"));

    const role = user?.role;

    let menu = [];

    // =========================
    // ADMIN MENU
    // =========================

    if (role === "ADMIN") {

        menu = [

            {
                title: "Admin Dashboard",
                icon: LayoutDashboard,
                path: "/admin-dashboard"
            },

            {
                title: "Users",
                icon: Users,
                path: "/users"
            },

            {
                title: "Doctors",
                icon: UserCog,
                path: "/doctors"
            },

            {
                title: "Add New Doctor",
                icon: UserPlus,
                path: "/create-doctor"
            },

            {
                title: "Appointments",
                icon: CalendarDays,
                path: "/admin-appointments"
            },

            {
                title: "Reports",
                icon: FileBarChart,
                path: "/admin-reports"
            },

            {
                title: "Settings",
                icon: Settings,
                path: "/settings"
            }

        ];

    }

    // =========================
    // DOCTOR MENU
    // =========================

    else if (role === "DOCTOR") {

        menu = [

            {
                title: "Doctor Dashboard",
                icon: LayoutDashboard,
                path: "/doctor-dashboard"
            },

            {
                title: "My Appointments",
                icon: CalendarDays,
                path: "/doctor-dashboard"
            },

            {
                title: "Profile",
                icon: UserRound,
                path: "/profile"
            },

            {
                title: "Settings",
                icon: Settings,
                path: "/settings"
            }

        ];

    }

    // =========================
    // PATIENT MENU
    // =========================

    else {

        menu = [

            {
                title: "Dashboard",
                icon: LayoutDashboard,
                path: "/dashboard"
            },

            {
                title: "Health Assessment",
                icon: HeartPulse,
                path: "/assessment"
            },

            {
                title: "Assessment History",
                icon: History,
                path: "/assessment-history"
            },

            {
                title: "Book Appointment",
                icon: CalendarDays,
                path: "/appointments"
            },

            {
                title: "My Appointments",
                icon: CalendarDays,
                path: "/my-appointments"
            },

            {
                title: "Doctors",
                icon: Users,
                path: "/doctors"
            },

            {
                title: "Profile",
                icon: UserRound,
                path: "/profile"
            },

            {
                title: "Settings",
                icon: Settings,
                path: "/settings"
            },

        {
            title: "Medicine Reminders",
            icon: Pill,
            path: "/medicines"
        }

        ];

    }

    return (

        <aside className="w-72 min-h-screen bg-slate-900 text-white shadow-xl relative">

            {/* =========================
                SIDEBAR HEADER
            ========================= */}

            <div className="p-6 border-b border-slate-700">

                <h1 className="text-3xl font-bold">
                    🏥 AfyaSmart
                </h1>

                <p className="text-sm text-slate-400 mt-2">
                    Smart Healthcare Platform
                </p>

            </div>


            {/* =========================
                NAVIGATION
            ========================= */}

            <nav className="mt-6 px-4 mb-24">

                {menu.map((item) => {

                    const Icon = item.icon;

                    return (

                        <NavLink
                            key={item.title}
                            to={item.path}
                            className={({ isActive }) =>
                                `flex items-center gap-4 p-4 rounded-xl mb-2 transition ${
                                    isActive
                                        ? "bg-blue-600"
                                        : "hover:bg-slate-800"
                                }`
                            }
                        >

                            <Icon size={22} />

                            <span>
                                {item.title}
                            </span>

                        </NavLink>

                    );

                })}

            </nav>


            {/* =========================
                LOGOUT
            ========================= */}

            <div className="absolute bottom-6 left-4 right-4">

                <button
                    onClick={() => {

                        localStorage.removeItem("user");

                        navigate("/login");

                    }}
                    className="flex items-center justify-center gap-4 w-full p-4 rounded-xl bg-red-600 hover:bg-red-700 transition"
                >

                    <LogOut size={20} />

                    Logout

                </button>

            </div>

        </aside>

    );

}