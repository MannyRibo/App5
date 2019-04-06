package com.example.reminder.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

@Entity(tableName = "game")
public class Game implements Parcelable {

        @PrimaryKey(autoGenerate = true)
        private Long id;

        @ColumnInfo
        private String titel;

        @ColumnInfo(name = "platform")
        private String platform;

        @ColumnInfo(name = "datum")
        private String datum;

        @ColumnInfo(name = "status")
        private String status;

        @ColumnInfo(name = "statusIndex")
        private int statusIndex;

    public Game(String titel, String platform, String datum, String status, int statusIndex) {
        this.id = id;
        this.titel = titel;
        this.platform = platform;
        this.datum = datum;
        this.status = status;
        this.statusIndex = statusIndex;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitel() {
        return titel;
    }

    public void setTitel(String titel) {
        this.titel = titel;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getDatum() {
        return datum;
    }

    public void setDatum(String datum) {
        this.datum = datum;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getStatusIndex() {
        return statusIndex;
    }

    public void setStatusIndex(int statusIndex) {
        this.statusIndex = statusIndex;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(id);
        }
        dest.writeString(titel);
        dest.writeString(platform);
        dest.writeString(datum);
        dest.writeString(status);
        dest.writeInt(statusIndex);
    }

    public static final Creator<Game> CREATOR = new Creator<Game>() {
        @Override
        public Game createFromParcel(Parcel in) {
            return new Game(in);
        }

        @Override
        public Game[] newArray(int size) {
            return new Game[size];
        }
    };

    protected Game(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        titel = in.readString();
        platform = in.readString();
        datum = in.readString();
        status = in.readString();
        statusIndex = in.readInt();
    }
}
