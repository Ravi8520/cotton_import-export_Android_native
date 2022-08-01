package com.ecotton.impex.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.ecotton.impex.fragments.CompanyDetailFragment;
import com.ecotton.impex.fragments.PersonalDetailFragment;

public class ProfilePageAdapter extends FragmentPagerAdapter {

    int selected_Tab;

    public ProfilePageAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        selected_Tab = behavior;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new PersonalDetailFragment();
            case 1:
                return new CompanyDetailFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return selected_Tab;
    }
}
