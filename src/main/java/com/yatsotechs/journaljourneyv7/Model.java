package com.yatsotechs.journaljourneyv7;

import android.os.Parcel;
import android.os.Parcelable;

public class Model {

    // string variable for
    // storing employee name.
    private String journeyName;

    // string variable for storing
    // employee contact number
    private String journeyDescription;

    // string variable for storing
    // employee address.
    private String journeyLocation;

    //lets experiment with an id
    private String journeyId;

    //an url for an image
    private String imageURL;

    // an empty constructor is
    // required when using
    // Firebase Realtime Database.

    public String getJourneyId(){
        return journeyId;
    }

    public void setJourneyId(String journeyId){
        this.journeyId = journeyId;
    }


    public Model() {

    }

    protected Model(Parcel in){
        journeyName = in.readString();
        journeyDescription = in.readString();
        journeyLocation = in.readString();
        journeyId = in.readString();
        imageURL = in.readString();
    }

    public static final Parcelable.Creator<Model> CREATOR = new Parcelable.Creator<Model>(){
        @Override
        public Model createFromParcel(Parcel in) {
            return new Model(in);
        }

        @Override
        public Model[] newArray(int size) {
            return new Model[size];
        }
    };

    // created getter and setter methods
    // for all our variables.
    public String getJourneyName() {
        return journeyName;
    }

    public void setJourneyName(String journeyName) {
        this.journeyName = journeyName;
    }

    public String getJourneyDescription() {
        return journeyDescription;
    }

    public void setJourneyDescription(String journeyDescription) {
        this.journeyDescription = journeyDescription;
    }

    public String getJourneyLocation() {
        return journeyLocation;
    }

    public void setJourneyLocation(String journeyLocation) {
        this.journeyLocation = journeyLocation;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public Model(String journeyName, String journeyDescription, String journeyLocation, String journeyId, String url) {

        this.journeyName = journeyName;
        this.journeyDescription = journeyDescription;
        this.journeyLocation = journeyLocation;
        this.journeyId = journeyId;
        this.imageURL = url;
    }


    public int describeContents() {
        return 0;
    }


    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(journeyName);
        dest.writeString(journeyDescription);
        dest.writeString(journeyLocation);
        dest.writeString(journeyId);
        dest.writeString(imageURL);
    }

}
