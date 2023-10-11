package com.stenisway.wan_android.newitem;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.stenisway.wan_android.R;
import com.stenisway.wan_android.banner.BannerAdapter;
import com.stenisway.wan_android.banner.bannerbean.Banner_Item;
import com.stenisway.wan_android.databinding.BannerItemBinding;
import com.stenisway.wan_android.databinding.NewsFooterItemBinding;
import com.stenisway.wan_android.databinding.NewsItemBinding;
import com.stenisway.wan_android.newitem.newsbean.New_Item;
import com.stenisway.wan_android.util.StringUtil;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class NewsAdapter extends ListAdapter<New_Item, RecyclerView.ViewHolder> {

    private final String TAG = this.getClass().getName();
    public static final int NEWS_NORMAL_ITEM = 0x1;
    public static final int NEWS_FOOTER_ITEM = 0x2;
    public static final int News_Banner = 0x3;
//    private NewsViewModel viewModel;
    private final StringUtil stringUtil;
    private List<Banner_Item> pic_list;

    public void setPic_list(List<Banner_Item> pic_list) {
        this.pic_list = pic_list;
    }

    private Boolean noBanner = false;

    private Boolean noProgress = false;

    public NewsAdapter(Boolean noBanner) {
        super(new ItemCallBack());
        if (noBanner) {
            this.noBanner = true;
        }
        stringUtil = new StringUtil();

    }

    private final Timer timer = new Timer();

    //如果將timer放在BindViewHolder的區塊，會導致重複建構timer導致自動跳轉同時多次執行
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == NEWS_NORMAL_ITEM) {
            NewsItemBinding binding = NewsItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new ItemViewHolder(binding);
        }
        if (viewType == News_Banner) {
            BannerItemBinding binding = BannerItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);

            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    new Handler(Looper.getMainLooper()).post(() -> {
                        int currentPosition = binding.vpBanner.getCurrentItem();
                        currentPosition++;
                        binding.vpBanner.setCurrentItem(currentPosition, true);

                    });
                }
            };
            timer.scheduleAtFixedRate(timerTask, 5000, 5000);
            //           (執行重複的區塊, 執行程式碼時先等候多久在開始, 之後每次執行的間隔時間)
            return new BannerViewHolder(binding);
        } else {
            NewsFooterItemBinding binding = NewsFooterItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new LoadingViewHolder(binding);
        }

    }

    @Override
    public int getItemViewType(int position) {

        if (noBanner) {
            if (position == getItemCount() - 1) {
                return NEWS_FOOTER_ITEM;
            } else {
                return NEWS_NORMAL_ITEM;
            }
        } else {
            if (position == getItemCount() - 1 && position != 0) {
                return NEWS_FOOTER_ITEM;
            } else if (position == 0) {
                return News_Banner;
            } else {
                return NEWS_NORMAL_ITEM;
            }
        }
    }

    @Override
    public int getItemCount() {
        int size = super.getItemCount();
        noProgress = size < 5;

        if (noBanner) {
            return size + 1;
        } else {
            return size + 2;
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        if (!noBanner) {
            Log.d(TAG + "position before", position + "");
            position = position - 1;
            Log.d(TAG + "position after", position + "");
        }


        if (holder.getItemViewType() == NEWS_NORMAL_ITEM) {
            New_Item item = getItem(position);
            ItemViewHolder myViewHolder = (ItemViewHolder) holder;
            String title = item.getTitle();
            if (stringUtil.isInvalid(title)){
                title = stringUtil.replaceInvalidChar(item.getTitle());
                myViewHolder.binding.txtTitle.setText(title);
            }
            myViewHolder.binding.txtTime.setText(item.getNiceDate());
            myViewHolder.binding.txtChapterName.setText(item.getChapterName());
            myViewHolder.binding.txtAuthor.setText(item.getAuthor());
            Log.e(TAG, item.getTitle());
            myViewHolder.itemView.setOnClickListener(view -> {
                Bundle bundle = new Bundle();
                bundle.putSerializable("newItemUrl", item);
                FragmentManager manager = ((AppCompatActivity) view.getContext()).getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                manager.setFragmentResult("newsItem", bundle);
                transaction.replace(R.id.fragment_layout, new NewsDetailFragment()).addToBackStack("news");
                transaction.commit();

            });
        } else if (holder.getItemViewType() == News_Banner) {
            BannerViewHolder bannerViewHolder = (BannerViewHolder) holder;
            if (pic_list != null) {
                BannerAdapter bannerAdapter = new BannerAdapter(pic_list);
                bannerViewHolder.binding.vpBanner.setAdapter(bannerAdapter);
                bannerViewHolder.binding.vpBanner.setCurrentItem(500);
            }


        } else {
            LoadingViewHolder myViewHolder2 = (LoadingViewHolder) holder;
            if (noProgress) {
                ((LoadingViewHolder) holder).binding.getRoot().setVisibility(View.GONE);
            } else {
                ((LoadingViewHolder) holder).binding.getRoot().setVisibility(View.VISIBLE);
                myViewHolder2.binding.txtLodingResult.setText(R.string.loading);
            }

        }

    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        timer.cancel();
        super.onDetachedFromRecyclerView(recyclerView);
    }

    public void goneProgress() {
        noProgress = true;
        this.notifyItemChanged(getItemCount());
    }

    private static class ItemViewHolder extends RecyclerView.ViewHolder {
        private final NewsItemBinding binding;

        public ItemViewHolder(@NonNull NewsItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }

    }

    private static class LoadingViewHolder extends RecyclerView.ViewHolder {
        private final NewsFooterItemBinding binding;

        public LoadingViewHolder(NewsFooterItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    private static class BannerViewHolder extends RecyclerView.ViewHolder {
        private final BannerItemBinding binding;

        public BannerViewHolder(BannerItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    private static class ItemCallBack extends DiffUtil.ItemCallback<New_Item> {

        @Override
        public boolean areItemsTheSame(@NonNull New_Item oldItem, @NonNull New_Item newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @SuppressLint("DiffUtilEquals")
        @Override
        public boolean areContentsTheSame(@NonNull New_Item oldItem, @NonNull New_Item newItem) {
            return oldItem == newItem;
        }
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
    }
}
