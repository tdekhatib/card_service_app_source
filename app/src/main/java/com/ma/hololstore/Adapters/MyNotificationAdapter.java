package com.ma.hololstore.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.ma.hololstore.R;
import com.ma.hololstore.Classes.notification_class;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class MyNotificationAdapter extends RecyclerView.Adapter<MyNotificationAdapter.MyViewHolder> {
    public static ArrayList<notification_class> alldata = new ArrayList<notification_class>();



    Context context;

    public void addNewData(List<notification_class> alldata1) {

        for (int i = 0; i < alldata1.size(); i++) {
            alldata.add(alldata1.get(i));
        }

        this.notifyDataSetChanged();
    }

    public void RefreshData() {
        this.notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        //        CircleImageView story_image;
//        ProgressBar progress;
//        LinearLayout add_to_my;

        TextView item_notification_title;
        TextView item_notification_Body;
        TextView item_notification_Date;
          public MyViewHolder(View view) {
            super(view);
            //  title_cared_product_rec = (TextView) view.findViewById(R.id.title_cared_product_rec);
              item_notification_title = view.findViewById(R.id.item_notification_title);
              item_notification_Body = view.findViewById(R.id.item_notification_body );
              item_notification_Date = view.findViewById(R.id.item_notification_date );

        }
    }

    public MyNotificationAdapter(Context context, ArrayList<notification_class> alldata) {
        this.alldata = alldata;
        this.context = context;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.item_notification_Body.setText(  alldata.get(position).Body);
        holder.item_notification_Date.setText(  alldata.get(position).Date1);
        holder.item_notification_title.setText(  alldata.get(position).Title);

    }

    @Override
    public int getItemCount() {
        return alldata.size();
    }

    public static int getIcCount() {
        return alldata.size();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification, parent, false);

        return new MyViewHolder(v);
    }



}