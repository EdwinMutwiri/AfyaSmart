import { useEffect, useMemo, useState } from "react";
import AppLayout from "../components/layout/AppLayout";
import {
    getUsers,
    toggleUserStatus
} from "../services/userService";

export default function UserManagement() {

    const [users, setUsers] = useState([]);
    const [search, setSearch] = useState("");
    const [roleFilter, setRoleFilter] = useState("ALL");

    useEffect(() => {
        loadUsers();
    }, []);

    const loadUsers = async () => {

        try {

            const response = await getUsers();

            setUsers(response.data);

        } catch (error) {

            console.error(error);

        }

    };

    const toggleStatus = async (id) => {

        try {

            await toggleUserStatus(id);

            loadUsers();

        } catch (error) {

            console.error(error);

            alert("Unable to update user.");

        }

    };

    const filteredUsers = useMemo(() => {

        return users.filter(user => {

            const fullName =
                `${user.firstName} ${user.lastName}`.toLowerCase();

            const matchesSearch =
                fullName.includes(search.toLowerCase()) ||
                user.email.toLowerCase().includes(search.toLowerCase());

            const matchesRole =
                roleFilter === "ALL" ||
                user.role === roleFilter;

            return matchesSearch && matchesRole;

        });

    }, [users, search, roleFilter]);

    return (

        <AppLayout>

            <h1 className="text-4xl font-bold mb-8">

                User Management

            </h1>

            <div className="bg-white rounded-2xl shadow-lg p-6">

                <div className="flex gap-4 mb-6">

                    <input
                        type="text"
                        placeholder="Search..."
                        className="border rounded-xl p-3 flex-1"
                        value={search}
                        onChange={(e) =>
                            setSearch(e.target.value)
                        }
                    />

                    <select
                        className="border rounded-xl p-3"
                        value={roleFilter}
                        onChange={(e) =>
                            setRoleFilter(e.target.value)
                        }
                    >

                        <option value="ALL">All Roles</option>
                        <option value="ADMIN">Admin</option>
                        <option value="DOCTOR">Doctor</option>
                        <option value="PATIENT">Patient</option>

                    </select>

                </div>

                <table className="w-full">

                    <thead className="bg-blue-600 text-white">

                        <tr>

                            <th className="p-4 text-left">Name</th>
                            <th className="p-4 text-left">Email</th>
                            <th className="p-4 text-left">Role</th>
                            <th className="p-4 text-left">Status</th>
                            <th className="p-4 text-center">Actions</th>

                        </tr>

                    </thead>

                    <tbody>

                        {filteredUsers.map(user => (

                            <tr
                                key={user.id}
                                className="border-b hover:bg-gray-50"
                            >

                                <td className="p-4">

                                    {user.firstName} {user.lastName}

                                </td>

                                <td className="p-4">

                                    {user.email}

                                </td>

                                <td className="p-4">

                                    {user.role}

                                </td>

                                <td className="p-4">

                                    <span
                                        className={`px-3 py-1 rounded-full text-sm ${
                                            user.enabled
                                                ? "bg-green-100 text-green-700"
                                                : "bg-red-100 text-red-700"
                                        }`}
                                    >

                                        {user.enabled
                                            ? "Active"
                                            : "Disabled"}

                                    </span>

                                </td>

                                <td className="p-4 text-center">

                                    <button
                                        onClick={() =>
                                            toggleStatus(user.id)
                                        }
                                        className={`px-4 py-2 rounded-lg text-white ${
                                            user.enabled
                                                ? "bg-red-600 hover:bg-red-700"
                                                : "bg-green-600 hover:bg-green-700"
                                        }`}
                                    >

                                        {user.enabled
                                            ? "Disable"
                                            : "Enable"}

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