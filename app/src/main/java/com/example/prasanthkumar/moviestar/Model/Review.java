package com.example.prasanthkumar.moviestar.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Review {

    /* @SerializedName("author")
     @Expose
    */
    private String author;
    /* @SerializedName("content")
     @Expose
    */
    private String content;
    private String id;

   /* public Review(String author, String content) {
        this.author = author;
        this.content = content;
    }
*/

    public Review(String author, String content, String id) {
        this.author = author;
        this.content = content;
        this.id = id;
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
