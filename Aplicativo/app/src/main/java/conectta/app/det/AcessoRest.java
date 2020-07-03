package conectta.app.det;

import android.os.Build;
import android.util.Log;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Lucas on 24/10/2017.
 */

public class AcessoRest {

    private int  TIMEOUT_MILLISEC = 3000;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");


    OkHttpClient client = new OkHttpClient();

    String post(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON,json);
        Request request = new Request.Builder().url(url).post(body).build();
        try(Response response = client.newCall(request).execute()) {
            Log.i("TESTE", ""+ response.body().string());
            return response.body().string();
        }

    }



    String run(String url) throws IOException {
        Request request = new Request.Builder().url(url).build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }

    }



}
