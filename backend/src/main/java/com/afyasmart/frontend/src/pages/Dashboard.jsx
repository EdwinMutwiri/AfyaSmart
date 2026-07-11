import { useEffect, useState } from "react";

export default function Dashboard() {

    const [user, setUser] = useState(null);

    useEffect(() => {

        const storedUser = localStorage.getItem("user");

        if (storedUser) {
            setUser(JSON.parse(storedUser));
        }

    }, []);

    return (

        <div className="min-h-screen bg-slate-100">

            <div className="bg-blue-600 text-white p-6 shadow-lg">

                <h1 className="text-3xl font-bold">

                    AfyaSmart Dashboard

                </h1>

                <p className="mt-2">

                    Welcome {user?.firstName} 👋

                </p>

            </div>

            <div className="grid md:grid-cols-4 gap-6 p-8">

                <Card title="Health Score" value="87%" />

                <Card title="BMI" value="24.2" />

                <Card title="Risk Level" value="Low" />

                <Card title="Appointments" value="2" />

            </div>

        </div>

    );
}

function Card({ title, value }) {

    return (

        <div className="bg-white rounded-2xl shadow-lg p-8">

            <h2 className="text-gray-500">

                {title}

            </h2>

            <p className="text-4xl font-bold text-blue-600 mt-3">

                {value}

            </p>

        </div>

    );

}