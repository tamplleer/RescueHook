package com.whynotpot.rescuehook.timber;

import android.util.Log;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import timber.log.Timber;

public class TimberReleaseTree extends Timber.Tree {

    private int MAX_LOG_LENGTH = 4000;

    @Override
    protected boolean isLoggable(@Nullable String tag, int priority) {

        //only log warn, error and wtf
        return priority != Log.VERBOSE && priority != Log.DEBUG && priority != Log.INFO;
    }

    @Override
    protected void log(int priority, @Nullable String tag, @NotNull String message,
                       @Nullable Throwable t) {
        if (isLoggable(tag, priority)) {

            //Report crash exceptions to CrashLytics
            if (priority == Log.ERROR && t != null) {
                //Crashlytics.log(e)
            }

            //Message is short no need to split
            if (message.length() < MAX_LOG_LENGTH) {
                logByAssertLevel(priority, tag, message);
                return;
            }

            //Split by line so they fit in maximum length's Log
            for (int i = 0, length = message.length(); i < length; i++) {
                int newLine = message.indexOf('\n', i);
                newLine = newLine != -1 ? newLine : length;
                do {
                    int end = Math.min(newLine, i + MAX_LOG_LENGTH);
                    String part = message.substring(i, end);
                    logByAssertLevel(priority, tag, part);
                    i = end;
                } while (i < newLine);
            }
        }
    }

    private void logByAssertLevel(int priority, @Nullable String tag, @NotNull String message) {
        if (priority == Log.ASSERT) {
            Log.wtf(tag, message);
        } else {
            Log.println(priority, tag, message);
        }
    }
}
