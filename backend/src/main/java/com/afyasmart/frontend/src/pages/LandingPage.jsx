import { Link } from "react-router-dom";
import {
  Activity,
  ShieldCheck,
  Brain,
  CalendarClock,
  HeartPulse,
  ArrowRight
} from "lucide-react";

export default function LandingPage() {
  return (
    <div className="min-h-screen bg-slate-50">

      {/* ================= NAVBAR ================= */}

      <nav className="bg-white shadow-sm sticky top-0 z-50">

        <div className="max-w-7xl mx-auto flex justify-between items-center px-8 py-5">

          <div>
            <h1 className="text-3xl font-extrabold text-blue-600">
              AfyaSmart
            </h1>

            <p className="text-sm text-gray-500">
              Intelligent Digital Healthcare
            </p>
          </div>

          <div className="space-x-4">

            <Link
              to="/login"
              className="px-5 py-2 rounded-xl border border-blue-600 text-blue-600 hover:bg-blue-600 hover:text-white transition"
            >
              Sign In
            </Link>

            <Link
              to="/register"
              className="px-5 py-2 rounded-xl bg-blue-600 text-white hover:bg-blue-700 transition"
            >
              Get Started
            </Link>

          </div>

        </div>

      </nav>

      {/* ================= HERO ================= */}

      <section className="max-w-7xl mx-auto px-8 py-20 grid lg:grid-cols-2 gap-16 items-center">

        <div>

          <span className="inline-block bg-green-100 text-green-700 px-4 py-2 rounded-full text-sm font-semibold">
            🇰🇪 Built in Kenya for Smarter Healthcare
          </span>

          <h1 className="mt-6 text-6xl font-extrabold text-gray-800 leading-tight">

            Your

            <span className="text-blue-600">

              {" "}Intelligent Digital{" "}

            </span>

            Health Companion

          </h1>

          <p className="mt-8 text-xl text-gray-600 leading-8">

            Empowering healthier lives through secure health records,
            smart health assessments, medication management,
            appointment scheduling, and connected care —
            all in one intelligent platform.

          </p>

          <div className="mt-10 flex gap-5">

            <Link
              to="/register"
              className="bg-blue-600 hover:bg-blue-700 text-white px-8 py-4 rounded-xl font-semibold flex items-center gap-2"
            >

              Get Started

              <ArrowRight size={20} />

            </Link>

            <Link
              to="/login"
              className="border border-blue-600 text-blue-600 hover:bg-blue-50 px-8 py-4 rounded-xl font-semibold"
            >

              Sign In

            </Link>

          </div>

          <div className="mt-10 flex flex-wrap gap-4 text-sm">

            <span className="bg-white px-4 py-2 rounded-full shadow">
              🔒 Secure Records
            </span>

            <span className="bg-white px-4 py-2 rounded-full shadow">
              🧠 Smart Health Insights
            </span>

            <span className="bg-white px-4 py-2 rounded-full shadow">
              📅 Connected Care
            </span>

          </div>

        </div>

        {/* ================= MOCK DASHBOARD ================= */}

        <div className="bg-white rounded-3xl shadow-2xl p-8">

          <div className="flex justify-between items-center">

            <h2 className="text-2xl font-bold">

              Health Dashboard

            </h2>

            <span className="text-green-600 font-bold">

              Low Risk

            </span>

          </div>

          <div className="grid grid-cols-2 gap-5 mt-8">

            <div className="bg-blue-50 rounded-2xl p-5">

              <p className="text-gray-500">

                Health Score

              </p>

              <h1 className="text-4xl font-bold text-blue-600">

                87%

              </h1>

            </div>

            <div className="bg-green-50 rounded-2xl p-5">

              <p className="text-gray-500">

                BMI

              </p>

              <h1 className="text-4xl font-bold text-green-600">

                23.9

              </h1>

            </div>

            <div className="bg-yellow-50 rounded-2xl p-5">

              <p className="text-gray-500">

                Next Appointment

              </p>

              <h3 className="font-bold">

                Tuesday

              </h3>

            </div>

            <div className="bg-purple-50 rounded-2xl p-5">

              <p className="text-gray-500">

                Medication

              </p>

              <h3 className="font-bold">

                Vitamin D

              </h3>

            </div>

          </div>

        </div>

      </section>

      {/* ================= FEATURES ================= */}

      <section className="max-w-7xl mx-auto px-8 pb-20">

        <h2 className="text-4xl font-bold text-center">

          Why Choose AfyaSmart?

        </h2>

        <p className="text-center text-gray-600 mt-4 max-w-3xl mx-auto">

          AfyaSmart combines preventive healthcare, intelligent analytics,
          and secure digital health services to help individuals make
          informed health decisions anytime and anywhere.

        </p>

        <div className="grid md:grid-cols-2 lg:grid-cols-4 gap-8 mt-16">

          <div className="bg-white rounded-2xl p-8 shadow">

            <Brain
              className="text-blue-600"
              size={42}
            />

            <h3 className="text-xl font-bold mt-5">

              Smart Health Assessment

            </h3>

            <p className="mt-3 text-gray-600">

              Receive personalized health insights and recommendations.

            </p>

          </div>

          <div className="bg-white rounded-2xl p-8 shadow">

            <ShieldCheck
              className="text-green-600"
              size={42}
            />

            <h3 className="text-xl font-bold mt-5">

              Secure Health Records

            </h3>

            <p className="mt-3 text-gray-600">

              Your health information remains private and protected.

            </p>

          </div>

          <div className="bg-white rounded-2xl p-8 shadow">

            <CalendarClock
              className="text-purple-600"
              size={42}
            />

            <h3 className="text-xl font-bold mt-5">

              Connected Care

            </h3>

            <p className="mt-3 text-gray-600">

              Manage appointments and stay connected with healthcare providers.

            </p>

          </div>

          <div className="bg-white rounded-2xl p-8 shadow">

            <HeartPulse
              className="text-red-500"
              size={42}
            />

            <h3 className="text-xl font-bold mt-5">

              Wellness Tracking

            </h3>

            <p className="mt-3 text-gray-600">

              Monitor your progress with intelligent health metrics and trends.

            </p>

          </div>

        </div>

      </section>

      {/* ================= FOOTER ================= */}

      <footer className="bg-slate-900 text-white text-center py-10">

        <Activity
          className="mx-auto mb-4 text-blue-400"
          size={40}
        />

        <h2 className="text-2xl font-bold">

          AfyaSmart

        </h2>

        <p className="mt-3 text-gray-400">

          Building the future of preventive healthcare.

        </p>

        <p className="mt-6 text-sm text-gray-500">

          © 2026 AfyaSmart. All Rights Reserved.

        </p>

      </footer>

    </div>
  );
}