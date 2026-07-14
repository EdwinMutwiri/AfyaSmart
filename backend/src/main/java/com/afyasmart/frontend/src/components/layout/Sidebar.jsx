import {
    LayoutDashboard,
    HeartPulse,
    History,
    CalendarDays,
    UserRound,
    Users,
    Settings,
    Shield,
    LogOut
} from "lucide-react";

import { NavLink, useNavigate } from "react-router-dom";


export default function Sidebar() {

    const user = JSON.parse(localStorage.getItem("user"));
    const navigate = useNavigate();

    let menu = [];

    // ===========================
    // PATIENT MENU
    // ===========================

    if (user?.role === "PATIENT") {

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
            }
        ];

    }

    // ===========================
    // DOCTOR MENU
    // ===========================

    if (user?.role === "DOCTOR") {

        menu = [
            {
                title: "Doctor Dashboard",
                icon: LayoutDashboard,
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

    // ===========================
    // ADMIN MENU
    // ===========================

    if (user?.role === "ADMIN") {

        menu = [
            {
                title: "Dashboard",
                icon: Shield,
                path: "/admin-dashboard"
            },
            {
                title: "Doctors",
                icon: Users,
                path: "/doctors"
            },

            {
                title: "User Management",
                icon: Users,
                path: "/users"
            },

            {
                title: "Settings",
                icon: Settings,
                path: "/settings"
            }
        ];

    }

    return (

        <aside className="relative flex flex-col w-72 min-h-screen bg-slate-900 text-white shadow-xl">

            <div className="p-6 border-b border-slate-700">

                <h1 className="text-3xl font-bold">
                    🏥 AfyaSmart
                </h1>

                <p className="text-sm text-slate-400 mt-2">
                    Smart Healthcare Platform
                </p>

            </div>

            <nav className="mt-6 px-4">

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

                            <span>{item.title}</span>

                        </NavLink>

                    );

                })}

            </nav>

            <div className="mt-auto p-4 border-t border-slate-700">

                <button
                    onClick={() => {
                        localStorage.removeItem("user");
                        navigate("/login");
                    }}
                    className="flex items-center justify-center gap-3 w-full p-3 rounded-xl bg-red-600 hover:bg-red-700 transition"
                >
                    <LogOut size={20} />
                    Logout
                </button>

            </div>

        </aside>

    );

}