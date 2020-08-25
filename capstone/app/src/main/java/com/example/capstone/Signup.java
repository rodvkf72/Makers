package com.example.capstone;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

public class Signup extends AppCompatActivity {
    String sign_name, sign_phone, sign_pass, sign_address, sign_email, sign_sex;
    EditText et_name, et_phone, et_pass, et_address, et_email;
    Button signbutton;
    RadioButton r1, r2;
    RadioGroup rg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        et_name = (EditText) findViewById(R.id.signup_name);
        et_phone = (EditText) findViewById(R.id.signup_phone);
        et_pass = (EditText) findViewById(R.id.signup_pass);
        et_address = (EditText) findViewById(R.id.signup_address);
        et_email = (EditText) findViewById(R.id.signup_email);

        rg = (RadioGroup) findViewById(R.id.radio_button_group);

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.sex_man){
                    sign_sex = "남";
                } else if (checkedId == R.id.sex_woman){
                    sign_sex = "여";
                }
            }
        });

        signbutton = (Button) findViewById(R.id.signup_signup);

        signbutton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                sign_name = et_name.getText().toString();
                sign_phone = et_phone.getText().toString();
                sign_pass = et_pass.getText().toString();
                sign_address = et_address.getText().toString();
                sign_email = et_email.getText().toString();

                SignupDB SDB = new SignupDB();
                SDB.execute();
            }
        });
    }

    // DB연동. DB 안 만들어놔서 주석처리 함 (로그인만 만들어 둠. 회원가입 DB 코드 추가해야 함)
    private class SignupDB extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... strings){
            // 인풋 파라메터값 생성
            String param = "u_name=" + sign_name + "&u_id=" + sign_phone + "&u_pass=" + sign_pass + "&u_address=" + sign_address + "&u_email=" + sign_email + "&u_sex=" + sign_sex + "";
            Log.e("POST",param);
            try {
                // 서버연결
                //URL url = new URL("http://192.168.0.53/capstone/Signup.php");
                URL url = new URL("http://192.168.0.53:9090/signup/");
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
                if (data.equals("insert_complete")) {
                    Toast.makeText(getApplicationContext(), "가입이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                    //여기서 인텐트를 통해서 로그인 후 메인페이지로 이동시키면 됨
                    Intent signintent = new Intent(Signup.this, MainPage.class);
                    signintent.putExtra("phone_num", sign_phone);
                    startActivity(signintent);
                    overridePendingTransition(R.anim.alphain_activity, R.anim.alphaout_activity);
                } else if (data.equals("insert_fail")){
                    Toast.makeText(getApplicationContext(), "아이디 중복입니다.", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getApplicationContext(), "인터넷 연결을 확인하세요.", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e){
                e.printStackTrace();
            }
            //overridePendingTransition(R.anim.alphain_activity, R.anim.alphaout_activity);
        }
    }
}
