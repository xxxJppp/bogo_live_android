package com.bogokj.xianrou.model;

import android.os.Parcel;
import android.os.Parcelable;

public class BogoVideoClassLabelModel implements Parcelable {
    private String id;
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
    }

    public BogoVideoClassLabelModel() {
    }

    protected BogoVideoClassLabelModel(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
    }

    public static final Parcelable.Creator<BogoVideoClassLabelModel> CREATOR = new Parcelable.Creator<BogoVideoClassLabelModel>() {
        @Override
        public BogoVideoClassLabelModel createFromParcel(Parcel source) {
            return new BogoVideoClassLabelModel(source);
        }

        @Override
        public BogoVideoClassLabelModel[] newArray(int size) {
            return new BogoVideoClassLabelModel[size];
        }
    };
}
