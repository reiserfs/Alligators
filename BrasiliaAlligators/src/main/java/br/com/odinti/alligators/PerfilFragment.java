package br.com.odinti.alligators;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by thiago on 13/01/17.
 */

public class PerfilFragment extends Fragment {

    View myView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.perfil_layout, container, false);

        final TextView tvpNome = (TextView) myView.findViewById(R.id.tvpNome);
        final TextView tvpEmail = (TextView) myView.findViewById(R.id.tvpEmail);
        final HashMap<String, String> args = new HashMap<String, String>();
        final HashMap<String, String> imgargs = new HashMap<String, String>();
        final ImageView ivFoto = (ImageView) myView.findViewById(R.id.ivFoto);
        final LocalStore localStore = new LocalStore(getActivity());

        final String[] blacklist = {"time","ativo","nome","sobrenome","email","id","cart_old","foto","foto_size","foto_type","plano","inventario"};

        args.put("url","perfil/index.json");

        NetClient netClient = (NetClient) new NetClient(getActivity(), new NetClient.AsyncResponse() {
            @Override
            public void processFinish(String output) throws JSONException {
                boolean jsongood = true;
                JSONObject associado = null;
                try {
                    associado = new JSONObject(output).getJSONObject("associado");
                } catch (JSONException e) {
                    e.printStackTrace();
                    jsongood = false;
                }
                if (jsongood) {
                    final int success = associado.getInt("id");

                    if (success > 0) {
                        tvpNome.setText(associado.getString("nome") + " " + associado.getString("sobrenome"));
                        tvpEmail.setText(associado.getString("email"));

                        imgargs.put("url","associados/imgfoto/" + success);

                        ImgNetClient imgNetClient = (ImgNetClient) new ImgNetClient(getActivity(), new ImgNetClient.AsyncResponse() {
                            @Override
                            public void processFinish(Drawable output) throws JSONException {
                                Log.d("V",String.valueOf(output));
                                ivFoto.setImageDrawable(output);
                            }
                        }).execute(imgargs);

                        final LinearLayout ll = (LinearLayout) myView.findViewById(R.id.llOne);

                        for (Iterator<String> iter = associado.keys(); iter.hasNext(); ) {
                            String key = iter.next();
                            if (!Arrays.asList(blacklist).contains(key)) {
                                final TextView rowTextView = new TextView(getActivity());
                                rowTextView.setText(key);
                                rowTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
                                rowTextView.setTypeface(null, Typeface.BOLD);
                                rowTextView.setTextColor(0xFF000000);
                                ll.addView(rowTextView);

                                final TextView rowTextViewb = new TextView(getActivity());
                                rowTextViewb.setText(associado.getString(key).toString());
                                ll.addView(rowTextViewb);
                            }
                        }

                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setMessage("Perfil não encontrado")
                                .setNegativeButton("Tentar Novamente", null)
                                .create()
                                .show();
                    }
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("Você não esta logado")
                            .setNegativeButton("Ir para tela de Login", null)
                            .create()
                            .show();
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    getActivity().startActivity(intent);
                }
            }
        }).execute(args);

        return myView;
    }
}
