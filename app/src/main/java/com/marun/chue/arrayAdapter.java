package com.marun.chue;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.graphics.Bitmap;

import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

public class arrayAdapter extends ArrayAdapter<cards> {
    Context context;
    public arrayAdapter(Context context, int resourceId, List<cards> items){
        super(context, resourceId, items);
    }
    public View getView(int position, View convertView, ViewGroup parent){
        cards card_item = getItem(position);
        if (convertView == null){

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item, parent, false);
        }
        TextView name = (TextView) convertView.findViewById(R.id.name);
        TextView age = (TextView) convertView.findViewById(R.id.age);
        TextView info = (TextView) convertView.findViewById(R.id.info);
        ImageView image = (ImageView) convertView.findViewById(R.id.image);
        name.setText(card_item.getName());
        age.setText(card_item.getAge().toString());
        info.setText(card_item.getInfo());
        switch(card_item.getProfileImageUrl()){
            case "default":
                Glide.with(convertView.getContext()).load(R.drawable.chuelogoc).into(image);
                break;
            default:
                Glide.clear(image);
                Glide.with(convertView.getContext()).load(card_item.getProfileImageUrl()).into(image);
                break;
        }
        return convertView;
    }
}
