package com.example.backgroundprocessing;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class UploadWorker extends Worker {

    private static final String TAG = UploadWorker.class.getName();

    public UploadWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    /**
     * Do your background processing here. doWork() is called on a
     * background thread - you are required to synchronously do your work and return the
     * Result from this method. Once you return from the
     * method, the Worker is considered to have finished what its doing and will be destroyed. If
     * you need to do your work asynchronously on a thread of your own choice, see ListenableWorker.
     *
     * A Worker is given a maximum of ten minutes to finish its execution and return a
     * Result. After this time has expired, the Worker will be signalled to stop.
     */
    @NonNull
    @Override
    public Result doWork() {
        Log.d(TAG, "Uploading files to the server...");

        int count = 0;

        for(int i = 1; i <= 500000000; i++){
            if (i % 50000000 == 0) {
                count += 10;
                Log.d(TAG, "uploading... " + count + "%");
            }
        }

        return Result.success();
    }
}
