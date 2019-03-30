package com.flip.amiibocharacters.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flip.amiibocharacters.R;
import com.flip.amiibocharacters.activity.CharacterDetailsActivity;
import com.flip.amiibocharacters.model.AmbiioCharacterModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Amit on 27-Mar-19.
 */
public class AmbiioCharacterAdapter extends
        RecyclerView.Adapter<AmbiioCharacterAdapter.AmbioCharacterViewHolder> implements Filterable {

    private List<AmbiioCharacterModel> filteredList;
    private Context context;
    private List<AmbiioCharacterModel> charactersMainList;
    private Filter filter;

    public AmbiioCharacterAdapter(Context context,
                                  List<AmbiioCharacterModel> characterModelList) {
        this.context = context;
        this.charactersMainList = characterModelList;
        this.filteredList = new ArrayList<AmbiioCharacterModel>();
    }

    @NonNull
    @Override
    public AmbiioCharacterAdapter.AmbioCharacterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_ambiio_character, parent, false);

        return new AmbioCharacterViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final AmbiioCharacterAdapter.AmbioCharacterViewHolder characterViewHolder, int position) {
        final AmbiioCharacterModel ambiioCharacterModel = charactersMainList.get(position);
        characterViewHolder.tvCharacterName.setText(ambiioCharacterModel.getCharacter());
        characterViewHolder.tvGameSeries.setText(ambiioCharacterModel.getGameSeries());

        characterViewHolder.listItemLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.row_color));

        characterViewHolder.listItemLayout.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View view) {
                Intent category = new Intent(view.getContext(), CharacterDetailsActivity.class);
                category.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Bundle b = new Bundle();
                b.putSerializable("ambiio_character_model", ambiioCharacterModel);
                category.putExtras(b); //pass data bundle
                context.startActivity(category);
            }
        });
    }

    @Override
    public int getItemCount() {
        return charactersMainList.size();
    }

    @Override
    public Filter getFilter() {
        if (filter == null) {
            filteredList.clear();
            filteredList.addAll(this.charactersMainList);
            filter = new AmbiioCharacterAdapter.RecordFilter(this, filteredList);
        }
        return filter;
    }

    /**
     * private Filter class to filter the results from list.
     */
    private class RecordFilter extends Filter {
        private final AmbiioCharacterAdapter myAdapter;
        private final List<AmbiioCharacterModel> originalList;
        private final List<AmbiioCharacterModel> filteredList;

        private RecordFilter(AmbiioCharacterAdapter myAdapter, List<AmbiioCharacterModel> originalList) {
            this.myAdapter = myAdapter;
            this.originalList = originalList;
            this.filteredList = new ArrayList<AmbiioCharacterModel>();
        }

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            filteredList.clear();
            final FilterResults results = new FilterResults();
            if (charSequence.length() == 0) {
                filteredList.addAll(originalList);
            } else {
                final String filterPattern = charSequence.toString().toLowerCase().trim();
                for (AmbiioCharacterModel user : originalList) {
                    if (user.getName().toLowerCase().contains(filterPattern)
                            || user.getCharacter().toLowerCase().contains(filterPattern)) {
                        filteredList.add(user);
                    }
                }
            }

            results.values = filteredList;
            results.count = filteredList.size();
            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            myAdapter.charactersMainList.clear();
            myAdapter.charactersMainList.addAll((ArrayList<AmbiioCharacterModel>) results.values);
            myAdapter.notifyDataSetChanged();
        }
    }

    class AmbioCharacterViewHolder extends RecyclerView.ViewHolder {
        private TextView tvCharacterName;
        private TextView tvGameSeries;
        private LinearLayout listItemLayout;


        private AmbioCharacterViewHolder(@NonNull View view) {
            super(view);

            tvCharacterName = (TextView) view.findViewById(R.id.idCharacterName);
            tvGameSeries = (TextView) view.findViewById(R.id.idgameSeries);
            listItemLayout = (LinearLayout) view.findViewById(R.id.ambioListItemLayout);

        }
    }
}
