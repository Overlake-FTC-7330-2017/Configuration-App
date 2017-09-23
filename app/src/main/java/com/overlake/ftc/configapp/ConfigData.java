package com.overlake.ftc.configapp;

/**
 * Created by EvanCoulson on 9/22/17.
 */

public class ConfigData {
    public String type;
    public String key;
    public String value;

    public ConfigData(String type, String key, String value) {
        this.type = type;
        this.key = key;
        this.value = value;
    }

    public String toString() {
        return "[" + type + "] " + key + ": " + value + "";
    }
}
