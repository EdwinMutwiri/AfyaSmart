import { useForm } from "react-hook-form";
import { Link } from "react-router-dom";
import { HeartPulse } from "lucide-react";

export default function LoginPage() {

    const {
        register,
        handleSubmit
    } = useForm();

    const onSubmit = (data) => {
        console.log(data);

        // Backend connection comes next
    };

    return (

        <div className="min-h-screen flex items-center justify-center bg-gradient-to-br from-blue-600 via-blue-500 to-green-500">

            <div className="bg-white w-full max-w-md rounded-3xl shadow-2xl p-10">

                <div className="flex justify-center">

                    <HeartPulse
                        size={55}
                        className="text-blue-600"
                    />

                </div>

                <h1 className="text-3xl font-bold text-center mt-4">

                    Welcome Back

                </h1>

                <p className="text-center text-gray-500 mt-2">

                    Login to AfyaSmart

                </p>

                <form
                    onSubmit={handleSubmit(onSubmit)}
                    className="mt-8 space-y-5">

                    <input

                        {...register("email")}

                        placeholder="Email"

                        className="w-full border rounded-xl px-4 py-3 focus:outline-none focus:ring-2 focus:ring-blue-600"

                    />

                    <input

                        {...register("password")}

                        type="password"

                        placeholder="Password"

                        className="w-full border rounded-xl px-4 py-3 focus:outline-none focus:ring-2 focus:ring-blue-600"

                    />

                    <button

                        className="w-full bg-blue-600 hover:bg-blue-700 text-white rounded-xl py-3 font-semibold transition"

                    >

                        Login

                    </button>

                </form>

                <p className="text-center mt-8">

                    Don't have an account?

                    <Link
                        to="/register"
                        className="text-blue-600 font-semibold ml-2">

                        Register

                    </Link>

                </p>

            </div>

        </div>

    );

}