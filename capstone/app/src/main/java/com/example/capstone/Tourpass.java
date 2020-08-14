package com.example.capstone;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

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

public class Tourpass extends Fragment {
    private View view;
    private ImageView iv;
    private String ID;

    Intent gintent;
    String id = "";
    String tourpass = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        view = inflater.inflate(R.layout.activity_create_qr, container, false);

        gintent = getActivity().getIntent();
        id = gintent.getExtras().getString("phone_num");

        iv = (ImageView)view.findViewById(R.id.qrcode);
        ID = id + "님 환영합니다.";

        TourpassCheckDB TCDB = new TourpassCheckDB();
        TCDB.execute();

        if(tourpass.equals("TRUE")){
            MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
            try{
                BitMatrix bitMatrix = multiFormatWriter.encode(ID, BarcodeFormat.QR_CODE,200,200);
                BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                iv.setImageBitmap(bitmap);
            }catch (Exception e){
                e.printStackTrace();
            }
        } else {

        }

        return view;
    }

    public class TourpassCheckDB extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            // 인풋 파라메터값 생성
            String param = "phone_text=" + id + "";
            Log.e("POST",param);
            try {
                // 서버연결
                URL url = new URL("http://172.30.1.56/capstone/TourpassCheck.php"); //이거 바꿔라
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
                JSONObject root = new JSONObject(data);
                JSONArray results = new JSONArray(root.getString("results"));
                for (int i = 0; i < results.length(); i++){
                    JSONObject content = results.getJSONObject(i);
                    tourpass = content.getString("tourpass");
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
