package org.androidtown.movieproject2.Details;

import android.os.Parcel;
import android.os.Parcelable;

public class EvaluationInfo implements Parcelable {
    /*
    *
    * id: 6253,
writer: "zas777",
movieId: 1,
writer_image: null,
time: "2020-02-26 13:40:06",
timestamp: 1582692006,
rating: 1,
contents: "vvv",
recommend: 0,*/
    private Integer id;
    private int movieId;
    private String writer;
    private String writer_image;
    private String time;
    private long timestamp;
    private String contents;
    private int recommend;
    private float rating;
    String report;
    public EvaluationInfo(int id,
                          int movieId,
                          String writer,
                          String writer_image,
                                  String time,
                                  long timestamp,
                                  String contents,
                                  int recommend,
                                  float rating,String report) {
        this.id = id;
        this.movieId=movieId;
        this.writer=writer;
        this.writer_image = writer_image;
        this.time = time;
        this.timestamp=timestamp;
        this.rating = rating;
        this.recommend = recommend;
        this.report = report;
        this.contents = contents;
    }

    public EvaluationInfo(Parcel src) {
        id = src.readInt();
        movieId=src.readInt();
        writer=src.readString();
        writer_image=src.readString();
        time = src.readString();
        recommend = src.readInt();
        report = src.readString();
        contents = src.readString();
        rating = src.readFloat();
    }

    public static final Creator CREATOR = new Creator() {
        public EvaluationInfo createFromParcel(Parcel in) {
            return new EvaluationInfo(in);
        }

        public EvaluationInfo[] newArray(int size) {
            return new EvaluationInfo[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        /*
        *
        *  id = src.readString();
        movieId=src.readInt();
        writer=src.readString();
        writer_image=src.readString();
        time = src.readString();
        recommend = src.readInt();
        report = src.readString();
        contents = src.readString();
        rating = src.readFloat();*/
        dest.writeInt(id);
        dest.writeInt(movieId);
        dest.writeString(writer);
        dest.writeString(writer_image);
        dest.writeString(time);
        dest.writeInt(recommend);
        dest.writeString(report);
        dest.writeString(contents);
        dest.writeFloat(rating);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
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

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
    }
}
