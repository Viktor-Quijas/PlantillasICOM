package edu.viktorquijas.primerapractica;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class BotonPerfil extends AppCompatActivity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vista_boton_perfil);

        Button btnRegresar = (Button) findViewById(R.id.botonRegresarPerfil);
        btnRegresar.setOnClickListener(this);

    }
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(BotonPerfil.this, MainActivity.class);
        startActivity(intent);
    }
}
