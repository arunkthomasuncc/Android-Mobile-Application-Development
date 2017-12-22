package com.example.roncherian.musicsearch;
//Ron Abraham Cherian - 801028678
//Arun Thomas Kunnumpuram
import java.io.Serializable;

/**
 * Created by roncherian on 07/10/17.
 */

public class MusicTrack implements Serializable {

    String name, artist, url, smallImageUrl, largeImageUrl;
    boolean isFavorite;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSmallImageUrl() {
        return smallImageUrl;
    }

    public void setSmallImageUrl(String smallImageUrl) {
        this.smallImageUrl = smallImageUrl;
    }

    public String getLargeImageUrl() {
        return largeImageUrl;
    }

    public void setLargeImageUrl(String largeImageUrl) {
        this.largeImageUrl = largeImageUrl;
    }

    public boolean getFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MusicTrack that = (MusicTrack) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (artist != null ? !artist.equals(that.artist) : that.artist != null) return false;
        if (url != null ? !url.equals(that.url) : that.url != null) return false;
        if (smallImageUrl != null ? !smallImageUrl.equals(that.smallImageUrl) : that.smallImageUrl != null)
            return false;
        return largeImageUrl != null ? largeImageUrl.equals(that.largeImageUrl) : that.largeImageUrl == null;

    }
}
