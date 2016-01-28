package com.baijiahulian.common.crop.utils;


import android.os.AsyncTask;

/**
 * Created by yanglei on 15/11/18.
 *
 * 简单的异步执行分发
 */
public class CCDispatchAsync {

    public interface CCDisPatchRunnable {
        /**
         * 后台执行操作
         */
        void runInBackground();

        /**
         * 主线程执行操作
         */
        void runInMain();
    }


    public static void dispatchAsync(CCDisPatchRunnable runnable) {
        new CCDispatchAsyncTask().execute(runnable);
    }

    private final static class CCDispatchAsyncTask extends AsyncTask<CCDisPatchRunnable, Void, CCDisPatchRunnable> {

        @Override
        protected CCDisPatchRunnable doInBackground(CCDisPatchRunnable... params) {
            if (params == null || params.length == 0)
                return null;
            CCDisPatchRunnable runnable = params[0];

            runnable.runInBackground();

            return runnable;
        }

        @Override
        protected void onPostExecute(CCDisPatchRunnable txDisPatchRunnable) {
            if (txDisPatchRunnable != null) {
                txDisPatchRunnable.runInMain();
            }
        }
    }
}
