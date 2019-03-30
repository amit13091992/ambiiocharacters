package com.flip.amiibocharacters.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Amit on 27-Mar-19.
 */
public class AmbiioCharacterModel implements Serializable {

    @SerializedName("release")
    @Expose
    public Release release;
    @SerializedName("amiiboSeries")
    @Expose
    private String amiiboSeries;
    @SerializedName("character")
    @Expose
    private String character;
    @SerializedName("gameSeries")
    @Expose
    private String gameSeries;
    @SerializedName("head")
    @Expose
    private String head;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("tail")
    @Expose
    private String tail;
    @SerializedName("type")
    @Expose
    private String type;

    public AmbiioCharacterModel(String amiiboSeries, String character, String gameSeries, String head, String image, String name, String tail, String type, String au, String eu, String jp, String na) {
        this.amiiboSeries = amiiboSeries;
        this.character = character;
        this.gameSeries = gameSeries;
        this.head = head;
        this.image = image;
        this.name = name;
        this.tail = tail;
        this.type = type;
        this.release = new Release(au, eu, jp, na);
    }

    public String getAmiiboSeries() {
        return amiiboSeries;
    }

    public void setAmiiboSeries(String amiiboSeries) {
        this.amiiboSeries = amiiboSeries;
    }

    public String getCharacter() {
        return character;
    }

    public void setCharacter(String character) {
        this.character = character;
    }

    public String getGameSeries() {
        return gameSeries;
    }

    public void setGameSeries(String gameSeries) {
        this.gameSeries = gameSeries;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Release getRelease() {
        return release;
    }

    public void setRelease(Release release) {
        this.release = release;
    }

    public String getTail() {
        return tail;
    }

    public void setTail(String tail) {
        this.tail = tail;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public class Release implements Serializable {
        @SerializedName("au")
        @Expose
        private String au;
        @SerializedName("eu")
        @Expose
        private String eu;
        @SerializedName("jp")
        @Expose
        private String jp;
        @SerializedName("na")
        @Expose
        private String na;

        Release(String au, String eu, String jp, String na) {
            this.au = au;
            this.eu = eu;
            this.jp = jp;
            this.na = na;
        }

        public String getAu() {
            return au;
        }

        public void setAu(String au) {
            this.au = au;
        }

        public String getEu() {
            return eu;
        }

        public void setEu(String eu) {
            this.eu = eu;
        }

        public String getJp() {
            return jp;
        }

        public void setJp(String jp) {
            this.jp = jp;
        }

        public String getNa() {
            return na;
        }

        public void setNa(String na) {
            this.na = na;
        }
    }
}
