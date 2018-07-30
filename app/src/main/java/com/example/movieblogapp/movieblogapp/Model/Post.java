package com.example.movieblogapp.movieblogapp.Model;

import java.io.Serializable;

public class Post implements Serializable{

    private String title;
    private String director;
    private String writer;
    private String relasedDate;
    private String rating;
    private String runtime;
    private String userInput;
    private String image;
    private String postDate;
    private String postID;
    private String userName;

    public Post() {
    }

    public Post(String title, String director, String writer, String relasedDate, String rating, String runtime, String userInput, String image, String postDate, String postID, String userName) {
        this.title = title;
        this.director = director;
        this.writer = writer;
        this.relasedDate = relasedDate;
        this.rating = rating;
        this.runtime = runtime;
        this.userInput = userInput;
        this.image = image;
        this.postDate = postDate;
        this.postID = postID;
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public String getRelasedDate() {
        return relasedDate;
    }

    public void setRelasedDate(String relasedDate) {
        this.relasedDate = relasedDate;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getRuntime() {
        return runtime;
    }

    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }

    public String getUserInput() {
        return userInput;
    }

    public void setUserInput(String userInput) {
        this.userInput = userInput;
    }


    public String getPostDate() {
        return postDate;
    }

    public void setPostDate(String postDate) {
        this.postDate = postDate;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPostID() {
        return postID;
    }

    public void setPostID(String postID) {
        this.postID = postID;
    }



}
