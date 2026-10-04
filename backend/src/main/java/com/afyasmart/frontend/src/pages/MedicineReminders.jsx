import { useEffect, useRef, useState } from "react";

import AppLayout from "../components/layout/AppLayout";

import {
    createMedicine,
    getPatientMedicines,
    updateMedicine,
    deactivateMedicine,
    deleteMedicine,
    markDoseAsTaken,
    getTodayDoses
} from "../services/medicineService";

import {
    Pill,
    Plus,
    Pencil,
    Trash2,
    Clock,
    CalendarDays,
    CheckCircle2,
    X,
    Loader2,
    Bell,
    TimerReset
} from "lucide-react";

import {
    requestNotificationPermission
} from "../utils/medicineReminder";


// ============================================================
// FREQUENCY OPTIONS
// ============================================================

const FREQUENCY_OPTIONS = [
    {
        value: "Once daily",
        count: 1,
        label: "Once daily"
    },
    {
        value: "Twice daily",
        count: 2,
        label: "Twice daily"
    },
    {
        value: "3 times daily",
        count: 3,
        label: "3 times daily"
    },
    {
        value: "4 times daily",
        count: 4,
        label: "4 times daily"
    }
];


// ============================================================
// DEFAULT FORM
// ============================================================

const createDefaultForm = () => ({
    medicineName: "",
    dosage: "",
    frequency: "Once daily",
    startDate: "",
    endDate: "",
    reminderTimes: [""],
    instructions: ""
});


// ============================================================
// TIME HELPERS
// ============================================================

const cleanTime = (time) => {
    if (!time) {
        return "";
    }

    return String(time).substring(0, 5);
};


const formatTime = (time) => {
    if (!time) {
        return "Not set";
    }

    const cleaned = cleanTime(time);

    const [hours, minutes] = cleaned.split(":");

    const date = new Date();

    date.setHours(
        Number(hours),
        Number(minutes),
        0,
        0
    );

    return date.toLocaleTimeString([], {
        hour: "numeric",
        minute: "2-digit"
    });
};


// ============================================================
// GET FREQUENCY COUNT
// ============================================================

const getFrequencyCount = (frequency) => {
    const option = FREQUENCY_OPTIONS.find(
        (item) => item.value === frequency
    );

    return option?.count || 1;
};


// ============================================================
// CREATE THE REQUIRED NUMBER OF TIME INPUTS
// ============================================================

const buildReminderTimes = (frequency, existingTimes = []) => {

    const count = getFrequencyCount(frequency);

    const cleaned = existingTimes
        .map(cleanTime)
        .filter(Boolean);

    const result = [];

    for (let index = 0; index < count; index++) {
        result.push(cleaned[index] || "");
    }

    return result;
};


// ============================================================
// MEDICINE REMINDERS PAGE
// ============================================================

export default function MedicineReminders() {

    // ========================================================
    // USER
    // ========================================================

    const storedUser = localStorage.getItem("user");

    let user = null;

    try {
        user = storedUser
            ? JSON.parse(storedUser)
            : null;
    } catch (error) {
        console.error(
            "Unable to read user from localStorage:",
            error
        );
    }

    const accountId = user?.id;


    // ========================================================
    // STATE
    // ========================================================

    const [medicines, setMedicines] = useState([]);

    const [todayDoses, setTodayDoses] = useState([]);

    /*
     * These are individual DOSES, not medicines.
     *
     * Example:
     *
     * Paracetamol 7:00 AM
     * Paracetamol 1:00 PM
     * Paracetamol 7:00 PM
     *
     * Each has a different dose.id.
     */
    const [dueDoses, setDueDoses] = useState([]);

    const [loading, setLoading] = useState(true);

    const [doseLoading, setDoseLoading] = useState(null);

    const [showForm, setShowForm] = useState(false);

    const [editingMedicine, setEditingMedicine] =
        useState(null);

    const [error, setError] = useState("");

    const [success, setSuccess] = useState("");

    const [formData, setFormData] =
        useState(createDefaultForm);


    /*
     * Tracks how many times each dose has been snoozed.
     *
     * Example:
     *
     * {
     *   12: 2,
     *   15: 1
     * }
     */
    const [snoozeCounts, setSnoozeCounts] = useState({});


    /*
     * Keeps track of snooze timers so they can be cleaned up
     * when the page is closed/unmounted.
     */
    const snoozeTimersRef = useRef({});


    // ========================================================
    // LOAD MEDICINES
    // ========================================================

    const loadMedicines = async () => {

        if (!accountId) {

            setError(
                "Unable to identify the logged-in patient."
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
                "Unable to load your medicines."
            );

        } finally {

            setLoading(false);
        }
    };


    // ========================================================
    // LOAD TODAY'S DOSES
    // ========================================================

    const loadTodayDoses = async () => {

        if (!accountId) {
            return;
        }

        try {

            const data =
                await getTodayDoses(accountId);

            setTodayDoses(data || []);

        } catch (err) {

            console.error(
                "Failed to load today's medicine doses:",
                err
            );

            /*
             * Do not block the entire page if dose loading fails.
             */
        }
    };


    // ========================================================
    // INITIAL LOAD
    // ========================================================

    useEffect(() => {

        const initialize = async () => {

            await requestNotificationPermission();

            await loadMedicines();

            await loadTodayDoses();
        };

        initialize();

    }, [accountId]);


    // ========================================================
    // CLEANUP SNOOZE TIMERS
    // ========================================================

    useEffect(() => {

        return () => {

            Object.values(
                snoozeTimersRef.current
            ).forEach((timer) => {
                clearTimeout(timer);
            });

            snoozeTimersRef.current = {};
        };

    }, []);


    // ========================================================
    // SEND DOSE NOTIFICATION
    // ========================================================

    const sendDoseNotification = (dose) => {

        if (!("Notification" in window)) {
            return;
        }

        if (Notification.permission !== "granted") {
            return;
        }

        const notification = new Notification(
            "AfyaSmart Medicine Reminder",
            {
                body:
                    `It is time to take ${dose.medicineName}` +
                    `${dose.dosage ? ` (${dose.dosage})` : ""}.`,
                icon: "/favicon.ico",
                tag: `medicine-dose-${dose.id}`
            }
        );

        setTimeout(() => {
            notification.close();
        }, 10000);
    };


    // ========================================================
    // CHECK CURRENT DOSES
    // ========================================================

    useEffect(() => {

        if (!todayDoses.length) {
            setDueDoses([]);
            return;
        }

        const checkDueDoses = () => {

            const now = new Date();

            const currentHours =
                String(now.getHours()).padStart(2, "0");

            const currentMinutes =
                String(now.getMinutes()).padStart(2, "0");

            const currentTime =
                `${currentHours}:${currentMinutes}`;


            const due = todayDoses.filter((dose) => {

                if (dose.status !== "PENDING") {
                    return false;
                }

                const scheduledTime =
                    cleanTime(dose.scheduledTime);

                /*
                 * A dose becomes due exactly at its scheduled
                 * hour and minute.
                 *
                 * Snoozed doses are handled separately.
                 */
                return scheduledTime === currentTime;
            });


            if (due.length > 0) {

                setDueDoses((current) => {

                    const existingIds =
                        new Set(
                            current.map((dose) => dose.id)
                        );

                    const newDoses =
                        due.filter(
                            (dose) =>
                                !existingIds.has(dose.id)
                        );

                    newDoses.forEach((dose) => {
                        sendDoseNotification(dose);
                    });

                    return [
                        ...current,
                        ...newDoses
                    ];
                });
            }
        };


        /*
         * Check immediately.
         */
        checkDueDoses();


        /*
         * Check every 30 seconds.
         */
        const interval =
            setInterval(
                checkDueDoses,
                30000
            );


        return () => {
            clearInterval(interval);
        };

    }, [todayDoses]);


    // ========================================================
    // FORM INPUT
    // ========================================================

    const handleChange = (event) => {

        const {
            name,
            value
        } = event.target;


        if (name === "frequency") {

            setFormData((previous) => ({

                ...previous,

                frequency: value,

                reminderTimes:
                    buildReminderTimes(
                        value,
                        previous.reminderTimes
                    )
            }));

            return;
        }


        setFormData((previous) => ({

            ...previous,

            [name]: value

        }));
    };


    // ========================================================
    // REMINDER TIME INPUT
    // ========================================================

    const handleReminderTimeChange = (
        index,
        value
    ) => {

        setFormData((previous) => {

            const reminderTimes =
                [...previous.reminderTimes];

            reminderTimes[index] = value;

            return {
                ...previous,
                reminderTimes
            };
        });
    };


    // ========================================================
    // RESET FORM
    // ========================================================

    const resetForm = () => {

        setFormData(
            createDefaultForm()
        );

        setEditingMedicine(null);

        setShowForm(false);
    };


    // ========================================================
    // SUBMIT MEDICINE
    // ========================================================

    const handleSubmit = async (event) => {

        event.preventDefault();


        if (!accountId) {

            setError(
                "Unable to identify the logged-in patient."
            );

            return;
        }


        try {

            setError("");

            setSuccess("");


            const reminderTimes =
                formData.reminderTimes
                    .map(cleanTime)
                    .filter(Boolean);


            const expectedCount =
                getFrequencyCount(
                    formData.frequency
                );


            if (
                reminderTimes.length !==
                expectedCount
            ) {

                setError(
                    `Please provide ${expectedCount} reminder time${
                        expectedCount > 1 ? "s" : ""
                    }.`
                );

                return;
            }


            /*
             * Prevent duplicate reminder times.
             */
            const uniqueTimes =
                [...new Set(reminderTimes)];


            if (
                uniqueTimes.length !==
                reminderTimes.length
            ) {

                setError(
                    "Each reminder time must be different."
                );

                return;
            }


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

                /*
                 * NEW MULTI-DOSE FIELD
                 */
                reminderTimes:
                    reminderTimes,

                instructions:
                    formData.instructions || null
            };


            if (editingMedicine) {

                await updateMedicine(
                    editingMedicine.id,
                    payload
                );

                setSuccess(
                    "Medicine updated successfully."
                );

            } else {

                await createMedicine(payload);

                setSuccess(
                    "Medicine reminder created successfully."
                );
            }


            resetForm();

            await loadMedicines();

            await loadTodayDoses();


        } catch (err) {

            console.error(
                "Failed to save medicine:",
                err
            );

            setError(
                err?.response?.data?.message ||
                "Unable to save medicine."
            );
        }
    };


    // ========================================================
    // EDIT MEDICINE
    // ========================================================

    const handleEdit = (medicine) => {

        const frequency =
            medicine.frequency ||
            "Once daily";


        /*
         * New backend:
         * medicine.reminderTimes
         *
         * Old records:
         * medicine.reminderTime
         */
        let existingTimes = [];


        if (
            Array.isArray(
                medicine.reminderTimes
            ) &&
            medicine.reminderTimes.length > 0
        ) {

            existingTimes =
                medicine.reminderTimes;

        } else if (
            medicine.reminderTime
        ) {

            existingTimes = [
                medicine.reminderTime
            ];
        }


        setEditingMedicine(medicine);


        setFormData({

            medicineName:
                medicine.medicineName || "",

            dosage:
                medicine.dosage || "",

            frequency:
                frequency,

            startDate:
                medicine.startDate || "",

            endDate:
                medicine.endDate || "",

            reminderTimes:
                buildReminderTimes(
                    frequency,
                    existingTimes
                ),

            instructions:
                medicine.instructions || ""
        });


        setShowForm(true);


        window.scrollTo({
            top: 0,
            behavior: "smooth"
        });
    };


    // ========================================================
    // DEACTIVATE MEDICINE
    // ========================================================

    const handleDeactivate = async (
        medicineId
    ) => {

        const confirmed =
            window.confirm(
                "Are you sure you want to deactivate this medicine reminder?"
            );


        if (!confirmed) {
            return;
        }


        try {

            setError("");

            setSuccess("");


            await deactivateMedicine(
                medicineId
            );


            setSuccess(
                "Medicine reminder deactivated."
            );


            await loadMedicines();

            await loadTodayDoses();


        } catch (err) {

            console.error(
                "Failed to deactivate medicine:",
                err
            );

            setError(
                "Unable to deactivate medicine."
            );
        }
    };


    // ========================================================
    // DELETE MEDICINE
    // ========================================================

    const handleDelete = async (
        medicineId
    ) => {

        const confirmed =
            window.confirm(
                "Are you sure you want to permanently delete this medicine?"
            );


        if (!confirmed) {
            return;
        }


        try {

            setError("");

            setSuccess("");


            await deleteMedicine(
                medicineId
            );


            setSuccess(
                "Medicine deleted successfully."
            );


            await loadMedicines();

            await loadTodayDoses();


        } catch (err) {

            console.error(
                "Failed to delete medicine:",
                err
            );

            setError(
                "Unable to delete medicine."
            );
        }
    };


    // ========================================================
    // MARK A SPECIFIC DOSE AS TAKEN
    // ========================================================

    const handleMarkTaken = async (
        doseId
    ) => {

        try {

            setDoseLoading(doseId);

            setError("");

            setSuccess("");


            /*
             * IMPORTANT:
             *
             * We now send doseId.
             *
             * This means:
             *
             * 7:00 AM dose -> doseId 10
             * 1:00 PM dose -> doseId 11
             * 7:00 PM dose -> doseId 12
             *
             * Each dose can be marked independently.
             */
            await markDoseAsTaken(
                doseId
            );


            /*
             * Remove the dose from the reminder panel.
             */
            setDueDoses((current) =>
                current.filter(
                    (dose) =>
                        dose.id !== doseId
                )
            );


            setSuccess(
                "Medicine dose marked as taken."
            );


            await loadTodayDoses();


        } catch (err) {

            console.error(
                "Failed to mark dose as taken:",
                err
            );

            setError(
                err?.response?.data?.message ||
                "Unable to mark this dose as taken."
            );

        } finally {

            setDoseLoading(null);
        }
    };


    // ========================================================
    // SNOOZE A DOSE
    // ========================================================

    const handleSnooze = (dose) => {

        const currentCount =
            snoozeCounts[dose.id] || 0;


        /*
         * Maximum = 3 snoozes.
         */
        if (currentCount >= 3) {

            setError(
                "This dose has already reached the maximum of 3 snoozes."
            );

            return;
        }


        const newCount =
            currentCount + 1;


        setSnoozeCounts((current) => ({

            ...current,

            [dose.id]: newCount

        }));


        /*
         * Immediately remove it from the reminder panel.
         */
        setDueDoses((current) =>
            current.filter(
                (item) =>
                    item.id !== dose.id
            )
        );


        setSuccess(
            `Reminder snoozed for 1 minute. Snooze ${newCount} of 3.`
        );


        /*
         * Clear an existing timer for this dose.
         * This prevents duplicate timers.
         */
        if (
            snoozeTimersRef.current[dose.id]
        ) {

            clearTimeout(
                snoozeTimersRef.current[dose.id]
            );
        }


        /*
         * ONE MINUTE SNOOZE
         */
        snoozeTimersRef.current[dose.id] =
            setTimeout(async () => {

                /*
                 * Refresh today's doses first.
                 */
                try {

                    const data =
                        await getTodayDoses(
                            accountId
                        );


                    const refreshedDoses =
                        data || [];


                    setTodayDoses(
                        refreshedDoses
                    );


                    const refreshedDose =
                        refreshedDoses.find(
                            (item) =>
                                item.id ===
                                dose.id
                        );


                    /*
                     * Only show it again if it is
                     * still pending.
                     */
                    if (
                        refreshedDose &&
                        refreshedDose.status ===
                            "PENDING"
                    ) {

                        setDueDoses((current) => {

                            const alreadyVisible =
                                current.some(
                                    (item) =>
                                        item.id ===
                                        refreshedDose.id
                                );


                            if (
                                alreadyVisible
                            ) {
                                return current;
                            }


                            sendDoseNotification(
                                refreshedDose
                            );


                            return [
                                ...current,
                                refreshedDose
                            ];
                        });
                    }

                } catch (err) {

                    console.error(
                        "Failed to refresh snoozed dose:",
                        err
                    );

                } finally {

                    delete snoozeTimersRef
                        .current[dose.id];
                }

            }, 60000);
    };


    // ========================================================
    // GET DOSES FOR A PARTICULAR MEDICINE
    // ========================================================

    const getMedicineDoses = (
        medicineId
    ) => {

        return todayDoses.filter(
            (dose) =>
                dose.medicineId ===
                medicineId
        );
    };


    // ========================================================
    // LOADING
    // ========================================================

    if (loading) {

        return (

            <AppLayout>

                <div className="flex min-h-[400px] items-center justify-center">

                    <div className="flex items-center gap-3 text-gray-600">

                        <Loader2
                            size={22}
                            className="animate-spin"
                        />

                        <span>
                            Loading medicine reminders...
                        </span>

                    </div>

                </div>

            </AppLayout>
        );
    }


    // ========================================================
    // RENDER
    // ========================================================

    return (

        <AppLayout>

            <div className="space-y-6">

                {/* ==================================================
                    HEADER
                ================================================== */}

                <div className="flex flex-col gap-4 md:flex-row md:items-center md:justify-between">

                    <div>

                        <div className="flex items-center gap-3">

                            <div className="rounded-xl bg-blue-100 p-3 text-blue-600">

                                <Pill size={24} />

                            </div>

                            <div>

                                <h1 className="text-2xl font-bold text-gray-900">

                                    Medicine Reminders

                                </h1>

                                <p className="text-sm text-gray-500">

                                    Keep track of your medicines and daily doses.

                                </p>

                            </div>

                        </div>

                    </div>


                    <button
                        type="button"
                        onClick={() => {
                            resetForm();
                            setShowForm(true);
                        }}
                        className="inline-flex items-center justify-center gap-2 rounded-xl bg-blue-600 px-5 py-3 font-medium text-white transition hover:bg-blue-700"
                    >

                        <Plus size={18} />

                        Add Medicine

                    </button>

                </div>


                {/* ==================================================
                    ACTIVE DOSE REMINDER PANEL
                ================================================== */}

                {dueDoses.length > 0 && (

                    <div className="rounded-2xl border border-orange-200 bg-orange-50 p-5 shadow-sm">

                        <div className="mb-4 flex items-center gap-3">

                            <div className="flex h-10 w-10 items-center justify-center rounded-full bg-orange-100 text-orange-600">

                                <Bell size={22} />

                            </div>

                            <div>

                                <h2 className="font-semibold text-gray-900">

                                    Medicine Reminder

                                </h2>

                                <p className="text-sm text-gray-600">

                                    You have medicine scheduled for now.

                                </p>

                            </div>

                        </div>


                        <div className="space-y-3">

                            {dueDoses.map((dose) => {

                                const snoozeCount =
                                    snoozeCounts[dose.id] || 0;


                                return (

                                    <div
                                        key={dose.id}
                                        className="rounded-xl border border-orange-200 bg-white p-4"
                                    >

                                        <div className="flex flex-col gap-4 md:flex-row md:items-center md:justify-between">

                                            <div>

                                                <h3 className="font-semibold text-gray-900">

                                                    {dose.medicineName}

                                                </h3>

                                                <p className="text-sm text-gray-600">

                                                    Dosage: {dose.dosage}

                                                </p>

                                                <p className="mt-1 text-sm text-orange-700">

                                                    Scheduled:
                                                    {" "}
                                                    <strong>
                                                        {formatTime(
                                                            dose.scheduledTime
                                                        )}
                                                    </strong>

                                                </p>

                                            </div>


                                            <div className="flex flex-wrap gap-2">

                                                <button
                                                    type="button"
                                                    disabled={
                                                        doseLoading ===
                                                        dose.id
                                                    }
                                                    onClick={() =>
                                                        handleMarkTaken(
                                                            dose.id
                                                        )
                                                    }
                                                    className="inline-flex items-center justify-center gap-2 rounded-xl bg-green-600 px-4 py-2.5 text-sm font-medium text-white hover:bg-green-700 disabled:cursor-not-allowed disabled:opacity-60"
                                                >

                                                    {doseLoading ===
                                                    dose.id ? (

                                                        <>

                                                            <Loader2
                                                                size={17}
                                                                className="animate-spin"
                                                            />

                                                            Recording...

                                                        </>

                                                    ) : (

                                                        <>

                                                            <CheckCircle2
                                                                size={17}
                                                            />

                                                            Mark as Taken

                                                        </>

                                                    )}

                                                </button>


                                                <button
                                                    type="button"
                                                    disabled={
                                                        snoozeCount >= 3
                                                    }
                                                    onClick={() =>
                                                        handleSnooze(
                                                            dose
                                                        )
                                                    }
                                                    className="inline-flex items-center justify-center gap-2 rounded-xl border border-gray-300 px-4 py-2.5 text-sm font-medium text-gray-700 hover:bg-gray-50 disabled:cursor-not-allowed disabled:opacity-50"
                                                >

                                                    <TimerReset
                                                        size={17}
                                                    />

                                                    {snoozeCount >= 3
                                                        ? "Snooze Limit Reached"
                                                        : `Snooze 1 min (${snoozeCount}/3)`
                                                    }

                                                </button>

                                            </div>

                                        </div>

                                    </div>
                                );
                            })}

                        </div>

                    </div>
                )}


                {/* ==================================================
                    SUCCESS MESSAGE
                ================================================== */}

                {success && (

                    <div className="flex items-center gap-3 rounded-xl border border-green-200 bg-green-50 p-4 text-green-700">

                        <CheckCircle2 size={20} />

                        <span>
                            {success}
                        </span>

                    </div>

                )}


                {/* ==================================================
                    ERROR MESSAGE
                ================================================== */}

                {error && (

                    <div className="flex items-center gap-3 rounded-xl border border-red-200 bg-red-50 p-4 text-red-700">

                        <X size={20} />

                        <span>
                            {error}
                        </span>

                    </div>

                )}


                {/* ==================================================
                    ADD / EDIT MEDICINE FORM
                ================================================== */}

                {showForm && (

                    <div className="rounded-2xl border border-gray-200 bg-white p-6 shadow-sm">

                        <div className="mb-6 flex items-center justify-between">

                            <div>

                                <h2 className="text-lg font-semibold text-gray-900">

                                    {editingMedicine
                                        ? "Edit Medicine"
                                        : "Add Medicine"
                                    }

                                </h2>

                                <p className="text-sm text-gray-500">

                                    Enter the medicine and reminder details.

                                </p>

                            </div>


                            <button
                                type="button"
                                onClick={resetForm}
                                className="rounded-lg p-2 text-gray-500 hover:bg-gray-100"
                            >

                                <X size={20} />

                            </button>

                        </div>


                        <form
                            onSubmit={handleSubmit}
                            className="grid gap-5 md:grid-cols-2"
                        >

                            {/* ==================================================
                                MEDICINE NAME
                            ================================================== */}

                            <div>

                                <label className="mb-2 block text-sm font-medium text-gray-700">

                                    Medicine Name

                                </label>

                                <input
                                    type="text"
                                    name="medicineName"
                                    value={
                                        formData.medicineName
                                    }
                                    onChange={
                                        handleChange
                                    }
                                    required
                                    placeholder="e.g. Paracetamol"
                                    className="w-full rounded-xl border border-gray-300 px-4 py-3 outline-none focus:border-blue-500"
                                />

                            </div>


                            {/* ==================================================
                                DOSAGE
                            ================================================== */}

                            <div>

                                <label className="mb-2 block text-sm font-medium text-gray-700">

                                    Dosage

                                </label>

                                <input
                                    type="text"
                                    name="dosage"
                                    value={
                                        formData.dosage
                                    }
                                    onChange={
                                        handleChange
                                    }
                                    required
                                    placeholder="e.g. 500 mg"
                                    className="w-full rounded-xl border border-gray-300 px-4 py-3 outline-none focus:border-blue-500"
                                />

                            </div>


                            {/* ==================================================
                                FREQUENCY
                            ================================================== */}

                            <div>

                                <label className="mb-2 block text-sm font-medium text-gray-700">

                                    Frequency

                                </label>

                                <select
                                    name="frequency"
                                    value={
                                        formData.frequency
                                    }
                                    onChange={
                                        handleChange
                                    }
                                    required
                                    className="w-full rounded-xl border border-gray-300 bg-white px-4 py-3 outline-none focus:border-blue-500"
                                >

                                    {FREQUENCY_OPTIONS.map(
                                        (option) => (

                                            <option
                                                key={
                                                    option.value
                                                }
                                                value={
                                                    option.value
                                                }
                                            >

                                                {option.label}

                                            </option>

                                        )
                                    )}

                                </select>

                            </div>


                            {/* ==================================================
                                REMINDER TIMES
                            ================================================== */}

                            <div>

                                <label className="mb-2 block text-sm font-medium text-gray-700">

                                    Reminder Times

                                </label>


                                <div className="space-y-3">

                                    {formData.reminderTimes.map(
                                        (
                                            time,
                                            index
                                        ) => (

                                            <div
                                                key={index}
                                                className="flex items-center gap-3"
                                            >

                                                <div className="flex h-10 w-10 shrink-0 items-center justify-center rounded-lg bg-blue-50 text-blue-600">

                                                    <Clock
                                                        size={18}
                                                    />

                                                </div>


                                                <div className="flex-1">

                                                    <input
                                                        type="time"
                                                        value={time}
                                                        onChange={(event) =>
                                                            handleReminderTimeChange(
                                                                index,
                                                                event.target.value
                                                            )
                                                        }
                                                        required
                                                        className="w-full rounded-xl border border-gray-300 px-4 py-3 outline-none focus:border-blue-500"
                                                    />

                                                </div>

                                            </div>
                                        )
                                    )}

                                </div>


                                <p className="mt-2 text-xs text-gray-500">

                                    Add one time for each scheduled dose.

                                </p>

                            </div>


                            {/* ==================================================
                                START DATE
                            ================================================== */}

                            <div>

                                <label className="mb-2 block text-sm font-medium text-gray-700">

                                    Start Date

                                </label>

                                <input
                                    type="date"
                                    name="startDate"
                                    value={
                                        formData.startDate
                                    }
                                    onChange={
                                        handleChange
                                    }
                                    required
                                    className="w-full rounded-xl border border-gray-300 px-4 py-3 outline-none focus:border-blue-500"
                                />

                            </div>


                            {/* ==================================================
                                END DATE
                            ================================================== */}

                            <div>

                                <label className="mb-2 block text-sm font-medium text-gray-700">

                                    End Date

                                </label>

                                <input
                                    type="date"
                                    name="endDate"
                                    value={
                                        formData.endDate
                                    }
                                    onChange={
                                        handleChange
                                    }
                                    className="w-full rounded-xl border border-gray-300 px-4 py-3 outline-none focus:border-blue-500"
                                />

                            </div>


                            {/* ==================================================
                                INSTRUCTIONS
                            ================================================== */}

                            <div className="md:col-span-2">

                                <label className="mb-2 block text-sm font-medium text-gray-700">

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
                                    rows="3"
                                    placeholder="e.g. Take after meals"
                                    className="w-full rounded-xl border border-gray-300 px-4 py-3 outline-none focus:border-blue-500"
                                />

                            </div>


                            {/* ==================================================
                                FORM BUTTONS
                            ================================================== */}

                            <div className="flex gap-3 md:col-span-2">

                                <button
                                    type="submit"
                                    className="rounded-xl bg-blue-600 px-5 py-3 font-medium text-white hover:bg-blue-700"
                                >

                                    {editingMedicine
                                        ? "Update Medicine"
                                        : "Save Medicine"
                                    }

                                </button>


                                <button
                                    type="button"
                                    onClick={
                                        resetForm
                                    }
                                    className="rounded-xl border border-gray-300 px-5 py-3 font-medium text-gray-700 hover:bg-gray-50"
                                >

                                    Cancel

                                </button>

                            </div>

                        </form>

                    </div>

                )}


                {/* ==================================================
                    TODAY'S SUMMARY
                ================================================== */}

                {medicines.length > 0 && (

                    <div className="rounded-2xl border border-blue-100 bg-blue-50 p-5">

                        <div className="flex items-center gap-3">

                            <CheckCircle2
                                size={22}
                                className="text-blue-600"
                            />

                            <div>

                                <h2 className="font-semibold text-gray-900">

                                    Today's Medication

                                </h2>

                                <p className="text-sm text-gray-600">

                                    {
                                        todayDoses.filter(
                                            (dose) =>
                                                dose.status ===
                                                "TAKEN"
                                        ).length
                                    }{" "}

                                    of{" "}

                                    {todayDoses.length}{" "}

                                    scheduled dose(s) taken today.

                                </p>

                            </div>

                        </div>

                    </div>

                )}


                {/* ==================================================
                    MEDICINE LIST
                ================================================== */}

                {medicines.length === 0 ? (

                    <div className="rounded-2xl border border-dashed border-gray-300 bg-white p-12 text-center">

                        <Pill
                            size={40}
                            className="mx-auto mb-4 text-gray-400"
                        />

                        <h2 className="text-lg font-semibold text-gray-800">

                            No medicine reminders yet

                        </h2>

                        <p className="mt-2 text-sm text-gray-500">

                            Add your first medicine to start tracking your doses.

                        </p>

                    </div>

                ) : (

                    <div className="grid gap-5 md:grid-cols-2">

                        {medicines.map(
                            (medicine) => {

                                const medicineDoses =
                                    getMedicineDoses(
                                        medicine.id
                                    );


                                return (

                                    <div
                                        key={
                                            medicine.id
                                        }
                                        className="rounded-2xl border border-gray-200 bg-white p-6 shadow-sm"
                                    >

                                        {/* ==================================================
                                            CARD HEADER
                                        ================================================== */}

                                        <div className="flex items-start justify-between gap-4">

                                            <div className="flex items-start gap-3">

                                                <div className="rounded-xl bg-blue-100 p-3 text-blue-600">

                                                    <Pill
                                                        size={22}
                                                    />

                                                </div>


                                                <div>

                                                    <h3 className="text-lg font-semibold text-gray-900">

                                                        {
                                                            medicine.medicineName
                                                        }

                                                    </h3>

                                                    <p className="text-sm text-gray-500">

                                                        {
                                                            medicine.dosage
                                                        }

                                                    </p>

                                                </div>

                                            </div>


                                            <span
                                                className={`rounded-full px-3 py-1 text-xs font-medium ${
                                                    medicine.active
                                                        ? "bg-green-100 text-green-700"
                                                        : "bg-gray-100 text-gray-600"
                                                }`}
                                            >

                                                {medicine.active
                                                    ? "Active"
                                                    : "Inactive"
                                                }

                                            </span>

                                        </div>


                                        {/* ==================================================
                                            DETAILS
                                        ================================================== */}

                                        <div className="mt-5 space-y-4">


                                            {/* FREQUENCY */}

                                            <div className="text-sm text-gray-600">

                                                <strong className="text-gray-800">

                                                    Frequency:

                                                </strong>{" "}

                                                {
                                                    medicine.frequency
                                                }

                                            </div>


                                            {/* REMINDER TIMES */}

                                            <div>

                                                <div className="mb-2 flex items-center gap-3 text-sm text-gray-600">

                                                    <Clock
                                                        size={17}
                                                    />

                                                    <strong className="text-gray-800">

                                                        Reminder Times

                                                    </strong>

                                                </div>


                                                <div className="flex flex-wrap gap-2">

                                                    {(
                                                        Array.isArray(
                                                            medicine.reminderTimes
                                                        ) &&
                                                        medicine.reminderTimes.length > 0
                                                            ? medicine.reminderTimes
                                                            : medicine.reminderTime
                                                                ? [
                                                                      medicine.reminderTime
                                                                  ]
                                                                : []
                                                    ).map(
                                                        (
                                                            time,
                                                            index
                                                        ) => (

                                                            <span
                                                                key={
                                                                    index
                                                                }
                                                                className="rounded-lg bg-blue-50 px-3 py-1.5 text-sm font-medium text-blue-700"
                                                            >

                                                                {
                                                                    formatTime(
                                                                        time
                                                                    )
                                                                }

                                                            </span>

                                                        )
                                                    )}

                                                </div>

                                            </div>


                                            {/* DATE RANGE */}

                                            <div className="flex items-center gap-3 text-sm text-gray-600">

                                                <CalendarDays
                                                    size={17}
                                                />

                                                <span>

                                                    {
                                                        medicine.startDate
                                                    }

                                                    {medicine.endDate &&
                                                        ` → ${medicine.endDate}`
                                                    }

                                                </span>

                                            </div>


                                            {/* INSTRUCTIONS */}

                                            {medicine.instructions && (

                                                <div className="rounded-xl bg-gray-50 p-3 text-sm text-gray-600">

                                                    <strong className="text-gray-800">

                                                        Instructions:

                                                    </strong>{" "}

                                                    {
                                                        medicine.instructions
                                                    }

                                                </div>

                                            )}

                                        </div>


                                        {/* ==================================================
                                            TODAY'S INDIVIDUAL DOSES
                                        ================================================== */}

                                        {medicine.active && (

                                            <div className="mt-5 border-t border-gray-100 pt-5">

                                                <div className="mb-3 flex items-center justify-between">

                                                    <h4 className="font-semibold text-gray-800">

                                                        Today's Doses

                                                    </h4>

                                                    <span className="text-xs text-gray-500">

                                                        {
                                                            medicineDoses.length
                                                        }{" "}

                                                        scheduled

                                                    </span>

                                                </div>


                                                {medicineDoses.length === 0 ? (

                                                    <div className="rounded-xl bg-gray-50 px-4 py-3 text-sm text-gray-500">

                                                        No doses scheduled for today.

                                                    </div>

                                                ) : (

                                                    <div className="space-y-2">

                                                        {medicineDoses.map(
                                                            (
                                                                dose
                                                            ) => (

                                                                <div
                                                                    key={
                                                                        dose.id
                                                                    }
                                                                    className="flex flex-col gap-3 rounded-xl border border-gray-100 bg-gray-50 p-3 sm:flex-row sm:items-center sm:justify-between"
                                                                >

                                                                    <div className="flex items-center gap-3">

                                                                        <Clock
                                                                            size={
                                                                                17
                                                                            }
                                                                            className="text-gray-500"
                                                                        />

                                                                        <div>

                                                                            <p className="font-medium text-gray-800">

                                                                                {
                                                                                    formatTime(
                                                                                        dose.scheduledTime
                                                                                    )
                                                                                }

                                                                            </p>

                                                                            <p className="text-xs text-gray-500">

                                                                                {
                                                                                    dose.status ===
                                                                                    "TAKEN"
                                                                                        ? `Taken ${
                                                                                              dose.takenAt
                                                                                                  ? new Date(
                                                                                                        dose.takenAt
                                                                                                    ).toLocaleTimeString(
                                                                                                        [],
                                                                                                        {
                                                                                                            hour: "numeric",
                                                                                                            minute: "2-digit"
                                                                                                        }
                                                                                                    )
                                                                                                  : ""
                                                                                          }`
                                                                                        : dose.status ===
                                                                                          "MISSED"
                                                                                            ? "Missed"
                                                                                            : "Pending"
                                                                                }

                                                                            </p>

                                                                        </div>

                                                                    </div>


                                                                    {dose.status ===
                                                                        "TAKEN" ? (

                                                                        <span className="inline-flex items-center gap-2 rounded-lg bg-green-100 px-3 py-2 text-sm font-medium text-green-700">

                                                                            <CheckCircle2
                                                                                size={
                                                                                    16
                                                                                }
                                                                            />

                                                                            Taken

                                                                        </span>

                                                                    ) : dose.status ===
                                                                      "MISSED" ? (

                                                                        <span className="rounded-lg bg-red-100 px-3 py-2 text-sm font-medium text-red-700">

                                                                            Missed

                                                                        </span>

                                                                    ) : (

                                                                        <button
                                                                            type="button"
                                                                            disabled={
                                                                                doseLoading ===
                                                                                dose.id
                                                                            }
                                                                            onClick={() =>
                                                                                handleMarkTaken(
                                                                                    dose.id
                                                                                )
                                                                            }
                                                                            className="inline-flex items-center justify-center gap-2 rounded-lg bg-green-600 px-3 py-2 text-sm font-medium text-white hover:bg-green-700 disabled:cursor-not-allowed disabled:opacity-60"
                                                                        >

                                                                            {doseLoading ===
                                                                            dose.id ? (

                                                                                <Loader2
                                                                                    size={
                                                                                        16
                                                                                    }
                                                                                    className="animate-spin"
                                                                                />

                                                                            ) : (

                                                                                <CheckCircle2
                                                                                    size={
                                                                                        16
                                                                                    }
                                                                                />

                                                                            )}

                                                                            {doseLoading ===
                                                                            dose.id
                                                                                ? "Recording..."
                                                                                : "Take Dose"
                                                                            }

                                                                        </button>

                                                                    )}

                                                                </div>

                                                            )
                                                        )}

                                                    </div>

                                                )}

                                            </div>

                                        )}


                                        {/* ==================================================
                                            ACTIONS
                                        ================================================== */}

                                        <div className="mt-4 flex gap-3">

                                            <button
                                                type="button"
                                                onClick={() =>
                                                    handleEdit(
                                                        medicine
                                                    )
                                                }
                                                className="flex flex-1 items-center justify-center gap-2 rounded-xl border border-gray-300 px-4 py-2.5 text-sm font-medium text-gray-700 hover:bg-gray-50"
                                            >

                                                <Pencil
                                                    size={16}
                                                />

                                                Edit

                                            </button>


                                            {medicine.active && (

                                                <button
                                                    type="button"
                                                    onClick={() =>
                                                        handleDeactivate(
                                                            medicine.id
                                                        )
                                                    }
                                                    className="rounded-xl border border-orange-200 px-4 py-2.5 text-sm font-medium text-orange-600 hover:bg-orange-50"
                                                >

                                                    Deactivate

                                                </button>

                                            )}


                                            <button
                                                type="button"
                                                onClick={() =>
                                                    handleDelete(
                                                        medicine.id
                                                    )
                                                }
                                                className="rounded-xl border border-red-200 p-2.5 text-red-600 hover:bg-red-50"
                                            >

                                                <Trash2
                                                    size={16}
                                                />

                                            </button>

                                        </div>

                                    </div>
                                );
                            }
                        )}

                    </div>

                )}

            </div>

        </AppLayout>
    );
}