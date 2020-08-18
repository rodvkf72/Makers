package com.example.capstone;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
    private Recommend context;
    private TextView placeTitle;
    private TextView placeInfo;
    private TextView preferenceRatio;
    private ImageView placeImage;

    private int test;

    public CustomInfoWindowAdapter(Recommend context){
        this.context = context;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View view = context.getLayoutInflater().inflate(R.layout.custom_info_window, null);

        placeTitle = (TextView) view.findViewById(R.id.tv_title);
        placeInfo = (TextView) view.findViewById(R.id.tv_subtitle);
        placeImage = (ImageView) view.findViewById(R.id.imageView1) ;
        preferenceRatio = (TextView) view.findViewById(R.id.tv_sunho);

        if (marker.getTitle().equals("해운대 해수욕장")){
            placeTitle.setText(marker.getTitle());
            placeInfo.setText("해운대해수욕장은 주변의 빼어난 자연경관과 어우러져 전국 제일의 해수욕장으로 각광받고 있으며 매년 7월 1일부터 8월 31일까지 2개월동안 해수욕이 가능하다.");
            preferenceRatio.setText("\n선호율 : 81%");
            placeImage.setImageResource(R.drawable.pic1);
        } else if (marker.getTitle().equals("부산 영화체험 박물관")) {
            placeTitle.setText(marker.getTitle());
            placeInfo.setText("부산영화체험박물관의 전시는 한 편의 재미있는 영화탐험스토리를 따라가면서 영화의 역사와 원리, 영화의 장르 및 제작방법, 영화축제 등의 영화 콘텐츠를 쉽고 재미있게 체험할 수 있도록 구성되어 있습니다.");
            preferenceRatio.setText("\n선호율 : 77%");
            placeImage.setImageResource(R.drawable.pic2);
        } else if (marker.getTitle().equals("광안리 해수욕장")){
            placeTitle.setText(marker.getTitle());
            placeInfo.setText("금련산에서 내린 질 좋은 사질(沙質)에 완만한 반월형(半月形)으로 휘어진 사장은 전국적으로 이름난 해수욕장이다.");
            preferenceRatio.setText("\n선호율 : 83%");
            placeImage.setImageResource(R.drawable.pic3);
        } else if (marker.getTitle().equals("감천 문화마을")){
            placeTitle.setText(marker.getTitle());
            placeInfo.setText("산자락을 따라 질서정연하게 늘어선 계단식 집단 주거형태와 모든 길이 통하는 미로미로(美路迷路) 골목길의 경관은 감천만의 독특함을 보여줍니다.");
            preferenceRatio.setText("\n선호율 : 83%");
            placeImage.setImageResource(R.drawable.pic4);
        } else if (marker.getTitle().equals("40계단 문화관광테마거리")){
            placeTitle.setText(marker.getTitle());
            placeInfo.setText("이곳에서는 부산의 역사와 피난민의 생활상, 지역 예술가들의 작품을 둘러볼 수 있다.");
            preferenceRatio.setText("\n선호율 : 74%");
            placeImage.setImageResource(R.drawable.pic5);
        } else if (marker.getTitle().equals("흰여울 문화마을")){
            placeTitle.setText(marker.getTitle());
            placeInfo.setText("절영해안산책로 가파른 담벼락 위로 독특한 마을 풍경이 보인다. 해안가 절벽 끝에 바다를 따라 난 좁은 골목길 안쪽으로 작은 집들이 다닥다닥 붙어 있다.");
            preferenceRatio.setText("\n선호율 : 81%");
            placeImage.setImageResource(R.drawable.pic6);
        } else if (marker.getTitle().equals("다대포 해수욕장")){
            placeTitle.setText(marker.getTitle());
            placeInfo.setText("주차장 등 편의시설이 잘 갖추어져 있어 여름철 가족단위 알뜰 피서지로 각광받고 있다. 주변 횟집의 싱싱한 생선회 맛이 일품이다.");
            preferenceRatio.setText("\n선호율 : 79%");
            placeImage.setImageResource(R.drawable.pic7);
        } else if (marker.getTitle().equals("국립 해양박물관")){
            placeTitle.setText(marker.getTitle());
            placeInfo.setText("대한민국 부산광역시 영도구 동삼동의 해양클러스터에 위치하고 있으며, 대한민국의 유일한 국립해양박물관이다.");
            preferenceRatio.setText("\n선호율 : 83%");
            placeImage.setImageResource(R.drawable.pic8);
        } else if (marker.getTitle().equals("아미산 전망대")){
            placeTitle.setText(marker.getTitle());
            placeInfo.setText("낙동강 하구의 일몰을 볼 수 있는 부산의 명소");
            preferenceRatio.setText("\n선호율 : 88%");
            placeImage.setImageResource(R.drawable.pic9);
        } else if (marker.getTitle().equals("암남 공원")) {
            placeTitle.setText(marker.getTitle());
            placeInfo.setText("암남공원(岩南公園)은 부산광역시 서구 암남동 산193번지 일원 진정산 일대의 자연공원이다.");
            preferenceRatio.setText("\n선호율 : 72%");
            placeImage.setImageResource(R.drawable.pic10);
        }
        return view;
    }

}
