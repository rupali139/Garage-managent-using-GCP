package com.example.rupali.demo.HelperClass;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rupali.demo.InfoActivity;
import com.example.rupali.demo.R;
import com.google.firebase.firestore.GeoPoint;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private List<ListItem> listItems;
    private Activity activity;


    public MyAdapter(List<ListItem> listItems,  Activity activity) {
        this.listItems = listItems;
        this.activity=activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v=LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_item,viewGroup,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        ListItem listItem=listItems.get(i);
        viewHolder.tvhead.setText(listItem.getHead());
        viewHolder.tvdes.setText(listItem.getDes());
        final String addressstr=viewHolder.tvdes.getText().toString();
        viewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent=new Intent(activity,InfoActivity.class);
               Bundle bundle=activity.getIntent().getExtras();
               bundle.putString("Garagename",viewHolder.tvhead.getText().toString());
               intent.putExtras(bundle);
               activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
         public TextView  tvhead,tvdes;
         public LinearLayout linearLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvhead=(TextView)itemView.findViewById(R.id.tvheading);
            tvdes=(TextView)itemView.findViewById(R.id.tvdescription);
            linearLayout=(LinearLayout)itemView.findViewById(R.id.linearlayout);
        }
    }


}
