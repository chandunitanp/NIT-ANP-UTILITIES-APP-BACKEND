package com.nit.noticeboard.model;

public enum Department {

    BIOTECHNOLOGY("Bio Technology"),
    CHEMICAL("Chemical Engineering"),
    CIVIL("Civil Engineering"),
    CSE("Computer Science and Engineering"),
    EEE("Electrical and Electronics Engineering"),
    ECE("Electronics and Communication Engineering"),
    MECH("Mechanical Engineering"),
    METALLURGY("Metallurgical and Materials Engineering"),
    SOS("School of Sciences"),
    SHM("School of Humanities & Management");

    private final String displayName;

    Department(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    // ✅ JAVA 8 COMPATIBLE
    public static Department fromString(String value) {

        for (Department d : Department.values()) {
            if (d.name().equalsIgnoreCase(value)
                || d.displayName.equalsIgnoreCase(value)) {
                return d;
            }
        }

        throw new IllegalArgumentException("Invalid department: " + value);
    }
}
