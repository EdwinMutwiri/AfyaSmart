import { useEffect, useState } from "react";

import {
    Pill,
    Plus,
    Clock3,
    CalendarDays,
    Edit3,
    Trash2,
    Power,
    X,
    Save,
    Loader2,
    AlertCircle
} from "lucide-react";

import AppLayout from "../components/layout/AppLayout";

import {
    createMedicine,
    getPatientMedicines,
    updateMedicine,
    deactivateMedicine,
    deleteMedicine
} from "../services/medicineService";


/**
 * ============================================================
 * AfyaSmart - Medicine Reminders
 * ============================================================
 *
 * Allows patients to:
 *
 * - View medicine reminders
 * - Add medicines
 * - Edit medicines
 * - Deactivate medicines
 * - Delete medicines
 *
 * ============================================================
 */

function MedicineReminders() {

    const storedUser = localStorage.getItem("user");

    let user = null;

    try {
        user = storedUser
            ? JSON.parse(storedUser)
            : null;
    } catch (error) {
        console.error(
            "Unable to read user information:",
            error
        );
    }

    const accountId = user?.id;

    const [medicines, setMedicines] = useState([]);

    const [loading, setLoading] = useState(true);

    const [saving, setSaving] = useState(false);

    const [error, setError] = useState("");

    const [showForm, setShowForm] = useState(false);

    const [editingMedicine, setEditingMedicine] =
        useState(null);

    const [formData, setFormData] = useState({
        medicineName: "",
        dosage: "",
        frequency: "",
        startDate: "",
        endDate: "",
        reminderTime: "",
        instructions: ""
    });


    // ========================================================
    // LOAD MEDICINES
    // ========================================================

    const loadMedicines = async () => {

        if (!accountId) {
            setError(
                "Patient account information could not be found."
            );

            setLoading(false);

            return;
        }

        try {

            setLoading(true);

            setError("");

            const data =
                await getPatientMedicines(accountId);

            setMedicines(data || []);

        } catch (err) {

            console.error(
                "Failed to load medicines:",
                err
            );

            setError(
                "Unable to load your medicines. Please try again."
            );

        } finally {

            setLoading(false);
        }
    };


    useEffect(() => {

        loadMedicines();

    }, [accountId]);


    // ========================================================
    // FORM HANDLING
    // ========================================================

    const handleChange = (event) => {

        const {
            name,
            value
        } = event.target;

        setFormData((previous) => ({
            ...previous,
            [name]: value
        }));
    };


    const resetForm = () => {

        setFormData({
            medicineName: "",
            dosage: "",
            frequency: "",
            startDate: "",
            endDate: "",
            reminderTime: "",
            instructions: ""
        });

        setEditingMedicine(null);

        setShowForm(false);
    };


    const openAddForm = () => {

        setEditingMedicine(null);

        setFormData({
            medicineName: "",
            dosage: "",
            frequency: "",
            startDate: "",
            endDate: "",
            reminderTime: "",
            instructions: ""
        });

        setShowForm(true);
    };


    const openEditForm = (medicine) => {

        setEditingMedicine(medicine);

        setFormData({
            medicineName:
                medicine.medicineName || "",

            dosage:
                medicine.dosage || "",

            frequency:
                medicine.frequency || "",

            startDate:
                medicine.startDate || "",

            endDate:
                medicine.endDate || "",

            reminderTime:
                medicine.reminderTime
                    ? medicine.reminderTime.substring(0, 5)
                    : "",

            instructions:
                medicine.instructions || ""
        });

        setShowForm(true);
    };


    // ========================================================
    // SAVE MEDICINE
    // ========================================================

    const handleSubmit = async (event) => {

        event.preventDefault();

        if (!accountId) {

            setError(
                "Patient account information could not be found."
            );

            return;
        }

        try {

            setSaving(true);

            setError("");

            const payload = {
                accountId: accountId,

                medicineName:
                    formData.medicineName,

                dosage:
                    formData.dosage,

                frequency:
                    formData.frequency,

                startDate:
                    formData.startDate,

                endDate:
                    formData.endDate || null,

                reminderTime:
                    formData.reminderTime || null,

                instructions:
                    formData.instructions || null
            };


            if (editingMedicine) {

                await updateMedicine(
                    editingMedicine.id,
                    payload
                );

            } else {

                await createMedicine(payload);
            }


            await loadMedicines();

            resetForm();

        } catch (err) {

            console.error(
                "Failed to save medicine:",
                err
            );

            setError(
                err?.response?.data?.message ||
                "Unable to save medicine. Please check your details."
            );

        } finally {

            setSaving(false);
        }
    };


    // ========================================================
    // DEACTIVATE
    // ========================================================

    const handleDeactivate = async (medicineId) => {

        const confirmed =
            window.confirm(
                "Are you sure you want to deactivate this medicine reminder?"
            );

        if (!confirmed) {
            return;
        }

        try {

            await deactivateMedicine(
                medicineId
            );

            await loadMedicines();

        } catch (err) {

            console.error(
                "Failed to deactivate medicine:",
                err
            );

            setError(
                "Unable to deactivate this medicine."
            );
        }
    };


    // ========================================================
    // DELETE
    // ========================================================

    const handleDelete = async (medicineId) => {

        const confirmed =
            window.confirm(
                "Delete this medicine permanently?"
            );

        if (!confirmed) {
            return;
        }

        try {

            await deleteMedicine(
                medicineId
            );

            await loadMedicines();

        } catch (err) {

            console.error(
                "Failed to delete medicine:",
                err
            );

            setError(
                "Unable to delete this medicine."
            );
        }
    };


    // ========================================================
    // UI
    // ========================================================

    return (
        <AppLayout>

            <div
                style={{
                    maxWidth: "1200px",
                    margin: "0 auto",
                    padding: "10px 0 40px"
                }}
            >

                {/* HEADER */}

                <div
                    style={{
                        display: "flex",
                        justifyContent: "space-between",
                        alignItems: "center",
                        gap: "20px",
                        marginBottom: "28px",
                        flexWrap: "wrap"
                    }}
                >

                    <div>

                        <h1
                            style={{
                                margin: 0,
                                fontSize: "30px",
                                fontWeight: 700
                            }}
                        >
                            Medicine Reminders
                        </h1>

                        <p
                            style={{
                                marginTop: "8px",
                                color: "#64748b"
                            }}
                        >
                            Keep track of your medicines and
                            never miss an important dose.
                        </p>

                    </div>


                    <button
                        onClick={openAddForm}
                        style={{
                            display: "flex",
                            alignItems: "center",
                            gap: "8px",
                            padding: "12px 18px",
                            border: "none",
                            borderRadius: "10px",
                            background: "#2563eb",
                            color: "white",
                            fontWeight: 600,
                            cursor: "pointer"
                        }}
                    >

                        <Plus size={18} />

                        Add Medicine

                    </button>

                </div>


                {/* ERROR */}

                {error && (

                    <div
                        style={{
                            display: "flex",
                            alignItems: "center",
                            gap: "10px",
                            padding: "14px",
                            marginBottom: "20px",
                            borderRadius: "10px",
                            background: "#fef2f2",
                            color: "#b91c1c"
                        }}
                    >

                        <AlertCircle size={20} />

                        {error}

                    </div>

                )}


                {/* FORM */}

                {showForm && (

                    <div
                        style={{
                            background: "white",
                            borderRadius: "16px",
                            padding: "24px",
                            marginBottom: "28px",
                            boxShadow:
                                "0 4px 20px rgba(0,0,0,0.06)"
                        }}
                    >

                        <div
                            style={{
                                display: "flex",
                                justifyContent: "space-between",
                                alignItems: "center",
                                marginBottom: "20px"
                            }}
                        >

                            <h2
                                style={{
                                    margin: 0,
                                    fontSize: "21px"
                                }}
                            >
                                {editingMedicine
                                    ? "Edit Medicine"
                                    : "Add Medicine"}
                            </h2>


                            <button
                                onClick={resetForm}
                                style={{
                                    border: "none",
                                    background: "transparent",
                                    cursor: "pointer"
                                }}
                            >

                                <X size={22} />

                            </button>

                        </div>


                        <form onSubmit={handleSubmit}>

                            <div
                                style={{
                                    display: "grid",
                                    gridTemplateColumns:
                                        "repeat(auto-fit, minmax(220px, 1fr))",
                                    gap: "18px"
                                }}
                            >

                                <FormField
                                    label="Medicine Name"
                                    name="medicineName"
                                    value={
                                        formData.medicineName
                                    }
                                    onChange={
                                        handleChange
                                    }
                                    placeholder="e.g. Paracetamol"
                                    required
                                />

                                <FormField
                                    label="Dosage"
                                    name="dosage"
                                    value={
                                        formData.dosage
                                    }
                                    onChange={
                                        handleChange
                                    }
                                    placeholder="e.g. 500 mg"
                                    required
                                />

                                <FormField
                                    label="Frequency"
                                    name="frequency"
                                    value={
                                        formData.frequency
                                    }
                                    onChange={
                                        handleChange
                                    }
                                    placeholder="e.g. Twice daily"
                                    required
                                />

                                <FormField
                                    label="Start Date"
                                    type="date"
                                    name="startDate"
                                    value={
                                        formData.startDate
                                    }
                                    onChange={
                                        handleChange
                                    }
                                    required
                                />

                                <FormField
                                    label="End Date"
                                    type="date"
                                    name="endDate"
                                    value={
                                        formData.endDate
                                    }
                                    onChange={
                                        handleChange
                                    }
                                />

                                <FormField
                                    label="Reminder Time"
                                    type="time"
                                    name="reminderTime"
                                    value={
                                        formData.reminderTime
                                    }
                                    onChange={
                                        handleChange
                                    }
                                />

                            </div>


                            <div
                                style={{
                                    marginTop: "18px"
                                }}
                            >

                                <label
                                    style={{
                                        display: "block",
                                        marginBottom: "7px",
                                        fontWeight: 600
                                    }}
                                >
                                    Instructions
                                </label>

                                <textarea
                                    name="instructions"
                                    value={
                                        formData.instructions
                                    }
                                    onChange={
                                        handleChange
                                    }
                                    placeholder="e.g. Take after meals"
                                    rows="3"
                                    style={{
                                        width: "100%",
                                        boxSizing: "border-box",
                                        padding: "12px",
                                        border:
                                            "1px solid #cbd5e1",
                                        borderRadius: "9px",
                                        resize: "vertical",
                                        fontFamily:
                                            "inherit"
                                    }}
                                />

                            </div>


                            <div
                                style={{
                                    display: "flex",
                                    justifyContent: "flex-end",
                                    gap: "10px",
                                    marginTop: "20px"
                                }}
                            >

                                <button
                                    type="button"
                                    onClick={resetForm}
                                    style={{
                                        padding: "11px 18px",
                                        border:
                                            "1px solid #cbd5e1",
                                        borderRadius: "9px",
                                        background: "white",
                                        cursor: "pointer"
                                    }}
                                >
                                    Cancel
                                </button>


                                <button
                                    type="submit"
                                    disabled={saving}
                                    style={{
                                        display: "flex",
                                        alignItems: "center",
                                        gap: "8px",
                                        padding: "11px 18px",
                                        border: "none",
                                        borderRadius: "9px",
                                        background: "#2563eb",
                                        color: "white",
                                        fontWeight: 600,
                                        cursor: saving
                                            ? "not-allowed"
                                            : "pointer"
                                    }}
                                >

                                    {saving ? (
                                        <Loader2
                                            size={18}
                                            className="spin"
                                        />
                                    ) : (
                                        <Save size={18} />
                                    )}

                                    {saving
                                        ? "Saving..."
                                        : editingMedicine
                                            ? "Update Medicine"
                                            : "Save Medicine"}

                                </button>

                            </div>

                        </form>

                    </div>

                )}


                {/* MEDICINES */}

                {loading ? (

                    <div
                        style={{
                            textAlign: "center",
                            padding: "60px"
                        }}
                    >

                        <Loader2
                            size={32}
                            className="spin"
                        />

                        <p>
                            Loading your medicines...
                        </p>

                    </div>

                ) : medicines.length === 0 ? (

                    <div
                        style={{
                            textAlign: "center",
                            padding: "70px 20px",
                            background: "white",
                            borderRadius: "16px"
                        }}
                    >

                        <Pill
                            size={50}
                            style={{
                                marginBottom: "12px",
                                opacity: 0.5
                            }}
                        />

                        <h2>
                            No medicines yet
                        </h2>

                        <p
                            style={{
                                color: "#64748b"
                            }}
                        >
                            Add your first medicine reminder
                            to start keeping track of your
                            medication.
                        </p>

                    </div>

                ) : (

                    <div
                        style={{
                            display: "grid",
                            gridTemplateColumns:
                                "repeat(auto-fit, minmax(300px, 1fr))",
                            gap: "20px"
                        }}
                    >

                        {medicines.map((medicine) => (

                            <MedicineCard
                                key={medicine.id}
                                medicine={medicine}
                                onEdit={
                                    openEditForm
                                }
                                onDeactivate={
                                    handleDeactivate
                                }
                                onDelete={
                                    handleDelete
                                }
                            />

                        ))}

                    </div>

                )}

            </div>

        </AppLayout>
    );
}


/**
 * ============================================================
 * Form Field Component
 * ============================================================
 */

function FormField({
    label,
    name,
    type = "text",
    value,
    onChange,
    placeholder,
    required = false
}) {

    return (

        <div>

            <label
                style={{
                    display: "block",
                    marginBottom: "7px",
                    fontWeight: 600
                }}
            >
                {label}
            </label>

            <input
                type={type}
                name={name}
                value={value}
                onChange={onChange}
                placeholder={placeholder}
                required={required}
                style={{
                    width: "100%",
                    boxSizing: "border-box",
                    padding: "11px 12px",
                    border: "1px solid #cbd5e1",
                    borderRadius: "9px",
                    outline: "none"
                }}
            />

        </div>
    );
}


/**
 * ============================================================
 * Medicine Card
 * ============================================================
 */

function MedicineCard({
    medicine,
    onEdit,
    onDeactivate,
    onDelete
}) {

    const active = medicine.active;

    return (

        <div
            style={{
                background: "white",
                borderRadius: "16px",
                padding: "22px",
                boxShadow:
                    "0 4px 18px rgba(0,0,0,0.05)",
                opacity: active ? 1 : 0.65
            }}
        >

            {/* CARD HEADER */}

            <div
                style={{
                    display: "flex",
                    justifyContent: "space-between",
                    alignItems: "flex-start",
                    gap: "12px"
                }}
            >

                <div
                    style={{
                        display: "flex",
                        gap: "12px",
                        alignItems: "center"
                    }}
                >

                    <div
                        style={{
                            width: "46px",
                            height: "46px",
                            borderRadius: "12px",
                            display: "flex",
                            alignItems: "center",
                            justifyContent: "center",
                            background: "#eff6ff",
                            color: "#2563eb"
                        }}
                    >

                        <Pill size={24} />

                    </div>


                    <div>

                        <h3
                            style={{
                                margin: 0,
                                fontSize: "18px"
                            }}
                        >
                            {medicine.medicineName}
                        </h3>

                        <p
                            style={{
                                margin: "4px 0 0",
                                color: "#64748b"
                            }}
                        >
                            {medicine.dosage}
                        </p>

                    </div>

                </div>


                <span
                    style={{
                        padding: "5px 9px",
                        borderRadius: "999px",
                        fontSize: "12px",
                        fontWeight: 600,
                        background: active
                            ? "#dcfce7"
                            : "#f1f5f9",
                        color: active
                            ? "#166534"
                            : "#64748b"
                    }}
                >
                    {active
                        ? "Active"
                        : "Inactive"}
                </span>

            </div>


            {/* DETAILS */}

            <div
                style={{
                    marginTop: "20px",
                    display: "grid",
                    gap: "12px"
                }}
            >

                <DetailRow
                    icon={<Clock3 size={17} />}
                    label="Frequency"
                    value={
                        medicine.frequency
                    }
                />

                <DetailRow
                    icon={<Clock3 size={17} />}
                    label="Reminder"
                    value={
                        medicine.reminderTime
                            ? medicine.reminderTime.substring(
                                0,
                                5
                            )
                            : "Not set"
                    }
                />

                <DetailRow
                    icon={
                        <CalendarDays
                            size={17}
                        />
                    }
                    label="Start"
                    value={
                        medicine.startDate
                    }
                />

                {medicine.endDate && (

                    <DetailRow
                        icon={
                            <CalendarDays
                                size={17}
                            />
                        }
                        label="End"
                        value={
                            medicine.endDate
                        }
                    />

                )}

            </div>


            {/* INSTRUCTIONS */}

            {medicine.instructions && (

                <div
                    style={{
                        marginTop: "18px",
                        padding: "12px",
                        background: "#f8fafc",
                        borderRadius: "9px",
                        color: "#475569",
                        fontSize: "14px"
                    }}
                >

                    <strong>
                        Instructions:
                    </strong>

                    <div
                        style={{
                            marginTop: "5px"
                        }}
                    >
                        {medicine.instructions}
                    </div>

                </div>

            )}


            {/* ACTIONS */}

            <div
                style={{
                    display: "flex",
                    gap: "8px",
                    marginTop: "20px",
                    flexWrap: "wrap"
                }}
            >

                <button
                    onClick={() =>
                        onEdit(medicine)
                    }
                    style={{
                        display: "flex",
                        alignItems: "center",
                        gap: "6px",
                        padding: "9px 12px",
                        border:
                            "1px solid #cbd5e1",
                        borderRadius: "8px",
                        background: "white",
                        cursor: "pointer"
                    }}
                >

                    <Edit3 size={16} />

                    Edit

                </button>


                {active && (

                    <button
                        onClick={() =>
                            onDeactivate(
                                medicine.id
                            )
                        }
                        style={{
                            display: "flex",
                            alignItems: "center",
                            gap: "6px",
                            padding: "9px 12px",
                            border:
                                "1px solid #fde68a",
                            borderRadius: "8px",
                            background: "#fffbeb",
                            color: "#92400e",
                            cursor: "pointer"
                        }}
                    >

                        <Power size={16} />

                        Deactivate

                    </button>

                )}


                <button
                    onClick={() =>
                        onDelete(medicine.id)
                    }
                    style={{
                        display: "flex",
                        alignItems: "center",
                        gap: "6px",
                        padding: "9px 12px",
                        border:
                            "1px solid #fecaca",
                        borderRadius: "8px",
                        background: "#fef2f2",
                        color: "#b91c1c",
                        cursor: "pointer"
                    }}
                >

                    <Trash2 size={16} />

                    Delete

                </button>

            </div>

        </div>
    );
}


/**
 * ============================================================
 * Detail Row
 * ============================================================
 */

function DetailRow({
    icon,
    label,
    value
}) {

    return (

        <div
            style={{
                display: "flex",
                alignItems: "center",
                gap: "10px"
            }}
        >

            <span
                style={{
                    color: "#64748b",
                    display: "flex"
                }}
            >
                {icon}
            </span>

            <span
                style={{
                    color: "#64748b",
                    fontSize: "14px"
                }}
            >
                {label}:
            </span>

            <strong
                style={{
                    fontSize: "14px"
                }}
            >
                {value}
            </strong>

        </div>
    );
}

export default MedicineReminders;