package com.stenisway.wan_android.categories;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.stenisway.wan_android.R;
import com.stenisway.wan_android.categories.categoriesbean.CgItem;
import com.stenisway.wan_android.categories.categoriesbean.CgTitle;
import com.stenisway.wan_android.databinding.CategoriesItemBinding;

import java.util.List;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.CgViewHolder> {

    private final List<CgTitle> cgTitles;
    private final List<CgItem> cgItems;

    private final String TAG = this.getClass().getName();

    public CategoriesAdapter(List<CgTitle> cgTitles, List<CgItem> cgItems) {
        this.cgItems = cgItems;
        this.cgTitles = cgTitles;

    }

    @NonNull
    @Override
    public CgViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CategoriesItemBinding binding = CategoriesItemBinding.inflate(LayoutInflater.from(parent.getContext()));
        return new CgViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CgViewHolder holder, int position) {

        CgTitle detail = cgTitles.get(position);
        holder.binding.txtCgtitle.setText(detail.name);
        holder.binding.cgLayout.removeAllViews();
        int itemSize = cgItems.size();
        for (int i = 0; i < itemSize; i++){
            CgItem item = cgItems.get(i);
            if (item.getParentChapterId() == detail.getId()){
                TextView cgButton = createButton(holder.itemView.getContext(), item);
                holder.binding.cgLayout.addView(cgButton);
            }
        }

    }

    private TextView createButton(Context context, CgItem item){

        TextView button = new TextView(context);
        button.setText(item.getName());
        button.setTextSize(16);
        button.setSingleLine();

        button.setPadding(0, 5, 0, 0);
        button.setGravity(Gravity.CENTER);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 50);
        ViewGroup.MarginLayoutParams marginLayoutParams = new ViewGroup.MarginLayoutParams(layoutParams);
        marginLayoutParams.setMargins(30, 30, 30, 30);
        button.setLayoutParams(marginLayoutParams);

        Log.d(TAG + "buttonMargin", marginLayoutParams.leftMargin+"");

        button.setBackgroundResource(R.drawable.black_shap);
        button.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            bundle.putInt("id", item.getId());
            Log.d("itemID", item.getId()+"");
            FragmentManager manager = ((AppCompatActivity)view.getContext()).getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            manager.setFragmentResult("categoriesItem", bundle);
            transaction.replace(R.id.fragment_layout, CategoriesDetailFragment.getInstance()).addToBackStack("categories");
            transaction.commit();
        });

        return button;
    }

    @Override
    public int getItemCount() {
        return cgTitles.size();
    }

    static class CgViewHolder extends RecyclerView.ViewHolder{

        public CategoriesItemBinding binding;

        public CgViewHolder(CategoriesItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}
