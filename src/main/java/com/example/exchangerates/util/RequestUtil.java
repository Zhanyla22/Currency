package com.example.exchangerates.util;

import okhttp3.*;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class RequestUtil {

    OkHttpClient client = new OkHttpClient();

    public static Request getRequest(String url) {
        return new Request.Builder()
                .url(url)
                .get()
                .build();
    }

    public String executeRequest(HttpUrl uri) throws Exception {
        Request request = new Request.Builder().url(uri).build();

        Call call = client.newCall(request);
        Response response = call.execute();
        if (response.isSuccessful())
            return Objects.requireNonNull(response.body()).string();
        return null;
    }
}