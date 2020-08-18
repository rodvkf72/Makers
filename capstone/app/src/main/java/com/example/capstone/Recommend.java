package com.example.capstone;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class Recommend extends Fragment implements OnMapReadyCallback {
    CustomInfoWindowAdapter adapter;

    View view;
    TextView recommend_text;
    private MapView mapView = null;
    private GoogleMap mMap = null;

    public Recommend()
    {
        // required
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        view = inflater.inflate(R.layout.recommend, container, false);
        mapView = (MapView)view.findViewById(R.id.map);
        //recommend_text = (TextView) view.findViewById(R.id.recommend_tv);

        mapView.getMapAsync(this);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onLowMemory();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //액티비티가 처음 생성될 때 실행되는 함수

        if(mapView != null)
        {
            mapView.onCreate(savedInstanceState);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // 구글 맵 객체 불러옴
        mMap = googleMap;

        List<MarkerItem> markerItemList = new ArrayList<MarkerItem>();
        markerItemList.add(new MarkerItem(35.158065, 129.160727, "해운대 해수욕장", 89));
        markerItemList.add(new MarkerItem(35.101667, 129.033787, "부산 영화체험 박물관", 87));
        markerItemList.add(new MarkerItem(35.153040, 129.118603, "광안리 해수욕장", 81));
        markerItemList.add(new MarkerItem(35.153040, 129.010592, "감천 문화마을", 82));
        markerItemList.add(new MarkerItem(35.104863, 129.034844, "40계단 문화관광테마거리", 72));
        markerItemList.add(new MarkerItem(35.078280, 129.045331, "흰여울 문화마을", 81));
        markerItemList.add(new MarkerItem(35.046908, 128.966439, "다대포 해수욕장", 83));
        markerItemList.add(new MarkerItem(35.078443, 129.080377, "국립 해양박물관", 85));
        markerItemList.add(new MarkerItem(35.052593, 128.960761, "아미산 전망대", 89));
        markerItemList.add(new MarkerItem(35.063311, 129.019297, "암남 공원", 84));

        //Marker
        for(int i = 0; i < 10; i++) {
            LatLng location = new LatLng(markerItemList.get(i).getLat(), markerItemList.get(i).getLon());

            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(location);
            markerOptions.title(markerItemList.get(i).getPlaceTitle());
            markerOptions.snippet("선호율 : " + String.valueOf(markerItemList.get(i).getPreferenceRatio()) + "%");

            int drawableId = getResources().getIdentifier("pic" + (i + 1), "drawable", "com.example.capstone");

            BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(drawableId);
            Bitmap b = bitmapdraw.getBitmap();
            Bitmap smallMarker = Bitmap.createScaledBitmap(b, 150, 150, false);
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));

            mMap.addMarker(markerOptions);
        }

        adapter = new CustomInfoWindowAdapter(this);
        mMap.setInfoWindowAdapter(adapter);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(35.078280, 129.045331)));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
    }

}
