package com.ma.hololstore.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ma.hololstore.Classes.Config;
import com.ma.hololstore.Classes.comments_class;
import com.ma.hololstore.R;

import java.util.ArrayList;

/**
 * Created by TDE on 12/5/2018.
 */

public class comments_adapter extends ArrayAdapter {
    private Context mContext;

    public ArrayList<comments_class> all_news;

    public comments_adapter(Context context, int textViewResourceId,
                            ArrayList<comments_class> TaskList2) {
        super(context, textViewResourceId, TaskList2);

          mContext = context;
        this.all_news = TaskList2;
    }


    private class ViewHolder {
        TextView item_support_name;

        TextView item_support_type;

     }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


        ViewHolder holder = null;
        final comments_class tb = all_news.get(position);

        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.item_comments, null);

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

            holder.item_support_name = (TextView) convertView.findViewById(R.id.item_comment_name);
            holder.item_support_type = (TextView) convertView.findViewById(R.id.item_support_type);

        } catch (Exception ex) {
        }

        holder.item_support_name.setText(tb.shop_name + " : "+tb.commet_desc);
          holder.item_support_type.setText(Config.GET_DATE(Long.valueOf(tb.commet_date)));

        return convertView;
    }
}