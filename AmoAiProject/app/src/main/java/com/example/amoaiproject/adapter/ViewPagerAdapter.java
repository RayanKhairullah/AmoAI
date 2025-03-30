package com.example.amoaiproject.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import com.example.amoaiproject.fragment.FragmentGenerateText;
import com.example.amoaiproject.fragment.FragmentImageRecognition;
import com.example.amoaiproject.fragment.FragmentHistory;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    public ViewPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new FragmentGenerateText();
            case 1:
                return new FragmentImageRecognition();
            case 2:
                return new FragmentHistory();
            default:
                return new FragmentGenerateText();
        }
    }

    @Override
    public int getCount() {
        return 3; // tiga tab: Generate Text, Generate Image, History
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Generate Text";
            case 1:
                return "Generate Image";
            case 2:
                return "History";
            default:
                return "";
        }
    }
}
