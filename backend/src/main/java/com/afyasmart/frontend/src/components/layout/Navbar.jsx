import { Bell } from "lucide-react";

export default function Navbar() {

    const user = JSON.parse(localStorage.getItem("user"));

    return (

        <header className="bg-white shadow px-8 py-5 flex justify-between items-center">

            <div>

                <h2 className="text-2xl font-bold text-slate-800">

                    Welcome back,

                    <span className="text-blue-600">

                        {" "}{user?.firstName}

                    </span>

                </h2>

                <p className="text-gray-500">

                    Your health at a glance

                </p>

            </div>

            <div className="flex items-center gap-6">

                <Bell
                    size={24}
                    className="text-gray-500 cursor-pointer"
                />

                <div className="text-right">

                    <h3 className="font-semibold">

                        {user?.firstName} {user?.lastName}

                    </h3>

                    <small className="text-gray-500">

                        {user?.role}

                    </small>

                </div>

            </div>

        </header>

    );

}