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

import com.stenisway.wan_android.databinding.FragmentLaterReadBinding;
import com.stenisway.wan_android.newitem.NewsAdapter;
import com.stenisway.wan_android.newitem.newsbean.New_Item;

import java.util.List;
import java.util.Objects;

public class LaterReadFragment extends Fragment {

    private LaterReadViewModel viewModel;

    private static LaterReadFragment laterReadFragment;

    public static LaterReadFragment getInstance() {
        if (laterReadFragment == null){
            laterReadFragment = new LaterReadFragment();
        }
        return laterReadFragment;
    }
    private FragmentLaterReadBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(LaterReadViewModel.class);
        binding = FragmentLaterReadBinding.inflate(LayoutInflater.from(requireContext()));
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        NewsAdapter adapter = new NewsAdapter(true, true);
        binding.rvLaterRead.setAdapter(adapter);
        binding.rvLaterRead.setLayoutManager(new LinearLayoutManager(requireContext()));
        viewModel.getLaterList().observe(getViewLifecycleOwner(), new_items -> {
            adapter.submitList(new_items);
            binding.rvLaterRead.scrollToPosition(0);

        });

    }

}