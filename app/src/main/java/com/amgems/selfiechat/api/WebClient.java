package com.amgems.selfiechat.api;

import retrofit.RestAdapter;

/**
 * @author shermpay on 2/22/15.
 */
public class WebClient {
    private SelfieChatService mSelfieChatService;
    public WebClient() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(SelfieChatService.ENDPOINT)
                .build();

        mSelfieChatService = restAdapter.create(SelfieChatService.class);
    }

    public SelfieChatService getSelfieChatService() {
        return mSelfieChatService;
    }
}
