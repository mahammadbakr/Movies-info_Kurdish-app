package com.example.pupulermovies.samples;

public class Movie {
    private int id;
    private String name;
    private String overview;
    private String img;
    private String date;
    private double popularity;
    private double vote;
    private int first;

    public Movie(int id, String name, String overview, String img, String date, double popularity, double vote, int first) {
        this.id = id;
        this.name = name;
        this.overview = overview;
        this.img = img;
        this.date = date;
        this.popularity = popularity;
        this.vote = vote;
        this.first = first;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getOverview() {
        return overview;
    }

    public String getImg() {
        return img;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getDate() {
        return date;
    }

    public double getPopularity() {
        return popularity;
    }

    public double getVote() {
        return vote;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public void setVote(double vote) {
        this.vote = vote;
    }

    public int getFirst() {
        return first;
    }

    public void setFirst(int first) {
        this.first = first;
    }

}
