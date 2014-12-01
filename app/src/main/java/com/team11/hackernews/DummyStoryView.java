package com.team11.hackernews;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.team11.hackernews.api.Comment;
import com.team11.hackernews.api.Story;
import com.team11.hackernews.api.accessors.CommentsAccessor;

import java.util.List;


public class DummyStoryView extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dummy_story_view);
        Story story = getIntent().getParcelableExtra(Story.STORY_PARCEL_KEY);
        ((TextView) findViewById(R.id.story_title)).setText(story.getTitle());
        final CommentsView commentView = (CommentsView) findViewById(R.id.dummy_comment_view);
        CommentsAccessor commentsAccessor = new CommentsAccessor();
        commentsAccessor.getChildComments(story, new CommentsAccessor.GetChildCommentsCallbacks() {
            @Override
            public void onSuccess(List<Comment> comments) {
                for (Comment comment : comments) {
                    commentView.add(comment);
                }
            }

            @Override
            public void onError() {
            }
        });
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
