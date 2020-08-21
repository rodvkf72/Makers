package com.example.capstone;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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
import java.util.ArrayList;

public class DeleteContents extends AppCompatActivity {
    Intent gintent;
    String phone_num = "";
    String notice_board = "";
    String contents_text = "";
    String no = "";

    ArrayList<String> nos = new ArrayList<String>();
    ArrayList<String> contents = new ArrayList<String>();
    ListView contents_listview;
    ArrayAdapter contents_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.noticeboard_delete);

        contents_adapter = new ArrayAdapter(this, R.layout.simpleitem, contents);
        contents_listview = (ListView) findViewById(R.id.delete_listview);

        gintent = getIntent();
        phone_num = gintent.getStringExtra("phone_num");

        DeleteFindDB dfdb = new DeleteFindDB();
        dfdb.execute();

        contents_listview.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                contents_text = (String)parent.getItemAtPosition(position);
                no = nos.get(position);

                show();
            }
        });
    }

    void show(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("게시글 삭제");
        builder.setMessage(no + "번 게시글을 삭제하시겠습니까?");
        builder.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DeleteDB ddb = new DeleteDB();
                ddb.execute();
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }

    public class DeleteFindDB extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            // 인풋 파라메터값 생성
            String param = "phone_num=" + phone_num + ""; //"phone_num=" + id + ""; 추가
            Log.e("POST",param);
            try {
                // 서버연결
                URL url = new URL("http://192.168.0.53/capstone/notice_board_find.php"); //이거 바꿔라!
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
            try{
                contents.clear();
                JSONObject root = new JSONObject(data);
                JSONArray results = new JSONArray(root.getString("results"));
                for (int i = 0; i < results.length(); i++){
                    JSONObject content = results.getJSONObject(i);
                    //여기서 필요한 부분만 가져오고 조건식 입력
                    no = content.getString("no");
                    notice_board = content.getString("title");
                    contents.add(notice_board.toString());
                    nos.add(no.toString());
                }
            } catch (Exception e){
                e.printStackTrace();
            }
            contents_listview.setAdapter(contents_adapter);
            //contents_adapter.notifyDataSetChanged();
        }
    }

    public class DeleteDB extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            // 인풋 파라메터값 생성
            String param = "num=" + no + ""; //"phone_num=" + id + ""; 추가
            Log.e("POST",param);
            try {
                // 서버연결
                URL url = new URL("http://192.168.0.53/capstone/notice_board_delete.php"); //이거 바꿔라!
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
            try{
                if(data.equals("delete")) {
                    finish();
                    overridePendingTransition(0, 0);
                    startActivity(getIntent());
                    overridePendingTransition(0, 0);
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    public void onBackPressed() {
        Intent deleteintent = new Intent();
        deleteintent.putExtra("1", "result");
        setResult(RESULT_OK, deleteintent);
        finish();
    }
}
