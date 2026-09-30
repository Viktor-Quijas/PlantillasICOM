package edu.viktorquijas.primerapractica;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collection;

public class ListActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayList<String> names;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        listView = (ListView) findViewById(R.id.listView);
        names = new ArrayList<>();

        names.add("");
        names.add("Kris");
        names.add("Susie");
        names.add("Ralsei");
        names.add("Noelle");
        names.add("");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, names);

        listView.setAdapter(arrayAdapter);

        listView.

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String saludo = "";
                int toastLength = 0;
                position -= 1;

                switch (position){
                    case 0:
                        saludo = "Kris: ...";
                        break;
                    case 1:
                        saludo = "Susie: STOP touching me, or you'll feel my AXE!";
                        break;
                    case 2:
                        saludo = "Ralsei: Hi, we were waiting for you";
                        break;
                    case 3:
                        saludo = "Noelle: Fahaha, stop i'm ticklish";
                        break;
                    case 4:
                        saludo = "INTERESTING...";
                        toastLength = Toast.LENGTH_LONG;
                        break;
                }


                Toast.makeText(ListActivity.this, saludo, toastLength).show();
            }
        });

    }
}
