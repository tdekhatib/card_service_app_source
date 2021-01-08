package com.ma.hololstore.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ma.hololstore.Classes.Config;
import com.ma.hololstore.Classes.notification_class;
import com.ma.hololstore.Classes.trans_class;
import com.ma.hololstore.R;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class TransAdapter extends RecyclerView.Adapter<TransAdapter.MyViewHolder> {
    public static ArrayList<trans_class> alldata = new ArrayList<trans_class>();



    Context context;

    public void addNewData(List<trans_class> alldata1) {

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

        TextView item_trans_from;
        TextView item_trans_to;
        TextView item_trans_money;
        TextView item_trans_date;
          public MyViewHolder(View view) {
            super(view);
            //  title_cared_product_rec = (TextView) view.findViewById(R.id.title_cared_product_rec);
              item_trans_from = view.findViewById(R.id.item_trans_from);
              item_trans_to = view.findViewById(R.id.item_trans_to );
              item_trans_money = view.findViewById(R.id.item_trans_money );
              item_trans_date = view.findViewById(R.id.item_trans_date );

        }
    }

    public TransAdapter(Context context, ArrayList<trans_class> alldata) {
        this.alldata = alldata;
        this.context = context;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.item_trans_date.setText( "تاريخ التحويل : "  + Config.GET_DATE(Long.valueOf( alldata.get(position).shop_date)));
        holder.item_trans_from.setText( "من الحساب : "+ alldata.get(position).shop_from);
        holder.item_trans_to.setText( "إلى الحساب : "+ alldata.get(position).shop_to);
        holder.item_trans_money.setText( "المبلغ : " +  alldata.get(position).shop_money + " دينار ");

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


        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_trans_money, parent, false);

        return new MyViewHolder(v);
    }



}