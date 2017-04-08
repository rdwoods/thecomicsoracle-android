package com.rwoods.thecomicsoracle.entity;

import android.os.Parcel;
import android.os.Parcelable;
import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import io.realm.RealmObject;

@JsonObject
public class Image extends RealmObject implements Parcelable {

    @JsonField(name = "icon_url")
    private String iconUrl;

    @JsonField(name = "medium_url")
    private String mediumUrl;

    @JsonField(name = "screen_url")
    private String screenUrl;

    @JsonField(name = "small_url")
    private String smallUrl;

    @JsonField(name = "super_url")
    private String superUrl;

    @JsonField(name = "thumb_url")
    private String thumbUrl;

    @JsonField(name = "tiny_url")
    private String tinyUrl;


    public Image(String iconUrl, String mediumUrl, String screenUrl, String smallUrl, String superUrl, String thumbUrl, String tinyUrl) {
        this.iconUrl = iconUrl;
        this.mediumUrl = mediumUrl;
        this.screenUrl = screenUrl;
        this.smallUrl = smallUrl;
        this.superUrl = superUrl;
        this.thumbUrl = thumbUrl;
        this.tinyUrl = tinyUrl;
    }

    public Image() {
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getMediumUrl() {
        return mediumUrl;
    }

    public void setMediumUrl(String mediumUrl) {
        this.mediumUrl = mediumUrl;
    }

    public String getScreenUrl() {
        return screenUrl;
    }

    public void setScreenUrl(String screenUrl) {
        this.screenUrl = screenUrl;
    }

    public String getSmallUrl() {
        return smallUrl;
    }

    public void setSmallUrl(String smallUrl) {
        this.smallUrl = smallUrl;
    }

    public String getSuperUrl() {
        return superUrl;
    }

    public void setSuperUrl(String superUrl) {
        this.superUrl = superUrl;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }

    public String getTinyUrl() {
        return tinyUrl;
    }

    public void setTinyUrl(String tinyUrl) {
        this.tinyUrl = tinyUrl;
    }


    @Override
    public int describeContents() {
        return this.hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        //dest.writeLong(id);
        dest.writeString(iconUrl);
        dest.writeString(mediumUrl);
        dest.writeString(screenUrl);
        dest.writeString(smallUrl);
        dest.writeString(superUrl);
        dest.writeString(thumbUrl);
        dest.writeString(tinyUrl);
    }


    private Image(Parcel in){
        this();
        //this.id = in.readLong();
        this.iconUrl = in.readString();
        this.mediumUrl = in.readString();
        this.screenUrl = in.readString();
        this.smallUrl = in.readString();
        this.superUrl = in.readString();
        this.thumbUrl = in.readString();
        this.tinyUrl = in.readString();
    }

    public static final Creator<Image> CREATOR = new Creator<Image>() {
        @Override
        public Image createFromParcel(Parcel in) {
            return new Image(in);
        }

        @Override
        public Image[] newArray(int size) {
            return new Image[size];
        }
    };
}