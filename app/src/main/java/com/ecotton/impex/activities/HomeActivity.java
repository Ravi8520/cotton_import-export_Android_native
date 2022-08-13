package com.ecotton.impex.activities;

import static com.ecotton.impex.MyApp.filterRequest;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.ecotton.impex.R;
import com.ecotton.impex.databinding.ActivityHomeBinding;
import com.ecotton.impex.fragments.HomeFragment;
import com.ecotton.impex.fragments.MyWorkFragment;
import com.ecotton.impex.fragments.SearchBuyerFragment;
import com.ecotton.impex.fragments.UtilitiesFragment;
import com.ecotton.impex.utils.SessionUtil;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    private Context mContext;

    HomeFragment homeFragment;
    public static String COMPANY_Name = "Company_name";
    public static String USER_Type = "User_type";

    private String company_name;
    private String user_type;

    private SessionUtil mSessionUtil;
    ActivityHomeBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mContext = this;

        if (getActionBar() != null) {
            getActionBar().hide();
        }

        mSessionUtil = new SessionUtil(mContext);
        Intent intent = getIntent();
        if (intent != null) {
            company_name = intent.getStringExtra(COMPANY_Name);
            user_type = intent.getStringExtra(USER_Type);
        }
        Bundle bundle = new Bundle();
        bundle.putString("company_name", company_name);
        bundle.putString("user_type", user_type);
        if (TextUtils.isEmpty(filterRequest.getProduct_id())) {
            filterRequest.setProduct_id("-1");
        }
        if (TextUtils.isEmpty(filterRequest.getCountry_id())) {
            filterRequest.setCountry_id("-1");
        }
        filterRequest.setCompany_id(mSessionUtil.getCompanyId());
        homeFragment = new HomeFragment();
        homeFragment.setArguments(bundle);
        replaceFragment(homeFragment);


        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.home:
                        replaceFragment(homeFragment);
                        break;
                    case R.id.seller:
                        SearchBuyerFragment searchBuyerFragment = new SearchBuyerFragment();
                        replaceFragment(searchBuyerFragment);
                        break;
                    case R.id.utilites:
                        UtilitiesFragment utilitiesFragment = new UtilitiesFragment();
                        replaceFragment(utilitiesFragment);
                        break;
                    case R.id.mywork:
                        MyWorkFragment myWorkFragment = new MyWorkFragment();
                        replaceFragment(myWorkFragment);
                        break;
                }
                return true;
            }
        });

        ChangeMenuTitle();

        binding.imgPostBy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences prefs = getSharedPreferences("cotton", MODE_PRIVATE);
                int idName = prefs.getInt("issetup", 0); //0 is the default value.
                if (idName == 0) {
                    CompanyDetailDailog();
                } else if (idName == 1) {
                    PostDialog();
                }
            }
        });
    }

    public void ChangeMenuTitle() {
        for (int i = 0; i < bottomNavigationView.getMaxItemCount(); i++) {
            if (mSessionUtil.getUsertype().equals("buyer")) {
                bottomNavigationView.getMenu().findItem(R.id.seller).setTitle(getResources().getString(R.string.seller));
            } else if (mSessionUtil.getUsertype().equals("seller")) {
                bottomNavigationView.getMenu().findItem(R.id.seller).setTitle(getResources().getString(R.string.lbl_buyer));
            }
        }

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.bottom_navigation);
        if (mSessionUtil.getUsertype().equals("buyer")) {
            item.setTitle(getResources().getString(R.string.seller));
        } else if (mSessionUtil.getUsertype().equals("seller")) {
            item.setTitle(getResources().getString(R.string.lbl_buyer));
        }
        return super.onPrepareOptionsMenu(menu);
    }


    public void CompanyDetailDailog() {
        final Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.company_details_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        TextView txt_cancel = (TextView) dialog.findViewById(R.id.txt_cancel);
        TextView txt_ok = (TextView) dialog.findViewById(R.id.txt_ok);

        txt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        txt_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                startActivity(new Intent(mContext, UpdateCompanyDetailsActivity.class));
            }
        });

        dialog.show();
    }

    public void PostDialog() {
        final Dialog dialog = new Dialog(mContext, R.style.NewDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.post_dailog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().setGravity(Gravity.BOTTOM);

        // e.g. bottom + left margins:
        dialog.getWindow().setGravity(Gravity.BOTTOM);

        WindowManager.LayoutParams layoutParams1 = dialog.getWindow().getAttributes();
        layoutParams1.y = 200; // bottom margin
        dialog.getWindow().setAttributes(layoutParams1);

        TextView lbl_post_to_buy = (TextView) dialog.findViewById(R.id.lbl_post_to_buy);
        TextView lbl_personal_buy = (TextView) dialog.findViewById(R.id.lbl_personal_buy);

        if (mSessionUtil.getUsertype().equals("buyer")) {
            lbl_post_to_buy.setText("Post to Import");
            lbl_personal_buy.setText("Private Import");
        }

        lbl_post_to_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                startActivity(new Intent(mContext, PostToSellActivity.class));
            }
        });

        lbl_personal_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                startActivity(new Intent(mContext, PrivateSellActivity.class));
                finish();
            }
        });

        dialog.show();
    }

    public void replaceFragment(Fragment fragment) {

        try {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_place, fragment);
            fragmentTransaction.commit();

        } catch (Exception e) {
            // TODO: handle exception
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}