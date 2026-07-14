import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { register } from "../services/authService";

export default function RegisterPage() {

    const navigate = useNavigate();

    const [form, setForm] = useState({
        firstName: "",
        lastName: "",
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

            await register(form);

            alert("Registration Successful!");

            navigate("/login");

        } catch (error) {

            alert(
                error.response?.data?.message ||
                "Registration Failed"
            );

        } finally {

            setLoading(false);

        }
    };

    return (

        <div className="min-h-screen bg-slate-100 flex justify-center items-center">

            <div className="bg-white p-10 rounded-3xl shadow-xl w-[450px]">

                <h1 className="text-3xl font-bold text-center text-blue-600">

                    Create Account

                </h1>

                <form
                    onSubmit={handleSubmit}
                    className="space-y-4 mt-8"
                >

                    <input
                        name="firstName"
                        value={form.firstName}
                        onChange={handleChange}
                        placeholder="First Name"
                        className="w-full border rounded-xl p-3"
                    />

                    <input
                        name="lastName"
                        value={form.lastName}
                        onChange={handleChange}
                        placeholder="Last Name"
                        className="w-full border rounded-xl p-3"
                    />

                    <input
                        name="email"
                        type="email"
                        value={form.email}
                        onChange={handleChange}
                        placeholder="Email"
                        className="w-full border rounded-xl p-3"
                    />

                    <input
                        name="password"
                        type="password"
                        value={form.password}
                        onChange={handleChange}
                        placeholder="Password"
                        className="w-full border rounded-xl p-3"
                    />

                    <button
                        type="submit"
                        disabled={loading}
                        className="w-full bg-green-600 text-white rounded-xl p-3 hover:bg-green-700"
                    >
                        {loading ? "Creating..." : "Register"}
                    </button>

                </form>

                <p className="text-center mt-5">

                    Already have an account?

                    <Link
                        className="text-blue-600 ml-2"
                        to="/login"
                    >
                        Login
                    </Link>

                </p>

            </div>

        </div>

    );
}