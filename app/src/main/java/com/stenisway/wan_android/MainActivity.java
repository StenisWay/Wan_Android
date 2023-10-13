package com.stenisway.wan_android;

import android.annotation.SuppressLint;
import android.content.Context;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.stenisway.wan_android.categories.CategoriesFragment;
import com.stenisway.wan_android.databinding.ActivityMainBinding;
import com.stenisway.wan_android.favorite.FavoriteFragment;
import com.stenisway.wan_android.newitem.NewsFragment;
import com.stenisway.wan_android.newitem.newsbean.New_Item;
import com.stenisway.wan_android.search.SearchFragment;
import com.stenisway.wan_android.setting.SettingFragment;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private final String TAG = this.getClass().getName();
    private ActivityMainBinding binding;
    private Main_ViewModel viewModel;
    private androidx.appcompat.widget.SearchView.SearchAutoComplete searchAutoComplete;
    private androidx.appcompat.widget.SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this));
        viewModel = new ViewModelProvider(this).get(Main_ViewModel.class);
        setContentView(binding.getRoot());
        initFragment();
        bottom_init();
        getAllData();
//        createShortcut();

        viewModel.getDeleteItem().observe(this, new_items -> viewModel.deleteLocalUnnecessaryData(new_items.toArray(new New_Item[0])));

    }

    @Override
    protected void onStop() {
        viewModel.getNetSituation().removeObservers(this);
        super.onStop();
    }

    @Override
    protected void onStart() {
        viewModel.getNetSituation().observe(this, aBoolean -> {
            if (!aBoolean) {
                if (viewModel.getDisConnectIsShow()) {
                    return;
                }
                AlertDialog.Builder dialog = getDisConnectDialog();
                dialog.show();
                viewModel.setDisConnectIsShow(true);
            }
        });
        super.onStart();
    }

    @SuppressLint("NonConstantResourceId")
    private void bottom_init() {
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {

            switch (item.getItemId()) {
                case R.id.menu_News:
                    showFragment(NewsFragment.getInstance());

                    break;
                case R.id.menu_Settings:
                    showFragment(SettingFragment.getInstance());

                    break;
                case R.id.menu_Categories:
                    showFragment(CategoriesFragment.getInstance());
                    break;

                case R.id.menu_Myfavorite:
                    showFragment(FavoriteFragment.getInstance());
                    break;
            }

            return true;
        });
    }

    private void initFragment() {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_layout, NewsFragment.getInstance());
        transaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        searchView = initSearchView(menu);
        searchAutoComplete = initSearchAutoComplete(searchView);
        viewModel.getHkLocal().observe(this, strings -> {
            if (strings != null) {

                ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this, R.layout.search_item, viewModel.toHKStringArray(strings));
                Log.d(TAG + "adapter", adapter.toString());

                searchAutoComplete.setAdapter(adapter);
                searchAutoComplete.setOnItemClickListener((adapterView, view, i, l) -> {
                    TextView textView = view.findViewById(android.R.id.text1);
                    Log.d(TAG + "getSearchString", textView.getText().toString());
                    searchFragmentInstance(textView.getText().toString());
                    hideKeyBoard(searchAutoComplete);
                    searchView.clearFocus();
                    searchAutoComplete.setText("");
                });

            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private SearchView.SearchAutoComplete initSearchAutoComplete(SearchView sv) {
        SearchView.SearchAutoComplete searchAutoCompleteInit = sv.findViewById(androidx.appcompat.R.id.search_src_text);
        searchAutoCompleteInit.setDropDownVerticalOffset(11);
        searchAutoCompleteInit.setOnEditorActionListener((textView, keyAction, keyEvent) -> {
            if (keyAction == EditorInfo.IME_ACTION_SEARCH ||
                    (keyEvent != null && KeyEvent.KEYCODE_ENTER == keyEvent.getKeyCode()
                            && keyEvent.getAction() == KeyEvent.ACTION_DOWN)) {
                searchFragmentInstance(textView.getText().toString());
                searchView.clearFocus();
                searchAutoCompleteInit.setText("");
                return false;
            }
            return false;
        });
        return searchAutoCompleteInit;
    }

    private SearchView initSearchView(Menu menu) {
        SearchView searchViewInit = (androidx.appcompat.widget.SearchView) menu.findItem(R.id.app_bar_search).getActionView();
        searchViewInit.setMaxWidth(700);
        return searchViewInit;
    }

    private void searchFragmentInstance(String searchText) {
        Bundle bundle = new Bundle();
        bundle.putString("searchText", searchText);
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        manager.setFragmentResult("searchItem", bundle);
        transaction.replace(R.id.fragment_layout, SearchFragment.getInstance(), "SEARCH_FRAGMENT").addToBackStack("news");
        transaction.commit();
    }

    private AlertDialog.Builder getDisConnectDialog() {
        return new AlertDialog.Builder(this)
                .setTitle("網路連線失敗，請嘗試確認網路狀況之後再重新啟動")
                .setCancelable(false)
                .setPositiveButton("離線模式請按這裡~", (dialog, which) -> {
                    viewModel.setDisConnectIsShow(false);
                    dialog.dismiss();
                });
    }


    @Override
    public void onBackPressed() {

        SearchFragment searchFragment = (SearchFragment) getSupportFragmentManager().findFragmentByTag("SEARCH_FRAGMENT");
        if (searchFragment != null && searchFragment.isVisible()) {
            if (searchAutoComplete.isShown()) {
                searchAutoComplete.setText("");
                try {
                    Method method = searchView.getClass().getDeclaredMethod("onCloseClicked");
                    method.setAccessible(true);
                    method.invoke(searchView);
                } catch (NoSuchMethodException | InvocationTargetException |
                         IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        super.onBackPressed();
    }

    private void getAllData() {
        viewModel.getHkOnNet();
        viewModel.getCategoriesTitleOnNet();
        viewModel.getBannerDataOnNet();

    }

    private void hideKeyBoard(View view) {

        InputMethodManager inputMethodManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

    }

    private void showFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout, fragment).commit();
    }

    public void changeTitle(int stringId) {
        Objects.requireNonNull(getSupportActionBar()).setTitle(getString(stringId));
    }

    public void searchViewHide() {
        searchView.setVisibility(View.INVISIBLE);
    }

    public void searchViewShow() {
        searchView.setVisibility(View.VISIBLE);
    }


    //    private void createShortcut() {
//        SharedPreferences preferences = this.getPreferences(Context.MODE_PRIVATE);
//        if (!(preferences.getBoolean("IsCreateShortcut", false))){
//            Intent shortcutIntent = new Intent(Intent.ACTION_MAIN);
//            shortcutIntent.setComponent(new ComponentName("com.example.wan_android", "com.example.wan_android.MainActivity" ));
//            ShortcutManager shortcutManager = this.getSystemService(ShortcutManager.class);
//            if (shortcutManager != null) {
//                if (shortcutManager.isRequestPinShortcutSupported()) {
//                    ShortcutInfo shortcut = new ShortcutInfo.Builder(this, "wan_screen")
//                            .setShortLabel("WanAndroid")
//                            .setLongLabel("WanAndroid")
//                            .setIcon(Icon.createWithResource(this, R.drawable.wan_icon))
//                            .setIntent(shortcutIntent)
//                            .build();
//
//                    shortcutManager.requestPinShortcut(shortcut, null);
//                } else
//                    Toast.makeText(this, "Pinned shortcuts are not supported!", Toast.LENGTH_SHORT).show();
//            }
//        }else {
//            return;
//        }
//        preferences.edit().putBoolean("IsCreateShortcut", true).apply();
//
//    }

//    private void createShortCut(){
//        Intent shortcutIntent = new Intent(Intent.ACTION_MAIN);
//        shortcutIntent.setComponent(new ComponentName("com.example.wan_android", "com.example.wan_android.MainActivity" ));
//                // 设置目标活动
//        ShortcutManager shortcutManager = getSystemService(ShortcutManager.class);
//        // 创建ShortcutInfo
//        ShortcutInfo shortcut = new ShortcutInfo.Builder(this, "shortcutId")
//                .setShortLabel("WanAndroid")
//                .setLongLabel("WanAndroid")
//                .setIcon(Icon.createWithResource(this, R.drawable.wan_icon))
//                .setIntent(shortcutIntent)
//                .build();
//        shortcutManager.addDynamicShortcuts(Collections.singletonList(shortcut));
//
//    }

}