package br.com.odinti.alligators;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

/**
 * Created by thiago on 16/01/17.
 */

public class ImgNetClient extends AsyncTask<HashMap<String, String>,Drawable,Drawable> {
    private HttpURLConnection client = null;
    private InputStream is = null;
    private URL dominio = null;
    public static final int CONNECTION_TIMEOUT=10000;
    public static final int READ_TIMEOUT=15000;
    private ProgressDialog carregando = null;

    public interface AsyncResponse {
        void processFinish(Drawable output) throws JSONException;
    }

    public AsyncResponse delegate = null;

    public ImgNetClient(Activity con, AsyncResponse delegate){
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
    protected Drawable doInBackground(HashMap<String, String>... params) {

        final HashMap<String, String> args = params[0];
        final Drawable erro = null;

        try {
            dominio = new URL("http://dev.associados.alligatorsfa.com.br/" + args.get("url"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return erro;
        }

        try {

            client = (HttpURLConnection) dominio.openConnection();
            client.setReadTimeout(READ_TIMEOUT);
            client.setConnectTimeout(CONNECTION_TIMEOUT);
            client.setRequestMethod("GET");
            client.connect();

        } catch (IOException e) {
            e.printStackTrace();
            return erro;
        }

        try {
            int response_code = client.getResponseCode();

            if (response_code == HttpURLConnection.HTTP_OK) {
                is = client.getInputStream();
                Drawable d = Drawable.createFromStream(is, "srcname");
                //Log.d("V",String.valueOf("GETDRAW: "+ is));
                return(d);

            }else{
                return(erro);
            }

        } catch (IOException e) {
            e.printStackTrace();
            return erro;
        } finally {
            client.disconnect();
        }
    }

    protected void onPostExecute(Drawable result){
        carregando.dismiss();
        try {
            delegate.processFinish(result);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
