package br.com.justworks.prestador.ServicoAki.Util;

// Uso:
// Declarar um objeto TextWatcher
// O valor do TextWatcher é o retorno da função Mask.insert, com dois parametros: string com o formato da mascara e a caixa de texto que irá receber a mascara
// ou o retorno da função Mask.monetario, apenas com a caixa de texto que será receberá o valor monetario
// Adicionar na caixa de texto o evento TextWatcher
// Ex:
// TextWatcher cpfMask = Mask.insert("###.###.###-##, editCpf);
// cpfMask.addTextChangedListener(editCpf)
// TextWatcher salarioMask = Mask.monetario(editSalario);
// salarioMask.addTextChangedListener(editSalario);

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.text.NumberFormat;

public abstract class Mask {
    public static String unmask(String s) {
        return s.replaceAll("[.]", "").replaceAll("[-]", "")
                .replaceAll("[/]", "").replaceAll("[(]", "")
                .replaceAll("[)]", "");
    }

    public static TextWatcher insert(final String mask, final EditText ediTxt) {
        return new TextWatcher() {
            boolean isUpdating;
            String old = "";

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                String str = Mask.unmask(s.toString());
                String mascara = "";
                if (isUpdating) {
                    old = str;
                    isUpdating = false;
                    return;
                }
                int i = 0;
                for (char m : mask.toCharArray()) {
                    if (m != '#' && str.length() > old.length()) {
                        mascara += m;
                        continue;
                    }
                    try {
                        mascara += str.charAt(i);
                    } catch (Exception e) {
                        break;
                    }
                    i++;
                }
                isUpdating = true;
                ediTxt.setText(mascara);
                ediTxt.setSelection(mascara.length());
            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            public void afterTextChanged(Editable s) {
            }
        };
    }

    public static TextWatcher monetario(final EditText ediTxt) {
        return new TextWatcher() {
            // Mascara monetaria para o preço do produto
            private String current = "";
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void afterTextChanged(Editable s) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().equals(current)){
                    ediTxt.removeTextChangedListener(this);

                    String cleanString = s.toString().replaceAll("[R$,.]", "");

                    float parsed = Float.parseFloat(cleanString);
                    String formatted = NumberFormat.getCurrencyInstance().format((parsed/100));

                    current = formatted.replaceAll("[R$]", "");
                    ediTxt.setText(current);
                    ediTxt.setSelection(current.length());

                    ediTxt.addTextChangedListener(this);
                }
            }

        };
    }
}