package com.example.bibliothequeapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerViewLivres;
    private FloatingActionButton fabAjouterLivre;
    private LivreAdapter livreAdapter;
    private List<Livre> listeLivres;
    private AppDatabase database;
    private ExecutorService executorService;
    private ActivityResultLauncher<Intent> addEditLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerViewLivres = findViewById(R.id.recyclerViewLivres);
        fabAjouterLivre = findViewById(R.id.fabAjouterLivre);
        database = AppDatabase.getInstance(this);
        executorService = Executors.newSingleThreadExecutor();
        listeLivres = new ArrayList<>();

        livreAdapter = new LivreAdapter(listeLivres, new LivreAdapter.OnLivreClickListener() {
            @Override
            public void onLivreClick(Livre livre) {
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra("livre", livre);
                startActivity(intent);
            }

            @Override
            public void onLivreLongClick(Livre livre, int position) {
                afficherOptions(livre);
            }
        });

        recyclerViewLivres.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewLivres.setAdapter(livreAdapter);

        addEditLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Intent data = result.getData();
                        Livre livre = (Livre) data.getSerializableExtra(AddEditActivity.EXTRA_LIVRE);
                        String mode = data.getStringExtra(AddEditActivity.EXTRA_MODE);
                        if (livre == null) return;
                        if (AddEditActivity.MODE_ADD.equals(mode)) {
                            ajouterLivre(livre);
                        } else {
                            modifierLivre(livre);
                        }
                    }
                }
        );

        fabAjouterLivre.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddEditActivity.class);
            intent.putExtra(AddEditActivity.EXTRA_MODE, AddEditActivity.MODE_ADD);
            addEditLauncher.launch(intent);
        });

        chargerLivres();
    }

    private void chargerLivres() {
        executorService.execute(() -> {
            List<Livre> livres = database.livreDao().getAllLivres();
            runOnUiThread(() -> {
                listeLivres.clear();
                listeLivres.addAll(livres);
                livreAdapter.notifyDataSetChanged();
            });
        });
    }

    private void ajouterLivre(Livre livre) {
        executorService.execute(() -> {
            livre.setId(0);
            database.livreDao().insert(livre);
            runOnUiThread(() -> {
                Toast.makeText(this, "Livre ajouté !", Toast.LENGTH_SHORT).show();
                chargerLivres();
            });
        });
    }

    private void modifierLivre(Livre livre) {
        executorService.execute(() -> {
            database.livreDao().update(livre);
            runOnUiThread(() -> {
                Toast.makeText(this, "Livre modifié !", Toast.LENGTH_SHORT).show();
                chargerLivres();
            });
        });
    }

    private void supprimerLivre(Livre livre) {
        executorService.execute(() -> {
            database.livreDao().delete(livre);
            runOnUiThread(() -> {
                Toast.makeText(this, "Livre supprimé !", Toast.LENGTH_SHORT).show();
                chargerLivres();
            });
        });
    }

    private void afficherOptions(Livre livre) {
        new AlertDialog.Builder(this)
                .setTitle(livre.getTitre())
                .setItems(new String[]{"Modifier", "Supprimer"}, (dialog, which) -> {
                    if (which == 0) {
                        Intent intent = new Intent(this, AddEditActivity.class);
                        intent.putExtra(AddEditActivity.EXTRA_MODE, AddEditActivity.MODE_EDIT);
                        intent.putExtra(AddEditActivity.EXTRA_LIVRE, livre);
                        addEditLauncher.launch(intent);
                    } else {
                        new AlertDialog.Builder(this)
                                .setTitle("Supprimer")
                                .setMessage("Confirmer la suppression ?")
                                .setPositiveButton("Supprimer", (d, w) -> supprimerLivre(livre))
                                .setNegativeButton("Annuler", null)
                                .show();
                    }
                }).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (executorService != null) executorService.shutdown();
    }
}