package com.example.newsfeed.Network;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.util.LruCache;
import android.widget.ImageView;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

public class ImageDownloader {
    private static LruCache<String, Bitmap> memoryCache;

    public ImageDownloader() {
        // Get max available VM memory, exceeding this amount will throw an
        // OutOfMemory exception. Stored in kilobytes as LruCache takes an
        // int in its constructor.
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

        // Use 1/8th of the available memory for this memory cache.
        final int cacheSize = maxMemory / 8;

        memoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                // The cache size will be measured in kilobytes rather than
                // number of items.
                return bitmap.getByteCount() / 1024;
            }
        };
    }

    private static void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            memoryCache.put(key, bitmap);
        }
    }

    private static Bitmap getBitmapFromMemCache(String key) {
        return memoryCache.get(key);
    }

    private static boolean isInCache(String url, ImageView imageView) {
        Bitmap bitmap = getBitmapFromMemCache(url);
        if (bitmap == null) {
            return false;
        }

        imageView.setImageBitmap(bitmap);
        return true;
    }

    private static BitmapDownloaderTask getBitmapDownloaderTask(ImageView imageView) {
        if (imageView != null) {
            Drawable drawable = imageView.getDrawable();
            if (drawable instanceof DownloadedDrawable) {
                DownloadedDrawable downloadedDrawable = (DownloadedDrawable) drawable;
                return downloadedDrawable.getBitmapDownloaderTask();
            }
        }
        return null;
    }

    private static Bitmap downloadBitmap(String url) {
        URL _url;
        HttpURLConnection urlConnection = null;
        try {
            _url = new URL(url);
            urlConnection = (HttpURLConnection) _url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setConnectTimeout(500);
            urlConnection.connect();

            final int statusCode = urlConnection.getResponseCode();
            if (statusCode != HttpURLConnection.HTTP_OK) {
                Log.e("ImageDownloader", "Error " + statusCode + " while retrieving bitmap from " + url);
                return null;
            }
            InputStream inputStream = null;

            inputStream = urlConnection.getInputStream();

            return BitmapFactory.decodeStream(inputStream);

        } catch (Exception e) {
            Log.w("ImageDownloader", "Error while retrieving bitmap from " + url, e.fillInStackTrace());
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return null;
    }

    public void download(String url, ImageView imageView) {
        if (!isInCache(url, imageView)) {
            BitmapDownloaderTask task = new BitmapDownloaderTask(imageView);
            DownloadedDrawable downloadedDrawable = new DownloadedDrawable(task);
            imageView.setImageDrawable(downloadedDrawable);
            task.execute(url);
        }
    }

    static class BitmapDownloaderTask extends AsyncTask<String, Void, Bitmap> {
        String url;
        private WeakReference<ImageView> imageView;

        BitmapDownloaderTask(ImageView imageView) {
            this.imageView = new WeakReference<>(imageView);
        }

        @Override
        // Actual download method, run in the task thread
        protected Bitmap doInBackground(String... params) {
            // params comes from the execute() call: params[0] is the url.
            url = params[0];
            return ImageDownloader.downloadBitmap(params[0]);
        }

        @Override
        // Once the image is downloaded, associates it to the imageView
        protected void onPostExecute(Bitmap bitmap) {
            ImageView imageview = imageView.get();
            BitmapDownloaderTask bitmapDownloaderTask = getBitmapDownloaderTask(imageview);
            // Change bitmap only if this process is still associated with it
            // ???
            if (this == bitmapDownloaderTask) {
                imageview.setImageBitmap(bitmap);
                if (bitmap != null) {
                    addBitmapToMemoryCache(url, bitmap);
                }
            }
        }
    }

    static class DownloadedDrawable extends ColorDrawable {
        private final BitmapDownloaderTask bitmapDownloaderTask;

        DownloadedDrawable(BitmapDownloaderTask bitmapDownloaderTask) {
            super(Color.BLACK);
            this.bitmapDownloaderTask = bitmapDownloaderTask;
        }

        BitmapDownloaderTask getBitmapDownloaderTask() {
            return bitmapDownloaderTask;
        }
    }
}
