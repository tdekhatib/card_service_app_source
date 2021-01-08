package com.ma.hololstore.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ma.hololstore.Classes.slider_class;
import com.ma.hololstore.R;
import com.ma.hololstore.Server.SERVER_INFO2;
import com.smarteist.autoimageslider.SliderViewAdapter;

 import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SliderAdapter extends
        SliderViewAdapter<SliderAdapter.SliderAdapterVH> {
    private Context context;
    private ArrayList<slider_class> mSliderItems = new ArrayList<>();
    public SliderAdapter(Context context , ArrayList<slider_class> sliderItems) {
        this.mSliderItems = sliderItems;
        this.context = context;
        notifyDataSetChanged();
    }
    public SliderAdapter() {
    }
    public void renewItems(ArrayList<slider_class> sliderItems) {
        this.mSliderItems = sliderItems;
        notifyDataSetChanged();
    }
    public void deleteItem(int position) {
        this.mSliderItems.remove(position);
        notifyDataSetChanged();
    }
    public void addItem(slider_class sliderItem) {
        this.mSliderItems.add(sliderItem);
        notifyDataSetChanged();
    }
    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_slider, null);
        return new SliderAdapterVH(inflate);
    }

    @Override
    public void onBindViewHolder(SliderAdapterVH viewHolder, final int position) {

       final slider_class sliderItem = mSliderItems.get(position);

         Picasso.get()
                .load(SERVER_INFO2.IMAGE_SERVER+ sliderItem.slider_image)
                .into(viewHolder.imageViewBackground);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             try {
                 Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(sliderItem.slider_url));
                 context.startActivity(browserIntent);
             }catch (Exception ex){}
             }
        });
    }

    @Override
    public int getCount() {
        //slider view count could be dynamic size
        return mSliderItems.size();
    }

     class SliderAdapterVH extends SliderViewAdapter.ViewHolder {

        View itemView;
        ImageView imageViewBackground;

        public SliderAdapterVH(View itemView) {
            super(itemView);
            imageViewBackground = itemView.findViewById(R.id.img_slider);
             this.itemView = itemView;
        }
    }

}
