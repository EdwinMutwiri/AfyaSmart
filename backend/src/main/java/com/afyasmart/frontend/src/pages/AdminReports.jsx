import { useState } from "react";

import {
    FileBarChart,
    FileText,
    Download,
    CalendarDays,
    Users,
    UserRound,
    Loader2
} from "lucide-react";

import AppLayout from "../components/layout/AppLayout";

import {
    downloadAppointmentPdf,
    downloadAppointmentExcel,
    downloadPatientPdf,
    downloadPatientExcel,
    downloadDoctorPdf,
    downloadDoctorExcel
} from "../services/reportService";


/**
 * ============================================================
 * AfyaSmart - Admin Reports
 * ============================================================
 *
 * Central reporting page for AfyaSmart administrators.
 *
 * Reports available:
 *
 * 1. Appointment Reports
 * 2. Patient Reports
 * 3. Doctor Reports
 *
 * Each report can be downloaded as:
 *
 * - PDF
 * - Excel
 * ============================================================
 */


export default function AdminReports() {

    const [loading, setLoading] = useState("");
    const [error, setError] = useState("");


    // ============================================================
    // GENERIC FILE DOWNLOAD HELPER
    // ============================================================

    const downloadFile = (blob, filename) => {

        const url = window.URL.createObjectURL(blob);

        const link = document.createElement("a");

        link.href = url;

        link.setAttribute("download", filename);

        document.body.appendChild(link);

        link.click();

        link.remove();

        window.URL.revokeObjectURL(url);
    };


    // ============================================================
    // DOWNLOAD HANDLER
    // ============================================================

    const handleDownload = async (
        reportType,
        format,
        downloadFunction,
        filename
    ) => {

        try {

            setError("");

            setLoading(`${reportType}-${format}`);

            const file = await downloadFunction();

            downloadFile(file, filename);

        } catch (err) {

            console.error(
                `Failed to download ${reportType} ${format} report:`,
                err
            );

            setError(
                `Unable to download the ${reportType} ${format} report. Please make sure the backend is running.`
            );

        } finally {

            setLoading("");

        }
    };


    // ============================================================
    // REPORT CARD
    // ============================================================

    const ReportCard = ({
        title,
        description,
        icon: Icon,
        type,
        pdfFunction,
        excelFunction,
        pdfFilename,
        excelFilename
    }) => {

        const pdfLoading =
            loading === `${type}-pdf`;

        const excelLoading =
            loading === `${type}-excel`;


        return (
            <div
                style={{
                    background: "#ffffff",
                    borderRadius: "16px",
                    padding: "24px",
                    border: "1px solid #e5e7eb",
                    boxShadow: "0 4px 12px rgba(0,0,0,0.05)"
                }}
            >

                {/* Header */}

                <div
                    style={{
                        display: "flex",
                        alignItems: "center",
                        gap: "14px",
                        marginBottom: "18px"
                    }}
                >

                    <div
                        style={{
                            width: "48px",
                            height: "48px",
                            borderRadius: "12px",
                            background: "#eef6ff",
                            display: "flex",
                            alignItems: "center",
                            justifyContent: "center"
                        }}
                    >

                        <Icon
                            size={25}
                            color="#2563eb"
                        />

                    </div>


                    <div>

                        <h2
                            style={{
                                margin: 0,
                                fontSize: "20px",
                                fontWeight: "700",
                                color: "#111827"
                            }}
                        >
                            {title}
                        </h2>

                        <p
                            style={{
                                margin: "5px 0 0",
                                color: "#6b7280",
                                fontSize: "14px"
                            }}
                        >
                            {description}
                        </p>

                    </div>

                </div>


                {/* Buttons */}

                <div
                    style={{
                        display: "flex",
                        gap: "12px",
                        flexWrap: "wrap"
                    }}
                >

                    {/* PDF */}

                    <button
                        onClick={() =>
                            handleDownload(
                                type,
                                "pdf",
                                pdfFunction,
                                pdfFilename
                            )
                        }
                        disabled={loading !== ""}
                        style={{
                            display: "flex",
                            alignItems: "center",
                            gap: "8px",
                            padding: "11px 18px",
                            borderRadius: "9px",
                            border: "none",
                            background: "#111827",
                            color: "#ffffff",
                            cursor:
                                loading !== ""
                                    ? "not-allowed"
                                    : "pointer",
                            opacity:
                                loading !== ""
                                    ? 0.65
                                    : 1,
                            fontWeight: "600"
                        }}
                    >

                        {pdfLoading ? (
                            <Loader2
                                size={17}
                                className="spin"
                            />
                        ) : (
                            <FileText size={17} />
                        )}

                        {pdfLoading
                            ? "Generating..."
                            : "Download PDF"}

                    </button>


                    {/* Excel */}

                    <button
                        onClick={() =>
                            handleDownload(
                                type,
                                "excel",
                                excelFunction,
                                excelFilename
                            )
                        }
                        disabled={loading !== ""}
                        style={{
                            display: "flex",
                            alignItems: "center",
                            gap: "8px",
                            padding: "11px 18px",
                            borderRadius: "9px",
                            border: "1px solid #d1d5db",
                            background: "#ffffff",
                            color: "#111827",
                            cursor:
                                loading !== ""
                                    ? "not-allowed"
                                    : "pointer",
                            opacity:
                                loading !== ""
                                    ? 0.65
                                    : 1,
                            fontWeight: "600"
                        }}
                    >

                        {excelLoading ? (
                            <Loader2
                                size={17}
                                className="spin"
                            />
                        ) : (
                            <Download size={17} />
                        )}

                        {excelLoading
                            ? "Generating..."
                            : "Download Excel"}

                    </button>

                </div>

            </div>
        );
    };


    // ============================================================
    // PAGE
    // ============================================================

    return (

        <AppLayout>

            <div
                style={{
                    padding: "30px",
                    background: "#f8fafc",
                    minHeight: "100vh"
                }}
            >

                {/* Page Header */}

                <div
                    style={{
                        display: "flex",
                        alignItems: "center",
                        gap: "15px",
                        marginBottom: "30px"
                    }}
                >

                    <div
                        style={{
                            width: "52px",
                            height: "52px",
                            borderRadius: "14px",
                            background: "#e0ecff",
                            display: "flex",
                            alignItems: "center",
                            justifyContent: "center"
                        }}
                    >

                        <FileBarChart
                            size={28}
                            color="#2563eb"
                        />

                    </div>


                    <div>

                        <h1
                            style={{
                                margin: 0,
                                fontSize: "28px",
                                fontWeight: "750",
                                color: "#111827"
                            }}
                        >
                            Reports Center
                        </h1>

                        <p
                            style={{
                                margin: "6px 0 0",
                                color: "#6b7280"
                            }}
                        >
                            Generate and download AfyaSmart system reports.
                        </p>

                    </div>

                </div>


                {/* Error Message */}

                {error && (

                    <div
                        style={{
                            background: "#fef2f2",
                            border: "1px solid #fecaca",
                            color: "#b91c1c",
                            padding: "14px 18px",
                            borderRadius: "10px",
                            marginBottom: "22px"
                        }}
                    >

                        {error}

                    </div>

                )}


                {/* Reports */}

                <div
                    style={{
                        display: "grid",
                        gridTemplateColumns:
                            "repeat(auto-fit, minmax(320px, 1fr))",
                        gap: "22px"
                    }}
                >

                    <ReportCard
                        title="Appointment Reports"
                        description="View and export appointment records."
                        icon={CalendarDays}
                        type="appointments"
                        pdfFunction={downloadAppointmentPdf}
                        excelFunction={downloadAppointmentExcel}
                        pdfFilename="AfyaSmart_Appointments_Report.pdf"
                        excelFilename="AfyaSmart_Appointments_Report.xlsx"
                    />


                    <ReportCard
                        title="Patient Reports"
                        description="View and export registered patient records."
                        icon={Users}
                        type="patients"
                        pdfFunction={downloadPatientPdf}
                        excelFunction={downloadPatientExcel}
                        pdfFilename="AfyaSmart_Patients_Report.pdf"
                        excelFilename="AfyaSmart_Patients_Report.xlsx"
                    />


                    <ReportCard
                        title="Doctor Reports"
                        description="View and export registered doctor profiles."
                        icon={UserRound}
                        type="doctors"
                        pdfFunction={downloadDoctorPdf}
                        excelFunction={downloadDoctorExcel}
                        pdfFilename="AfyaSmart_Doctors_Report.pdf"
                        excelFilename="AfyaSmart_Doctors_Report.xlsx"
                    />

                </div>

            </div>

        </AppLayout>
    );
}