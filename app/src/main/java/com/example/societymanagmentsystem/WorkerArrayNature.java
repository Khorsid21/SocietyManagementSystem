package com.example.societymanagmentsystem;

import org.jetbrains.annotations.NotNull;

public enum WorkerArrayNature {
    NATURE0("Cooks",R.drawable.worker_cook, R.drawable.worker_cookimage),
    NATURE1("Drivers",R.drawable.worker_driver, R.drawable.nature_001),
    NATURE2("Food vendors",R.drawable.worker_food, R.drawable.worker_foodimage),
    NATURE3("Electrician",R.drawable.worker_electrician, R.drawable.worker_electricianimage),
    NATURE4("Plumber",R.drawable.worker_plumber, R.drawable.worker_plumberimage);

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

    private WorkerArrayNature(String title, int userDrawable, int natureDrawable) {
        this.title = title;
        this.userDrawable = userDrawable;
        this.natureDrawable = natureDrawable;
    }

}
