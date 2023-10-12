package com.resort.resortapp;

import javafx.scene.control.CheckBox;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public enum Rooms{
    ALL_ROOMS("all"),
    ROOM_J("j"),
    ROOM_G("g"),
    ATTIC("attic"),
    KUBO_1("k1"),
    KUBO_2("k2"),
    EXCLUSIVE("exclusive");

    final String abbreviatedName;


    Rooms(String abbreviatedName) {
        this.abbreviatedName = abbreviatedName;
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
        return abbreviatedName;
    }
    public static String displayToAbbv(String displayName) {
        for (Rooms room : Rooms.values()) {
            if (room.getDisplayName().equalsIgnoreCase(displayName)) {
                return room.getAbbreviatedName();
            }
        }
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
    public static Set<String> getRoomAbbreviateNamesSet(){
        Set<String> result = new HashSet<>();

        for(Rooms room : values()){
            result.add(room.getAbbreviatedName());
        }
        result.remove("all");
        return result;
    }
    public static List<String> getRoomAbbreviateNamesList(){
        List<String> result = new ArrayList<>();

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
        StringBuilder sb = new StringBuilder();
        if(roomCheckboxes.get(0).isSelected()){
            sb.append(ROOM_J.getAbbreviatedName()).append(", ");
        }
        if(roomCheckboxes.get(1).isSelected()){
            sb.append(ROOM_G.getAbbreviatedName()).append(", ");
        }
        if(roomCheckboxes.get(2).isSelected()){
            sb.append(ATTIC.getAbbreviatedName()).append(", ");
        }
        if(roomCheckboxes.get(3).isSelected()){
            sb.append(KUBO_1.getAbbreviatedName()).append(", ");
        }
        if(roomCheckboxes.get(4).isSelected()){
            sb.append(KUBO_2.getAbbreviatedName()).append(", ");
        }
        if(roomCheckboxes.get(5).isSelected()){
            sb.append(EXCLUSIVE.getAbbreviatedName()).append(", ");
        }
        if (!sb.isEmpty()) {
            sb.delete(sb.length() - 2, sb.length());
        }
        return sb.toString();
    }

    public static Set<String> manageCheckboxesSetDisplayName(List<CheckBox> roomCheckboxes){
        Set<String> result = new HashSet<>();
        //TODO CHECK ORDER
        if(roomCheckboxes.get(0).isSelected()){
            result.add(ROOM_J.getDisplayName());
        }
        if(roomCheckboxes.get(1).isSelected()){
            result.add(ROOM_G.getDisplayName());
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
        if(roomCheckboxes.get(5).isSelected()){
            result.add(EXCLUSIVE.getDisplayName());
        }
        return result;
    }
    public static Set<String> manageCheckboxesSetAbbreviatedName(List<CheckBox> roomCheckboxes){
        Set<String> result = new HashSet<>();
        //TODO CHECK ORDER
        if(roomCheckboxes.get(0).isSelected()){
            result.add(ROOM_J.getAbbreviatedName());
        }
        if(roomCheckboxes.get(1).isSelected()){
            result.add(ROOM_G.getAbbreviatedName());
        }
        if(roomCheckboxes.get(2).isSelected()){
            result.add(ATTIC.getAbbreviatedName());
        }
        if(roomCheckboxes.get(3).isSelected()){
            result.add(KUBO_1.getAbbreviatedName());
        }
        if(roomCheckboxes.get(4).isSelected()){
            result.add(KUBO_2.getAbbreviatedName());
        }
        if(roomCheckboxes.get(5).isSelected()){
            result.add(EXCLUSIVE.getAbbreviatedName());
        }
        return result;
    }

    public static Set<String> stringToSet(String string){
        Set<String> result = new HashSet<>();
        Collections.addAll(result, string.split(", "));
        return result;
    }
    public static void tickCheckboxes(String room, List<CheckBox> roomCheckBoxes) {
        Set<String> roomSet = stringToSet(room);
        if(roomSet.contains(Rooms.ROOM_J.getAbbreviatedName())){
            roomCheckBoxes.get(0).setSelected(true);
        }
        if(roomSet.contains(Rooms.ROOM_G.getAbbreviatedName())){
            roomCheckBoxes.get(1).setSelected(true);
        }
        if(roomSet.contains(Rooms.ATTIC.getAbbreviatedName())){
            roomCheckBoxes.get(2).setSelected(true);
        }
        if(roomSet.contains(Rooms.KUBO_1.getAbbreviatedName())){
            roomCheckBoxes.get(3).setSelected(true);
        }
        if(roomSet.contains(Rooms.KUBO_2.getAbbreviatedName())){
            roomCheckBoxes.get(4).setSelected(true);
        }
        if(roomSet.contains(Rooms.EXCLUSIVE.getAbbreviatedName())){
            roomCheckBoxes.get(5).setSelected(true);
        }
    }
}