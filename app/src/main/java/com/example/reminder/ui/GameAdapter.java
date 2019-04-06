package com.example.reminder.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.reminder.R;
import com.example.reminder.model.Game;

import java.util.List;

public class GameAdapter extends RecyclerView.Adapter<GameAdapter.ViewHolder> {

    private List<Game> mGames;

    public GameAdapter(List<Game> mGames) {
        this.mGames = mGames;
    }

    @NonNull
    @Override
    public GameAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.custom_layout, null);
        // Return a new holder instance
        GameAdapter.ViewHolder viewHolder = new GameAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull GameAdapter.ViewHolder viewHolder, int i) {
        Game game = mGames.get(i);
        viewHolder.titel.setText(game.getTitel());
        viewHolder.platform.setText(game.getPlatform());
        viewHolder.datum.setText(game.getDatum());
        viewHolder.status.setText(game.getStatus());
    }

    @Override
    public int getItemCount() {
        return mGames.size();
    }

    public void swapList(List<Game> newList) {
        mGames = newList;
        if (newList != null) {
            // Force the RecyclerView to refresh
            this.notifyDataSetChanged();
        }
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView titel;
        TextView platform;
        TextView datum;
        TextView status;

        public ViewHolder(View itemView) {
            super(itemView);
            titel = itemView.findViewById(R.id.gameTitel);
            platform = itemView.findViewById(R.id.gamePlatform);
            datum = itemView.findViewById(R.id.gameDatum);
            status = itemView.findViewById(R.id.gameStatus);
        }
    }
}
