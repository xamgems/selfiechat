package com.amgems.selfiechat.api;

import com.amgems.selfiechat.model.Snap;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.mime.TypedFile;

/**
 * @author shermpay on 2/22/15.
 */
public interface SelfieChatService {
    public static final String ENDPOINT = "http://173.250.154.142:8080";

    @GET("/get_snap")
    public void getSnap(Callback<Response> callback);

    @Multipart
    @POST("/send_snap")
    public void sendSnap(@Part("snap") TypedFile file,  Callback<Response> callback);
}
