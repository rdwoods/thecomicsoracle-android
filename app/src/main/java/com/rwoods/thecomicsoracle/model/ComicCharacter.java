package com.rwoods.thecomicsoracle.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.squareup.moshi.Json;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class ComicCharacter extends RealmObject implements Parcelable{

    @PrimaryKey
    private long id;

    private String name;

    private String gender;

    @Json(name = "image")
    private Image image;

    private String description;


    public ComicCharacter(Long id, String name, String gender, Image image, String characterDescription){
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.image = image;
        this.description = characterDescription;
    }

    public ComicCharacter() {
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }


    @Override
    public int describeContents() {
        return this.hashCode();
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeString(gender);
        dest.writeParcelable(image, flags);
        dest.writeString(description);
    }


    private ComicCharacter(Parcel in) {
        this();
        this.id = in.readLong();
        this.name = in.readString();
        this.gender = in.readString();
        this.image = in.readParcelable(Image.class.getClassLoader());
        this.description = in.readString();
    }

    public static final Creator<ComicCharacter> CREATOR = new Creator<ComicCharacter>() {
        @Override
        public ComicCharacter createFromParcel(Parcel in) {
            return new ComicCharacter(in);
        }

        @Override
        public ComicCharacter[] newArray(int size) {
            return new ComicCharacter[size];
        }
    };
}