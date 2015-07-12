package com.panter.panterapp.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.panter.panterapp.R;
import com.panter.panterapp.logic.MenuObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MenuObjectAdapter extends ArrayAdapter<MenuObject> {

    Context context;
    int layoutResourceId;
    ArrayList<MenuObject> data = null;

    public MenuObjectAdapter(Context context, int layoutResourceId, ArrayList<MenuObject> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View row, ViewGroup parent) {
        MenuObjectHolder holder = null;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new MenuObjectHolder();
//            holder.imgIcon = (ImageView)row.findViewById(R.id.imgIcon);
            holder.txtName = (TextView)row.findViewById(R.id.txtName);

            row.setTag(holder);
        }
        else
        {
            holder = (MenuObjectHolder)row.getTag();
        }
        holder.txtName.setText(data.get(position).getName());

//        holder.imgIcon.setImageBitmap(getBitmapFromURL(data.get(position).getIconURL()));

        return row;
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            Log.e("src", src);
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            Log.e("Bitmap","returned");
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Exception",e.getMessage());
            return null;
        }
    }

    static class MenuObjectHolder
    {
//        ImageView imgIcon;
        TextView txtName;
    }
}