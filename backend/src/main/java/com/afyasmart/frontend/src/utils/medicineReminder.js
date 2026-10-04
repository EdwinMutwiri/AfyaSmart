// ============================================================
// AfyaSmart - Medicine Reminder Utility
// ============================================================
//
// This utility checks the patient's medicine reminder times
// and displays a browser notification when a medicine is due.
//
// It runs entirely in the browser, which means it does not
// require a new backend endpoint.
//
// ============================================================

/**
 * Ask the browser for permission to send notifications.
 */
export const requestNotificationPermission = async () => {
    // Browser does not support notifications
    if (!("Notification" in window)) {
        console.warn(
            "This browser does not support notifications."
        );

        return "unsupported";
    }

    // Permission already granted
    if (Notification.permission === "granted") {
        return "granted";
    }

    // Permission was previously denied
    if (Notification.permission === "denied") {
        return "denied";
    }

    // Ask the user
    const permission =
        await Notification.requestPermission();

    return permission;
};


/**
 * Send a medicine reminder notification.
 */
export const sendMedicineNotification = (medicine) => {

    if (!("Notification" in window)) {
        return;
    }

    if (Notification.permission !== "granted") {
        return;
    }

    const medicineName =
        medicine.medicineName || "Your medicine";

    const dosage =
        medicine.dosage || "";

    const reminderTime =
        medicine.reminderTime || "";

    const notification = new Notification(
        "AfyaSmart Medicine Reminder",
        {
            body:
                `It is time to take ${medicineName}` +
                `${dosage ? ` (${dosage})` : ""}.`,
            icon: "/favicon.ico",
            tag: `medicine-${medicine.id}-${reminderTime}`
        }
    );

    // Automatically close notification after 10 seconds
    setTimeout(() => {
        notification.close();
    }, 10000);
};


/**
 * Determine whether a medicine is due right now.
 *
 * We compare the current hour and minute with the
 * medicine's reminderTime.
 */
export const isMedicineDue = (medicine) => {

    if (!medicine) {
        return false;
    }

    if (!medicine.active) {
        return false;
    }

    if (!medicine.reminderTime) {
        return false;
    }

    const now = new Date();

    const currentHours =
        String(now.getHours()).padStart(2, "0");

    const currentMinutes =
        String(now.getMinutes()).padStart(2, "0");

    const currentTime =
        `${currentHours}:${currentMinutes}`;

    const medicineTime =
        medicine.reminderTime.substring(0, 5);

    return currentTime === medicineTime;
};


/**
 * Create a unique key for today's reminder.
 *
 * This prevents the same medicine notification from
 * appearing repeatedly during the same minute.
 */
export const getReminderKey = (medicine) => {

    const today =
        new Date().toISOString().split("T")[0];

    const reminderTime =
        medicine.reminderTime
            ? medicine.reminderTime.substring(0, 5)
            : "";

    return `afyasmart-reminder-${today}-${medicine.id}-${reminderTime}`;
};


/**
 * Check all medicines and notify the patient when one
 * is due.
 */
export const checkMedicineReminders = (medicines) => {

    if (!Array.isArray(medicines)) {
        return;
    }

    medicines.forEach((medicine) => {

        if (!isMedicineDue(medicine)) {
            return;
        }

        const reminderKey =
            getReminderKey(medicine);

        /*
         * localStorage remembers that we already showed
         * this reminder today.
         */
        const alreadyShown =
            localStorage.getItem(reminderKey);

        if (alreadyShown) {
            return;
        }

        sendMedicineNotification(medicine);

        localStorage.setItem(
            reminderKey,
            "shown"
        );
    });
};