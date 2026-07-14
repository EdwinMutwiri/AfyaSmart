import { Routes, Route } from "react-router-dom";

import LandingPage from "./pages/LandingPage";
import LoginPage from "./pages/LoginPage";
import RegisterPage from "./pages/RegisterPage";
import Dashboard from "./pages/Dashboard";
import Assessment from "./pages/Assessment";
import AssessmentHistory from "./pages/AssessmentHistory";
import AppointmentPage from "./pages/AppointmentPage";
import DoctorDirectory from "./pages/DoctorDirectory";
import MyAppointments from "./pages/MyAppointments";
import DoctorDashboard from "./pages/DoctorDashboard";

import ProtectedRoute from "./components/auth/ProtectedRoute";
import AdminDashboard from "./pages/AdminDashboard";

import UserManagement from "./pages/UserManagement";
import CreateDoctor from "./pages/CreateDoctor";


function App() {

    return (

        <Routes>

            {/* Public Routes */}

            <Route path="/" element={<LandingPage />} />

            <Route path="/login" element={<LoginPage />} />

            <Route path="/register" element={<RegisterPage />} />



            {/* Patient Routes */}

            <Route
                path="/dashboard"
                element={
                    <ProtectedRoute allowedRoles={["PATIENT"]}>
                        <Dashboard />
                    </ProtectedRoute>
                }
            />

            <Route
                path="/assessment"
                element={
                    <ProtectedRoute allowedRoles={["PATIENT"]}>
                        <Assessment />
                    </ProtectedRoute>
                }
            />

            <Route
                path="/assessment-history"
                element={
                    <ProtectedRoute allowedRoles={["PATIENT"]}>
                        <AssessmentHistory />
                    </ProtectedRoute>
                }
            />

            <Route
                path="/appointments"
                element={
                    <ProtectedRoute allowedRoles={["PATIENT"]}>
                        <AppointmentPage />
                    </ProtectedRoute>
                }
            />

            <Route
                path="/my-appointments"
                element={
                    <ProtectedRoute allowedRoles={["PATIENT"]}>
                        <MyAppointments />
                    </ProtectedRoute>
                }
            />

            <Route
                path="/doctors"
                element={
                    <ProtectedRoute allowedRoles={["PATIENT"]}>
                        <DoctorDirectory />
                    </ProtectedRoute>
                }
            />



            {/* Doctor Routes */}

            <Route
                path="/doctor-dashboard"
                element={
                    <ProtectedRoute allowedRoles={["DOCTOR"]}>
                        <DoctorDashboard />
                    </ProtectedRoute>
                }
            />


            {/* Admin Routes */}
            <Route
                path="/admin-dashboard"
                element={
                    <ProtectedRoute allowedRoles={["ADMIN"]}>
                        <AdminDashboard />
                    </ProtectedRoute>
                }
            />

            <Route
                path="/users"
                element={
                    <ProtectedRoute allowedRoles={["ADMIN"]}>
                        <UserManagement />
                    </ProtectedRoute>
                }
            />

            <Route
                path="/create-doctor"
                element={
                    <ProtectedRoute allowedRoles={["ADMIN"]}>
                        <CreateDoctor />
                    </ProtectedRoute>
                }
            />



        </Routes>

    );

}

export default App;