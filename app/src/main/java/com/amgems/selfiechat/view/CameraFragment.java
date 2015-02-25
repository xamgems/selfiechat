package com.amgems.selfiechat.view;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.amgems.selfiechat.HomeActivity;
import com.amgems.selfiechat.R;
import com.amgems.selfiechat.api.SelfieChatService;
import com.amgems.selfiechat.model.Snap;

import java.io.File;
import java.io.IOException;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedFile;

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

    private Snap mCurrentSnap;

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
                             final Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_camera, container, false);
        mPreview = (ImageView) rootView.findViewById(R.id.camera_preview);
        mSnapButton = (Button) rootView.findViewById(R.id.camera_snap);
        mSendButton = (Button) rootView.findViewById(R.id.camera_send);

        mSnapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takeSnap();
            }
        });
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelfieChatService selfieChatService = HomeActivity.webClient.getSelfieChatService();
                File toSend = mCurrentSnap.getFile();
                selfieChatService.sendSnap(new TypedFile("image/jpg", toSend),
                        new Callback<Response>() {
                            @Override
                            public void success(Response response, Response response2) {
                                Toast.makeText(getActivity(), "Sent Snap!", Toast.LENGTH_SHORT)
                                        .show();
                            }

                            @Override
                            public void failure(RetrofitError error) {
                                Log.e(getClass().getSimpleName(),
                                        "RetrofitError " + error.getMessage());
                            }
                        });
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

    private void takeSnap() {
        File tempFile = null;
        try {
            File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            if (!storageDir.exists()) {
                boolean success = storageDir.mkdirs();
                if (!success) {
                    Log.e(getClass().getSimpleName(), "TakeSnap() unable to mkdirs at "
                            +storageDir);
                }
            }
            mCurrentSnap = new Snap(storageDir.getAbsolutePath());
            tempFile = mCurrentSnap.getFile();
        } catch (IOException e) {
            Log.e(getClass().getSimpleName(), "TakeSnap() " + e.getMessage());
        }
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
        startActivityForResult(cameraIntent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
    }

    private void handleImage(Intent data) {
        Bitmap imageBitmap = null;
        Toast.makeText(getActivity(), "Displaying Fullscale Image", Toast.LENGTH_SHORT).show();
        imageBitmap = mCurrentSnap.getBitmap(mPreview);

        if (imageBitmap != null) {
            mPreview.setImageBitmap(imageBitmap);
        } else {
            Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT)
                    .show();
            Log.d(getClass().getSimpleName(), "ImageBitmap was null");
        }
    }
}
