package org.androidtown.movieproject2;

import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable {
    //영화 목록
    public int id;
    public String title;
    public String title_eng;
    public String date;
    public float user_rating;
    public float audience_rating;
    public float reviewer_rating;
    public float reservation_rate;
    public int reservation_grade;
    public int grade;
    public String thumb;
    public String image;

    //영화 상세
    public String photos;
    public String videos;
    public String outlinks;
    public String genre;
    public int duration;
    public int audience;
    public String synopsis;
    public String director;
    public String actor;
    public int like;
    public int dislike;

    ///readComment(한줄평 읽기)

    public String writer;
    public int movieId;
    public String writer_image;
    public String time;
    public int timestamp;
    public float rating;
    public String contents;
    public int recommend;

    public Movie(){
    }

    public Movie(Parcel src) {
        id = src.readInt();
        title = src.readString();
        title_eng = src.readString();
        date = src.readString();
        user_rating = src.readFloat();
        audience_rating = src.readFloat();
        reviewer_rating = src.readFloat();
        reservation_rate = src.readFloat();
        reservation_grade = src.readInt();
        grade = src.readInt();
        thumb = src.readString();
        image = src.readString();
        photos=src.readString();
        videos = src.readString();
        outlinks=src.readString();
        genre=src.readString();
        duration=src.readInt();
        audience=src.readInt();
        synopsis=src.readString();
        director=src.readString();
        actor=src.readString();
        like=src.readInt();
        dislike=src.readInt();
        writer = src.readString();
        movieId = src.readInt();
        writer_image = src.readString();
        time = src.readString();
        timestamp = src.readInt();
        rating = src.readFloat();
        contents = src.readString();
        recommend = src.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
         /*
    * id = src.readInt();
        title = src.readString();
        title_eng = src.readString();
        date = src.readString();
        user_rating = src.readFloat();
        audience_rating = src.readFloat();
        reviewer_rating = src.readFloat();
        reservation_rating = src.readFloat();
        reservation_grade = src.readInt();
        grade = src.readInt();
        thumb = src.readString();
        image = src.readString();
        photos=src.readString();
        videos = src.readString();
        outlinks=src.readString();
        genre=src.readString();
        duration=src.readInt();
        audience=src.readInt();
        synopsis=src.readString();
        director=src.readString();
        actor=src.readString();
        like=src.readInt();
        dislike=src.readInt();
        writer = src.readString();
        movieId = src.readInt();
        writer_image = src.readInt();
        time = src.readString();
        timestamp = src.readInt();
        rating = src.readFloat();
        contents = src.readString();
        recommend = src.readInt();
    * */
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(title_eng);
        dest.writeString(date);
        dest.writeFloat(user_rating);
        dest.writeFloat(audience_rating);
        dest.writeFloat(reviewer_rating);
        dest.writeFloat(reservation_rate);
        dest.writeInt(reservation_grade);
        dest.writeInt(grade);
        dest.writeString(thumb);
        dest.writeString(image);
        dest.writeString(photos);
        dest.writeString(videos);
        dest.writeString(outlinks);
        dest.writeString(genre);
        dest.writeInt(duration);
        dest.writeInt(audience);
        dest.writeString(synopsis);
        dest.writeString(director);
        dest.writeString(actor);
        dest.writeInt(like);
        dest.writeInt(dislike);
        dest.writeString(writer);
        dest.writeInt(movieId);
        dest.writeString(writer_image);
        dest.writeString(time);
        dest.writeInt(timestamp);
        dest.writeFloat(rating);
        dest.writeString(contents);
        dest.writeInt(recommend);
    }
    @Override
    public int describeContents() {
        return 0;
    }
    public static final Creator CREATOR = new Creator(){
        public Movie createFromParcel(Parcel in){
            return new Movie(in);
        }
        public Movie[] newArray(int size){
            return new Movie[size];
        }
    };
    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", title_eng='" + title_eng + '\'' +
                ", date='" + date + '\'' +
                ", user_rating=" + user_rating +
                ", audience_rating=" + audience_rating +
                ", reviewer_rating=" + reviewer_rating +
                ", reservation_rate=" + reservation_rate +
                ", reservation_grade=" + reservation_grade +
                ", grade=" + grade +
                ", thumb='" + thumb + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle_eng() {
        return title_eng;
    }

    public void setTitle_eng(String title_eng) {
        this.title_eng = title_eng;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public float getUser_rating() {
        return user_rating;
    }

    public void setUser_rating(float user_rating) {
        this.user_rating = user_rating;
    }

    public float getAudience_rating() {
        return audience_rating;
    }

    public void setAudience_rating(float audience_rating) {
        this.audience_rating = audience_rating;
    }

    public float getReviewer_rating() {
        return reviewer_rating;
    }

    public void setReviewer_rating(float reviewer_rating) {
        this.reviewer_rating = reviewer_rating;
    }

    public float getReservation_rate() {
        return reservation_rate;
    }

    public void setReservation_rate(float reservation_rate) {
        this.reservation_rate = reservation_rate;
    }

    public int getReservation_grade() {
        return reservation_grade;
    }

    public void setReservation_grade(int reservation_grade) {
        this.reservation_grade = reservation_grade;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPhotos() {
        return photos;
    }

    public void setPhotos(String photos) {
        this.photos = photos;
    }

    public String getVideos() {
        return videos;
    }

    public void setVideos(String videos) {
        this.videos = videos;
    }

    public String getOutlinks() {
        return outlinks;
    }

    public void setOutlinks(String outlinks) {
        this.outlinks = outlinks;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getAudience() {
        return audience;
    }

    public void setAudience(int audience) {
        this.audience = audience;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getActor() {
        return actor;
    }

    public void setActor(String actor) {
        this.actor = actor;
    }

    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }

    public int getDislike() {
        return dislike;
    }

    public void setDislike(int dislike) {
        this.dislike = dislike;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
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

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
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
}
