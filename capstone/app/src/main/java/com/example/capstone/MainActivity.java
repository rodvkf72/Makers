package com.example.capstone;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class MainActivity extends AppCompatActivity {
    private ImageView mImageVIew;
    private int nBefore = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ConstraintLayout CL = (ConstraintLayout)findViewById(R.id.constraintLayout);
        CL.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event){
                switch(event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        Intent intent = new Intent(MainActivity.this, LogIn.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.alphain_activity, R.anim.alphaout_activity);
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_MOVE:
                }
                return true;
            }
        });
    }
}
