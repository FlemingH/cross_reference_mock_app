package com.volvo.crm;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MockController {

    private static OkHttpClient client = new OkHttpClient();

    public String getMockList() {

        String urlTemplate = "https://piut7crwt6.execute-api.ap-east-1.amazonaws.com/default/lambda-get-s3-mock-list";
        Request request = new Request.Builder().url(urlTemplate).get().build();

        Call call = client.newCall(request);

        try {
            Response response = call.execute();
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
