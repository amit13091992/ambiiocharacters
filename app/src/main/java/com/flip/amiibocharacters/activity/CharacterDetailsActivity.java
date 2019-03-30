package com.flip.amiibocharacters.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.flip.amiibocharacters.R;
import com.flip.amiibocharacters.app.AppController;
import com.flip.amiibocharacters.model.AmbiioCharacterModel;
import com.flip.amiibocharacters.network.ConectivityManager;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class CharacterDetailsActivity extends AppCompatActivity {

    ConectivityManager connectionManager;
    private AmbiioCharacterModel ambiioCharacterModel;
    private ImageView imgCharacter;
    private TextView tvAmbioSeries;
    private TextView tvCharacter;
    private TextView tvGameSeries;
    private TextView tvName;
    private TextView tvHead;
    private TextView tvTail;
    private TextView tvType;
    private TextView tvAmbioSeriesText;
    private TextView tvAU;
    private TextView tvEU;
    private TextView tvJP;
    private TextView tvNA;
    private ProgressBar progressBar;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_details);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(" ");
        setSupportActionBar(toolbar);
        //this line shows back button
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(AppController.getInstance().getApplicationContext(), R.color.colorPrimaryDark));
        }

        //initialize the UI components.
        imgCharacter = (ImageView) findViewById(R.id.idambioCharacterImage);
        tvAmbioSeries = (TextView) findViewById(R.id.idambioSeriesValue);
        tvCharacter = (TextView) findViewById(R.id.idCharacterValue);
        tvGameSeries = (TextView) findViewById(R.id.idGameSeriesValue);
        tvName = (TextView) findViewById(R.id.idNameValue);
        tvHead = (TextView) findViewById(R.id.idHeadValue);
        tvTail = (TextView) findViewById(R.id.idTailValue);
        tvType = (TextView) findViewById(R.id.idTypeValue);
        tvAmbioSeriesText = (TextView) findViewById(R.id.idAmbioSeriesText);
        tvAU = (TextView) findViewById(R.id.idAUvalue);
        tvEU = (TextView) findViewById(R.id.idEUvalue);
        tvJP = (TextView) findViewById(R.id.idJPvalue);
        tvNA = (TextView) findViewById(R.id.idNAvalue);
        progressBar = (ProgressBar) findViewById(R.id.progress_circular);

        connectionManager =
                new ConectivityManager(AppController.getInstance().getApplicationContext());
        /*
        Getting Character details bundle
         */
        Intent i = getIntent();
        Bundle bundle = i.getExtras();
        assert bundle != null;
        ambiioCharacterModel = (AmbiioCharacterModel) bundle.getSerializable("ambiio_character_model");
        if (ambiioCharacterModel != null) {
            setImagefromURL(ambiioCharacterModel, imgCharacter);
            tvAmbioSeries.setText(ambiioCharacterModel.getAmiiboSeries());
            tvCharacter.setText(ambiioCharacterModel.getCharacter());
            tvGameSeries.setText(ambiioCharacterModel.getGameSeries());
            tvName.setText(ambiioCharacterModel.getName());
            tvHead.setText(ambiioCharacterModel.getHead());
            tvTail.setText(ambiioCharacterModel.getTail());
            tvType.setText(ambiioCharacterModel.getType());
            tvAmbioSeriesText.setText(ambiioCharacterModel.getAmiiboSeries());

            /**
             * Checking for null values.
             */
            if (ambiioCharacterModel.getRelease().getAu() != null
                    && !ambiioCharacterModel.getRelease().getAu().isEmpty()
                    && !ambiioCharacterModel.getRelease().getAu().equals("null")) {
                tvAU.setText(ambiioCharacterModel.getRelease().getAu());
            } else {
                tvAU.setText(" --- ");
            }

            if (ambiioCharacterModel.getRelease().getEu() != null
                    && !ambiioCharacterModel.getRelease().getEu().isEmpty()
                    && !ambiioCharacterModel.getRelease().getEu().equals("null")) {
                tvEU.setText(ambiioCharacterModel.getRelease().getEu());

            } else {
                tvEU.setText(" --- ");
            }
            if (ambiioCharacterModel.getRelease().getJp() != null
                    && !ambiioCharacterModel.getRelease().getJp().isEmpty()
                    && !ambiioCharacterModel.getRelease().getJp().equals("null")) {
                tvJP.setText(ambiioCharacterModel.getRelease().getJp());

            } else {
                tvJP.setText(" --- ");
            }
            if (ambiioCharacterModel.getRelease().getNa() != null
                    && !ambiioCharacterModel.getRelease().getNa().isEmpty()
                    && !ambiioCharacterModel.getRelease().getNa().equals("null")) {
                tvNA.setText(ambiioCharacterModel.getRelease().getNa());
            } else {
                tvNA.setText(" --- ");
            }
        }
    }

    @SuppressLint("CheckResult")
    private void setImagefromURL(AmbiioCharacterModel ambiioCharacterModel, final ImageView imgCharacter) throws NullPointerException {
        final Uri uri = Uri.parse(ambiioCharacterModel.getImage());

//        if (connectionManager.isConnected()) {
//            Picasso.get()
//                    .load(uri)
//                    .networkPolicy(NetworkPolicy.OFFLINE)
//                    .error(R.drawable.ic_no_internet)
//                    .placeholder(R.drawable.ic_image_loading)
//                    .into(imgCharacter);
//        } else {
            Picasso.get()
                    .load(uri)
                    .networkPolicy(NetworkPolicy.OFFLINE)
                    .into(imgCharacter, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError(Exception e) {
                            Picasso.get()
                                    .load(uri)
                                    .error(R.drawable.ic_no_internet)
                                    .placeholder(R.drawable.ic_image_loading)
                                    .into(imgCharacter, new Callback() {
                                        @Override
                                        public void onSuccess() {
                                        }

                                        @Override
                                        public void onError(Exception e) {
                                            Log.v("Picasso", "Could not fetch image");
                                            Toast.makeText(CharacterDetailsActivity.this, "Unable" +
                                                            " to load Image, at the moment.",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    });
        //}
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}

