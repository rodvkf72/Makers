package com.example.capstone;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class API extends AppCompatActivity {
    String title = "";
    String contenttype = "";
    String location = "";

    ListView api_contents_listview;
    ApiAdapter api_contents_adapter;
    ArrayList<ApiWord> api_contents = new ArrayList<ApiWord>();

    @Override
    protected  void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.api);

        api_contents_adapter = new ApiAdapter(this, R.layout.api_simpleitem, api_contents);
        api_contents_listview = (ListView) findViewById(R.id.api_listview);

        OpenAPI openapi = new OpenAPI();
        openapi.execute();

    }

    public class OpenAPI extends AsyncTask<Void, Void, String> {
        /*
        private String url;
        public OpenAPI(String url) {
            this.url = url;
        }
        */
        @Override
        protected String doInBackground(Void... params) {

            // parsing할 url 지정(API 키 포함해서)
            try {
                StringBuilder urlBuilder = new StringBuilder("http://api.visitkorea.or.kr/openapi/service/rest/KorService/areaBasedList"); /*URL*/
                urlBuilder.append("?" + URLEncoder.encode("ServiceKey", "UTF-8") + "=" + "RRZuhOlOrDgZ7l6GUJVh3u2PS9sQXljCcjBKi4Uv0cDKBeUw2s9TFscjx6D%2FNlxzkUnc2xW8stIUfqnrK84tPg%3D%3D"); /*Service Key*/
                //urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8"));	//현재 페이지 번호
                //urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8"));	//한 페이지 결과 수
                urlBuilder.append("&" + URLEncoder.encode("MobileApp", "UTF-8") + "=" + URLEncoder.encode("AppTest", "UTF-8"));	//서비스명
                urlBuilder.append("&" + URLEncoder.encode("MobileOS", "UTF-8") + "=" + URLEncoder.encode("ETC", "UTF-8"));	//IOS, AND, WIN, ETC
                //urlBuilder.append("&" + URLEncoder.encode("arrange", "UTF-8") + "=" + URLEncoder.encode("B", "UTF-8"));	//A=제목순, B=조회순, C=수정일순, D=생성일순
                //urlBuilder.append("&" + URLEncoder.encode("cat1", "UTF-8") + "=" + URLEncoder.encode("B", "UTF-8"));	//대분류 코드
                //urlBuilder.append("&" + URLEncoder.encode("contentTypeId", "UTF-8") + "=" + URLEncoder.encode("15", "UTF-8"));	//관광타입 ID
                urlBuilder.append("&" + URLEncoder.encode("areaCode", "UTF-8") + "=" + URLEncoder.encode("6", "UTF-8"));	//지역 코드
                //urlBuilder.append("&" + URLEncoder.encode("sigunguCode", "UTF-8") + "=" + URLEncoder.encode("4", "UTF-8"));	//시군구 코드
                //urlBuilder.append("&" + URLEncoder.encode("cat2", "UTF-8") + "=" + URLEncoder.encode("B", "UTF-8"));	//중분류 코드
                //urlBuilder.append("&" + URLEncoder.encode("cat3", "UTF-8") + "=" + URLEncoder.encode("B", "UTF-8"));	//소분류 코드
                //urlBuilder.append("&" + URLEncoder.encode("listYN", "UTF-8") + "=" + URLEncoder.encode("Y", "UTF-8"));
                //urlBuilder.append("&" + URLEncoder.encode("midifiedtime", "UTF-8") + "=" + URLEncoder.encode("4", "UTF-8"));	//콘텐츠 수정일
                URL url = new URL(urlBuilder.toString());

                DocumentBuilderFactory dbFactoty = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = null;
                try {
                    dBuilder = dbFactoty.newDocumentBuilder();
                } catch (ParserConfigurationException e) {
                    e.printStackTrace();
                }
                Document doc = null;
                try {
                    doc = dBuilder.parse(String.valueOf(url));
                } catch (IOException | SAXException e) {
                    e.printStackTrace();
                }

                // root tag
                doc.getDocumentElement().normalize();
                System.out.println("Root element: " + doc.getDocumentElement().getNodeName()); // Root element: result

                // 파싱할 tag
                NodeList nList = doc.getElementsByTagName("item");

                api_contents.clear();
                for (int temp = 0; temp < nList.getLength(); temp++) {
                    Node nNode = nList.item(temp);
                    if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                        Element eElement = (Element) nNode;
                        //location = getTagValue("addr1", eElement);
                        //contenttype = getTagValue("contenttypeid", eElement);
                        //title = getTagValue("title", eElement);

                        title = getTagValue("title", eElement);
                        contenttype = getTagValue("contenttypeid", eElement);
                        location =  getTagValue("addr1", eElement);

                        Log.d("OPEN_API","title  : " + getTagValue("title", eElement));
                        Log.d("OPEN_API","type  : " + getTagValue("contenttypeid", eElement));
                        Log.d("OPEN_API","location : " + getTagValue("addr1", eElement));

                        api_contents.add(new ApiWord(title, contenttype, location));
                    }    // for end
                }    // if end
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        private String getTagValue(String tag, Element eElement) {
            NodeList nlList = eElement.getElementsByTagName(tag).item(0).getChildNodes();
            Node nValue = (Node) nlList.item(0);
            if (nValue == null) {
                return null;
            }
            return nValue.getNodeValue();
        }

        @Override
        protected void onPostExecute(String str) {
            super.onPostExecute(str);
            api_contents_listview.setAdapter(api_contents_adapter);
        }
    }
}
