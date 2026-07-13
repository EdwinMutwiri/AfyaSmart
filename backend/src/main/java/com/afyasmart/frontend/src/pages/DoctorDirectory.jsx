import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import AppLayout from "../components/layout/AppLayout";
import { getAllDoctors } from "../services/doctorService";

export default function DoctorDirectory() {

    const [doctors, setDoctors] = useState([]);
    const navigate = useNavigate();

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

    return (

        <AppLayout>

            <h1 className="text-3xl font-bold mb-8">

                Available Doctors

            </h1>

            <div className="grid md:grid-cols-3 gap-6">

                {doctors.map((doctor) => (

                    <div
                        key={doctor.id}
                        className="bg-white rounded-2xl shadow-lg p-6 hover:shadow-xl transition"
                    >

                        <h2 className="text-2xl font-bold text-blue-600">

                            {doctor.doctorName}

                        </h2>

                        <p className="mt-2">

                            {doctor.specialization}

                        </p>

                        <p className="text-gray-500 mt-2">

                            🏥 {doctor.hospital}

                        </p>

                        <p className="mt-2">

                            {doctor.yearsExperience} Years Experience

                        </p>

                        <p className="mt-2 font-bold text-lg">

                            KES {doctor.consultationFee}

                        </p>

                        <button
                            onClick={() =>
                                navigate("/appointments", {
                                    state: {
                                        doctorId: doctor.id,
                                        doctorName: doctor.doctorName,
                                        specialization: doctor.specialization
                                    }
                                })
                            }
                            className="mt-6 w-full bg-blue-600 hover:bg-blue-700 text-white py-3 rounded-xl"
                        >
                            Book Appointment
                        </button>

                    </div>

                ))}

            </div>

        </AppLayout>

    );

}