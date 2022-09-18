package com.example.societymanagmentsystem;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SocData {
    @SerializedName("0")
    @Expose
    private String _0;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("1")
    @Expose
    private String _1;
    @SerializedName("s_name")
    @Expose
    private String sName;
    @SerializedName("2")
    @Expose
    private String _2;
    @SerializedName("block")
    @Expose
    private String block;

    public String get0() {
        return _0;
    }

    public void set0(String _0) {
        this._0 = _0;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String get1() {
        return _1;
    }

    public void set1(String _1) {
        this._1 = _1;
    }

    public String getsName() {
        return sName;
    }

    public void setsName(String sName) {
        this.sName = sName;
    }

    public String get2() {
        return _2;
    }

    public void set2(String _2) {
        this._2 = _2;
    }

    public String getBlock() {
        return block;
    }

    public void setBlock(String block) {
        this.block = block;
    }

}
