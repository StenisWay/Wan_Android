package com.stenisway.wan_android.banner;

import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.stenisway.wan_android.banner.bannerbean.Banner_Item;
import com.stenisway.wan_android.databinding.BannerPictureBinding;

import java.util.List;

public class BannerAdapter extends RecyclerView.Adapter<BannerAdapter.BannerViewHolder> {

    private final List<Banner_Item> picList;

    private final String TAG = this.getClass().getName();


    public BannerAdapter(List<Banner_Item> picList){
        this.picList = picList;
        Log.d(TAG + "piclist_items", "BannerAdapter: " + picList.toString());
    }

    @NonNull
    @Override
    public BannerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        BannerPictureBinding binding = BannerPictureBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new BannerViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull BannerViewHolder holder, int position) {

        if (picList.size() != 0){
            int i = position % picList.size();
            Log.d(TAG + "picUrl", picList.get(i).getImagePath());

            Glide.with(holder.binding.getRoot())
                    .load(Uri.parse(picList.get(i).getImagePath()))

                    .into(holder.binding.bannerPicture);
        }



    }

    @Override
    public int getItemCount() {
        return Integer.MAX_VALUE;
    }

    static class BannerViewHolder extends RecyclerView.ViewHolder{

        private final BannerPictureBinding binding;

        public BannerViewHolder(BannerPictureBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}
