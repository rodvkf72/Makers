package com.example.capstone;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class LogIn extends AppCompatActivity {
    private long backPressedTime = 0;
    private final long FINISH_INTERVAL_TIME = 2000;

    String id = "";
    String pass = "";

    Button log;
    Button sign;
    TextView tvid, tvpw, tvsignup;

    EditText etid, etpw;

    @Override
    protected  void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        etid = (EditText)findViewById(R.id.editid);
        etpw = (EditText)findViewById(R.id.editpw);
        tvid = (TextView)findViewById(R.id.id_find);
        tvpw = (TextView)findViewById(R.id.pw_find);
        tvsignup = (TextView)findViewById(R.id.sign_up);

        log = (Button)findViewById(R.id.login);

        tvsignup.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(LogIn.this, Signup.class);
                startActivity(intent);
                overridePendingTransition(R.anim.rightin_activity, R.anim.leftout_activity);
            }
        });

        log.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                id = etid.getText().toString();
                pass = etpw.getText().toString();
                LoginDB LDB = new LoginDB();
                LDB.execute();
                //Intent intent = new Intent(LogIn.this, MainPage.class);
                //intent.putExtra("phone_num", id);
                //startActivity(intent);
                //overridePendingTransition(R.anim.alphain_activity, R.anim.alphaout_activity);
            }
        });
    }

    // DB연동. DB 안 만들어놔서 주석처리 함 (로그인만 만들어 둠. 회원가입 DB 코드 추가해야 함)
    private class LoginDB extends AsyncTask<String, Integer, String>{
        @Override
        protected String doInBackground(String... strings){
            // 인풋 파라메터값 생성
            String param = "u_id=" + id + "&u_pass=" + pass + "";
            Log.e("POST",param);
            try {
                // 서버연결
                URL url = new URL("http://192.168.0.53/capstone/Login.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.connect();

                // 안드로이드 -> 서버 파라메터값 전달
                OutputStream outs = conn.getOutputStream();
                outs.write(param.getBytes("UTF-8"));
                outs.flush();
                outs.close();

                // 서버 -> 안드로이드 파라메터값 전달
                InputStream is = null;
                BufferedReader in = null;
                String data = "";

                is = conn.getInputStream();
                in = new BufferedReader(new InputStreamReader(is), 8 * 1024);
                String line = null;
                StringBuffer buff = new StringBuffer();
                while ( ( line = in.readLine() ) != null )
                {
                    buff.append(line + "\n");
                }
                data = buff.toString().trim();

                // 서버에서 응답
                return data;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String data) {
            try {
                if (data.equals("true")) {
                    //Toast.makeText(getApplicationContext(), "로그인!", Toast.LENGTH_SHORT).show();
                    //여기서 인텐트를 통해서 로그인 후 메인페이지로 이동시키면 됨
                    Intent dbintent = new Intent(LogIn.this, MainPage.class);
                    dbintent.putExtra("phone_num", id);
                    startActivity(dbintent);
                    overridePendingTransition(R.anim.alphain_activity, R.anim.alphaout_activity);
                } else {
                    Toast.makeText(getApplicationContext(), "아이디(전화번호) 또는 비밀번호를 확인해주세요.", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e){
                e.printStackTrace();
            }
            //overridePendingTransition(R.anim.alphain_activity, R.anim.alphaout_activity);
        }
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
