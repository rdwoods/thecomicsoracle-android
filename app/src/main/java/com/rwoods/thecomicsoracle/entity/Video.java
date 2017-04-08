package com.rwoods.thecomicsoracle.entity;

import android.os.Parcel;
import android.os.Parcelable;
import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

@JsonObject
public class Video extends RealmObject implements Parcelable {

    @PrimaryKey
    @JsonField
    private long id;

    @JsonField
    private String name;

    @JsonField(name = "image")
    private Image image;

    @JsonField(name = "high_url")
    private String highUrl;

    @JsonField(name = "low_url")
    private String lowUrl;

    public Video(long id, String name, Image image, String highUrl, String lowUrl) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.highUrl = highUrl;
        this.lowUrl = lowUrl;
    }

    public Video() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public String getHighUrl() {
        return highUrl;
    }

    public void setHighUrl(String highUrl) {
        this.highUrl = highUrl;
    }

    public String getLowUrl() {
        return lowUrl;
    }

    public void setLowUrl(String lowUrl) {
        this.lowUrl = lowUrl;
    }

    @Override
    public int describeContents() {
        return this.hashCode();
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeParcelable(image, flags);
        dest.writeString(highUrl);
        dest.writeString(lowUrl);
    }


    private Video(Parcel in){
        this();
        this.id = in.readLong();
        this.name = in.readString();
        this.image = in.readParcelable(Image.class.getClassLoader());
        this.highUrl = in.readString();
        this.lowUrl = in.readString();
    }

    public static final Creator<Video> CREATOR = new Creator<Video>() {
        @Override
        public Video createFromParcel(Parcel in) {
            return new Video(in);
        }

        @Override
        public Video[] newArray(int size) {
            return new Video[size];
        }
    };
}