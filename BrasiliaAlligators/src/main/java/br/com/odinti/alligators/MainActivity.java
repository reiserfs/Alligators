package br.com.odinti.alligators;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText etUser = (EditText) findViewById(R.id.etUser);
        final Button btLogout = (Button) findViewById(R.id.btLogout);

        Intent intent = getIntent();
        String email = intent.getStringExtra("email");

        String messsage = email + " Logado ";
        etUser.setText(email);
    }
}
