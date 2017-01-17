package br.com.odinti.alligators;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class RegistroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        final EditText etEmail = (EditText) findViewById(R.id.etEmail);
        final EditText etCpf = (EditText) findViewById(R.id.etCpf);
        final EditText etData = (EditText) findViewById(R.id.etData);
        final EditText etPassword = (EditText) findViewById(R.id.etPassword);
        final EditText etPassword2 = (EditText) findViewById(R.id.etPassword2);

        final Button btRegistro = (Button) findViewById(R.id.btRegistro);

        btRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = etEmail.getText().toString();
                final int cpf = Integer.parseInt(etCpf.getText().toString());
                final String data = etData.getText().toString();
                final String password = etPassword.getText().toString();
                final String password2 = etPassword2.getText().toString();

                Response.Listener<String> responseListener = new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");

                            if (success) {
                                Intent intent = new Intent(RegistroActivity.this, LoginActivity.class);
                                RegistroActivity.this.startActivity(intent);
                            }
                            else{
                                AlertDialog.Builder builder = new AlertDialog.Builder(RegistroActivity.this);
                                builder.setMessage("Falha no Cadastro")
                                        .setNegativeButton("Tentar Novamente",null)
                                        .create()
                                        .show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                RegistroCadastro registroCadastro = new RegistroCadastro(email,cpf,data,password,password2,responseListener);
                RequestQueue queue = Volley.newRequestQueue(RegistroActivity.this);
                queue.add(registroCadastro);
            }
        });
    }
}
