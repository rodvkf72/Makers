package com.example.capstone;

//하나의 리스트 뷰에 두 가지 텍스트를 보여주기 위한 클래스
public class CustomWord {
    private String title;
    private String contents;

    public String getTitle() {
        return title;
    }
    public String getContents() {
        return contents;
    }

    public CustomWord(String t, String c) {
        title = t;
        contents = c;
    }
}
