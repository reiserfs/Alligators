package br.com.odinti.alligators;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by thiago on 13/01/17.
 */

public class PerfilUsuario extends Fragment {

    View myView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.perfil_usuario, container, false);

        final TextView tvpNome = (TextView) myView.findViewById(R.id.tvpNome);
        final TextView tvpEmail = (TextView) myView.findViewById(R.id.tvpEmail);

        LinearLayout ll = (LinearLayout) myView.findViewById(R.id.llOne);

        final int N = 10; // total number of textviews to add

        final TextView[] myTextViews = new TextView[N]; // create an empty array;

        for (int i = 0; i < N; i++) {
            // create a new textview
            final TextView rowTextView = new TextView(getActivity());

            // set some properties of rowTextView or something
            rowTextView.setText("This is row #" + i);
            rowTextView.setId(i+5);

            // add the textview to the linearlayout
            ll.addView(rowTextView);


            // save a reference to the textview for later
            myTextViews[i] = rowTextView;
        }

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");

                    if(success){
                        Log.d("V",String.valueOf(success));
                    }
                    else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setMessage("Falha ao carregar dados do perfil")
                                .setNegativeButton("Tentar Novamente",null)
                                .create()
                                .show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };

        DadosPerfil dadosPerfil = new DadosPerfil(responseListener);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(dadosPerfil);

        return myView;


    }

}
