package com.ma.hololstore.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ma.hololstore.Classes.Config;
import com.ma.hololstore.Classes.support_class;
import com.ma.hololstore.R;

import java.util.ArrayList;

/**
 * Created by TDE on 12/5/2018.
 */

public class support_adapter extends ArrayAdapter {
    private Context mContext;

    public ArrayList<support_class> all_news;

    public support_adapter(Context context, int textViewResourceId,
                           ArrayList<support_class> TaskList2) {
        super(context, textViewResourceId, TaskList2);

          mContext = context;
        this.all_news = TaskList2;
    }


    private class ViewHolder {
        TextView item_support_name;
        TextView item_support_date;
        TextView item_support_desc;
        TextView item_support_type;

     }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


        ViewHolder holder = null;
        final support_class tb = all_news.get(position);

        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.item_supports, null);

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
            holder.item_support_date = (TextView) convertView.findViewById(R.id.item_support_date);
            holder.item_support_desc = (TextView) convertView.findViewById(R.id.item_support_desc);
            holder.item_support_name = (TextView) convertView.findViewById(R.id.item_support_name);
            holder.item_support_type = (TextView) convertView.findViewById(R.id.item_support_type);

        } catch (Exception ex) {
        }

        holder.item_support_name.setText(tb.support_name);
        holder.item_support_desc.setText(tb.support_desc);
        holder.item_support_date.setText(Config.GET_DATE(Long.valueOf(tb.support_date)));
        if (tb.support_type.equals("0")) {
            holder.item_support_type.setText("مفتوحة");
            holder.item_support_type.setBackgroundColor(mContext.getResources().getColor( R.color.orange));
        }
        else
            if (tb.support_type.equals("1"))
            {
                holder.item_support_type.setText("محلولة");
                holder.item_support_type.setBackgroundColor(mContext.getResources().getColor( R.color.green));

            }
            else
                if(tb.support_type.equals("2"))
                {
                    holder.item_support_type.setText("ملغاة");
                    holder.item_support_type.setBackgroundColor(mContext.getResources().getColor( R.color.red));

                }
        return convertView;
    }
}