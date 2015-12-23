package com.shyling.healthmanager.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.shyling.healthmanager.R;
import com.shyling.healthmanager.fragment.ChatFragment;
import com.shyling.healthmanager.fragment.HistoryFragment;
import com.shyling.healthmanager.fragment.TestFragment;
import com.shyling.healthmanager.util.Utils;

import static com.shyling.healthmanager.R.color;
import static com.shyling.healthmanager.R.id;
import static com.shyling.healthmanager.R.layout;
import static com.shyling.healthmanager.R.string;

/**
 * Created by shy on 2015/11/8.
 */
public class MainActivity extends AppCompatActivity {
    android.support.v4.app.FragmentManager fragmentManager;
    ImageButton history, test, chat;
    ViewPager viewPager;
    ActionBar actionBar;
    private AlertDialog aboutDialog;
    Fragment[] fragments;
    long backButtonProcessTime;

//    Fragment testFragment,historyFragment,chatFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
        setContentView(layout.main_activity);

        findViews();

        fragmentManager = getSupportFragmentManager();
        fragments = new Fragment[]{
                new HistoryFragment(),
                new TestFragment(),
                new ChatFragment()
        };
        FragmentPagerAdapter fragmentPagerAdapter = new FragmentPagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                return fragments[position];
            }

            @Override
            public int getCount() {
                return fragments.length;
            }
        };
        viewPager.setAdapter(fragmentPagerAdapter);
        setListener();

    }
    private void setListener() {
        final View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                history.setBackgroundColor(getResources().getColor(color.gray));
                test.setBackgroundColor(getResources().getColor(color.gray));
                chat.setBackgroundColor(getResources().getColor(color.gray));
                v.setBackgroundColor(getResources().getColor(color.white));
                if (v == history) {
                    viewPager.setCurrentItem(0);
                } else if (v == test) {
                    viewPager.setCurrentItem(1);
                } else if (v == chat) {
                    viewPager.setCurrentItem(2);
                }
            }
        };
        history.setOnClickListener(onClickListener);
        test.setOnClickListener(onClickListener);
        chat.setOnClickListener(onClickListener);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        onClickListener.onClick(history);
                        break;
                    case 1:
                        onClickListener.onClick(test);
                        break;
                    case 2:
                        onClickListener.onClick(chat);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        onClickListener.onClick(test);
    }

    public AlertDialog getAboutDialog() {
        if (aboutDialog == null) {
            aboutDialog = new AlertDialog.Builder(this).setTitle(string.about).setMessage(string.about_content).create();
        }
        return aboutDialog;
    }

    private void findViews() {
        viewPager = (ViewPager) findViewById(id.viewpager);
        history = (ImageButton) findViewById(id.history);
        test = (ImageButton) findViewById(id.test);
        chat = (ImageButton) findViewById(id.chat);
//        testFragment = new TestFragment();
//        historyFragment = new HistoryFragment();
//        chatFragment = new ChatFragment();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater menuInflater = this.getMenuInflater();
        menuInflater.inflate(R.menu.main_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == id.about) {
            getAboutDialog().show();
        } else if (itemId == id.exit) {
            System.exit(0);//退出app
        } else if (itemId == id.setting) {
            startActivity(new Intent(this, SettingActivity.class));
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - backButtonProcessTime <= 2000) {
            finish();
        }else {
            Utils.Toast(string.doublebackbutton);
            backButtonProcessTime = System.currentTimeMillis();
        }
    }
}
