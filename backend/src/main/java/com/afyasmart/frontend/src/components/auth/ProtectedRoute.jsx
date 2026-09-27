import { Navigate } from "react-router-dom";

/**
 * ============================================================
 * AfyaSmart - Protected Route
 * ============================================================
 *
 * Protects application routes based on:
 *
 * 1. Whether a user is logged in
 * 2. The role assigned to the logged-in user
 *
 * Roles supported by AfyaSmart:
 *
 * - PATIENT
 * - DOCTOR
 * - ADMIN
 *
 * ============================================================
 */

export default function ProtectedRoute({
    children,
    allowedRoles = []
}) {

    /**
     * Retrieve the currently logged-in user.
     */
    const storedUser =
        localStorage.getItem("user");

    /**
     * If there is no logged-in user,
     * redirect to the login page.
     */
    if (!storedUser) {
        return (
            <Navigate
                to="/login"
                replace
            />
        );
    }


    /**
     * Convert the stored JSON string back into
     * a JavaScript object.
     */
    let user;

    try {

        user = JSON.parse(storedUser);

    } catch (error) {

        console.error(
            "Invalid user data in localStorage:",
            error
        );

        /**
         * Remove corrupted authentication data.
         */
        localStorage.removeItem("user");

        return (
            <Navigate
                to="/login"
                replace
            />
        );
    }


    /**
     * Make sure a valid user object exists.
     */
    if (!user || !user.role) {

        localStorage.removeItem("user");

        return (
            <Navigate
                to="/login"
                replace
            />
        );
    }


    /**
     * Normalize the role.
     *
     * This protects us from differences such as:
     *
     * "admin"
     * "Admin"
     * " ADMIN "
     *
     * All become:
     *
     * "ADMIN"
     */
    const userRole =
        String(user.role)
            .trim()
            .toUpperCase();


    /**
     * Normalize all allowed roles as well.
     */
    const normalizedAllowedRoles =
        allowedRoles.map(
            (role) =>
                String(role)
                    .trim()
                    .toUpperCase()
        );


    /**
     * Check whether the logged-in user's role
     * is allowed to access this route.
     */
    if (
        normalizedAllowedRoles.length > 0 &&
        !normalizedAllowedRoles.includes(userRole)
    ) {

        console.warn(
            "Access denied.",
            {
                userRole,
                allowedRoles: normalizedAllowedRoles
            }
        );

        return (
            <Navigate
                to="/login"
                replace
            />
        );
    }


    /**
     * User is authenticated and authorized.
     */
    return children;
}