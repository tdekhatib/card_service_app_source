package com.ma.hololstore.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ma.hololstore.Classes.Config;
import com.ma.hololstore.R;
import com.ma.hololstore.Classes.serial_class;

import java.util.ArrayList;

/**
 * Created by TDE on 12/5/2018.
 */

public class my_seller_adapter extends ArrayAdapter {
    private Context mContext;

    public ArrayList<serial_class> all_news;

    public my_seller_adapter(Context context, int textViewResourceId,
                             ArrayList<serial_class> TaskList2) {
        super(context, textViewResourceId, TaskList2);

          mContext = context;
        this.all_news = TaskList2;
    }


    private class ViewHolder {
        TextView item_card_name;
        TextView item_serial_value;
        TextView item_serial_company;
        TextView item_serial_date;

     }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


        ViewHolder holder = null;
        final serial_class tb = all_news.get(position);

        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.item_serials, null);

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
            holder.item_serial_company = (TextView) convertView.findViewById(R.id.item_serial_company);
            holder.item_serial_date = (TextView) convertView.findViewById(R.id.item_serial_data);
            holder.item_serial_value = (TextView) convertView.findViewById(R.id.item_serial_value);

        } catch (Exception ex) {
        }

        holder.item_card_name.setText(tb.card_name);
        holder.item_serial_value.setText("رقم البطاقة :"+tb.serial_value);
        holder.item_serial_company.setText("الرقم التسلسلي :" + tb.serial_company);

        holder.item_serial_date.setText(Config.GET_DATE(Long.valueOf(tb.serial_date)));

        return convertView;
    }
}