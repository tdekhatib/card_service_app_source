package com.ma.hololstore.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ma.hololstore.Classes.Config;
import com.ma.hololstore.Classes.card_class;
import com.ma.hololstore.R;
import com.ma.hololstore.Server.SERVER_INFO2;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by TDE on 12/5/2018.
 */

public class card_adapter extends ArrayAdapter {
    private Context mContext;

    public ArrayList<card_class> all_news;

    public card_adapter(Context context, int textViewResourceId,
                        ArrayList<card_class> TaskList2) {
        super(context, textViewResourceId, TaskList2);

          mContext = context;
        this.all_news = TaskList2;
    }


    private class ViewHolder {
        TextView item_card_name;
         TextView item_card_buyer_price;

         ImageView item_card_image;

    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


        ViewHolder holder = null;
        final card_class tb = all_news.get(position);

        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.item_cards, null);

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
             holder.item_card_image = (ImageView) convertView.findViewById(R.id.item_card_image);
             holder.item_card_buyer_price = (TextView) convertView.findViewById(R.id.item_card_buyer_price);

        } catch (Exception ex) {
        }

        holder.item_card_name.setText(tb.card_name);
        Picasso.get().load( SERVER_INFO2.URL2+"files/" +tb.card_image)
                .into(holder.item_card_image);
        if (Config.shop_price == 0)
         holder.item_card_buyer_price.setText("السعر " + tb.card_seller_price + " دينار "   );

        else        if (Config.shop_price == 1)

            holder.item_card_buyer_price.setText("السعر " + tb.card_buyer_price + " دينار "   );

            return convertView;
    }
}