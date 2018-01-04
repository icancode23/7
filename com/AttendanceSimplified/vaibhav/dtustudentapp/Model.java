package com.AttendanceSimplified.vaibhav.dtustudentapp;

public class Model {
    private String abs;
    private String perc;
    private String pres;
    private String sub;

    public Model(String sub, String pres, String abs, String perc) {
        this.sub = sub;
        this.pres = pres;
        this.abs = abs;
        this.perc = perc;
    }

    public String getsub() {
        return this.sub;
    }

    public String getpres() {
        return this.pres;
    }

    public String getabs() {
        return this.abs;
    }

    public String getperc() {
        return this.perc;
    }
}
