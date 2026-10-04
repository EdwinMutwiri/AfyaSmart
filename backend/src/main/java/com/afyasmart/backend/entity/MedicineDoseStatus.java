package com.afyasmart.backend.entity;

/**
 * ============================================================
 * AfyaSmart - Medicine Dose Status
 * ============================================================
 *
 * Represents the status of a scheduled medicine dose.
 * ============================================================
 */
public enum MedicineDoseStatus {

    /**
     * Dose is scheduled but has not yet been taken.
     */
    PENDING,

    /**
     * Patient confirmed that the dose was taken.
     */
    TAKEN,

    /**
     * Scheduled dose was not taken.
     */
    MISSED
}