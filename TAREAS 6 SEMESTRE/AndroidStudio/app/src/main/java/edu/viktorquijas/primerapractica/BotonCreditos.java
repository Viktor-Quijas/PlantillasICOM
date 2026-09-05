package edu.viktorquijas.primerapractica;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class BotonCreditos extends AppCompatActivity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vista_boton_creditos);

        Button btnRegresar = (Button) findViewById(R.id.botonRegresarCreditos);
        btnRegresar.setOnClickListener(this);

    }
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(BotonCreditos.this, MainActivity.class);
        startActivity(intent);
    }
}
