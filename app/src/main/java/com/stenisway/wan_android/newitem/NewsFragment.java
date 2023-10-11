package com.stenisway.wan_android.newitem;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.stenisway.wan_android.MainActivity;
import com.stenisway.wan_android.R;
import com.stenisway.wan_android.databinding.FragmentNewsBinding;

import java.util.ArrayList;

public class NewsFragment extends Fragment {

    private NewsViewModel viewModel;
    private FragmentNewsBinding binding;
    private NewsAdapter adapter;

    private final String TAG = this.getClass().getName();

    public NewsFragment() {
    }
    public static NewsFragment getInstance() {

        return new NewsFragment();

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentNewsBinding.inflate(LayoutInflater.from(requireContext()));
        viewModel = new ViewModelProvider(this).get(NewsViewModel.class);
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setRecycleView();

    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        MainActivity activity = (MainActivity) requireActivity();
        activity.changeTitle(R.string.News);
    }

    private void setRecycleView(){

        adapter = new NewsAdapter(false);

        binding.recycleNews.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recycleNews.setAdapter(adapter);

        viewModel.getNews().observe(getViewLifecycleOwner(), items -> adapter.submitList(new ArrayList<>(items), () ->

                viewModel.getBanner().observe(getViewLifecycleOwner(), banner_items -> {
                    adapter.setPic_list(banner_items);
                    adapter.notifyItemChanged(0);
                })

        ));


        binding.recycleNews.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                Log.d(TAG + "newItemChange", "onScrollStateChanged: " + newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (viewModel.needToScrollToTop){
                    binding.recycleNews.scrollToPosition(0);
                    viewModel.needToScrollToTop = false;
                }else {
                    if (dy < 0 ) return;
                    LinearLayoutManager manager = (LinearLayoutManager) binding.recycleNews.getLayoutManager();
                    assert manager != null;
                    int position = manager.findLastVisibleItemPosition();
                    if (position == adapter.getItemCount() - 1){
                        viewModel.getNewsData();
                        Log.d(TAG + "到底讀取資料", "讀取成功");
                        Log.d(TAG + "是否可以滑動", viewModel.needToScrollToTop + "");
                    }
                }
            }
        });

    }

    @Override
    public void onDestroyView() {
//        快速切換fragment會發生lifeowner是null的情況，因此在destroy時，將觀察移除
        viewModel.getBanner().removeObservers(getViewLifecycleOwner());
        super.onDestroyView();


    }
}
