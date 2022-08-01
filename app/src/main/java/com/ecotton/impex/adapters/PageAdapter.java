package com.ecotton.impex.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.ecotton.impex.fragments.ExportsFragment;
import com.ecotton.impex.fragments.GinningFragment;
import com.ecotton.impex.fragments.SpinningFragment;

public class PageAdapter extends FragmentPagerAdapter {

    int selected_Tab;

    public PageAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);

        selected_Tab = behavior;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0 : return new GinningFragment();
            case 1 : return new SpinningFragment();
            case 2 : return new ExportsFragment();
            default: return null;

        }

    }

    @Override
    public int getCount() {
        return selected_Tab;
    }
}
