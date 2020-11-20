package com.example.capstone.together.listview;

import android.graphics.Bitmap;

//하나의 리스트 뷰에 두 가지 텍스트를 보여주기 위한 클래스
public class CustomWord {
    //private String image;
    private String title;
    private String contents;
    //private Bitmap bmap;

    //public String getImage() { return image; }
    public String getTitle() {
        return title;
    }
    public String getContents() {
        return contents;
    }
    //public Bitmap getBmap() { return bmap; }

    /*
    public void setImage(String image) {
        this.image = image;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setContents(String contents) {
        this.contents = contents;
    }
    */

    /*
    public CustomWord(String i, String t, String c) {
        image = i;
        title = t;
        contents = c;
    }
     */
    public CustomWord(/*Bitmap b,*/ String t, String c) {
        //bmap = b;
        title = t;
        contents = c;
    }
}