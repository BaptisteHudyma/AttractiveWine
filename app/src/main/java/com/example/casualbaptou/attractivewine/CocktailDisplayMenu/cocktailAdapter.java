package com.example.casualbaptou.attractivewine.CocktailDisplayMenu;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.casualbaptou.attractivewine.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class cocktailAdapter extends RecyclerView.Adapter<cocktailAdapter.ViewHolder> implements Filterable {

    private List<DisplayerContainer> cocktailDataset;
    private List<DisplayerContainer> cocktailDatasetFiltered;
    private CocktailAdapterListener listener;

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView cocktailName;
        public ImageView thumb;

        private ViewHolder(View v) {
            super(v);
            cocktailName = v.findViewById(R.id.cocktailName);
            thumb = v.findViewById(R.id.cocktail_thumb);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // send selected contact in callback
                    listener.onCocktailSelected(cocktailDatasetFiltered.get(getAdapterPosition()));
                }
            });
        }
    }

    public cocktailAdapter(List<DisplayerContainer> cocktailDataset, CocktailAdapterListener listener) {
        this.cocktailDataset = cocktailDataset;
        this.cocktailDatasetFiltered = cocktailDataset;
        this.listener = listener;
    }

    // Create new views (invoked by the layout manager)
    @Override
    @NonNull
    public cocktailAdapter.ViewHolder onCreateViewHolder( @NonNull ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cocktail_row_container, parent, false);

        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder( @NonNull ViewHolder holder, int position) {
        DisplayerContainer DP = cocktailDatasetFiltered.get(position);
        holder.cocktailName.setText( DP.getCocktailName() );
        Picasso.get().load(DP.getImageLink()).into(holder.thumb);
    }

    @Override
    public int getItemCount() {
        return cocktailDatasetFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    cocktailDatasetFiltered = cocktailDataset;
                } else {
                    List<DisplayerContainer> filteredList = new ArrayList<>();
                    for (DisplayerContainer row : cocktailDataset) {
                        if (row.getCocktailName().toLowerCase().contains(charString.toLowerCase()) || row.getID().contains(charSequence)) {
                            filteredList.add(row);
                        }
                    }

                    cocktailDatasetFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = cocktailDatasetFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                cocktailDatasetFiltered = (ArrayList<DisplayerContainer>) filterResults.values;

                String placeHolder = cocktailDatasetFiltered.size() + " cocktails found";
                CocktailDisplayActivity.numberOfSelected.setText( placeHolder );
                notifyDataSetChanged();
            }
        };
    }

    public interface CocktailAdapterListener {
        void onCocktailSelected(DisplayerContainer cocktail);
    }
}
