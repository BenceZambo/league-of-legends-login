package model;

import com.google.gson.annotations.SerializedName;

public enum Server {
    @SerializedName("1")
    NA(1),
    @SerializedName("2")
    EUW(2),
    @SerializedName("3")
    EUNE(3),
    @SerializedName("4")
    TR(4),
    @SerializedName("5")
    RU(5),
    @SerializedName("6")
    BR(6),
    @SerializedName("7")
    LAN(7),
    @SerializedName("8")
    LAS(8),
    @SerializedName("9")
    OCE(9),
    @SerializedName("10")
    KR(10),
    @SerializedName("11")
    JP(11),
    @SerializedName("12")
    PBE(12);

    public final int value;

    Server(int value) {
        this.value = value;
    }
}
