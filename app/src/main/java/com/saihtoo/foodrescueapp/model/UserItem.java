package com.saihtoo.foodrescueapp.model;

import android.os.Parcel;
import android.os.Parcelable;

public class UserItem implements Parcelable
{
    private int userID;
    private String fullName, email, phone, address, password;

    public UserItem(String fullName, String email, String phone, String address, String password) {
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.password = password;
    }

    public UserItem() { }

    //Getter and Setter
    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    //Generating Parcelable

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeInt(this.userID);
        dest.writeString(this.fullName);
        dest.writeString(this.email);
        dest.writeString(this.address);
        dest.writeString(this.password);
    }

    public void readFromParcel(Parcel source)
    {
        this.userID = source.readInt();
        this.fullName = source.readString();
        this.email = source.readString();
        this.address = source.readString();
        this.password = source.readString();
    }

    protected UserItem(Parcel in)
    {
        this.userID = in.readInt();
        this.fullName = in.readString();
        this.email = in.readString();
        this.address = in.readString();
        this.password = in.readString();
    }

    public static final Parcelable.Creator<UserItem> CREATOR = new Parcelable.Creator<UserItem>()
    {
        @Override
        public UserItem createFromParcel(Parcel source)
        {
            return new UserItem(source);
        }

        @Override
        public UserItem[] newArray(int size)
        {
            return new UserItem[size];
        }
    };
}
