package com.team11.hackernews.api;

public class HackerNewsAPI {
    public static final String ROOT_PATH = "https://hacker-news.firebaseio.com/v0";
    public static final String USER = "/user";
    public static final String ITEM = "/item";
    public static final String TOP_STORIES = "/topstories";

    public static String getItemPathById(int id) {
        return ROOT_PATH + ITEM + "/" + id;
    }

    public static String getUserPathById(int id) {
        return ROOT_PATH + USER + "/" + id;
    }
}
