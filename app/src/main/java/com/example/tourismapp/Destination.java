package com.example.tourismapp;

import android.os.Parcel;
import android.os.Parcelable;

public class Destination implements Parcelable {

    private String name;
    private String details;
    private String imageUrl;
    private String pageId; // Add pageId field

    public Destination(String name, String details, String imageUrl, String pageId) {
        this.name = name;
        this.details = details;
        this.imageUrl = imageUrl;
        this.pageId = pageId;
    }

    protected Destination(Parcel in) {
        name = in.readString();
        details = in.readString();
        imageUrl = in.readString();
        pageId = in.readString(); // Read pageId from Parcel
    }

    public static final Creator<Destination> CREATOR = new Creator<Destination>() {
        @Override
        public Destination createFromParcel(Parcel in) {
            return new Destination(in);
        }

        @Override
        public Destination[] newArray(int size) {
            return new Destination[size];
        }
    };

    public String getName() {
        return name;
    }

    public String getDetails() {
        return details;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getPageId() {
        return pageId;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(details);
        dest.writeString(imageUrl);
        dest.writeString(pageId); // Write pageId to Parcel
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
