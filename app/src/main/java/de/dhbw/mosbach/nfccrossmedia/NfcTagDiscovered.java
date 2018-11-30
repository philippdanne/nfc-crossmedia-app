package de.dhbw.mosbach.nfccrossmedia;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class NfcTagDiscovered extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc_tag_discovered);
    }
}
