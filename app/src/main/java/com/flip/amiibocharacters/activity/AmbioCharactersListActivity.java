package com.flip.amiibocharacters.activity;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.flip.amiibocharacters.R;
import com.flip.amiibocharacters.adapter.AmbiioCharacterAdapter;
import com.flip.amiibocharacters.app.AppController;
import com.flip.amiibocharacters.model.AmbiioCharacterModel;
import com.flip.amiibocharacters.network.ConectivityManager;
import com.flip.amiibocharacters.network.MySharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

@SuppressWarnings({"UnusedAssignment", "deprecation"})
public class AmbioCharactersListActivity extends AppCompatActivity implements
        ConectivityManager.ConnectivityReceiverListener {

    MySharedPreferences sharedPreferencesAction;
    boolean doubleBackToExitPressedOnce = false;
    private RecyclerView ambioCharacterRecyclerview;
    private ArrayList<AmbiioCharacterModel> ambiioCharacterModelArrayList = new ArrayList<>();
    private AmbiioCharacterAdapter ambiiolistAdapter;
    private ConectivityManager connectionManager;
    private LinearLayout progressLayout;
    private LinearLayout idNetworkErrorLayout;
    private String jsonResponse;
    private SearchView searchView;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialize UI elements...
        Toolbar toolbar = findViewById(R.id.toolbar_main);
        toolbar.setTitle(" ");
        setSupportActionBar(toolbar);

        //initialize the UI elements
        ambioCharacterRecyclerview = (RecyclerView) findViewById(R.id.idAmbiioCharactersList);
        progressLayout = (LinearLayout) findViewById(R.id.idLayoutProgressBar);
        idNetworkErrorLayout = (LinearLayout) findViewById(R.id.idNetworkErrorLayout);
        Button btnRetry = (Button) findViewById(R.id.idBtnRetry);
        CoordinatorLayout corCoordinatorLayout =
                (CoordinatorLayout) findViewById(R.id.mainActivtyRootLayout);

        connectionManager = new ConectivityManager(AppController.getInstance().getApplicationContext());
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        connectionManager = new ConectivityManager(AppController.getInstance().getApplicationContext());
        registerReceiver(connectionManager, filter);

//region OFFLINE FUNCTIONALITY --->
        sharedPreferencesAction = new MySharedPreferences(AppController.getInstance().getApplicationContext());

        //TODO offline functionality check properly.
        if (!connectionManager.isConnected()) {
            // Toast.makeText(this, "Get true boolean from shared.", Toast.LENGTH_SHORT).show();
            ArrayList<AmbiioCharacterModel> cacheCharacterList = sharedPreferencesAction.getListObject("ambiolist", AmbiioCharacterModel.class);
            Log.e("New list: ", String.valueOf(cacheCharacterList.size()));
            if (cacheCharacterList.size() == 0) {
                idNetworkErrorLayout.setVisibility(View.VISIBLE);
            } else {
                if (!connectionManager.isConnected()) {
                    Toast.makeText(this, "Offline mode.", Toast.LENGTH_SHORT).show();
                    ambioCharacterRecyclerview.setLayoutManager(new LinearLayoutManager(AppController.getInstance().getApplicationContext()));
                    ambioCharacterRecyclerview.setItemAnimator(new DefaultItemAnimator());
                    ambiiolistAdapter = new AmbiioCharacterAdapter(AppController.getInstance().getApplicationContext(), cacheCharacterList);
                    ambioCharacterRecyclerview.setAdapter(ambiiolistAdapter);
                    ambiiolistAdapter.notifyDataSetChanged();
                    System.out.println("finish from cache data: " + ambiioCharacterModelArrayList.size());
                } else {
                    ambioCharacterRecyclerview.setLayoutManager(new LinearLayoutManager(AppController.getInstance().getApplicationContext()));
                    ambioCharacterRecyclerview.setItemAnimator(new DefaultItemAnimator());
                    Load_json_data();
                }
            }
        } else {
            ambioCharacterRecyclerview.setLayoutManager(new LinearLayoutManager(AppController.getInstance().getApplicationContext()));
            ambioCharacterRecyclerview.setItemAnimator(new DefaultItemAnimator());
            Load_json_data();
        }
//endregion OFFLINE FUNCTIONALITY --->


        btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (connectionManager.isConnected()) {
                    idNetworkErrorLayout.setVisibility(View.GONE);
                    ambioCharacterRecyclerview.setLayoutManager(new LinearLayoutManager(AppController.getInstance().getApplicationContext()));
                    ambioCharacterRecyclerview.setItemAnimator(new DefaultItemAnimator());
                    Load_json_data();
                } else {
                    idNetworkErrorLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        //search intent which will trigger the searchview for working.
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
        }
    }

    private void Load_json_data() {
        String URL = "https://www.amiiboapi.com/api/amiibo/";
        progressLayout.setVisibility(View.VISIBLE);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new com.android.volley.Response.Listener<String>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(String response) {
                Log.i("volley server response: ", response);
                progressLayout.setVisibility(View.GONE);
                ambioCharacterRecyclerview.setVisibility(View.VISIBLE);
                System.out.println("size_: " + ambiioCharacterModelArrayList.size());

                ambiiolistAdapter = new AmbiioCharacterAdapter(AppController.getInstance().getApplicationContext(), ambiioCharacterModelArrayList);
                ambioCharacterRecyclerview.setAdapter(ambiiolistAdapter);
                ambiiolistAdapter.notifyDataSetChanged();
                System.out.println("finish: " + ambiiolistAdapter.getItemCount());
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VOLLEY error", error.toString());
                if (error.toString().contains("Timeout")) {
                    Toast.makeText(AppController.getInstance().getApplicationContext(), "This is taking Longer than expected. Retry again!", Toast.LENGTH_SHORT).show();
                    progressLayout.setVisibility(View.GONE);
                }
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @SuppressLint("LongLogTag")
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            protected com.android.volley.Response<String> parseNetworkResponse(final NetworkResponse response) {
                final String[] responseString = {""};
                if (response != null) {
                    responseString[0] = String.valueOf(response.data);
                    // can get more details such as response.headers
                    //Log.e("VOLLEY data response - ", Arrays.toString(response.data));
                }
                Cache.Entry cacheEntry = null;
                try {
                    assert response != null;
                    jsonResponse = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                    // get your work done here
                    Log.e("VOLLEY data length: ", String.valueOf(response.data.length));
                    //Log.e("VOLLEY data --- ", json);

                    JSONObject jsonObject = new JSONObject(jsonResponse);
                    JSONArray jsonArray = jsonObject.getJSONArray("amiibo");
                    //Log.d("json character list data: ", jsonObject.toString());
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        String amiiboSeries = object.optString("amiiboSeries");
                        String character = object.optString("character");
                        String gameSeries = object.optString("gameSeries");
                        String image = object.optString("image");
                        String name = object.optString("name");
                        String head = object.optString("head");
                        String tail = object.optString("tail");
                        String type = object.optString("type");
                        //Log.e("JSON", object.toString(i));

                        JSONObject release = object.getJSONObject("release");
                        String au = release.optString("au");
                        String eu = release.optString("eu");
                        String jp = release.optString("jp");
                        String na = release.optString("na");
                        //Log.e("JSON inner: ", release.toString());

                        AmbiioCharacterModel ambiioCharacterModel =
                                new AmbiioCharacterModel(amiiboSeries, character, gameSeries, head, image, name, tail, type, au, eu, jp, na);
                        ambiioCharacterModelArrayList.add(ambiioCharacterModel);
                    }
                    /**
                     * Storing data in cache memory and sharedpreferences[to populate data when
                     * offline].
                     */
                    sharedPreferencesAction.putListObject("ambiolist", ambiioCharacterModelArrayList);
                    cacheEntry = HttpHeaderParser.parseCacheHeaders(response);
                    if (cacheEntry == null) {
                        cacheEntry = new Cache.Entry();
                    }
                    final long cacheHitButRefreshed = 3 * 60 * 1000; // in 3 minutes cache will be hit, but also refreshed on background
                    final long cacheExpired = 12 * 60 * 60 * 1000; // in 12 hours this cache
                    // entry expires completely
                    long now = System.currentTimeMillis();
                    final long softExpire = now + cacheHitButRefreshed;
                    final long ttl = now + cacheExpired;
                    cacheEntry.data = response.data;
                    cacheEntry.softTtl = softExpire;
                    cacheEntry.ttl = ttl;
                    String headerValue;
                    headerValue = response.headers.get("Date");
                    if (headerValue != null) {
                        cacheEntry.serverDate = HttpHeaderParser.parseDateAsEpoch(headerValue);
                    }
                    headerValue = response.headers.get("Last-Modified");
                    if (headerValue != null) {
                        cacheEntry.lastModified = HttpHeaderParser.parseDateAsEpoch(headerValue);
                    }

                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return com.android.volley.Response.success(
                        responseString[0],
                        cacheEntry);
            }

            @Override
            protected void deliverResponse(String response) {
                super.deliverResponse(response);
            }

            @Override
            public void deliverError(VolleyError error) {
                super.deliverError(error);
            }

            @Override
            protected VolleyError parseNetworkError(VolleyError volleyError) {
                return super.parseNetworkError(volleyError);
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(AppController.getInstance().getApplicationContext());
        requestQueue.add(stringRequest);
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            // close search view on back button pressed
            if (!searchView.isIconified()) {
                searchView.setIconified(true);
                return;
            }
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 1500);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        // Get the SearchView and set the searchable configuration
        final SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);
        // listening to search query text change
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                ambiiolistAdapter.getFilter().filter(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(final String query) {
                ambiiolistAdapter.getFilter().filter(query);
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
            case R.id.action_search:
                item.setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (connectionManager.isConnected()) {
            idNetworkErrorLayout.setVisibility(View.GONE);
            ambioCharacterRecyclerview.setLayoutManager(new LinearLayoutManager(AppController.getInstance().getApplicationContext()));
            ambioCharacterRecyclerview.setItemAnimator(new DefaultItemAnimator());
            Load_json_data();
        } else {
            idNetworkErrorLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(connectionManager);
    }
}
