package com.team11.hackernews;

import android.app.Activity;
import android.content.Intent;
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
                    //todo handle errors, at the moment webview goes blank with invalid urls
                    //I expected this to be were loading errors are handled but
                    //this didnt get called with a 404 (http://google.com/gregerg)
                    //or with a missing or invalid protocal (htztp://www.google.com)
                    Toast.makeText(activity, "eee", Toast.LENGTH_LONG);
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
        MenuItem menuItem = menu.findItem(R.id.menu_web_item_share);
        if (menuItem != null) {
            mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);
            Intent shareIntent = ShareCompat.IntentBuilder.from(this.getActivity())
                    .setType("text/plain")
                    .setText(mUrl).getIntent();
            mShareActionProvider.setShareIntent(shareIntent);
        }
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
    }

}
