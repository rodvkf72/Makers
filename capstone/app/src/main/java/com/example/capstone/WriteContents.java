package com.example.capstone;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

public class WriteContents extends AppCompatActivity {
    Intent gintent;
    String gphonenum = "";

    String write_name = "";
    String write_email = "";
    String write_sex = "";

    final int[] selected_area_item_radio = {0};
    final int[] selected_time_item_radio = {0};
    String selected_area_item = "";
    String selected_time_item = "";
    String title = "";
    String main = "";

    EditText write_title, write_main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notice_board_write);

        gintent = getIntent();
        gphonenum = gintent.getExtras().getString("phone_num");

        ConfirmUserDB CUDB = new ConfirmUserDB();
        CUDB.execute();

        write_title = (EditText)findViewById(R.id.write_title);
        write_main = (EditText)findViewById(R.id.write_main);

        Button area_select = (Button)findViewById(R.id.write_area_select);
        Button time_select = (Button)findViewById(R.id.write_time_select);
        Button finish_button = (Button)findViewById(R.id.write_finish);

        area_select.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v){
                final String[] area_items = new String[]{"서울", "부산", "대구", "강원도", "광주"};
                Arrays.sort(area_items);

                AlertDialog.Builder area_dialog = new AlertDialog.Builder(WriteContents.this);
                area_dialog.setTitle("지역을 선택하세요")
                        .setSingleChoiceItems(area_items, 0, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                selected_area_item_radio[0] = which;
                            }
                        })
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                selected_area_item = area_items[selected_area_item_radio[0]];
                            }
                        }).create().show();
            }
        });

        time_select.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v){
                final String[] time_items = new String[]{"당일", "1박 2일", "2박 3일", "3박 4일", "4일 이상"};
                Arrays.sort(time_items);

                AlertDialog.Builder area_dialog = new AlertDialog.Builder(WriteContents.this);
                area_dialog.setTitle("일정을 선택하세요.")
                        .setSingleChoiceItems(time_items, 0, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                selected_time_item_radio[0] = which;
                            }
                        })
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                selected_time_item = time_items[selected_time_item_radio[0]];
                            }
                        }).create().show();
            }
        });

        finish_button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v){
                title = write_title.getText().toString();
                main = write_main.getText().toString();

                EnrollmentDB EDB = new EnrollmentDB();
                EDB.execute();
            }
        });
    }

    public class ConfirmUserDB extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            // 인풋 파라메터값 생성
            String param = "cudb_phonenum=" + gphonenum + "";
            Log.e("POST", param);
            try {
                // 서버연결
                URL url = new URL("http://172.30.1.56/capstone/write_contents_check.php");
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
                while ((line = in.readLine()) != null) {
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
                JSONObject root = new JSONObject(data);
                JSONArray results = new JSONArray(root.getString("results"));
                for (int i = 0; i < results.length(); i++) {
                    JSONObject content = results.getJSONObject(i);
                    //여기서 필요한 부분만 가져오고 조건식 입력
                    write_name = content.getString("name");
                    write_email = content.getString("email");
                    write_sex = content.getString("sex");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public class EnrollmentDB extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            // 인풋 파라메터값 생성
            String param = "enroll_phonenum=" + gphonenum + "&enroll_name=" + write_name + "&enroll_email=" + write_email
                    + "&enroll_sex=" + write_sex + "&enroll_contents_title=" + title + "&enroll_contents=" + main + "&enroll_area=" + selected_area_item + "&enroll_time=" + selected_time_item + "";
            Log.e("POST", param);
            try {
                // 서버연결
                URL url = new URL("http://172.30.1.56/capstone/write_contents_enrollment.php");
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
                while ((line = in.readLine()) != null) {
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
                if(data.equals("complete")){
                    //Toast.makeText(getApplicationContext(), "게시글 등록 완료 !", Toast.LENGTH_SHORT).show();
                    Intent writeintent = new Intent();
                    writeintent.putExtra("1", "result");
                    setResult(RESULT_OK, writeintent);
                    finish();
                } else if(data.equals("fail")) {
                    Toast.makeText(getApplicationContext(), "게시글 등록에 실패했습니다.", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
