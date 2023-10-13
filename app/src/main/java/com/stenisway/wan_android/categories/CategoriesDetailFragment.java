package com.stenisway.wan_android.categories;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.stenisway.wan_android.databinding.FragmentCategoriesDetailBinding;
import com.stenisway.wan_android.newitem.NewsAdapter;
import com.stenisway.wan_android.ui.BaseNextFragment;

import java.util.ArrayList;
import java.util.Objects;

public class CategoriesDetailFragment extends BaseNextFragment {

    private CategoriesDetailViewModel viewModel;
    private FragmentCategoriesDetailBinding binding;

    private final String TAG = this.getClass().getName();

    public static CategoriesDetailFragment getInstance() {

        return new CategoriesDetailFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(CategoriesDetailViewModel.class);
        binding = FragmentCategoriesDetailBinding.inflate(LayoutInflater.from(requireContext()));
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FragmentManager manager = getParentFragmentManager();
        manager.setFragmentResultListener("categoriesItem", this, (requestKey, result) -> viewModel.setCategoriesId(result.getInt("id")));

        viewModel.getCategoriesId().observe(getViewLifecycleOwner(), integer -> viewModel.getCategoriesData(integer));

        NewsAdapter adapter = new NewsAdapter(true);
        binding.rvCategories.setAdapter(adapter);
        binding.rvCategories.setLayoutManager(new LinearLayoutManager(requireContext()));

        viewModel.getCategoriesDetails().observe(getViewLifecycleOwner(), items -> {

            adapter.submitList(new ArrayList<>(items));
            if (viewModel.needToScrollToTop) {
                binding.rvCategories.scrollToPosition(0);
                viewModel.needToScrollToTop = false;
            }

        });

        viewModel.getNullItem().observe(getViewLifecycleOwner(), aBoolean -> {
            if (aBoolean) {

                binding.rvCategories.setVisibility(View.GONE);
                binding.txtNoData.setVisibility(View.VISIBLE);

            }
        });


        binding.rvCategories.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                Log.d(TAG + "newItemChange", "onScrollStateChanged: " + newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            //已解決，沒有載入的物件，progress一樣在運轉
                if (viewModel.needToScrollToTop){
                    binding.rvCategories.scrollToPosition(0);
                    viewModel.needToScrollToTop = false;
                }else {
                    if (dy < 0 ) return;
                    LinearLayoutManager manager = (LinearLayoutManager) binding.rvCategories.getLayoutManager();
                    assert manager != null;
                    int position = manager.findLastVisibleItemPosition();
                    if (position == adapter.getItemCount() - 1){
                        int id = Objects.requireNonNull(viewModel.getCategoriesId().getValue());
                        viewModel.getCategoriesData(id);
                        if (viewModel.getIsLastPage()){
                            Log.d(TAG + "islast", viewModel.getIsLastPage() + "");
                            View view = binding.rvCategories.getLayoutManager().findViewByPosition(adapter.getItemCount()-1);
                            assert view != null;
                            view.setVisibility(View.GONE);
                        }
                        Log.d(TAG + "到底讀取資料", "讀取成功");
                        Log.d(TAG + "是否可以滑動", viewModel.needToScrollToTop + "");
                    }
                }

            }
        });

    }

}