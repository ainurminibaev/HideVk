package com.ainur.hidevk.adapters;
 
import com.ainur.hidevk.fragments.OpenChatFragment;
import com.ainur.hidevk.fragments.HideChatFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
 
public class ViewPagerAdapter extends FragmentPagerAdapter {
 
    // Declare the number of ViewPager pages
    final int PAGE_COUNT = 2;
 
    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }
 
    @Override
    public Fragment getItem(int arg0) {
        switch (arg0) {
 
        // Open FragmentTab1.java
        case 0:
            OpenChatFragment fragmenttab1 = new OpenChatFragment();
            return fragmenttab1;
 
        // Open FragmentTab2.java
        case 1:
            HideChatFragment fragmenttab2 = new HideChatFragment();
            return fragmenttab2;
        }
        return null;
    }
 
    @Override
    public int getCount() {
        return PAGE_COUNT;
    }
 
}