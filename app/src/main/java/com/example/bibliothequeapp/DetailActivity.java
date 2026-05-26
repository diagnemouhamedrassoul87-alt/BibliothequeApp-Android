package com.example.bibliothequeapp;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class DetailActivity extends AppCompatActivity {

    TextView tvTitre, tvAuteur, tvIsbn, tvDisponibilite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        tvTitre = findViewById(R.id.tvTitre);
        tvAuteur = findViewById(R.id.tvAuteur);
        tvIsbn = findViewById(R.id.tvIsbn);
        tvDisponibilite = findViewById(R.id.tvDisponibilite);

        Livre livre = (Livre) getIntent().getSerializableExtra("livre");

        if (livre != null) {
            tvTitre.setText(livre.getTitre());
            tvAuteur.setText("Auteur : " + livre.getAuteur());
            tvIsbn.setText("ISBN : " + livre.getIsbn());
            if (livre.isDisponible()) {
                tvDisponibilite.setText("Disponible");
            } else {
                tvDisponibilite.setText("Indisponible");
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}