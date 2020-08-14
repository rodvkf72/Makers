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


        placeTitle.setText("흰여울문화마을");
        placeInfo.setText("절영해안산책로 가파른 담벼락 위로 독특한 마을 풍경이 보인다. 해안가 절벽 끝에 바다를 따라 난 좁은 골목길 안쪽으로 작은 집들이 다닥다닥 붙어 있다.");
        preferenceRatio.setText("\n선호율 : 81%");
        placeImage.setImageResource(R.drawable.pic6);

        return view;
    }

}
