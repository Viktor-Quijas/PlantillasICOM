package com.example.dbmaster;
import android.content.Intent;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Buscar_eliminar_actualizar extends AppCompatActivity {
    EditText Ebuscar,nnombre,napellido;
    TextView nombre, apellido;
    Button Bbuscar,BEliminar,BActualizar,BLista;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_eliminar_actualizar);
        nombre=(TextView)findViewById(R.id.nombre);
        apellido=(TextView)findViewById(R.id.apellido);
        Ebuscar=(EditText)findViewById(R.id.Ebuscar);
        Bbuscar=(Button)findViewById(R.id.Bbuscar);
        BEliminar=(Button)findViewById(R.id.BEliminar);
        nnombre=(EditText)findViewById(R.id.NNombre);
        napellido=(EditText)findViewById(R.id.NApellido);
        BActualizar=(Button)findViewById(R.id.BActualizar);
        BLista=(Button)findViewById(R.id.blista);
        Bbuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DB db = new DB(getApplicationContext(),null,null,1);
                String buscar = Ebuscar.getText().toString();
                String[] datos;
                datos=db.buscar_reg(buscar);
                nombre.setText(datos[0]);
                apellido.setText(datos[1]);
                Toast.makeText(getApplicationContext(),datos[2],Toast.LENGTH_SHORT).show();
            }
        });
        BEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DB db = new DB(getApplicationContext(),null,null,1);
                String Nombre = nombre.getText().toString();
                String mensaje =db.eliminar(Nombre);
                Toast.makeText(getApplicationContext(),mensaje,Toast.LENGTH_SHORT).show();
            }
        });
        BActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DB db = new DB(getApplicationContext(),null,null,1);
                String Buscar = nombre.getText().toString();
                String Nombre = nnombre.getText().toString();
                String Apellido = napellido.getText().toString();
                String Mensaje =db.actualizar(Buscar, Nombre, Apellido);
                Toast.makeText(getApplicationContext(),Mensaje,Toast.LENGTH_SHORT).show();
            }
        });
        BLista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intento = new Intent(getApplicationContext(),Lista.class);
                startActivity(intento);
            }
        });
    }




}
