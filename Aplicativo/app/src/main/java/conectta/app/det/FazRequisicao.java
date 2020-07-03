package conectta.app.det;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FazRequisicao extends AsyncTask<Object, Void, String> {

    Context context;

    String json = null;
    public final OkHttpClient client = new OkHttpClient();
    public List<double[]> lista = new ArrayList<double[]>();



    public FazRequisicao(Context context) {
        this.context = context;
    }

    @Override
    public String doInBackground(Object... objects) {

//        Long timestamp = (Long) objects[0];
//        InfoSensores infoTemp = (InfoSensores) objects[1];
        String fog = (String) objects[0];
        String port = (String) objects[1];
        String route = (String) objects[2];
        int aux = (Integer) objects[3];
        String json = (String) objects[4];

        Log.i("AsyncTask", "Thread: " +
                Thread.currentThread().getName() + " - " +aux);

        try {
//            String url = "http://"+fog+":"+port+"/"+route;
            String url = "http://192.168.1.197:5000/test";

//                    Gson gson = new Gson();

//            Gson gson = new GsonBuilder().setPrettyPrinting().create();
//            InfoSensores info = new InfoSensores(infoTemp.getLista(), timestamp);
//            json = gson.toJson(info, info.getClass());
            Log.i("APRESENTACAO NO ASYNC", json);
            post(url, json);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "Cabou";
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.i("STATUS", getStatus()+"");
        Log.i("ACABOU", s);
    }

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    void post(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        Response response = client.newCall(request).execute();
        response.close();
            if(response.isSuccessful()) {
                Log.i("RESULTADO", "Sucesso" + response.code());
            } else{
                Log.i("RESULTADO", "Deu ruim" + response.code());
            }

    }



}
