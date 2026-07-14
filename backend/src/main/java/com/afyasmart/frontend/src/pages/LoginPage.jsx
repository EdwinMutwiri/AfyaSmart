import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { login } from "../services/authService";

export default function LoginPage() {

    const navigate = useNavigate();

    const [form, setForm] = useState({
        email: "",
        password: ""
    });

    const [loading, setLoading] = useState(false);

    const handleChange = (e) => {
        setForm({
            ...form,
            [e.target.name]: e.target.value
        });
    };

    const handleSubmit = async (e) => {

        e.preventDefault();

        try {

            setLoading(true);

            const response = await login(form);

            const user = response.data;

            localStorage.setItem(
                "user",
                JSON.stringify(user)
            );

            alert("Login Successful!");

            if (user.role === "ADMIN") {

                navigate("/admin-dashboard");

            } else if (user.role === "DOCTOR") {

                navigate("/doctor-dashboard");

            } else {

                navigate("/dashboard");

            }

        } catch (error) {

            alert(
                error.response?.data?.message ||
                "Invalid credentials"
            );

        } finally {

            setLoading(false);

        }

    };

    return (

        <div className="min-h-screen bg-slate-100 flex justify-center items-center">

            <div className="bg-white p-10 rounded-3xl shadow-xl w-[420px]">

                <h1 className="text-3xl font-bold text-center text-blue-600">

                    Welcome Back

                </h1>

                <p className="text-center text-gray-500 mt-2">

                    Login to AfyaSmart

                </p>

                <form
                    onSubmit={handleSubmit}
                    className="mt-8 space-y-5"
                >

                    <input
                        type="email"
                        name="email"
                        value={form.email}
                        onChange={handleChange}
                        placeholder="Email"
                        className="w-full border rounded-xl p-3"
                    />

                    <input
                        type="password"
                        name="password"
                        value={form.password}
                        onChange={handleChange}
                        placeholder="Password"
                        className="w-full border rounded-xl p-3"
                    />

                    <button
                        type="submit"
                        disabled={loading}
                        className="w-full bg-blue-600 text-white rounded-xl p-3 hover:bg-blue-700"
                    >
                        {loading ? "Logging in..." : "Login"}
                    </button>

                </form>

                <p className="text-center mt-6">

                    Don't have an account?

                    <Link
                        className="text-blue-600 ml-2"
                        to="/register"
                    >
                        Register
                    </Link>

                </p>

            </div>

        </div>

    );
}