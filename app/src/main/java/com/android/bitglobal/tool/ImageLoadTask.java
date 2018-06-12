package com.android.bitglobal.tool;

/**
 * Created by bitbank on 16/9/30.
 */
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;

public class ImageLoadTask extends AsyncTask<String, Void, Bitmap> {
    private Handler handler;
    private ImageView mImageView;
    public ImageLoadTask(Handler handler) {
        this.handler = handler;
    }

    @Override
    protected void onCancelled(Bitmap bitmap) {
        super.onCancelled(bitmap);
        mImageView.setImageBitmap(bitmap);
    }

    protected void onPostExecute(Bitmap result) {
        Message msg = new Message();
        msg.obj = result;
        mImageView.setImageBitmap(result);
        handler.sendMessage(msg);
    }
public void setImageView(ImageView imageView)
{
     mImageView = imageView;
}
    protected Bitmap doInBackground(String... getUrls) {
        InputStream inputStream = null;
        HttpURLConnection urlConnection = null;

        try {
            // open connection
            URL url = new URL(getUrls[0]);
            urlConnection = (HttpURLConnection) url.openConnection();
            /* for Get request */
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Authorization",getUrls[1]);
            urlConnection.setRequestProperty("Cookie", "zuid="+getUrls[2]);

            int fileLength = urlConnection.getContentLength();
            int statusCode = urlConnection.getResponseCode();
            if (statusCode == 200) {
                inputStream = urlConnection.getInputStream();
                byte data[] = new byte[4096];
                long total = 0;
                int count;
                ByteArrayOutputStream output = new ByteArrayOutputStream();
                while ((count = inputStream.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    if (fileLength > 0 && handler != null) {
                        handler.sendEmptyMessage(((int) (total * 100 / fileLength)) - 1);
                    }
                    output.write(data, 0, count);
                }
                ByteArrayInputStream bufferInput = new ByteArrayInputStream(output.toByteArray());
                Bitmap bitmap = BitmapFactory.decodeStream(bufferInput);
                inputStream.close();
                bufferInput.close();
                output.close();
                Log.i("image", "already get the image by uuid : " + getUrls[0]);
                handler.sendEmptyMessage(100);
                return bitmap;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return null;
    }

}