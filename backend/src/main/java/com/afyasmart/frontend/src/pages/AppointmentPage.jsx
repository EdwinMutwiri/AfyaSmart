import { useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import AppLayout from "../components/layout/AppLayout";
import { bookAppointment } from "../services/appointmentService";

export default function AppointmentPage() {

    const user = JSON.parse(localStorage.getItem("user"));

    const location = useLocation();
    const navigate = useNavigate();

    const selectedDoctor = location.state;

    const doctors = [

        {
            name: "Dr. Mercy Wanjiku",
            specialization: "General Medicine"
        },
        {
            name: "Dr. David Otieno",
            specialization: "Cardiology"
        },
        {
            name: "Dr. Sarah Njeri",
            specialization: "Pediatrics"
        },
        {
            name: "Dr. Brian Mwangi",
            specialization: "Dermatology"
        },
        {
            name: "Dr. Faith Achieng",
            specialization: "Gynecology"
        }

    ];

    const [form, setForm] = useState({

        doctorName: selectedDoctor?.doctorName || "",

        specialization: selectedDoctor?.specialization || "",

        appointmentDate: "",

        appointmentTime: "",

        reason: ""

    });

    const handleDoctorChange = (e) => {

        const doctor = doctors.find(d => d.name === e.target.value);

        setForm({

            ...form,

            doctorName: doctor.name,

            specialization: doctor.specialization

        });

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

            await bookAppointment({

                accountId: user.id,

                doctorName: form.doctorName,

                specialization: form.specialization,

                appointmentDate: form.appointmentDate,

                appointmentTime: form.appointmentTime,

                reason: form.reason

            });

            alert("Appointment booked successfully!");

            navigate("/dashboard");

        } catch (err) {

            alert("Failed to book appointment.");

        }

    };

    return (

        <AppLayout>

            <div className="max-w-2xl mx-auto bg-white rounded-3xl shadow-xl p-8">

                <h1 className="text-3xl font-bold text-blue-600 mb-8">

                    Book Appointment

                </h1>

                {selectedDoctor && (

                    <div className="bg-blue-50 border border-blue-200 rounded-xl p-4 mb-6">

                        <h2 className="font-bold text-blue-700">

                            Selected Doctor

                        </h2>

                        <p className="mt-2 text-lg">

                            {form.doctorName}

                        </p>

                    </div>

                )}

                <form onSubmit={handleSubmit} className="space-y-5">

                    {!selectedDoctor && (

                        <select
                            className="w-full border rounded-xl p-3"
                            onChange={handleDoctorChange}
                            value={form.doctorName}
                        >

                            <option value="">

                                Select Doctor

                            </option>

                            {doctors.map((doctor) => (

                                <option
                                    key={doctor.name}
                                    value={doctor.name}
                                >

                                    {doctor.name}

                                </option>

                            ))}

                        </select>

                    )}

                    <input
                        className="w-full border rounded-xl p-3 bg-gray-100"
                        value={form.specialization}
                        readOnly
                    />

                    <input
                        type="date"
                        name="appointmentDate"
                        className="w-full border rounded-xl p-3"
                        onChange={handleChange}
                        value={form.appointmentDate}
                        required
                    />

                    <input
                        type="time"
                        name="appointmentTime"
                        className="w-full border rounded-xl p-3"
                        onChange={handleChange}
                        value={form.appointmentTime}
                        required
                    />

                    <textarea
                        name="reason"
                        rows="4"
                        placeholder="Reason for appointment"
                        className="w-full border rounded-xl p-3"
                        onChange={handleChange}
                        value={form.reason}
                        required
                    />

                    <button
                        className="w-full bg-blue-600 hover:bg-blue-700 text-white py-4 rounded-xl"
                    >

                        Confirm Appointment

                    </button>

                </form>

            </div>

        </AppLayout>

    );

}