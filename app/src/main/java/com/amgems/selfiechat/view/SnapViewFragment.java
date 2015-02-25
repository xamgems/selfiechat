package com.amgems.selfiechat.view;

import android.os.Bundle;
import android.os.Environment;
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
import java.io.InputStream;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * Use the {@link SnapViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SnapViewFragment extends Fragment {
    private ImageView mSnapImage;
    private Button mDownloadButton;

    public static SnapViewFragment newInstance() {
        SnapViewFragment fragment = new SnapViewFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public SnapViewFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_snap_view, container, false);
        mSnapImage = (ImageView) root.findViewById(R.id.snap_image);
        mDownloadButton = (Button) root.findViewById(R.id.download_snap);
        mDownloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelfieChatService selfieChatService = HomeActivity.webClient.getSelfieChatService();
                selfieChatService.getSnap(new Callback<Response>() {
                    @Override
                    public void success(Response response, Response response2) {
                        try {
                            InputStream inputStream = response.getBody().in();
                            displaySnap(inputStream);
                        } catch (IOException e) {
                            Toast.makeText(getActivity(), "Error saving image as temp file",
                                    Toast.LENGTH_SHORT).show();
                            Log.e(getClass().getSimpleName(), e.getMessage());
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Toast.makeText(getActivity(), "Error downloading image",
                                Toast.LENGTH_SHORT).show();
                        Log.d(getClass().getSimpleName(), error.getMessage());
                    }
                });
            }
        });
        return root;
    }

    private void displaySnap(InputStream inputStream) {
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        Snap snap = new Snap(inputStream, storageDir);
        mSnapImage.setImageBitmap(snap.getBitmap(mSnapImage));
    }
}
