package org.androidtown.movieproject2.Details.DetailViews;

import android.os.Parcel;
import android.os.Parcelable;

public class ReviewItem implements Parcelable {
    private String id;
    private int movieId;
    private String writer;
    private String writer_image;
    private String time;
    private long timestamp;
    private String contents;
    private int recommend;
    private float rating;

    public ReviewItem() {
    }

    public ReviewItem(String id, int movieId, String writer, String writer_image, String time, int timestamp, String contents, int recommend, float rating) {
        this.id = id;
        this.movieId = movieId;
        this.writer = writer;
        this.writer_image = writer_image;
        this.time = time;
        this.timestamp = timestamp;
        this.contents = contents;
        this.recommend = recommend;
        this.rating = rating;
    }

    protected ReviewItem(Parcel in) {
        id = in.readString();
        movieId = in.readInt();
        writer = in.readString();
        writer_image = in.readString();
        time = in.readString();
        timestamp = in.readInt();
        contents = in.readString();
        recommend = in.readInt();
        rating = in.readFloat();
    }

    public static final Creator<ReviewItem> CREATOR = new Creator<ReviewItem>() {
        @Override
        public ReviewItem createFromParcel(Parcel in) {
            return new ReviewItem(in);
        }

        @Override
        public ReviewItem[] newArray(int size) {
            return new ReviewItem[size];
        }
    };

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public String getWriter_image() {
        return writer_image;
    }

    public void setWriter_image(String writer_image) {
        this.writer_image = writer_image;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public int getRecommend() {
        return recommend;
    }

    public void setRecommend(int recommend) {
        this.recommend = recommend;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeInt(movieId);
        dest.writeString(writer);
        dest.writeString(writer_image);
        dest.writeString(time);
        dest.writeLong(timestamp);
        dest.writeString(contents);
        dest.writeInt(recommend);
        dest.writeFloat(rating);
    }
}
