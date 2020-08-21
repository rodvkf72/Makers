package com.example.capstone;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class TogetherContents extends AppCompatActivity {
    TextView recommend_text;

    String phone_main = "";
    String name_main = "";
    String email_main = "";
    String sex_main = "";
    String contents_title_main = "";
    String contents_main = "";
    String area_main = "";
    String time_main = "";

    TextView tv_phone;
    TextView tv_name;
    TextView tv_email;
    TextView tv_sex;
    TextView tv_contents_title;
    TextView tv_contents;
    TextView tv_area;
    TextView tv_time;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.togethercontents);

        phone_main = getIntent().getExtras().getString("phone_num");
        name_main = getIntent().getExtras().getString("name");
        email_main = getIntent().getExtras().getString("email");
        sex_main = getIntent().getExtras().getString("sex");
        contents_title_main = getIntent().getExtras().getString("title");
        contents_main = getIntent().getExtras().getString("content");
        area_main = getIntent().getExtras().getString("area");
        time_main = getIntent().getExtras().getString("time_t");

        tv_phone = (TextView) findViewById(R.id.together_phone);
        tv_name = (TextView) findViewById(R.id.together_name);
        tv_email = (TextView) findViewById(R.id.together_email);
        tv_sex = (TextView) findViewById(R.id.together_sex);
        tv_contents_title = (TextView)findViewById(R.id.together_contents_title);
        tv_contents = (TextView) findViewById(R.id.together_contents);
        tv_area = (TextView) findViewById(R.id.together_area);
        tv_time = (TextView) findViewById(R.id.together_time);

        tv_phone.setText(phone_main);
        tv_name.setText(name_main);
        tv_email.setText(email_main);
        tv_sex.setText(sex_main);
        tv_contents_title.setText(contents_title_main);
        tv_contents.setText(contents_main);
        tv_area.setText(area_main);
        tv_time.setText(time_main);
    }
}
