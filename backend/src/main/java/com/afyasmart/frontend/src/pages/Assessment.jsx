import { useState } from "react";
import { analyzeHealth } from "../services/assessmentService";

export default function Assessment() {

    const user = JSON.parse(localStorage.getItem("user"));

    const [form, setForm] = useState({
        accountId: user.id,
        age: "",
        gender: "",
        height: "",
        weight: "",
        systolic: "",
        diastolic: "",
        bloodSugar: "",
        exerciseDays: "",
        smoker: false,
        alcohol: false
    });

    const [result, setResult] = useState(null);

    const handleChange = (e) => {

        const { name, value, type, checked } = e.target;

        setForm({
            ...form,
            [name]: type === "checkbox" ? checked : value
        });

    };

    const handleSubmit = async (e) => {

        e.preventDefault();

        try {

            const response = await analyzeHealth(form);

            setResult(response.data);

            localStorage.setItem(
                "latestAssessment",
                JSON.stringify(response.data)
            );

        } catch (error) {

            alert("Assessment failed.");

        }

    };

    return (

        <div className="min-h-screen bg-slate-100 p-8">

            <div className="max-w-4xl mx-auto bg-white rounded-3xl shadow-xl p-10">

                <h1 className="text-3xl font-bold text-blue-600">

                    Health Assessment

                </h1>

                <form
                    onSubmit={handleSubmit}
                    className="grid grid-cols-2 gap-5 mt-8"
                >

                    <input
                        name="age"
                        placeholder="Age"
                        onChange={handleChange}
                        className="border p-3 rounded-xl"
                    />

                    <input
                        name="gender"
                        placeholder="Gender"
                        onChange={handleChange}
                        className="border p-3 rounded-xl"
                    />

                    <input
                        name="height"
                        placeholder="Height (cm)"
                        onChange={handleChange}
                        className="border p-3 rounded-xl"
                    />

                    <input
                        name="weight"
                        placeholder="Weight (kg)"
                        onChange={handleChange}
                        className="border p-3 rounded-xl"
                    />

                    <input
                        name="systolic"
                        placeholder="Systolic BP"
                        onChange={handleChange}
                        className="border p-3 rounded-xl"
                    />

                    <input
                        name="diastolic"
                        placeholder="Diastolic BP"
                        onChange={handleChange}
                        className="border p-3 rounded-xl"
                    />

                    <input
                        name="bloodSugar"
                        placeholder="Blood Sugar"
                        onChange={handleChange}
                        className="border p-3 rounded-xl"
                    />

                    <input
                        name="exerciseDays"
                        placeholder="Exercise Days/Week"
                        onChange={handleChange}
                        className="border p-3 rounded-xl"
                    />

                    <label className="flex items-center gap-2">

                        <input
                            type="checkbox"
                            name="smoker"
                            onChange={handleChange}
                        />

                        Smoker

                    </label>

                    <label className="flex items-center gap-2">

                        <input
                            type="checkbox"
                            name="alcohol"
                            onChange={handleChange}
                        />

                        Alcohol

                    </label>

                    <button
                        className="col-span-2 bg-blue-600 text-white p-4 rounded-xl"
                    >

                        Analyze My Health

                    </button>

                </form>

                {result && (

                    <div className="mt-10 border-t pt-8">

                        <h2 className="text-2xl font-bold">

                            Assessment Result

                        </h2>

                        <div className="grid grid-cols-2 gap-5 mt-6">

                            <Card title="Health Score" value={result.healthScore} />

                            <Card title="BMI" value={result.bmi} />

                            <Card title="Risk Level" value={result.riskLevel} />

                        </div>

                        <div className="mt-8">

                            <h3 className="font-bold text-xl">

                                Recommendations

                            </h3>

                            <p className="mt-3 text-gray-700">

                                {result.recommendation}

                            </p>

                        </div>

                    </div>

                )}

            </div>

        </div>

    );

}

function Card({ title, value }) {

    return (

        <div className="bg-blue-50 p-6 rounded-2xl">

            <h3 className="text-gray-500">

                {title}

            </h3>

            <p className="text-3xl font-bold text-blue-600">

                {value}

            </p>

        </div>

    );

}