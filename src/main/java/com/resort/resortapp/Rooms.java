package com.resort.resortapp;

import javafx.scene.control.CheckBox;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    public static Rooms fromString(String roomValue) {
        for (Rooms room : values()) {
            if (room.abbreviatedName.equals(roomValue) || room.getDisplayName().equals(roomValue)) {
                return room;
            }
        }
        return ALL_ROOMS; // Default value if no matching enum constant is found
    }
    public static List<String> getRoomDisplayNameList(){
        List<String> result = new ArrayList<>();

        for(int i = 0; i < 6; i++){
            result.add(values[i].getDisplayName());
        }
        result.remove(0);

        return result;
    }
    public static Set<String> getRoomAbbreviateNameSet(){
        Set<String> result = new HashSet<>();

        for(Rooms room : values()){
            result.add(room.getAbbreviatedName());
        }
        result.remove("all");

        return result;
    }
    public static Set<String> getRoomDisplayNameSet(){
        Set<String> result = new HashSet<>();

        for(Rooms room : values()){
            result.add(room.getDisplayName());
        }
        result.remove("ALL ROOMS");

        return result;
    }

    public static String manageCheckboxesString(List<CheckBox> roomCheckboxes){
//        List<String> result = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        if(roomCheckboxes.get(0).isSelected()){
            sb.append(ROOM_G.getAbbreviatedName()).append(", ");
//            result.add(ROOM_G.getAbbreviatedName());
        }
        if(roomCheckboxes.get(1).isSelected()){
            sb.append(ROOM_J.getAbbreviatedName()).append(", ");
//            result.add(ROOM_J.getAbbreviatedName());
        }
        if(roomCheckboxes.get(2).isSelected()){
            sb.append(ATTIC.getAbbreviatedName()).append(", ");
//            result.add(ATTIC.getAbbreviatedName());
        }
        if(roomCheckboxes.get(3).isSelected()){
            sb.append(KUBO_1.getAbbreviatedName()).append(", ");
//            result.add(KUBO_1.getAbbreviatedName());
        }
        if(roomCheckboxes.get(4).isSelected()){
            sb.append(KUBO_2.getAbbreviatedName()).append(", ");
//            result.add(KUBO_2.getAbbreviatedName());
        }
        if (!sb.isEmpty()) {
            sb.delete(sb.length() - 2, sb.length());
        }
        return sb.toString();
//        return result.toString();
    }

    public static Set<String> manageCheckboxesSet(List<CheckBox> roomCheckboxes){
        Set<String> result = new HashSet<>();
        if(roomCheckboxes.get(0).isSelected()){
            result.add(ROOM_G.getDisplayName());
        }
        if(roomCheckboxes.get(1).isSelected()){
            result.add(ROOM_J.getDisplayName());
        }
        if(roomCheckboxes.get(2).isSelected()){
            result.add(ATTIC.getDisplayName());
        }
        if(roomCheckboxes.get(3).isSelected()){
            result.add(KUBO_1.getDisplayName());
        }
        if(roomCheckboxes.get(4).isSelected()){
            result.add(KUBO_2.getDisplayName());
        }
        return result;
    }
}