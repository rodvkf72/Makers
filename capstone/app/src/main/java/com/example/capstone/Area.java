package com.example.capstone;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

public class Area extends AppCompatActivity {
    CustomAdapter mAdapter;

    ArrayList<String> area_menu;
    ArrayList<String> gu_menu;
    ArrayList<String> time_menu;
    ArrayList<String> sex_menu;
    //ArrayList<String> sex_menu = new ArrayList<String>();

    ListView area_listview;
    ListView gu_listview;
    ListView time_listview;
    ListView sex_listview;

    ArrayAdapter area_adapter;
    ArrayAdapter gu_adapter;
    ArrayAdapter time_adapter;
    ArrayAdapter sex_adapter;

    String area_text = "";
    String gu_text = "";
    String time_text = "";
    String sex_text = "";

    String id = "";
    String idt = "";

    /*
    지역 설정란 입니다.
    임시로 어레이 리스트에 데이터를 직접 입력하였으며
    데이터베이스에서 가져와서 사용할 수도 있습니다.
    */
    @Override
    protected  void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.area);

        Intent gintent = getIntent();
        id = gintent.getExtras().getString("phone_num");
        idt = gintent.getExtras().getString("phone_num");

        area_menu = new ArrayList<String>(Arrays.asList("부산", "서울", "강원도", "대구"));
        //gu_menu = new ArrayList<String>(Arrays.asList("사상구", "중구", "해운대구"));
        time_menu = new ArrayList<String>(Arrays.asList("당일", "1박 2일", "2박 3일", "3박 4일", "4일 이상"));
        sex_menu = new ArrayList<String>(Arrays.asList("남", "여"));

        area_adapter = new ArrayAdapter(this, R.layout.simpleitem, area_menu);
        //gu_adapter = new ArrayAdapter(this, R.layout.simpleitem, gu_menu);
        time_adapter = new ArrayAdapter(this, R.layout.simpleitem, time_menu);
        sex_adapter = new ArrayAdapter(this, R.layout.simpleitem, sex_menu);

        area_listview = (ListView) findViewById(R.id.area);
        //gu_listview = (ListView) findViewById(R.id.gu);
        time_listview = (ListView) findViewById(R.id.a_time);
        sex_listview = (ListView) findViewById(R.id.sex);

        area_listview.setAdapter(area_adapter);

        /*
        지역과 같은 아이템을 클릭했을 시 다음 어레이 리스트가 보이도록 함
         */
        area_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                //gu_text = (String) parent.getItemAtPosition(position);
                area_text = (String) parent.getItemAtPosition(position);
                //gu_listview.setAdapter(gu_adapter);
                time_listview.setAdapter(time_adapter);
            }
        });
        /*
        gu_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        */
        time_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                time_text = (String) parent.getItemAtPosition(position);
                sex_listview.setAdapter(sex_adapter);
            }
        });
        sex_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                sex_text = (String) parent.getItemAtPosition(position);

                SettingDB setDB = new SettingDB();
                setDB.execute();
                /*Intent intent = new Intent(Area.this, MainPage.class);
                area_text = "테스트용";
                intent.putExtra("area_text", area_text);
                //intent.putExtra("gu_text", gu_text);
                intent.putExtra("time_text", time_text);
                intent.putExtra("sex_text", sex_text);
                intent.putExtra("phone_num", id);   //테스트용
                startActivity(intent);*/
            }
        });

    }

    /*
    사용자 설정에 대한 데이터베이스 연결
     */
    public class SettingDB extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            // 인풋 파라메터값 생성
            String param = "phone_text=" + id + "&area_text=" + area_text + "&time_text=" + time_text + "&sex_text=" + sex_text + "";
            Log.e("POST",param);
            try {
                // 서버연결
                URL url = new URL("http://192.168.0.53/capstone/AreaSetting.php"); //이거 바꿔라
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
                /*area_menu.clear();

                JSONObject root = new JSONObject(data);
                JSONArray results = new JSONArray(root.getString("results"));
                for (int i = 0; i < results.length(); i++){
                    JSONObject content = results.getJSONObject(i);
                    //area_text = content.getString("area_text");
                    //gu_text = content.getString("gu_text");
                    //time_text = content.getString("time_text");
                    //sex_text = content.getString("sex_text");
                    //BUILDING_MENU.add(building_name);
                    //building_adapter.notifyDataSetChanged();
                }*/
            } catch (Exception e){
                e.printStackTrace();
            }
            Intent intent = new Intent(Area.this, MainPage.class);
            //intent.putExtra("area_text", area_text);
            //intent.putExtra("gu_text", gu_text);
            //intent.putExtra("time_text", time_text);
            //intent.putExtra("sex_text", sex_text);
            intent.putExtra("phone_num", id);   //테스트용
            startActivity(intent);
            //overridePendingTransition(R.anim.alphaout_activity, R.anim.alphain_activity);
        }
    }
}
