package com.example.campuslink.model;

public class HomeGridItem {
    private final String name;
    private final int drawableId;

    public HomeGridItem(String name, int drawableId) {
        this.name = name;
        this.drawableId = drawableId;
    }

    public String getName() {
        return name;
    }

    public int getDrawableId() {
        return drawableId;
    }
}
