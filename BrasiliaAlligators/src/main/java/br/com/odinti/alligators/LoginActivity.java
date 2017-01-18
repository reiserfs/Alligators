package br.com.odinti.alligators;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.util.HashMap;


public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        CookieManager cookieManager = new CookieManager();
        CookieHandler.setDefault(cookieManager);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText etEmail = (EditText) findViewById(R.id.etEmail);
        final EditText etPassword = (EditText) findViewById(R.id.etPassword);
        final TextView txCadastro = (TextView) findViewById(R.id.txCadastro);
        final Button btLogin = (Button) findViewById(R.id.btLogin);
        final LocalStore localStore = new LocalStore(this);
        final Login login = localStore.getLoggedIn();

        if (login.user!=null) {
            etEmail.setText(login.user);
            etPassword.setText(login.password);
        }

        txCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(LoginActivity.this, RegistroActivity.class);
                LoginActivity.this.startActivity(registerIntent);
            }
        });

        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final HashMap<String, String> args = new HashMap<String, String>();

                args.put("user",etEmail.getText().toString());
                args.put("password",etPassword.getText().toString());
                args.put("url","login/login.json");

                final AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);

                NetClient netClient = (NetClient) new NetClient(LoginActivity.this, new NetClient.AsyncResponse() {
                    @Override
                    public void processFinish(String output) throws JSONException {

                        JSONObject jsonResponse = new JSONObject(output);
                        boolean success = jsonResponse.getBoolean("success");

                        if(success){
                            Login login = new Login(etEmail.getText().toString(),etPassword.getText().toString());
                            localStore.storeData(login);

                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                            LoginActivity.this.startActivity(intent);
                        }
                        else {

                            builder.setMessage("Falha no Login")
                                    .setNegativeButton("Tentar Novamente",null)
                                    .create()
                                    .show();
                        }
                    }
                }).execute(args);
            }
        });
    }
}
