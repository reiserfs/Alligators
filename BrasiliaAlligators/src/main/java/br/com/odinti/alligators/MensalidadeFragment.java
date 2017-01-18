package br.com.odinti.alligators;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.HashMap;

/**
 * Created by thiago on 13/01/17.
 */

public class MensalidadeFragment extends Fragment {

    View myView;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.mensalidade_layout, container, false);

        final HashMap<String, String> args = new HashMap<String, String>();
        args.put("url","perfil/mens.json");

        NetClient netClient = (NetClient) new NetClient(getActivity(), new NetClient.AsyncResponse() {
            @Override
            public void processFinish(String output) throws JSONException {
                boolean jsongood = true;
                JSONArray mensalidade = null;
                Log.d("V",String.valueOf("OUTPUT: "+ output));
                try {
                    mensalidade = new JSONArray(output);
                } catch (JSONException e) {
                     e.printStackTrace();
                    jsongood = false;
                }
                if (jsongood) {
                    if (mensalidade.length() > 0) {
                        for (int i = 0; i < mensalidade.length(); i++) {
                            JSONObject row = mensalidade.getJSONObject(i);

                            TableRow rowView = new TableRow(myView.getContext());
                            final TableLayout tabela = (TableLayout) myView.findViewById(R.id.tbTabela);
                            DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getActivity());
                            LayoutInflater inflator = getActivity().getLayoutInflater();
                            inflator.inflate(R.layout.mensalidade_linhas, rowView);
                            final TextView vencimento = (TextView) rowView.findViewById(R.id.tvpVencimento);
                            final TextView pagamento = (TextView) rowView.findViewById(R.id.tvpPagamento);
                            final TextView valor = (TextView) rowView.findViewById(R.id.tvpValor);
                            final TextView pago = (TextView) rowView.findViewById(R.id.tvpPago);

                            //vencimento.setText(dateFormat.format(row.getString(("vencimento"))));
                            vencimento.setText(row.getString(("vencimento")));
                            pagamento.setText(row.getString(("pago")));
                            valor.setText(row.getString(("valor_base")));
                            pago.setText(row.getString(("valor_pago")));

                            tabela.addView(rowView);
                           // id = row.getInt("id");
                           // name = row.getString("name");
                        }

                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setMessage("Mensalidades não encontradas")
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
