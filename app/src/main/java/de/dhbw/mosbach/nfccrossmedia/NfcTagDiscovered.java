package de.dhbw.mosbach.nfccrossmedia;

import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

import de.dhbw.mosbach.nfccrossmedia.utilities.NetworkUtils;

public class NfcTagDiscovered extends AppCompatActivity {

    protected TextView nfcTextView;
    protected ProgressBar loadingApiProgressBar;
    protected TextView nfcErrorTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc_tag_discovered);
        nfcTextView = findViewById(R.id.nfcTextView);
        loadingApiProgressBar = findViewById(R.id.loadingApiProgressBar);
        nfcErrorTextView = findViewById(R.id.nfcErrorTextView);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        getNfcData(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        getNfcData(intent);
    }

    private void getNfcData(Intent intent){
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
            Parcelable[] rawMessages =
                    intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            if (rawMessages != null) {
                NdefMessage message = (NdefMessage) rawMessages[0];
                NdefRecord dataRecord = message.getRecords()[1];

                String payloadString = new String(dataRecord.getPayload());

                this.triggerApiCall(payloadString);
            }
        }
    }

    private void triggerApiCall(String NfcContent){
        URL apiURL = NetworkUtils.buildUrl(NfcContent);
        new ProductTask().execute(apiURL);
    }

    private void parseJson(String fullJson) throws JSONException {
        JSONObject product = new JSONObject(fullJson);
        String productName = product.getString("productName");
        showJsonDataView();
        nfcTextView.setText(productName);
    }

    private void showJsonDataView(){
        loadingApiProgressBar.setVisibility(View.GONE);
        nfcErrorTextView.setVisibility(View.GONE);
        nfcTextView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage(){
        loadingApiProgressBar.setVisibility(View.GONE);
        nfcErrorTextView.setVisibility(View.VISIBLE);
        nfcTextView.setVisibility(View.GONE);
    }

    public class ProductTask extends AsyncTask<URL, Void, String> {

        @Override
        protected String doInBackground(URL... urls) {
            URL productUrl = urls[0];
            String productResult = null;
            try {
                productResult = NetworkUtils.getResponseFromHttpUrl(productUrl);
            } catch (IOException e){
                e.printStackTrace();
            }
            return productResult;
        }

        @Override
        protected void onPostExecute(String s) {
            if (s != null && !s.equals("")){
                try {
                    parseJson(s);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                showErrorMessage();
            }
        }
    }
}
