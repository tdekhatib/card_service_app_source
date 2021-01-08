package com.ma.hololstore.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ma.hololstore.Classes.Config;
import com.ma.hololstore.Classes.card_class;
import com.ma.hololstore.Classes.shop_class;
import com.ma.hololstore.R;
import com.ma.hololstore.Server.SERVER_INFO2;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by TDE on 12/5/2018.
 */

public class search_adapter extends ArrayAdapter {
    private Context mContext;

    public ArrayList<shop_class> all_news;

    public search_adapter(Context context, int textViewResourceId,
                          ArrayList<shop_class> TaskList2) {
        super(context, textViewResourceId, TaskList2);

          mContext = context;
        this.all_news = TaskList2;
    }


    private class ViewHolder {
        TextView item_card_name;
         TextView item_card_buyer_price;

         TextView item_shop_search_call_us;

    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


        ViewHolder holder = null;
        final shop_class tb = all_news.get(position);

        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.item_search, null);

            holder = new ViewHolder();
            convertView.setTag(holder);
        } else

        {
            holder = (ViewHolder) convertView.getTag();
        }
        try {

        } catch (Exception ex) {
        }
        try {
            holder.item_card_name = (TextView) convertView.findViewById(R.id.item_card_name);
              holder.item_card_buyer_price = (TextView) convertView.findViewById(R.id.item_card_buyer_price);
              holder.item_shop_search_call_us = (TextView) convertView.findViewById(R.id.item_shop_search_call_us);

        } catch (Exception ex) {
        }

        holder.item_card_name.setText(tb.shop_name);
        holder.item_card_buyer_price.setText(tb.shop_address);


        holder.item_shop_search_call_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", tb.shop_mobile, null));
               mContext. startActivity(intent);
            }
        });
            return convertView;
    }
}