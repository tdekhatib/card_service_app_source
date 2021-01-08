package com.ma.hololstore.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ma.hololstore.Classes.category_class;
import com.ma.hololstore.R;
import com.ma.hololstore.Server.SERVER_INFO2;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by TDE on 12/5/2018.
 */

public class category_adapter extends ArrayAdapter {
    private Context mContext;

    public ArrayList<category_class> all_news;

    public category_adapter(Context context, int textViewResourceId,
                            ArrayList<category_class> TaskList2) {
        super(context, textViewResourceId, TaskList2);

          mContext = context;
        this.all_news = TaskList2;
    }


    private class ViewHolder {
        TextView item_category_name;

         ImageView item_category_image;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


        ViewHolder holder = null;
        final category_class tb = all_news.get(position);

        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.item_category, null);

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
            holder.item_category_name = (TextView) convertView.findViewById(R.id.item_category_name);
             holder.item_category_image = (ImageView) convertView.findViewById(R.id.item_category_image);

        } catch (Exception ex) {
        }

        holder.item_category_name.setText(tb.category_name);
        Picasso.get().load( SERVER_INFO2.URL2+"files/" +tb.category_image)
                .into(holder.item_category_image);
       return convertView;
    }
}