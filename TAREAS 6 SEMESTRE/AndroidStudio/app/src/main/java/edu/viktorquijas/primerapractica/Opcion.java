package edu.viktorquijas.primerapractica;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Opcion extends AppCompatActivity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView res;
        EditText edt1, edt2;
        Button btnSumar, btnRestar, btnMultiplicar, btnDividir, btnLimpiar;

        res = (TextView) findViewById(R.id.lblResultado);
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

    }
}
