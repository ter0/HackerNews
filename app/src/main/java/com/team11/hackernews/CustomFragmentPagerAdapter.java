package com.team11.hackernews;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.team11.hackernews.api.data.Thread;

public class CustomFragmentPagerAdapter extends FragmentStatePagerAdapter {

    public SecondPage mCurrentSecondPage;
    private WebViewFragment mWebViewFragment;
    private ThreadFragment mThreadFragment;
    private MainFragment mMainFragment;
    private int mItemCount;

    public CustomFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
        mItemCount = 1;
        mMainFragment = MainFragment.newInstance();
    }

    public MainFragment getMainFragment() {
        return mMainFragment;
    }

    @Override
    public Fragment getItem(int i) {
        //change this to reuse fragments in future
        Fragment fragment;
        if (i == 0) {
            fragment = mMainFragment;
        } else if (i == 1 && mCurrentSecondPage == SecondPage.WEBVIEW) {
            fragment = mWebViewFragment;
        } else if (i == 1 && mCurrentSecondPage == SecondPage.COMMENTS) {
            fragment = mThreadFragment;
        } else {
            throw new IllegalArgumentException("no fragment for " + String.valueOf(i));
        }
        return fragment;
    }

    public void saveMainFragment(Bundle bundle) {
        mMainFragment.saveState(bundle);
    }

    public void setWebViewFragment(String url) {
        mItemCount = 2;
        mWebViewFragment = WebViewFragment.newInstance(url);
        mCurrentSecondPage = SecondPage.WEBVIEW;
        notifyDataSetChanged();
    }

    public void setThreadFragment(Thread thread) {
        mItemCount = 2;
        mThreadFragment = ThreadFragment.newInstance(thread);
        mCurrentSecondPage = SecondPage.COMMENTS;
        notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(Object item) {
        if (item instanceof MainFragment) {
            return POSITION_UNCHANGED;
        } else if (mCurrentSecondPage == SecondPage.WEBVIEW && item == mWebViewFragment) {
            return POSITION_UNCHANGED;
        } else if (mCurrentSecondPage == SecondPage.COMMENTS && item == mThreadFragment) {
            return POSITION_UNCHANGED;
        } else {
            return POSITION_NONE;
        }
    }

    public void refresh(){
        mMainFragment.refresh();
     if (mCurrentSecondPage == SecondPage.WEBVIEW){
         setWebViewFragment(mWebViewFragment.getURL());
     }else if(mCurrentSecondPage == SecondPage.COMMENTS){
         setThreadFragment(mThreadFragment.getThread());
     }
    }

    public Thread getThread(){
        return mThreadFragment.getThread();
    }

    public String getWebUrl() {
        return mWebViewFragment.getURL();
    }

    @Override
    public int getCount() {
        return mItemCount;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "OBJECT " + (position + 1);
    }

    public enum SecondPage {
        WEBVIEW,
        COMMENTS
    }
}
