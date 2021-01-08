package com.ma.hololstore.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ma.hololstore.Classes.Config;
import com.ma.hololstore.R;
import com.ma.hololstore.Classes.service_class;

import java.util.ArrayList;

/**
 * Created by TDE on 12/5/2018.
 */

public class service_adapter extends ArrayAdapter {
    private Context mContext;

    public ArrayList<service_class> all_news;

    public service_adapter(Context context, int textViewResourceId,
                           ArrayList<service_class> TaskList2) {
        super(context, textViewResourceId, TaskList2);

          mContext = context;
        this.all_news = TaskList2;
    }


    private class ViewHolder {
        TextView item_service_name;
        TextView item_service_desc;
        TextView item_servcce_seller_price;
        TextView item_service_work;
        TextView  item_servcce_price;

    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        final service_class tb = all_news.get(position);
        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.item_service, null);
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

            holder.item_servcce_seller_price = (TextView) convertView.findViewById(R.id.item_servcce_seller_price);

            holder.item_service_desc = (TextView) convertView.findViewById(R.id.item_service_desc);
             holder.item_service_name = (TextView) convertView.findViewById(R.id.item_service_name);
            holder.item_service_work = (TextView) convertView.findViewById(R.id.item_servcce_work);
            holder.item_servcce_price = (TextView) convertView.findViewById(R.id.item_servcce_price);
        } catch (Exception ex) {
        }
        holder.item_service_name.setText(tb.service_name);
        holder.item_service_desc.setText(tb.service_desc);
        holder.item_servcce_seller_price.setText("السعر للتاجر : " + tb.service_seller_price+ " " + Config.coin_name);
        holder.item_service_work.setText("من الساعة " + tb.service_from +  " حتى الساعة "  +tb.service_to);
         holder.item_servcce_price.setText("السعر للزبون : " + tb.service_price + " " +Config.coin_name);
        return convertView;
    }
}