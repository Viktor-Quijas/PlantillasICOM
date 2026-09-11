package edu.viktorquijas.primerapractica;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;

public class Opcion extends AppCompatActivity implements View.OnClickListener {

    private TextView txtRes;
    private EditText edt1, edt2;
    private Button btnSumar, btnRestar, btnMultiplicar, btnDividir, btnLimpiar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opcion);

        txtRes = (TextView) findViewById(R.id.lblResultado);
        edt1 = (EditText) findViewById(R.id.txtPrimerOperando);
        edt2 = (EditText) findViewById(R.id.txtSegundoOperando);
        btnSumar = (Button) findViewById(R.id.btnSumar);
        btnRestar = (Button) findViewById(R.id.btnRestar);
        btnMultiplicar = (Button) findViewById(R.id.btnMultiplicar);
        btnDividir = (Button) findViewById(R.id.btnDividir);
        btnLimpiar = (Button) findViewById(R.id.btnLimpiar);

        btnSumar.setOnClickListener(this);
        btnRestar.setOnClickListener(this);
        btnMultiplicar.setOnClickListener(this);
        btnDividir.setOnClickListener(this);
        btnLimpiar.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        double x, y, res = 0;
        String strX, strY;

        if (v.getId() == R.id.btnLimpiar){
            edt1.setText("");
            edt2.setText("");
            txtRes.setText("0");
            return;
        }

        strX = edt1.getText().toString().trim();
        strY = edt2.getText().toString().trim();

        if (strX.isEmpty() || strY.isEmpty()) {
            Toast.makeText(this, "Por favor llena ambos campos", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            x = Double.parseDouble(strX);
            y = Double.parseDouble(strY);

        } catch (NumberFormatException e){
            Toast.makeText(this,"Solo carácteres numéricos", Toast.LENGTH_SHORT).show();
            return;
        }

        if (v.getId() == R.id.btnSumar){
            res = x + y;
        } else if (v.getId() == R.id.btnRestar) {
            res = x - y;
        } else if (v.getId() == R.id.btnMultiplicar){
            res = x * y;
        } else if (v.getId() == R.id.btnDividir){
            if (y == 0) {
                Toast.makeText(this, "No se puede dividir entre cero", Toast.LENGTH_SHORT).show();
                return;
            }
            res = x / y;
        }

        txtRes.setText(String.valueOf(res));
    }
}
