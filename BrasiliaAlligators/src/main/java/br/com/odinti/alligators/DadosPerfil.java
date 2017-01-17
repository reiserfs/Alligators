package br.com.odinti.alligators;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by thiago on 16/01/17.
 */

public class DadosPerfil extends StringRequest {
    private static final String URL_LOGIN = "http://dev.associados.alligatorsfa.com.br/perfil/index.json";
    private Map<String, String> params;

    public DadosPerfil(Response.Listener<String> listener){
        super(Method.POST, URL_LOGIN, listener, null);
        params = new HashMap<>();
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
