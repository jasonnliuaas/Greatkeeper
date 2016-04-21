package com.newline.housekeeper;

public class Param {
    
    private String name;
    private String value;
    
    public Param(String name, Object value){
        this.name = name;
        this.value = value == null ? "" : value.toString();
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

}
