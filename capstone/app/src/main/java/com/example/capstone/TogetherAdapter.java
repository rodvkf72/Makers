package com.example.capstone;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class TogetherAdapter extends ArrayAdapter<CustomWord> {
    public TogetherAdapter(Context context, int simpleitem, ArrayList<CustomWord> customwords) {
        super(context,0, customwords);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CustomWord word = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.together_simpleitem, parent, false);
        }
        TextView title = (TextView) convertView.findViewById(R.id.text_view_1);
        TextView content = (TextView) convertView.findViewById(R.id.text_view_2);
        title.setText(word.getTitle());
        content.setText(word.getContents());

        return convertView;
    }
}
