package com.rgdgr8.regionalcountries;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;

public class MainFragment extends Fragment {
    private static final String TAG = "MainFrag";
    private String lastSearchedRegion = "asia";
    private CountryAdapter adapter;
    private CountryDao dao;

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);

        CountryDataBase db = CountryDataBase.getDataBase(getActivity());
        dao = db.getDao();
        new CountryDownloadTask(lastSearchedRegion).execute();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull @NotNull Menu menu, @NonNull @NotNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main_menu, menu);

        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                new CountryDownloadTask(query).execute();
                searchView.clearFocus();
                lastSearchedRegion = query;
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull @NotNull MenuItem item) {
        if (item.getItemId() == R.id.clear_db) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    dao.deleteAll();
                }
            }).start();
            Toast.makeText(getActivity(), "Database Cleared", Toast.LENGTH_SHORT).show();
            return true;
        } else if (item.getItemId() == R.id.refresh) {
            new CountryDownloadTask(lastSearchedRegion).execute();
        }
        return super.onOptionsItemSelected(item);
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @org.jetbrains.annotations.NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        RecyclerView rv = rootView.findViewById(R.id.recycler_view);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));

        initializeAdapter();
        rv.setAdapter(adapter);

        return rootView;
    }

    private void initializeAdapter() {
        if (adapter == null) {
            adapter = new CountryAdapter();
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    private void setBackgroundToNormal(boolean normal) {
        FrameLayout frameLayout = getActivity().findViewById(R.id.frame);
        if (normal) {
            frameLayout.setBackground(null);
        } else {
            frameLayout.setBackgroundResource(R.drawable.no_network_connection_error);
        }
    }

    private class CountryDownloadTask extends AsyncTask<Void, Void, Void> {
        private final String region;
        private int dataAvailable;

        public CountryDownloadTask(String region) {
            this.region = region;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            publishProgress();
            dataAvailable = DataFetcher.getData(region, dao, getActivity());
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            setBackgroundToNormal(true);
        }

        @Override
        protected void onPostExecute(Void unused) {
            TextView textView = getActivity().findViewById(R.id.error_tv);
            switch (dataAvailable) {
                case DataFetcher.NO_INTERNET:
                    Toast.makeText(getActivity(), getString(R.string.no_network), Toast.LENGTH_LONG).show();
                    if(adapter==null || adapter.getItemCount()==0) {
                        setBackgroundToNormal(false);
                    }
                    break;
                case DataFetcher.ERROR_404:
                    Toast.makeText(getActivity(), "Error 404", Toast.LENGTH_SHORT).show();
                    textView.setText("No results found for " + region);
                    break;
                default:
                    textView.setText("");
            }

            initializeAdapter();
        }
    }

    private class CountryHolder extends RecyclerView.ViewHolder {
        private static final String TAG = "CountryHolder";
        private final TextView name;
        private final TextView capital;
        private final ImageView flag;
        private final TextView region;
        private final TextView subRegion;
        private final TextView population;
        private final TextView borders;
        private final TextView languages;

        public CountryHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name_tv);
            capital = itemView.findViewById(R.id.capital_tv);
            flag = itemView.findViewById(R.id.flag);
            region = itemView.findViewById(R.id.region_tv);
            subRegion = itemView.findViewById(R.id.sub_region_tv);
            population = itemView.findViewById(R.id.population_tv);
            borders = itemView.findViewById(R.id.borders_tv);
            languages = itemView.findViewById(R.id.languages_tv);
        }

        public void bind(Country country) throws IOException {
            name.setText(country.getName());
            capital.setText(country.getCapital());

            String flagUrl = country.getFlag_url();
            Log.i(TAG, "flag_url: " + flagUrl);

            Glide.with(getActivity())
                    .load(flagUrl)
                    .diskCacheStrategy(DiskCacheStrategy.DATA)//caching image as raw bytes.
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .error(R.drawable.no_network_connection_error)
                    .into(flag);

            region.setText(country.getRegion());
            subRegion.setText(country.getSubRegion());
            population.setText(country.getPopulation());
            borders.setText(country.getBorders());
            languages.setText(country.getLanguages());
        }

    }

    private class CountryAdapter extends RecyclerView.Adapter<CountryHolder> {
        private final List<Country> countries;

        public CountryAdapter() {
            this.countries = DataFetcher.getCountries();
        }

        @NonNull
        @NotNull
        @Override
        public CountryHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            return new CountryHolder(inflater.inflate(R.layout.list_item, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull @NotNull MainFragment.CountryHolder holder, int position) {
            try {
                holder.bind(countries.get(position));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {
            return countries.size();
        }
    }
}
