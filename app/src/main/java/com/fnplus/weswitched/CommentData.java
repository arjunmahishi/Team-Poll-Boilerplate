package com.fnplus.weswitched;

/**
 * Created by arjun on 8/6/17.
 */

public class CommentData {
    public String username, PhotoUrl, message;

    CommentData(){}

    CommentData(String u, String p, String m){
        this.username = u;
        this.PhotoUrl = p;
        this.message = m;
    }
}
