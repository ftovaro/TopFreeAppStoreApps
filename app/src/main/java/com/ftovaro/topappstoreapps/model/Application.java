package com.ftovaro.topappstoreapps.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.orm.SugarRecord;

/**
 * Model of an Application.
 * Created by FelipeTovar on 10-Apr-16.
 */
public class Application extends SugarRecord implements Parcelable {

    private String author;
    private String category;
    private String name;
    private String summary;
    private String image_url;
    private String rights;

    public Application(){}

    public Application(Parcel in) {
        readFromParcel(in);
    }

    public Application(String author,
                       String category,
                       String name,
                       String summary,
                       String image_url,
                       String rights){
        this.author = author;
        this.category = category;
        this.name = name;
        this.summary = summary;
        this.image_url = image_url;
        this.rights = rights;
    }

    public String getAuthor() {
        return author;
    }

    public String getCategory() {
        return category;
    }

    public String getName() {
        return name;
    }

    public String getSummary() {
        return summary;
    }

    public String getImage_url() {
        return image_url;
    }

    public String getRights() {
        return rights;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public void setRights(String rights) {
        this.rights = rights;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(author);
        dest.writeString(category);
        dest.writeString(name);
        dest.writeString(summary);
        dest.writeString(image_url);
        dest.writeString(rights);
    }

    private void readFromParcel(Parcel in) {
        author = in.readString();
        category = in.readString();
        name = in.readString();
        summary = in.readString();
        image_url = in.readString();
        rights = in.readString();
    }

    public static final Parcelable.Creator<Application> CREATOR
            = new Parcelable.Creator<Application>() {
        public Application createFromParcel(Parcel in) {
            return new Application(in);
        }

        public Application[] newArray(int size) {
            return new Application[size];
        }
    };
}
