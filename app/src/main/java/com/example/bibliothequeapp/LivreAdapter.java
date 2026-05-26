package com.example.bibliothequeapp;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class LivreAdapter extends RecyclerView.Adapter<LivreAdapter.LivreViewHolder> {

    public interface OnLivreClickListener {
        void onLivreClick(Livre livre);
        void onLivreLongClick(Livre livre, int position);
    }

    private List<Livre> listeLivres;
    private OnLivreClickListener listener;

    public LivreAdapter(List<Livre> listeLivres, OnLivreClickListener listener) {
        this.listeLivres = listeLivres;
        this.listener = listener;
    }

    @NonNull
    @Override
    public LivreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_livre, parent, false);
        return new LivreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LivreViewHolder holder, int position) {
        Livre livre = listeLivres.get(position);

        holder.tvTitreLivre.setText(livre.getTitre());
        holder.tvAuteurLivre.setText("Auteur : " + livre.getAuteur());
        holder.tvIsbnLivre.setText("ISBN : " + livre.getIsbn());

        if (livre.isDisponible()) {
            holder.tvDisponibilite.setText("Disponible");
            holder.tvDisponibilite.setBackgroundColor(Color.parseColor("#2E7D32"));
        } else {
            holder.tvDisponibilite.setText("Indisponible");
            holder.tvDisponibilite.setBackgroundColor(Color.parseColor("#C62828"));
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onLivreClick(livre);
        });

        holder.itemView.setOnLongClickListener(v -> {
            if (listener != null) {
                int pos = holder.getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION)
                    listener.onLivreLongClick(livre, pos);
            }
            return true;
        });
    }

    @Override
    public int getItemCount() { return listeLivres.size(); }

    public static class LivreViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitreLivre, tvAuteurLivre, tvIsbnLivre, tvDisponibilite;

        public LivreViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitreLivre = itemView.findViewById(R.id.tvTitreLivre);
            tvAuteurLivre = itemView.findViewById(R.id.tvAuteurLivre);
            tvIsbnLivre = itemView.findViewById(R.id.tvIsbnLivre);
            tvDisponibilite = itemView.findViewById(R.id.tvDisponibilite);
        }
    }
}