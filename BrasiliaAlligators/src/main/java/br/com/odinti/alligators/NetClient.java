package br.com.odinti.alligators;

import android.app.Activity;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

/**
 * Created by thiago on 16/01/17.
 */

public class NetClient extends AsyncTask<HashMap<String, String>,String,String> {
    private HttpURLConnection client = null;
    private InputStream is = null;
    private OutputStream os = null;
    private URL dominio = null;
    public static final int CONNECTION_TIMEOUT=10000;
    public static final int READ_TIMEOUT=15000;
    private ProgressDialog carregando = null;

    public interface AsyncResponse {
        void processFinish(String output) throws JSONException;
    }

    public AsyncResponse delegate = null;

    public NetClient(Activity con,AsyncResponse delegate){
        carregando = new ProgressDialog(con);
        this.delegate = delegate;
    }

    @Override
    protected void onPreExecute(){
        super.onPreExecute();

        carregando.setMessage("\tCarregando...");
        carregando.setCancelable(false);
        carregando.show();
    }

    @Override
    protected String doInBackground(HashMap<String, String>... params) {

        final HashMap<String, String> args = params[0];
        final Uri.Builder builder = new Uri.Builder();

        try {
            dominio = new URL("http://dev.associados.alligatorsfa.com.br/" + args.get("url"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return "exception";
        }

        try {
            client = (HttpURLConnection) dominio.openConnection();
            client.setReadTimeout(READ_TIMEOUT);
            client.setConnectTimeout(CONNECTION_TIMEOUT);
            client.setRequestMethod("POST");

            client.setDoInput(true);
            client.setDoOutput(true);

            for(HashMap.Entry<String, String> entry : args.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();

                builder.appendQueryParameter(key, value);
            }

            String query = builder.build().getEncodedQuery();

            os = client.getOutputStream();

            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));

            writer.write(query);
            writer.flush();
            writer.close();
            os.close();

            client.connect();
        } catch (IOException e) {
            e.printStackTrace();
            return "exception";
        }

        try {
            int response_code = client.getResponseCode();

            if (response_code == HttpURLConnection.HTTP_OK) {
                is = client.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));

                StringBuilder result = new StringBuilder();
                String line;

                while ((line = reader.readLine()) !=null) {
                    result.append(line);
                }

                return(result.toString());
            }else{
                return("unsuccessful");
            }

        } catch (IOException e) {
            e.printStackTrace();
            return "exception";
        } finally {
            client.disconnect();
        }
    }

    protected void onPostExecute(String result){
        carregando.dismiss();
        try {
            delegate.processFinish(result);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
