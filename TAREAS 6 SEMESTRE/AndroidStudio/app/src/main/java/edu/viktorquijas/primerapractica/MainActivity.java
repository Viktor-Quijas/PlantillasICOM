package edu.viktorquijas.primerapractica;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    EditText editText1;
    TextView textView1;

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

        editText1 = (EditText) findViewById(R.id.editText1);
        textView1 = (TextView) findViewById(R.id.textView);
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
        } else if (id == R.id.botonIngresar){
            String nombre = editText1.getText().toString();
            textView1.setText("Hola " + nombre);

            Toast.makeText(this,"Pícame ñya", Toast.LENGTH_SHORT).show();
        }
    }
}