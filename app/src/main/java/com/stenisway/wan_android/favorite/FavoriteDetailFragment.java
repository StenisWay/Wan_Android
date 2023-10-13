package com.stenisway.wan_android.favorite;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.stenisway.wan_android.databinding.FragmentFavoriteDetailFaragmentBinding;
import com.stenisway.wan_android.newitem.NewsAdapter;
import com.stenisway.wan_android.newitem.newsbean.New_Item;

import java.util.List;

public class FavoriteDetailFragment extends Fragment {

    private FavoriteDetailFragmentViewModel mViewModel;
    private FragmentFavoriteDetailFaragmentBinding binding;

    private static FavoriteDetailFragment favoriteDetailFragment;

    public static FavoriteDetailFragment getInstance() {
        if (favoriteDetailFragment == null){
            favoriteDetailFragment = new FavoriteDetailFragment();
        }
        return favoriteDetailFragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(FavoriteDetailFragmentViewModel.class);
        binding = FragmentFavoriteDetailFaragmentBinding.inflate(LayoutInflater.from(requireContext()));
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        NewsAdapter adapter = new NewsAdapter(true, true);
        binding.rvFavorite.setAdapter(adapter);
        binding.rvFavorite.setLayoutManager(new LinearLayoutManager(requireContext()));
        mViewModel.getFavoriteList().observe(getViewLifecycleOwner(), new_items -> {
            adapter.submitList(new_items);
            binding.rvFavorite.scrollToPosition(0);
        });

    }


}