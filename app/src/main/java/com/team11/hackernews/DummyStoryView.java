package com.team11.hackernews;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.team11.hackernews.api.Thread;

public class DummyStoryView extends ActionBarActivity {

    private CommentsView mCommentView;
    private Thread mStory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dummy_story_view);
        mStory = getIntent().getParcelableExtra(Thread.THREAD_PARCEL_KEY);
        ((TextView) findViewById(R.id.story_title)).setText(mStory.getTitle());

        mCommentView = (CommentsView) findViewById(R.id.dummy_comment_view);
        mCommentView.setThread(mStory);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_dummy_story_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
