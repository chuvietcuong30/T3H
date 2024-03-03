package com.example.step;

public class HistoryEntry {
    private float calo;
    private float km;
    private String formattedEntry;
    private String buttonTextData;

    public HistoryEntry(float calo, float km, String formattedEntry, String buttonTextData) {
        this.calo = calo;
        this.km = km;
        this.formattedEntry = formattedEntry;
        this.buttonTextData = buttonTextData;
    }

    public float getCalo() {
        return calo;
    }

    public float getKm() {
        return km;
    }

    public String getFormattedEntry() {
        return formattedEntry;
    }

    public String getButtonTextData() {
        return buttonTextData;
    }
}

