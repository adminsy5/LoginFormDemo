package com.msfpiyush.loginformdemo;

public class model
{
    String name,email,course,purl;

    model()
    {

    }

    public model(String name, String email, String course, String purl) {
        this.name = name;
        this.email = email;
        this.course = course;
        this.purl = purl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getPurl() {
        return purl;
    }

    public void setPurl(String purl) {
        this.purl = purl;
    }
}
