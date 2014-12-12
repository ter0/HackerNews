package com.team11.hackernews;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent ignored) {
        Intent intent = new Intent(context, WatcherService.class);
        context.startService(intent);
    }
}
