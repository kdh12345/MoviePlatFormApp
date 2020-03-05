package org.androidtown.movieproject2.Details.DetailViews;


public class MoviePictureInfo {
    String imageUrl;
    boolean isMovie;
    String youtubeUrl;

    public MoviePictureInfo(String imageUrl, boolean isMovie, String youtubeUrl) {
        this.imageUrl = imageUrl;
        this.isMovie = isMovie;
        this.youtubeUrl = youtubeUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isMovie() {
        return isMovie;
    }

    public void setMovie(boolean movie) {
        isMovie = movie;
    }

    public String getYoutubeUrl() {
        return youtubeUrl;
    }

    public void setYoutubeUrl(String youtubeUrl) {
        this.youtubeUrl = youtubeUrl;
    }
}

