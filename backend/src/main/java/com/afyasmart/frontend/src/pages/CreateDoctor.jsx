import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import AppLayout from "../components/layout/AppLayout";
import { createDoctor } from "../services/adminDoctorService";
import { getAllSpecializations } from "../services/specializationService";

export default function CreateDoctor() {

    const navigate = useNavigate();

    const [specializations, setSpecializations] = useState([]);

    const [form, setForm] = useState({
        firstName: "",
        lastName: "",
        email: "",
        password: "",
        specializationId: "",
        hospital: "",
        licenseNumber: "",
        yearsExperience: "",
        consultationFee: "",
        bio: ""
    });

    useEffect(() => {
        loadSpecializations();
    }, []);

    const loadSpecializations = async () => {
        try {
            const response = await getAllSpecializations();
            setSpecializations(response.data);
        } catch (error) {
            console.error(error);
        }
    };

    const handleChange = (e) => {
        setForm({
            ...form,
            [e.target.name]: e.target.value
        });
    };

    const handleSubmit = async (e) => {

        e.preventDefault();

        try {

            await createDoctor(form);

            alert("Doctor created successfully!");

            navigate("/users");

        } catch (error) {

            alert(
                error.response?.data?.message ||
                "Failed to create doctor."
            );

        }

    };

    return (

        <AppLayout>

            <div className="max-w-3xl mx-auto bg-white rounded-2xl shadow-xl p-8">

                <h1 className="text-3xl font-bold text-blue-600 mb-8">
                    Create Doctor
                </h1>

                <form
                    onSubmit={handleSubmit}
                    className="grid grid-cols-2 gap-5"
                >

                    <input
                        name="firstName"
                        placeholder="First Name"
                        className="border rounded-xl p-3"
                        onChange={handleChange}
                    />

                    <input
                        name="lastName"
                        placeholder="Last Name"
                        className="border rounded-xl p-3"
                        onChange={handleChange}
                    />

                    <input
                        name="email"
                        type="email"
                        placeholder="Email"
                        className="border rounded-xl p-3 col-span-2"
                        onChange={handleChange}
                    />

                    <input
                        name="password"
                        type="password"
                        placeholder="Temporary Password"
                        className="border rounded-xl p-3 col-span-2"
                        onChange={handleChange}
                    />

                    <select
                        name="specializationId"
                        className="border rounded-xl p-3 col-span-2"
                        onChange={handleChange}
                    >

                        <option value="">
                            Select Specialization
                        </option>

                        {specializations.map((s) => (

                            <option
                                key={s.id}
                                value={s.id}
                            >
                                {s.name}
                            </option>

                        ))}

                    </select>

                    <input
                        name="hospital"
                        placeholder="Hospital"
                        className="border rounded-xl p-3"
                        onChange={handleChange}
                    />

                    <input
                        name="licenseNumber"
                        placeholder="License Number"
                        className="border rounded-xl p-3"
                        onChange={handleChange}
                    />

                    <input
                        name="yearsExperience"
                        type="number"
                        placeholder="Years of Experience"
                        className="border rounded-xl p-3"
                        onChange={handleChange}
                    />

                    <input
                        name="consultationFee"
                        type="number"
                        placeholder="Consultation Fee"
                        className="border rounded-xl p-3"
                        onChange={handleChange}
                    />

                    <textarea
                        name="bio"
                        rows="5"
                        placeholder="Doctor Biography"
                        className="border rounded-xl p-3 col-span-2"
                        onChange={handleChange}
                    />

                    <button
                        className="col-span-2 bg-blue-600 hover:bg-blue-700 text-white py-4 rounded-xl"
                    >
                        Create Doctor
                    </button>

                </form>

            </div>

        </AppLayout>

    );

}