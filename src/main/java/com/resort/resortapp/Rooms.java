package com.resort.resortapp;

public enum Rooms{
    ALL_ROOMS("all", 32),
    ROOM_J("j", 6),
    ROOM_G("g", 9),
    ATTIC("attic", 7),
    KUBO_1("k1", 5),
    KUBO_2("k2", 5);

    final String abbreviatedName;
    final int pax;

    static public final Rooms[] values = values();

    Rooms(String abbreviatedName, int pax) {
        this.abbreviatedName = abbreviatedName;
        this.pax = pax;
    }

    public int getPax() {
        return pax;
    }

    public Rooms prev() {
        return values[(ordinal() - 1  + values.length) % values.length];
    }

    public Rooms next() {
        return values[(ordinal() + 1) % values.length];
    }
    public String getDisplayName() {
        return this.name().replace("_", " ");
    }

    public String getAbbreviatedName() {
        return abbreviatedName;
    }
    public static String abbvToDisplay(String abbreviatedName) {
        for (Rooms room : Rooms.values()) {
            if (room.getAbbreviatedName().equalsIgnoreCase(abbreviatedName)) {
                return room.getDisplayName();
            }
        }
        // If no match is found, you can return the input string as is or any other default value
        return abbreviatedName;
    }
    public static String displayToAbbv(String displayName) {
        for (Rooms room : Rooms.values()) {
            if (room.getDisplayName().equalsIgnoreCase(displayName)) {
                return room.getAbbreviatedName();
            }
        }
        // If no match is found, you can return the input string as is or any other default value
        return displayName;
    }
}