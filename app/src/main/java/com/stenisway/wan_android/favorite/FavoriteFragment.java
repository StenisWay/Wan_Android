package com.stenisway.wan_android.favorite;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.stenisway.wan_android.MainActivity;
import com.stenisway.wan_android.R;
import com.stenisway.wan_android.databinding.FragmentFavoriteBinding;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.Arrays;

public class FavoriteFragment extends Fragment {

    private FavoriteViewModel mViewModel;
    private FragmentFavoriteBinding binding;

    public static FavoriteFragment getInstance() {
        return new FavoriteFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentFavoriteBinding.inflate(LayoutInflater.from(requireContext()));
        mViewModel = new ViewModelProvider(this).get(FavoriteViewModel.class);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mViewModel.setTabTitles(Arrays.asList(getString(R.string.favorite_article), getString(R.string.latter_read)));
        binding.vpFavorite.setAdapter(new FragmentStateAdapter(getChildFragmentManager(), getLifecycle()) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {

                switch (position){

                    case 0:
                        return new FavoriteDetailFragment();

                    case 1:
                        return new LaterReadFragment();

                }

                return null;

            }

            @Override
            public int getItemCount() {
                return mViewModel.getTabTitles().size();
            }
        });
//

        binding.vpFavorite.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });

        new TabLayoutMediator(
                binding.tlFavorite,
                binding.vpFavorite,
                (tab, position) -> tab.setText(mViewModel.getTabTitles().get(position))
                ).attach();

    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        MainActivity activity = (MainActivity) requireActivity();
        activity.changeTitle(R.string.favorite_article);
    }
}