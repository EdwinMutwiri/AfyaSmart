import { useEffect, useState } from "react";
import { bookAppointment } from "../services/appointmentService";
import { getAllDoctors } from "../services/doctorService";

export default function AppointmentPage() {

    const user = JSON.parse(localStorage.getItem("user"));

    const [doctors, setDoctors] = useState([]);

    const [form, setForm] = useState({
        doctorName: "",
        specialization: "",
        appointmentDate: "",
        appointmentTime: "",
        reason: ""
    });

    useEffect(() => {

        loadDoctors();

    }, []);

    const loadDoctors = async () => {

        try {

            const response = await getAllDoctors();

            setDoctors(response.data);

        } catch (error) {

            console.error(error);

        }

    };

    const handleDoctorChange = (e) => {

        const doctor = doctors.find(
            d => d.doctorName === e.target.value
        );

        setForm({

            ...form,

            doctorName: doctor.doctorName,

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

        } catch (error) {

            alert("Failed to book appointment.");

        }

    };

    return (

        <div className="min-h-screen bg-slate-100 p-10">

            <div className="max-w-2xl mx-auto bg-white rounded-3xl shadow-xl p-8">

                <h1 className="text-3xl font-bold text-blue-600 mb-8">

                    Book Appointment

                </h1>

                <form
                    onSubmit={handleSubmit}
                    className="space-y-5"
                >

                    <select
                        className="w-full border rounded-xl p-3"
                        value={form.doctorName}
                        onChange={handleDoctorChange}
                    >

                        <option value="">

                            Select Doctor

                        </option>

                        {doctors.map((doctor) => (

                            <option
                                key={doctor.id}
                                value={doctor.doctorName}
                            >

                                {doctor.doctorName}

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
                        value={form.appointmentDate}
                        onChange={handleChange}
                    />

                    <input
                        type="time"
                        name="appointmentTime"
                        className="w-full border rounded-xl p-3"
                        value={form.appointmentTime}
                        onChange={handleChange}
                    />

                    <textarea
                        name="reason"
                        rows="4"
                        className="w-full border rounded-xl p-3"
                        placeholder="Reason for appointment"
                        value={form.reason}
                        onChange={handleChange}
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