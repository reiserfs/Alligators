package br.com.odinti.alligators;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by thiago on 12/01/17.
 */

public class RegistroCadastro extends StringRequest {

    private static final String URL_CADASTRO = "http://dev.associados.alligatorsfa.com.br/login/cadastro.json";
    private Map<String, String> params;

    public RegistroCadastro(String email, int cpf, String data, String password, String password2, Response.Listener<String> listener){
        super(Method.POST, URL_CADASTRO, listener, null);
        params = new HashMap<>();
        params.put("email",email);
        params.put("nascimento",data);
        params.put("cpf",Integer.toString(cpf));
        params.put("senha",password);
        params.put("confirma_senha",password2);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
