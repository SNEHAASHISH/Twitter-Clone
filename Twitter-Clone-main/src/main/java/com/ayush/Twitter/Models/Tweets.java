package com.ayush.Twitter.Models;

import javax.persistence.*;

@Entity
public class Tweets {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int authorId;
    private String authorName;
    private String tweet;
    private String createdAt;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getTweet() {
        return tweet;
    }

    public void setTweet(String tweet) {
        this.tweet = tweet;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Tweets(int id, int authorId, String authorName, String tweet, String createdAt) {
        this.id = id;
        this.authorId = authorId;
        this.authorName = authorName;
        this.tweet = tweet;
        this.createdAt = createdAt;
    }

    public Tweets() {
    }
}
