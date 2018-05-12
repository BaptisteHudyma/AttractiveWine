package com.example.casualbaptou.attractivewine.CocktailDisplayMenu;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.casualbaptou.attractivewine.R;

import java.util.List;

public class cocktailAdapter extends RecyclerView.Adapter<cocktailAdapter.ViewHolder> {

    private List<DisplayerContainer> cocktailDataset;

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView cocktailName;
        public ImageView thumb;

        public ViewHolder(View v) {
            super(v);
            cocktailName = v.findViewById(R.id.cocktailName);
            thumb = v.findViewById(R.id.cocktail_thumb);
        }
    }

    public cocktailAdapter(List<DisplayerContainer> cocktailDataset) {
        this.cocktailDataset = cocktailDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public cocktailAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cocktail_row_container, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        DisplayerContainer DP = cocktailDataset.get(position);
        holder.cocktailName.setText( DP.getCocktailName() );
        //holder.thumb.setImageURI();

    }

    @Override
    public int getItemCount() {
        return cocktailDataset.size();
    }
}
