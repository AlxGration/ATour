package com.alex.atour.ui.list;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;


import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.alex.atour.DTO.ChampInfo;
import com.alex.atour.DTO.User;
import com.alex.atour.R;
import com.alex.atour.models.ChampsListRecyclerAdapter;
import com.alex.atour.ui.champ.ChampActivity;
import com.alex.atour.ui.create.champ.ChampCreationActivity;
import com.alex.atour.ui.list.search.SearchFragment;
import com.alex.atour.ui.profile.ProfileActivity;


public class MainActivity extends AppCompatActivity implements
        ChampsListRecyclerAdapter.IonItemClickListener{

    private ViewPager viewPager;
    private ChampsPagerAdapter champsPagerAdapter;
    private TextView tvList, tvMy, tvManage;
    private ImageView imgSearch;
    private ImageButton imgCancel;
    private EditText etSearch;
    private ConstraintLayout tabsLayout;
    private FrameLayout frameLayout;
    private SearchFragment searchFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        champsPagerAdapter = new ChampsPagerAdapter(getSupportFragmentManager());
        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(champsPagerAdapter);

        tvMy = findViewById(R.id.tv_tab_1);
        tvList = findViewById(R.id.tv_tab_2);
        tvManage = findViewById(R.id.tv_tab_3);
        etSearch = findViewById(R.id.et_search);
        imgCancel = findViewById(R.id.img_cancel);
        imgSearch = findViewById(R.id.img_search);
        tabsLayout = findViewById(R.id.tabs_layout);
        frameLayout = findViewById(R.id.frame_layout);

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

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //swapping views depends on search line
                //0 - main list, >0 - searchFragment
                showView(charSequence.length());
            }
            @Override
            public void afterTextChanged(Editable editable) {}
        });
        etSearch.setOnEditorActionListener((textView, actionId, keyEvent) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                String searchRequest = textView.getText().toString();
                showFragmentWithResult(searchRequest);
                return true;
            }
            return false;
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
    public void startChampActivityWith(ChampInfo info) {
        Intent intent = new Intent(this, ChampActivity.class);
        intent.putExtra("champInfo", info);
        startActivity(intent);
    }

    public void onClickNewChamp(View view) {
        startActivity(
                new Intent(this, ChampCreationActivity.class)
        );
    }

    public void onClickProfile(View view) {
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra("userID", User.MyID);//userID or "MY" (comparing in FirebaseDB.class)
        startActivity(intent);
    }

    private void showView(int len){
        //hide main list, show searchFragment
        if (len > 0){
            tabsLayout.setVisibility(View.GONE);
            imgSearch.setVisibility(View.INVISIBLE);
            imgCancel.setVisibility(View.VISIBLE);
            frameLayout.setVisibility(View.VISIBLE);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_layout,  getSearchFragment())
                    .commitNow();
        }else{
            searchFragment.setSearchQuery("");

            //hide searchFragment, show main list
            tabsLayout.setVisibility(View.VISIBLE);
            imgSearch.setVisibility(View.VISIBLE);

            imgCancel.setVisibility(View.INVISIBLE);
            frameLayout.setVisibility(View.INVISIBLE);
        }
    }
    private Fragment getSearchFragment(){
        if (searchFragment == null){
            searchFragment = new SearchFragment();
        }
        return searchFragment;
    }
    private void showFragmentWithResult(String searchRequest){
        if (searchFragment != null && frameLayout.getVisibility() == View.VISIBLE){
            searchFragment.setSearchQuery(searchRequest);
        }
    }

    public void onClickCancel(View view) {
        searchFragment.setSearchQuery("");
        etSearch.setText("");
    }

    @Override
    public void onBackPressed() {
        if (etSearch.getText().toString().isEmpty())
            super.onBackPressed();
        else onClickCancel(null);
    }
}