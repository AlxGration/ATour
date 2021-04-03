package com.alex.atour.ui.list;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;


import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.TextView;

import com.alex.atour.R;
import com.alex.atour.models.ChampsListRecyclerAdapter;
import com.alex.atour.ui.champ.ChampActivity;
import com.alex.atour.ui.create.champ.ChampCreationActivity;
import com.alex.atour.ui.profile.ProfileActivity;


public class MainActivity extends AppCompatActivity implements
        ChampsListRecyclerAdapter.IonItemClickListener{

    private ViewPager viewPager;
    private SectionsPagerAdapter sectionsPagerAdapter;
    private TextView tvList, tvMy, tvManage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);

        tvMy = findViewById(R.id.tv_tab_1);
        tvList = findViewById(R.id.tv_tab_2);
        tvManage = findViewById(R.id.tv_tab_3);

        tvMy.setOnClickListener(view -> viewPager.setCurrentItem(0));
        tvList.setOnClickListener(view -> viewPager.setCurrentItem(1));
        tvManage.setOnClickListener(view -> viewPager.setCurrentItem(2));

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }

            @Override
            public void onPageSelected(int position) {
                if (position == 0){
                    setActiveTab(tvMy);
                    setInactiveTab(tvList);
                    setInactiveTab(tvManage);
                }else if (position == 1){
                    setActiveTab(tvList);
                    setInactiveTab(tvMy);
                    setInactiveTab(tvManage);
                }else{
                    setActiveTab(tvManage);
                    setInactiveTab(tvList);
                    setInactiveTab(tvMy);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {  }
        });

    }

    private void setActiveTab(TextView tv){
        if (tv == null) return;;

        tv.setTextSize(getResources().getInteger(R.integer.active_tap_size));
        tv.setTypeface(tv.getTypeface(), Typeface.BOLD);
        tv.setTextColor(getResources().getColor(R.color.black));
    }
    private void setInactiveTab(TextView tv){
        if (tv == null) return;;

        tv.setTextSize(getResources().getInteger(R.integer.inactive_tap_size));
        tv.setTypeface(tv.getTypeface(), Typeface.NORMAL);
        tv.setTextColor(getResources().getColor(R.color.grey));
    }


    // обработчик нажатия на элемент списка
    // открывает акнивность информации о чемпионате
    @Override
    public void startChampActivityWith(String champID) {
        Intent intent = new Intent(this, ChampActivity.class);
        intent.putExtra("champID", champID);
        startActivity(intent);
    }


    public void onClickSearch(View view) {
        //Intent intent = new Intent(MainActivity.this, SearchActivity.class);
        //startActivity(intent);
        //overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    public void onClickNewChamp(View view) {
        startActivity(
                new Intent(this, ChampCreationActivity.class)
        );
    }

    public void onClickProfile(View view) {
        startActivity(
                new Intent(this, ProfileActivity.class)
        );
    }
}