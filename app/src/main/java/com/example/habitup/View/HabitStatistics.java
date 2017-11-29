package com.example.habitup.View;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.habitup.R;

import java.util.ArrayList;

/**
 * This activity displays the user habit statistics, such as progression statuses for
 * all habits.
 */
public class HabitStatistics extends AppCompatActivity {

    ArrayList<Fragment> frags;
    FragmentPagerAdapter adapterViewPager;
    ViewPager vpPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stats);

        // Set back button
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        frags = new ArrayList<Fragment>();
        frags.add(HabitStatusFragment.newInstance(0));
        frags.add(ChartFragment.newInstance(1));

        vpPager = (ViewPager) findViewById(R.id.statsPager);
        adapterViewPager = new MyPagerAdapter(getSupportFragmentManager(), frags);
        vpPager.setAdapter(adapterViewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.stats_tab);
        tabLayout.setupWithViewPager(vpPager);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                vpPager.setCurrentItem(tab.getPosition());

                Fragment fr = adapterViewPager.getItem(tab.getPosition());

                if (fr instanceof ChartFragment) {
                    ((ChartFragment) frags.get(1)).animateChart();
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                vpPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                vpPager.setCurrentItem(tab.getPosition());
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

    public static class MyPagerAdapter extends FragmentPagerAdapter {
        private String[] titles = {"Habit Statuses", "Event Graph"};
        private ArrayList<Fragment> fragList;

        public MyPagerAdapter(FragmentManager fragmentManager, ArrayList<Fragment> fragList) {
            super(fragmentManager);
            this.fragList = fragList;
        }

        // Returns total number of pages
        @Override
        public int getCount() {
            return fragList.size();
        }

        // Returns the fragment to display for that page
        @Override
        public Fragment getItem(int position) {
            return fragList.get(position);
        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

    }
}
