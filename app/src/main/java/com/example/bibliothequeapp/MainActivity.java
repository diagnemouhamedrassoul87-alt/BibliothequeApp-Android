package com.example.bibliothequeapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_ADD_EDIT = 100;
    private RecyclerView recyclerViewLivres;
    private LivreAdapter livreAdapter;
    private ArrayList<Livre> listeLivres;
    private FloatingActionButton fabAjouterLivre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerViewLivres = findViewById(R.id.recyclerViewLivres);
        fabAjouterLivre = findViewById(R.id.fabAjouterLivre);

        initialiserLivres();

        recyclerViewLivres.setLayoutManager(new LinearLayoutManager(this));
        livreAdapter = new LivreAdapter(listeLivres);
        recyclerViewLivres.setAdapter(livreAdapter);

        fabAjouterLivre.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddEditActivity.class);
            intent.putExtra(AddEditActivity.EXTRA_MODE, AddEditActivity.MODE_ADD);
            startActivityForResult(intent, REQUEST_ADD_EDIT);
        });
    }

    private void initialiserLivres() {
        listeLivres = new ArrayList<>();
        listeLivres.add(new Livre(1, "Le Petit Prince", "Antoine de Saint-Exupéry", "9780156013987", true));
        listeLivres.add(new Livre(2, "L'Étranger", "Albert Camus", "9782070360024", false));
        listeLivres.add(new Livre(3, "Les Misérables", "Victor Hugo", "9782253096344", true));
        listeLivres.add(new Livre(4, "Une si longue lettre", "Mariama Bâ", "9782841290529", true));
        listeLivres.add(new Livre(5, "Le Vieux Nègre et la Médaille", "Ferdinand Oyono", "9782264018304", false));
        listeLivres.add(new Livre(6, "Madame Bovary", "Gustave Flaubert", "9782070409228", true));
        listeLivres.add(new Livre(7, "La Peste", "Albert Camus", "9782070360420", false));
        listeLivres.add(new Livre(8, "Sous l'orage", "Seydou Badian", "9782708707691", true));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ADD_EDIT && resultCode == RESULT_OK && data != null) {
            String mode = data.getStringExtra(AddEditActivity.EXTRA_MODE);
            Livre livre = (Livre) data.getSerializableExtra(AddEditActivity.EXTRA_LIVRE);
            int position = data.getIntExtra(AddEditActivity.EXTRA_POSITION, -1);

            if (livre == null) return;

            if (AddEditActivity.MODE_ADD.equals(mode)) {
                livre.setId(listeLivres.size() + 1);
                listeLivres.add(livre);
                Toast.makeText(this, "Livre ajouté !", Toast.LENGTH_SHORT).show();
            } else if (AddEditActivity.MODE_EDIT.equals(mode) && position >= 0) {
                listeLivres.set(position, livre);
                Toast.makeText(this, "Livre modifié !", Toast.LENGTH_SHORT).show();
            }
            livreAdapter.notifyDataSetChanged();
        }
    }
}