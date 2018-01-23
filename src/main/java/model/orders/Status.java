package model.orders;

import com.google.gson.annotations.SerializedName;

public enum Status {
    @SerializedName("0")
    UNKNOWN(0),
    @SerializedName("1")
    PROCESSING(1),
    @SerializedName("2")
    UNKNOWN2(2),
    @SerializedName("3")
    PAUSED(3);

    public final int value;

    Status(int value) {
        this.value = value;
    }
}
