package com.camper.rental.security;

public enum Permission {
    // Fleet
    FLEET_READ,
    FLEET_WRITE,

    // Reservations
    RESERVATIONS_USER,
    RESERVATIONS_ADMIN,

    // CMS
    CMS_ADMIN,

    // Profile
    PROFILE_READ
}

