import { useForm } from "react-hook-form";
import { Link } from "react-router-dom";

export default function RegisterPage() {

    const {

        register,

        handleSubmit

    } = useForm();

    const onSubmit = (data) => {

        console.log(data);

    };

    return (

        <div className="min-h-screen flex justify-center items-center bg-slate-100">

            <div className="bg-white rounded-3xl shadow-xl w-full max-w-lg p-10">

                <h1 className="text-3xl font-bold text-center">

                    Create Account

                </h1>

                <form
                    onSubmit={handleSubmit(onSubmit)}
                    className="space-y-4 mt-8">

                    <input
                        {...register("firstName")}
                        placeholder="First Name"
                        className="w-full border rounded-xl px-4 py-3"
                    />

                    <input
                        {...register("lastName")}
                        placeholder="Last Name"
                        className="w-full border rounded-xl px-4 py-3"
                    />

                    <input
                        {...register("email")}
                        placeholder="Email"
                        className="w-full border rounded-xl px-4 py-3"
                    />

                    <input
                        {...register("password")}
                        type="password"
                        placeholder="Password"
                        className="w-full border rounded-xl px-4 py-3"
                    />

                    <select
                        {...register("role")}
                        className="w-full border rounded-xl px-4 py-3">

                        <option value="PATIENT">

                            Patient

                        </option>

                        <option value="DOCTOR">

                            Doctor

                        </option>

                    </select>

                    <button

                        className="w-full bg-green-600 text-white rounded-xl py-3 hover:bg-green-700"

                    >

                        Register

                    </button>

                </form>

                <p className="text-center mt-6">

                    Already have an account?

                    <Link
                        to="/login"
                        className="text-blue-600 ml-2">

                        Login

                    </Link>

                </p>

            </div>

        </div>

    );

}