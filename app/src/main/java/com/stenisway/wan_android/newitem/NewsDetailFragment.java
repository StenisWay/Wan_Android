package com.stenisway.wan_android.newitem;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuHost;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;

import com.stenisway.wan_android.R;
import com.stenisway.wan_android.databinding.FragmentNewsDetailBinding;
import com.stenisway.wan_android.newitem.newsbean.New_Item;
import com.stenisway.wan_android.ui.BaseNextFragment;
import com.just.agentweb.AgentWeb;

import java.util.Objects;

public class NewsDetailFragment extends BaseNextFragment {

    private NewsDetailViewModel viewModel;
    private FragmentNewsDetailBinding binding;
    private AgentWeb mAgentWeb;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNewsDetailBinding.inflate(LayoutInflater.from(requireContext()), container, false);
        viewModel = new ViewModelProvider(this).get(NewsDetailViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FragmentManager manager = getParentFragmentManager();
        manager.setFragmentResultListener("newsItem", this, (requestKey, result) -> {

            New_Item new_item = (New_Item) result.getSerializable("newItemUrl");

            mAgentWeb = AgentWeb.with(requireActivity())
                    .setAgentWebParent(binding.webViewContainer,
                            new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
                    .useDefaultIndicator()
                    .createAgentWeb()
                    .ready()
                    .go(new_item.getLink());
            new_item.setLaterRead(false);
            new_item.setFavorite(false);
            viewModel.setNetItem(new_item);
            setmenu(requireActivity());
        });

        viewModel.getNetItem().observe(getViewLifecycleOwner(), item -> viewModel.getItemFromLocal(item.getId()));

    }

    @Override
    public void onPause() {
        mAgentWeb.getWebLifeCycle().onPause();
        super.onPause();
    }

    @Override
    public void onResume() {
        mAgentWeb.getWebLifeCycle().onResume();
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        mAgentWeb.getWebLifeCycle().onDestroy();
        super.onDestroyView();
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        changeTitle(R.string.content);
    }

    private void setmenu(MenuHost menuHost) {
        menuHost.addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.detail_menu, menu);

                MenuItem itemSearch = menu.findItem(R.id.app_bar_search);
                itemSearch.setVisible(false);

                MenuItem itemFavorite = menu.findItem(R.id.menu_favortie);
                MenuItem itemLaterRead = menu.findItem(R.id.menu_later);


                viewModel.getLocalItem().observe(getViewLifecycleOwner(), item -> {
                    if (item != null) {
                        if (item.getFavorite()) {
                            Objects.requireNonNull(viewModel.getNetItem().getValue()).setFavorite(true);
                            itemFavorite.setIcon(R.drawable.baseline_favorite_red_24);
                        } else {
                            itemFavorite.setIcon(R.drawable.baseline_favorite_white_24);
                        }
                        if (item.getLaterRead()) {
                            Objects.requireNonNull(viewModel.getNetItem().getValue()).setLaterRead(true);
                            itemLaterRead.setIcon(R.drawable.baseline_watch_later_orange_24);
                        } else {
                            itemLaterRead.setIcon(R.drawable.baseline_watch_later_white_24);

                        }
                    }
                });
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()) {

                    case R.id.menu_favortie:
                        if (viewModel.getLocalItem().getValue() == null) {
                            Objects.requireNonNull(viewModel.getNetItem().getValue()).setFavorite(true);
                            viewModel.insertNewItem();
                        } else {
                            Objects.requireNonNull(viewModel.getNetItem().getValue()).setFavorite(!Objects.requireNonNull(viewModel.getNetItem().getValue()).getFavorite());
                            viewModel.getLocalItem().getValue().setFavorite(false);
                            viewModel.updateNewItem();
                        }
                        break;
                    case R.id.menu_later:
                        if (viewModel.getLocalItem().getValue() == null) {
                            Objects.requireNonNull(viewModel.getNetItem().getValue()).setLaterRead(true);
                            viewModel.insertNewItem();
                        } else {
                            Objects.requireNonNull(viewModel.getNetItem().getValue()).setLaterRead(!Objects.requireNonNull(viewModel.getNetItem().getValue()).getLaterRead());
                            viewModel.getLocalItem().getValue().setLaterRead(false);
                            viewModel.updateNewItem();
                        }
                        break;
                }
                return false;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);

    }

}