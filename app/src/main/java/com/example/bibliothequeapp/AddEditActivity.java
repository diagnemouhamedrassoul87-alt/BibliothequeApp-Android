package com.example.bibliothequeapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class AddEditActivity extends AppCompatActivity {

    public static final String EXTRA_MODE = "MODE";
    public static final String EXTRA_LIVRE = "LIVRE";
    public static final String EXTRA_POSITION = "POSITION";
    public static final String MODE_ADD = "ADD";
    public static final String MODE_EDIT = "EDIT";

    private EditText etTitre, etAuteur, etIsbn;
    private Switch switchDisponible;
    private Button btnEnregistrer;
    private TextView tvTitreFormulaire;
    private String mode;
    private Livre livreAModifier;
    private int positionLivre = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);

        tvTitreFormulaire = findViewById(R.id.tvTitreFormulaire);
        etTitre = findViewById(R.id.etTitre);
        etAuteur = findViewById(R.id.etAuteur);
        etIsbn = findViewById(R.id.etIsbn);
        switchDisponible = findViewById(R.id.switchDisponible);
        btnEnregistrer = findViewById(R.id.btnEnregistrer);

        Intent intent = getIntent();
        mode = intent.getStringExtra(EXTRA_MODE);

        if (MODE_EDIT.equals(mode)) {
            tvTitreFormulaire.setText("Modifier le livre");
            livreAModifier = (Livre) intent.getSerializableExtra(EXTRA_LIVRE);
            positionLivre = intent.getIntExtra(EXTRA_POSITION, -1);
            if (livreAModifier != null) {
                etTitre.setText(livreAModifier.getTitre());
                etAuteur.setText(livreAModifier.getAuteur());
                etIsbn.setText(livreAModifier.getIsbn());
                switchDisponible.setChecked(livreAModifier.isDisponible());
            }
        } else {
            mode = MODE_ADD;
            tvTitreFormulaire.setText("Ajouter un livre");
        }

        btnEnregistrer.setOnClickListener(v -> enregistrerLivre());
    }

    private void enregistrerLivre() {
        String titre = etTitre.getText().toString().trim();
        String auteur = etAuteur.getText().toString().trim();
        String isbn = etIsbn.getText().toString().trim();
        boolean disponible = switchDisponible.isChecked();

        if (!validerFormulaire(titre, auteur, isbn)) return;

        Livre livre;
        if (MODE_EDIT.equals(mode) && livreAModifier != null) {
            livre = new Livre(livreAModifier.getId(), titre, auteur, isbn, disponible);
        } else {
            livre = new Livre(0, titre, auteur, isbn, disponible);
        }

        Intent resultIntent = new Intent();
        resultIntent.putExtra(EXTRA_MODE, mode);
        resultIntent.putExtra(EXTRA_LIVRE, livre);
        resultIntent.putExtra(EXTRA_POSITION, positionLivre);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    private boolean validerFormulaire(String titre, String auteur, String isbn) {
        boolean valide = true;
        if (TextUtils.isEmpty(titre)) {
            etTitre.setError("Le titre est obligatoire");
            valide = false;
        }
        if (TextUtils.isEmpty(auteur)) {
            etAuteur.setError("L'auteur est obligatoire");
            valide = false;
        }
        if (!TextUtils.isEmpty(isbn) && isbn.length() < 10) {
            etIsbn.setError("L'ISBN doit contenir au moins 10 caractères");
            valide = false;
        }
        return valide;
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}