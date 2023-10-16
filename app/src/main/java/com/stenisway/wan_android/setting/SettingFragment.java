package com.stenisway.wan_android.setting;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.stenisway.wan_android.MainActivity;
import com.stenisway.wan_android.R;
import com.stenisway.wan_android.databinding.FragmentSettingBinding;

public class SettingFragment extends Fragment {

    private FragmentSettingBinding binding;

    private   MainActivity activity ;

    public static SettingFragment getInstance() {
        return new SettingFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        activity = (MainActivity) requireActivity();
        binding = FragmentSettingBinding.inflate(LayoutInflater.from(requireContext()));
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.custViewSendadvice.setOnClickListener(view1 -> sentEmail(requireContext()));

        binding.txtThankTeacherHuang.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://sites.google.com/site/ronforwork/Home/android-2"));
            startActivity(intent);
        });

    }
    public void sentEmail(Context context){
        final String emailUrl = "qazse753753@gmail.com";
        final String emailSubject = "Wan Android Advices";
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setType("text/html");
        intent.putExtra(Intent.EXTRA_EMAIL, emailUrl);
        intent.putExtra(Intent.EXTRA_SUBJECT, emailSubject);
        context.startActivity(intent);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        activity.changeTitle(R.string.settings);
    }

}