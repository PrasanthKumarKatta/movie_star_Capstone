package com.example.prasanthkumar.moviestar.Model;

public class Review {

    /* @SerializedName("author")
     @Expose
    */
    private String author;
    /* @SerializedName("content")
     @Expose
    */
    private String content;

    public Review(String author, String content) {
        this.author = author;
        this.content = content;

    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
