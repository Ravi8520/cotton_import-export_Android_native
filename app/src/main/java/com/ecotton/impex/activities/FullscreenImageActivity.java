package com.ecotton.impex.activities;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.ecotton.impex.adapters.PhotoViewAdapter;
import com.ecotton.impex.databinding.ActivityFullscreenImageBinding;
import com.ecotton.impex.utils.ValidationUtil;

import java.util.ArrayList;

public class FullscreenImageActivity extends AppCompatActivity {

    String img;
    ActivityFullscreenImageBinding binding;
    PhotoViewAdapter mPhotoViewAdapter;
    public static final String ITEMS = "items";
    public static final String SINGLE_ITEM = "image";
    public static final String POSITION = "pos";
    FullscreenImageActivity mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFullscreenImageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mContext = this;


        binding.backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            finish();
            return;
        }

        ArrayList<String> mImageList = new ArrayList<>();

        if (bundle.getStringArrayList(ITEMS) != null) {
            mImageList.addAll(bundle.getStringArrayList(ITEMS));
        } else if (ValidationUtil.validateString(bundle.getString(SINGLE_ITEM, ""))) {
            mImageList.add(bundle.getString(SINGLE_ITEM, ""));
        }

        mPhotoViewAdapter = new PhotoViewAdapter(mContext, mImageList);
        binding.mViewPager.setAdapter(mPhotoViewAdapter);

        if (bundle.getInt(POSITION, -1) != -1) {
            binding.mViewPager.setCurrentItem(bundle.getInt(POSITION));
        }
    }


    /*Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            img = bundle.getString("image");
            Glide.with(getApplicationContext())
                    .load(img)
                    .apply(AppUtil.getPlaceholderRequestOption())
                    .into(binding.ivFullscreen);
        }*/
}