package br.com.odinti.alligators;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by thiago on 16/01/17.
 */

public class NetClient {
    private HttpURLConnection client = null;
    private InputStream is = null;

    public InputStream request(String[] args, String url) throws IOException {
        URL dominio = new URL ("http://dev.associados.alligatorsfa.com.br/" + url);

        client = (HttpURLConnection) dominio.openConnection();
        client.connect();
        is = client.getInputStream();

        return is;
    }

}
