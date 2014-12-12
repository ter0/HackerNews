package com.team11.hackernews;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ShareCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.team11.hackernews.api.data.Story;

public class WebViewFragment extends Fragment {

    public static final String URL_EXTRA = "URL_EXTRA";

    private static final String ARG_URL = "ARG_URL";

    private String mUrl;

    private OnFragmentInteractionListener mListener;

    private WebView webView;

    private ShareActionProvider mShareActionProvider;

    public WebViewFragment() {
        // Required empty public constructor
    }

    public String getURL() {
        return mUrl;
    }

    public static WebViewFragment newInstance(String url) {
        WebViewFragment fragment = new WebViewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_URL, url);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUrl = getArguments().getString(ARG_URL);
        }
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_web_view, container, false);
        webView = (WebView) rootView.findViewById(R.id.webView);
        if (savedInstanceState == null) {
            webView.getSettings().setJavaScriptEnabled(true);
            final Activity activity = this.getActivity();
            webView.setWebViewClient(new WebViewClient() {
                @Override
                public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                    Toast.makeText(activity, "Webpage failed", Toast.LENGTH_LONG);
                    super.onReceivedError(view, errorCode, description, failingUrl);
                }
            });
            webView.loadUrl(mUrl);
        } else {
            webView.restoreState(savedInstanceState);
        }
        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        webView.saveState(outState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_web_view, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem shareMenuItem = menu.findItem(R.id.menu_web_item_share);
        if (shareMenuItem != null) {
            mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(shareMenuItem);
            Intent shareIntent = ShareCompat.IntentBuilder.from(this.getActivity())
                    .setType("text/plain")
                    .setText(mUrl).getIntent();
            mShareActionProvider.setShareIntent(shareIntent);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_open_browser) {
            mListener.openExternalApps(Uri.parse(mUrl));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        public void openExternalApps(Uri uri);
    }

}
