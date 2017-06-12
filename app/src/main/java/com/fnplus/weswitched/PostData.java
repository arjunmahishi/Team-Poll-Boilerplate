package com.fnplus.weswitched;

/**
 * Created by arjun on 7/6/17.
 */

public class PostData {
    public String userName, quote, category;
    public int votes;

    PostData(){}

    PostData(String u, String q, String c){
        this.userName = u;
        this.quote = q;
        this.category = c;
        this.votes = 0;
    }
}
