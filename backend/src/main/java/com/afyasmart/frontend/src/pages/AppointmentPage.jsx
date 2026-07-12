import { useState } from "react";
import { bookAppointment } from "../services/appointmentService";

export default function AppointmentPage() {

    const user = JSON.parse(localStorage.getItem("user"));

    const [form, setForm] = useState({
        doctorName: "",
        specialization: "",
        appointmentDate: "",
        appointmentTime: "",
        reason: ""
    });

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

            setForm({
                doctorName: "",
                specialization: "",
                appointmentDate: "",
                appointmentTime: "",
                reason: ""
            });

        } catch (err) {

            alert("Failed to book appointment.");

        }

    };

    return (

        <div className="min-h-screen bg-slate-100 p-10">

            <div className="max-w-2xl mx-auto bg-white rounded-3xl shadow-xl p-8">

                <h1 className="text-3xl font-bold text-blue-600 mb-8">

                    Book Appointment

                </h1>

                <form onSubmit={handleSubmit} className="space-y-5">

                    <select
                        className="w-full border rounded-xl p-3"
                        onChange={handleDoctorChange}
                        value={form.doctorName}
                    >

                        <option>Select Doctor</option>

                        {doctors.map((doctor) => (

                            <option
                                key={doctor.name}
                                value={doctor.name}
                            >
                                {doctor.name}
                            </option>

                        ))}

                    </select>

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
                    />

                    <input
                        type="time"
                        name="appointmentTime"
                        className="w-full border rounded-xl p-3"
                        onChange={handleChange}
                        value={form.appointmentTime}
                    />

                    <textarea
                        name="reason"
                        className="w-full border rounded-xl p-3"
                        rows="4"
                        placeholder="Reason for appointment"
                        onChange={handleChange}
                        value={form.reason}
                    />

                    <button
                        className="w-full bg-blue-600 hover:bg-blue-700 text-white py-4 rounded-xl"
                    >
                        Book Appointment
                    </button>

                </form>

            </div>

        </div>

    );

}