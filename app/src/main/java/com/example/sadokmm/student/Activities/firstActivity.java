package com.example.sadokmm.student.Activities;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TableLayout;

import com.example.sadokmm.student.Adapters.PageAdapterFirst;
import com.example.sadokmm.student.Objects.User;
import com.example.sadokmm.student.R;

import java.util.ArrayList;
import java.util.List;

public class firstActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private PageAdapterFirst pageAdapter;

    public static List<User> listUser = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        tabLayout=(TabLayout)findViewById(R.id.tabLayoutFirst);
        viewPager=(ViewPager) findViewById(R.id.viewPagerFirst);

        tabLayout.addTab(tabLayout.newTab().setText(""));
        tabLayout.addTab(tabLayout.newTab().setText(""));

        pageAdapter=new PageAdapterFirst(getSupportFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(pageAdapter);
        tabLayout.setupWithViewPager(viewPager);





    }
}
