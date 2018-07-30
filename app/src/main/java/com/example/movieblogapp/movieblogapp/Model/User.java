package com.example.movieblogapp.movieblogapp.Model;

public class User {

    private String firstName;
    private String lastName;
    private String image;
    private String yourEmail;
    private String password;
    private String birthday;

    public User() {
    }

    public User(String firstName, String lastName,String image, String yourEmail, String password, String birthday) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.image = image;
        this.yourEmail = yourEmail;
        this.password = password;
        this.birthday = birthday;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getYourEmail() {
        return yourEmail;
    }

    public void setYourEmail(String yourEmail) {
        this.yourEmail = yourEmail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }
}
