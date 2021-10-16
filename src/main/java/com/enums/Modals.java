package com.enums;

public enum Modals {
    SETTING,
    ATTRIBUTION,
    PAUSE,
    HELP,
    PROFILE;

    public String getFileName() {
        switch (this) {
            case SETTING: {
                return "Setting";
            }
            case ATTRIBUTION: {
                return "Attribution";
            }
            case PAUSE: {
                return "Pause";
            }
            case HELP: {
                return "Help";
            }
            case PROFILE: {
                return "Profile";
            }
            default: {
                System.err.println("ERROR (Modals.java): Type not implemented for getting file name!");
                return "Menu";
            }
        }
    }

    public String getWindowName() {
        switch (this) {
            case SETTING: {
                return "Settings";
            }
            case ATTRIBUTION: {
                return "Kemu Kupu - Asset Attributions";
            }
            case PAUSE: {
                return "Game Paused";
            }
            case HELP: {
                return "Help";
            }
            case PROFILE: {
                return "User Profile";
            }
            default: {
                System.err.println("ERROR (modals.java): Type not implemented for getting window name!");
                return "Kemu Kupu";
            }
        }
    }
}