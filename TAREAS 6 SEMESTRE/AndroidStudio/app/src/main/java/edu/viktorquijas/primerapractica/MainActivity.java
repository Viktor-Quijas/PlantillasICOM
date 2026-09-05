package edu.viktorquijas.primerapractica;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button b1, b2, b3;

        b1 = (Button) findViewById(R.id.botonCreditos);
        b2 = (Button) findViewById(R.id.botonPerfil);
        b3 = (Button) findViewById(R.id.botonRefresh);

        b1.setOnClickListener(this);
        b2.setOnClickListener(this);
        b3.setOnClickListener(this);
    }

    @Override
    public void onClick(View v){
        int id = v.getId();

        if (id == R.id.botonCreditos){
            Intent intent = new Intent(MainActivity.this, BotonCreditos.class);
            startActivity(intent);
        } else if (id == R.id.botonPerfil){
            Intent intent = new Intent(MainActivity.this, BotonPerfil.class);
            startActivity(intent);
        } else if (id == R.id.botonRefresh){
            Intent intent = new Intent(MainActivity.this, Inicio.class);
            startActivity(intent);
        }
    }
}