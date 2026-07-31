/*
=========================================================
 DashboardCard Component
---------------------------------------------------------
 Displays a reusable statistics card on the Admin Dashboard.

 Features:
 ✔ Displays title
 ✔ Displays value
 ✔ Displays icon
 ✔ Custom background colour
 ✔ Hover animation
 ✔ Responsive design

 Author: AfyaSmart Project
=========================================================
*/

export default function DashboardCard({

    title,
    value,
    icon: Icon,
    color

}) {

    return (

        <div
            className="
                bg-white
                rounded-2xl
                shadow-lg
                p-6
                transition
                duration-300
                hover:shadow-2xl
                hover:-translate-y-1
            "
        >

            {/* Card Layout */}

            <div className="flex justify-between items-center">

                {/* Statistics */}

                <div>

                    <p className="text-gray-500 text-sm">

                        {title}

                    </p>

                    <h2 className="text-4xl font-bold mt-3">

                        {value}

                    </h2>

                </div>

                {/* Icon */}

                <div
                    className={`
                        ${color}
                        text-white
                        p-4
                        rounded-2xl
                    `}
                >

                    <Icon size={30} />

                </div>

            </div>

        </div>

    );

}