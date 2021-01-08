package com.ma.hololstore.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ma.hololstore.Classes.Config;
import com.ma.hololstore.R;
import com.ma.hololstore.Classes.request_class;

import java.util.ArrayList;

/**
 * Created by TDE on 12/5/2018.
 */

public class request_adapter extends ArrayAdapter {
    private Context mContext;

    public ArrayList<request_class> all_news;

    public request_adapter(Context context, int textViewResourceId,
                           ArrayList<request_class> TaskList2) {
        super(context, textViewResourceId, TaskList2);

          mContext = context;
        this.all_news = TaskList2;
    }


    private class ViewHolder {
        TextView item_request_name;
        TextView item_request_date;
        TextView item_request_money;
        TextView item_request_type;

     }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


        ViewHolder holder = null;
        final request_class tb = all_news.get(position);

        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.item_request, null);

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
            holder.item_request_date = (TextView) convertView.findViewById(R.id.item_request_date);
            holder.item_request_money = (TextView) convertView.findViewById(R.id.item_request_money);
            holder.item_request_name = (TextView) convertView.findViewById(R.id.item_request_name);
            holder.item_request_type = (TextView) convertView.findViewById(R.id.item_request_type);

        } catch (Exception ex) {
        }

        holder.item_request_name.setText(" الخدمة: "+tb.service_name);
        holder.item_request_date.setText(Config.GET_DATE(Long.valueOf(tb.request_date)));
        holder.item_request_money.setText("رقم الموبايل : " + (tb.request_mobile) + "\n" + "رقم الاشتراك : " + (tb.request_eshtrak)  );
        if (tb.request_type.equals("0")) {
            holder.item_request_type.setText("قيد الانجاز");
            holder.item_request_type.setBackgroundColor(mContext.getResources().getColor( R.color.orange));
        }
        else
            if (tb.request_type.equals("1"))
            {
                holder.item_request_type.setText("منفذه");
                holder.item_request_type.setBackgroundColor(mContext.getResources().getColor( R.color.green));

            }
            else
                if(tb.request_type.equals("2"))
                {
                    holder.item_request_type.setText("ملغاة");
                    holder.item_request_type.setBackgroundColor(mContext.getResources().getColor( R.color.red));

                }
        return convertView;
    }
}