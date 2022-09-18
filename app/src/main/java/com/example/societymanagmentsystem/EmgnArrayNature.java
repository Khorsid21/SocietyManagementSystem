package com.example.societymanagmentsystem;

import org.jetbrains.annotations.NotNull;

public enum  EmgnArrayNature {
    NATURE0("Ambulance",R.drawable.ambulance, R.drawable.ambheader),
    NATURE1("Police",R.drawable.policestation, R.drawable.policeheader),
    NATURE2("Blood",R.drawable.blooddonation, R.drawable.bloodheaer),
    NATURE3("Fire",R.drawable.firetruck, R.drawable.firefighterheader),
    NATURE4("Woman Safety",R.drawable.strong, R.drawable.womenheader);

    @NotNull
    private final String title;
    private final int userDrawable;
    private final int natureDrawable;

    @NotNull
    public final String getTitle() {
        return this.title;
    }

    public int getUserDrawable() {
        return this.userDrawable;
    }

    public int getNatureDrawable() {
        return this.natureDrawable;
    }

    private EmgnArrayNature(String title, int userDrawable, int natureDrawable) {
        this.title = title;
        this.userDrawable = userDrawable;
        this.natureDrawable = natureDrawable;
    }
}
