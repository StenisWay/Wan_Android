package com.stenisway.wan_android.search;

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

import com.stenisway.wan_android.R;
import com.stenisway.wan_android.databinding.FragmentSearchBinding;
import com.stenisway.wan_android.newitem.NewsAdapter;
import com.stenisway.wan_android.ui.BaseNextFragment;

import java.util.ArrayList;
import java.util.Objects;

public class SearchFragment extends BaseNextFragment {

    private SearchViewModel viewModel;

    public static SearchFragment getInstance() {
        return new SearchFragment();
    }

    private final String TAG = this.getClass().getName();

    private FragmentSearchBinding binding;

    private String keyWord;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentSearchBinding.inflate(LayoutInflater.from(requireContext()));
        viewModel = new ViewModelProvider(this).get(SearchViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FragmentManager manager = getParentFragmentManager();
        manager.setFragmentResultListener("searchItem", this, (requestKey, result) -> {
            keyWord = result.getString("searchText");
            viewModel.getSearchData(keyWord);
        });

        NewsAdapter searchNewsAdapter = new NewsAdapter(true);
        binding.rvSearchList.setAdapter(searchNewsAdapter);
        binding.rvSearchList.setLayoutManager(new LinearLayoutManager(requireContext()));

        viewModel.getSearch().observe(getViewLifecycleOwner(), items -> {
            Log.d("data", Objects.requireNonNull(viewModel.getSearch().getValue()).get(0).getTitle());
            searchNewsAdapter.submitList(viewModel.getSearch().getValue());
        });


        viewModel.getSearch().observe(getViewLifecycleOwner(), items -> {
            searchNewsAdapter.submitList(new ArrayList<>(items), () -> Log.d(TAG + "Observer", "finish submit list"));
            if (viewModel.needToScrollToTop){
                binding.rvSearchList.scrollToPosition(0);
                viewModel.needToScrollToTop = false;
            }
        });

        binding.rvSearchList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                Log.d(TAG + "SearchItemChange", "onScrollStateChanged: " + newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy < 0 ) return;
                LinearLayoutManager manager = (LinearLayoutManager) binding.rvSearchList.getLayoutManager();

                assert manager != null;
                int position = manager.findLastVisibleItemPosition();
                if (position == searchNewsAdapter.getItemCount() - 1){
                    viewModel.getSearchData(keyWord);
                }
            }
        });

    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        changeTitle(R.string.search_result);
    }


}