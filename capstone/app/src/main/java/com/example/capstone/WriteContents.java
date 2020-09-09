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

//게시글 작성
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

        //글을 작성하는 사용자의 이름, 이메일, 성별을 가지고 옴. (글을 작성할 때 수정이 불가능하게 하기 위함)
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

                //지역 선택을 라디오 버튼 팝업창으로 띄움
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

                //일정 선택을 라디오 버튼 팝업창으로 띄움
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

        //작성완료 시 작성된 제목과 내용을 데이터베이스에 전송
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

    //사용자의 이름, 이메일, 성별을 가져오는 데이터베이스
    public class ConfirmUserDB extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            // 인풋 파라메터값 생성
            String param = "cudb_phonenum=" + gphonenum + "";
            Log.e("POST", param);
            try {
                // 서버연결
                //URL url = new URL("http://192.168.0.53/capstone/write_contents_check.php");
                URL url = new URL("http://192.168.0.50:9090/noticeboard_content_check/");
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

    //작성된 내용과 사용자의 정보를 저장하는 데이터베이스
    public class EnrollmentDB extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            // 인풋 파라메터값 생성
            String param = "enroll_phonenum=" + gphonenum + "&enroll_name=" + write_name + "&enroll_email=" + write_email
                    + "&enroll_sex=" + write_sex + "&enroll_contents_title=" + title + "&enroll_contents=" + main + "&enroll_area=" + selected_area_item + "&enroll_time=" + selected_time_item + "";
            Log.e("POST", param);
            try {
                // 서버연결
                //URL url = new URL("http://192.168.0.53/capstone/write_contents_enrollment.php");  //PHP 코드
                URL url = new URL("http://192.168.0.50:9090/noticeboard_insert/");  //GO 코드
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
                if((!data.equals("fail") && (!data.equals(null)))){
                    //게시글이 저장될 경우 파이어베이스 클라우드 메시지(FCM)을 실행. 조건에 맞는 사람에게 해당 게시글이 등록되었다고 푸시알림 전송
                    PushSendDB psdb = new PushSendDB();
                    psdb.execute();

                    /*
                    finish() 로 종료하였기에 이 전 액티비티로 되돌아가지만 setResult를 통하여 StartActivityForResult에 값울 전송했기에
                    최신화 된 상태를 가져옴. 즉, 이 전 액티비티가 새로고침 되는 셈. 따라서 게시글을 등록 하자마자 게시글 목록에 올라와 있는 것을 확인할 수 있음
                     */
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

    /*
    파이어베이스 클라우드 메시지(FCM)를 웹 서버에서 처리하도록 하였기에 해당 주소로 연결만 하고 데이터를 보내거나 받아오지는 않음
    이 방식이 문제가 될 시 파라미터 값을 전송하고 그 값을 서버에서 받아서 처리하도록 구현해야 함
     */
    public class PushSendDB extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            try {
                // 서버연결
                //URL url = new URL("http://192.168.0.53/capstone/write_contents_enrollment.php");
                URL url = new URL("http://192.168.0.50:9090/send_alarm/");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.connect();

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
        }
    }
}
