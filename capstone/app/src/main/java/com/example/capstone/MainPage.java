package com.example.capstone;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
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
    private AR unityAR = new AR();
    private Qna optionFragment = new Qna();

    int requestCode;
    int resultCode;
    Intent data;

    Button LogO;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainpage);

        //위치정보 서비스
        final LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        //다른 액티비티에서의 변화가 있을 경우 즉시 반영시키기 위함
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
                    case R.id.navigation_ar: {
                        //하단 버튼 클릭 시 사용자 위치정보 확인
                        /*if ( Build.VERSION.SDK_INT >= 23 &&
                                ContextCompat.checkSelfPermission( getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
                            ActivityCompat.requestPermissions( MainPage.this, new String[] {  android.Manifest.permission.ACCESS_FINE_LOCATION  },
                                    0 );
                            Toast.makeText(MainPage.this, "위치정보 권한을 허용해 주세요.", Toast.LENGTH_SHORT).show();
                        } else {
                            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 1, mLocationListener);
                            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 100, 1, mLocationListener);
                        }*/

                        transaction.replace(R.id.frame_layout, unityAR).commitAllowingStateLoss();
                        break;
                        //AR이라 프래그먼트로 구현이 어려움
                        //Intent intent = new Intent(MainPage.this, UnityPlayerActivity.class);
                        //startActivity(intent);
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

        //로그아웃 버튼 클릭 시
        LogO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainPage.this, LogIn.class);
                startActivity(intent);
                overridePendingTransition(R.anim.rightin_activity, R.anim.leftout_activity);
            }
        });
    }

    //위치정보 확인 리스너
    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            double longitude = location.getLongitude();
            double latitude = location.getLatitude();
            double altitude = location.getAltitude();
            float accuracy = location.getAccuracy();
            String provider = location.getProvider();

            Toast.makeText(MainPage.this, "현재 위치 \n위도 " + latitude + "\n경도" + longitude, Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(MainPage.this, UnityPlayerActivity.class);
            intent.putExtra("longitude", longitude);
            intent.putExtra("latitude", latitude);
            startActivity(intent);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

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
