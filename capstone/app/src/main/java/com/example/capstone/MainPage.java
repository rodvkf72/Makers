package com.example.capstone;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainPage extends AppCompatActivity {
    private long backPressedTime = 0;
    private final long FINISH_INTERVAL_TIME = 2000;

    private FragmentManager fragmentManager = getSupportFragmentManager();

    // 4개 메뉴에 들어갈 프레그먼트
    private Together chatFragment = new Together();
    private Recommend mapFragment = new Recommend();
    private Tourpass createQR = new Tourpass();
    private Qna optionFragment = new Qna();

    int requestCode;
    int resultCode;
    Intent data;

    Button LogO;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainpage);

        onActivityResult(requestCode, resultCode, data);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_view);

        LogO = (Button)findViewById(R.id.logout);

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_layout, chatFragment).commitAllowingStateLoss();

        //bottomNavigationView 아이템이 선택될 때 호출될 리스너 등록
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                switch (item.getItemId()) {
                    case R.id.navigation_chat: {
                        transaction.replace(R.id.frame_layout, chatFragment).commitAllowingStateLoss();
                        break;
                    }
                    case R.id.navigation_map: {
                        transaction.replace(R.id.frame_layout, mapFragment).commitAllowingStateLoss();
                        break;
                    }
                    case R.id.navigation_tourpass: {
                        transaction.replace(R.id.frame_layout, createQR).commitAllowingStateLoss();
                        break;
                    }
                    case R.id.navigation_options: {
                        transaction.replace(R.id.frame_layout, optionFragment).commitAllowingStateLoss();
                        break;
                    }
                }

                return true;
            }
        });
        LogO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainPage.this, LogIn.class);
                startActivity(intent);
                overridePendingTransition(R.anim.rightin_activity, R.anim.leftout_activity);
            }
        });
    }

    //뒤로가기 버튼 두번 누를 시 종료
    public void onBackPressed(){
        long tempTime = System.currentTimeMillis();
        long intervalTime = tempTime - backPressedTime;

        if(0 <= intervalTime && FINISH_INTERVAL_TIME >= intervalTime) {
            finishAffinity();
            System.runFinalization();
            System.exit(0);
        } else {
            backPressedTime = tempTime;
            Toast.makeText(getApplicationContext(), "종료를 원하시면 한번 더 누르세요.", Toast.LENGTH_SHORT).show();
        }
    }
}
