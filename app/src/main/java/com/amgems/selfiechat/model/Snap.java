package com.amgems.selfiechat.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author shermpay on 2/22/15.
 */
public class Snap {
    public static final int BUFF_SIZE = 4096;
    public static final String FILE_PREFIX = "TEMP_";
    public static final String FILE_SUFFIX = ".jpg";
    private File mSnapFile;

    public Snap(InputStream inputStream, File dir) throws IOException {
        mSnapFile = File.createTempFile(FILE_PREFIX, FILE_SUFFIX, dir);
        FileOutputStream outputStream = new FileOutputStream(mSnapFile);
        byte[] buff = new byte[BUFF_SIZE];
        int read = 0;
        while (read != -1) {
            read = inputStream.read(buff);
            outputStream.write(buff);
        }
    }

    public Snap(String path) throws IOException {
        mSnapFile = File.createTempFile(FILE_PREFIX, FILE_SUFFIX, new File(path));
    }

    public byte[] toByteArray() {
        try (FileInputStream inputStream = new FileInputStream(mSnapFile)) {
            byte[] buff = new byte[BUFF_SIZE];
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            try {
                int read = 0;
                while (read != -1) {
                    read = inputStream.read(buff);
                    outputStream.write(buff);
                }
                return outputStream.toByteArray();
            } catch (IOException e) {
                Log.e(getClass().getSimpleName(), e.getMessage());
            }
        } catch (FileNotFoundException e) {
            Log.e(getClass().getSimpleName(),"FileNotFound " + mSnapFile.getAbsolutePath() +
                    e.getMessage());
        } catch (IOException e) {
            Log.e(getClass().getSimpleName(), "NotDir " + mSnapFile.getAbsolutePath() +
                    e.getMessage());
        }
        return null;
    }

    public Bitmap getBitmap(ImageView imageView) {
        // Get the dimensions of the View
        int targetW = imageView.getWidth();
        int targetH = imageView.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mSnapFile.getAbsolutePath(), bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;

        return BitmapFactory.decodeFile(mSnapFile.getAbsolutePath(), bmOptions);
    }

    public File getFile() {
        return mSnapFile;
    }

    public static File createTempFile(File storageDir) throws IOException {
//       Create a unique signature for temp files
        String timeStamp = Long.toString(System.currentTimeMillis());
        String imageFileName = "JPEG_";

        return File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
    }

}
