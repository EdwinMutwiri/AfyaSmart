import { useEffect, useState } from "react";
import AppLayout from "../components/layout/AppLayout";
import { getAllUsers } from "../services/userService";

import { useNavigate } from "react-router-dom";


export default function UserManagement() {

    const [users, setUsers] = useState([]);
    const navigate = useNavigate();

    useEffect(() => {
        loadUsers();
    }, []);

    const loadUsers = async () => {
        try {
            const response = await getAllUsers();
            setUsers(response.data);
        } catch (error) {
            console.error(error);
        }
    };

    return (

        <AppLayout>

            <div className="flex justify-between items-center mb-8">

                <h1 className="text-3xl font-bold">
                    User Management
                </h1>

                <button
                    onClick={() => navigate("/create-doctor")}
                    className="bg-blue-600 hover:bg-blue-700 text-white px-6 py-3 rounded-xl"
                >
                    + Create Doctor
                </button>

            </div>

            <div className="bg-white rounded-2xl shadow-lg overflow-hidden">

                <table className="w-full">

                    <thead className="bg-slate-100">

                        <tr>

                            <th className="p-4 text-left">Name</th>

                            <th className="p-4 text-left">Email</th>

                            <th className="p-4 text-left">Role</th>

                            <th className="p-4 text-left">Status</th>

                            <th className="p-4 text-left">Actions</th>

                        </tr>

                    </thead>

                    <tbody>

                        {users.map((user) => (

                            <tr
                                key={user.id}
                                className="border-t"
                            >

                                <td className="p-4">
                                    {user.firstName} {user.lastName}
                                </td>

                                <td className="p-4">
                                    {user.email}
                                </td>

                                <td className="p-4">

                                    <span className="px-3 py-1 rounded-full bg-blue-100 text-blue-700">

                                        {user.role}

                                    </span>

                                </td>

                                <td className="p-4">

                                    {user.enabled ? (
                                        <span className="text-green-600">
                                            Active
                                        </span>
                                    ) : (
                                        <span className="text-red-600">
                                            Disabled
                                        </span>
                                    )}

                                </td>

                                <td className="p-4">

                                    <button
                                        className="bg-yellow-500 hover:bg-yellow-600 text-white px-4 py-2 rounded-lg"
                                    >
                                        Edit
                                    </button>

                                </td>

                            </tr>

                        ))}

                    </tbody>

                </table>

            </div>

        </AppLayout>

    );

}