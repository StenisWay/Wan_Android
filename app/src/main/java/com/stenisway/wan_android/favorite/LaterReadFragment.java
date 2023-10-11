package com.stenisway.wan_android.favorite;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.stenisway.wan_android.databinding.FragmentLaterReadBinding;
import com.stenisway.wan_android.newitem.NewsAdapter;

public class LaterReadFragment extends Fragment {

    private LaterReadViewModel mViewModel;

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
        mViewModel = new ViewModelProvider(this).get(LaterReadViewModel.class);
        binding = FragmentLaterReadBinding.inflate(LayoutInflater.from(requireContext()));
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        NewsAdapter adapter = new NewsAdapter(true);
        adapter.goneProgress();
        binding.rvLaterRead.setAdapter(adapter);
        binding.rvLaterRead.setLayoutManager(new LinearLayoutManager(requireContext()));

        mViewModel.getLaterList().observe(getViewLifecycleOwner(), adapter::submitList);

    }

}