package com.stenisway.wan_android.categories;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stenisway.wan_android.MainActivity;
import com.stenisway.wan_android.R;
import com.stenisway.wan_android.categories.categoriesbean.CgItem;
import com.stenisway.wan_android.categories.categoriesbean.CgTitle;
import com.stenisway.wan_android.databinding.FragmentCategoriesBinding;

import java.util.List;

public class CategoriesFragment extends Fragment {

    public static CategoriesFragment getInstance() {

        return new CategoriesFragment();
    }
    private CategoriesViewModel mViewModel;
    private FragmentCategoriesBinding binding;

    private MainActivity activity;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(CategoriesViewModel.class);
        binding = FragmentCategoriesBinding.inflate(LayoutInflater.from(requireContext()));
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activity = (MainActivity) requireActivity();
        binding.rvCglist.setLayoutManager(new LinearLayoutManager(requireContext()));

        mViewModel.getCgTitles().observe(getViewLifecycleOwner(), new Observer<List<CgTitle>>() {
            @Override
            public void onChanged(List<CgTitle> cgTitles) {
                mViewModel.getCgItem().observe(getViewLifecycleOwner(), new Observer<List<CgItem>>() {
                    @Override
                    public void onChanged(List<CgItem> cgItems) {
                        CategoriesAdapter adapter = new CategoriesAdapter(cgTitles, cgItems);
                        binding.rvCglist.setAdapter(adapter);
                    }
                });
            }
        });


    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        activity.changeTitle(R.string.Component);
    }


}