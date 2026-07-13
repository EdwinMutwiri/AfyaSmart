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

function App() {
    return (
        <Routes>
            <Route path="/" element={<LandingPage />} />
            <Route path="/login" element={<LoginPage />} />
            <Route path="/register" element={<RegisterPage />} />
            <Route path="/dashboard" element={<Dashboard />} />
            <Route path="/assessment" element={<Assessment />} />
            <Route
                path="/assessment-history"
                element={<AssessmentHistory />}
            />
            <Route
                path="/appointments"
                element={<AppointmentPage />}
            />

            <Route path="/doctors" element={<DoctorDirectory />} />

            <Route
                path="/my-appointments"
                element={<MyAppointments />}
            />



        </Routes>
    );
}

export default App;