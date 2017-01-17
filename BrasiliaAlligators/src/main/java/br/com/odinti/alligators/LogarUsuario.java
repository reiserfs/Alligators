package br.com.odinti.alligators;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by thiago on 12/01/17.
 */

public class LogarUsuario extends StringRequest {
    private static final String URL_LOGIN = "http://dev.associados.alligatorsfa.com.br/login/login.json";
    private Map<String, String> params;

    public LogarUsuario(String email, String password, Response.Listener<String> listener){
        super(Method.POST, URL_LOGIN, listener, null);
        params = new HashMap<>();
        params.put("user",email);
        params.put("password",password);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
