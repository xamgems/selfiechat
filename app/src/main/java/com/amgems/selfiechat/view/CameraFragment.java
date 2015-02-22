package com.amgems.selfiechat.view;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.amgems.selfiechat.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CameraFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CameraFragment extends Fragment {
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;

    private ImageView mPreview;
    private Button mSnapButton;
    private Button mSendButton;

    private String mCurrentPhotoPath;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CameraFragment.
     */
    public static CameraFragment newInstance() {
        CameraFragment fragment = new CameraFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public CameraFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_camera, container, false);
        mPreview = (ImageView) rootView.findViewById(R.id.camera_preview);
        mSnapButton = (Button) rootView.findViewById(R.id.camera_snap);
        mSendButton = (Button) rootView.findViewById(R.id.camera_send);

        mSnapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto();
            }
        });

        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Toast.makeText(getActivity(), "Image Captured!", Toast.LENGTH_SHORT).show();
                handleImage(data);
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(getActivity(), "Cancelled Image Capture", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "Image Capture Failed!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Log.d(getClass().getSimpleName(), "Unknown request code " + requestCode);
        }
    }

    private void takePhoto() {
        File tempFile = null;
        try {
            tempFile = createTempFile();
        } catch (IOException e) {
            Log.e(getClass().getSimpleName(), e.getMessage());
        }
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        boolean usingThumbnail = tempFile == null;
        if (!usingThumbnail) {
            Log.d(getClass().getSimpleName(), "putExtra()" + Uri.fromFile(tempFile));
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
        }
        startActivityForResult(cameraIntent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
    }

    private File createTempFile() throws IOException {
//       Create a unique signature for temp files
        String timeStamp = Long.toString(System.currentTimeMillis());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        File tempFile = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        mCurrentPhotoPath = tempFile.getAbsolutePath();

        return tempFile;
    }

    private void handleImage(Intent data) {
        Bitmap imageBitmap = null;
        boolean usingThumbnail = mCurrentPhotoPath == null;
        if  (!usingThumbnail) {
            Toast.makeText(getActivity(), "Displaying Fullscale Image", Toast.LENGTH_SHORT).show();
            imageBitmap = getBitmapFromFile(mPreview, mCurrentPhotoPath);
        }

        if (usingThumbnail) {
            Bundle extras = data.getExtras();
            Toast.makeText(getActivity(), "Displaying Thumbnail", Toast.LENGTH_SHORT).show();
            imageBitmap = (Bitmap) extras.get("data");
        }

        if (imageBitmap != null) {
            mPreview.setImageBitmap(imageBitmap);
        } else {
            Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT)
                    .show();
            Log.d(getClass().getSimpleName(), "ImageBitmap was null");
        }
    }

    private Bitmap getBitmapFromFile(ImageView imageView, String pathToFile) {
        // Get the dimensions of the View
        int targetW = imageView.getWidth();
        int targetH = imageView.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(pathToFile, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;

        return BitmapFactory.decodeFile(pathToFile, bmOptions);
    }
}
