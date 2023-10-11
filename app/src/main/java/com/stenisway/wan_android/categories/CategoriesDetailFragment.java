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

    private CategoriesDetailViewModel mViewModel;
    private FragmentCategoriesDetailBinding binding;

    private final String TAG = this.getClass().getName();

    public static CategoriesDetailFragment getInstance() {

        return new CategoriesDetailFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(CategoriesDetailViewModel.class);
        binding = FragmentCategoriesDetailBinding.inflate(LayoutInflater.from(requireContext()));
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FragmentManager manager = getParentFragmentManager();
        manager.setFragmentResultListener("categoriesItem", this, (requestKey, result) -> mViewModel.setCategoriesId(result.getInt("id")));

        mViewModel.getCategoriesId().observe(getViewLifecycleOwner(), integer -> mViewModel.getCategoriesData(integer));

        NewsAdapter adapter = new NewsAdapter(true);
        binding.rvCategories.setAdapter(adapter);
        binding.rvCategories.setLayoutManager(new LinearLayoutManager(requireContext()));

        mViewModel.getCategoriesDetails().observe(getViewLifecycleOwner(), items -> {
            adapter.submitList(new ArrayList<>(items));
            if (mViewModel.needToScrollToTop) {
                binding.rvCategories.scrollToPosition(0);
                mViewModel.needToScrollToTop = false;
            }

        });

        mViewModel.getIsLastPage().observe(getViewLifecycleOwner(), aBoolean -> {

            if (aBoolean) {
                adapter.goneProgress();
            }

        });

        mViewModel.getNullItem().observe(getViewLifecycleOwner(), aBoolean -> {
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
                if (dy < 0) return;
                LinearLayoutManager manager = (LinearLayoutManager) binding.rvCategories.getLayoutManager();

                assert manager != null;
                int position = manager.findLastVisibleItemPosition();
                if (position == adapter.getItemCount() - 1) {

                    if (mViewModel.getCategoriesDetails().getValue() != null && !(mViewModel.getCategoriesDetails().getValue().isEmpty())) {
                        int size = Objects.requireNonNull(mViewModel.getCategoriesDetails().getValue()).size();
                        int id = Objects.requireNonNull(mViewModel.getCategoriesId().getValue());
                        if (id != -1 && (size > 6)) {
                            mViewModel.getCategoriesData(id);
                        }
                    }
                }
            }
        });

    }

}