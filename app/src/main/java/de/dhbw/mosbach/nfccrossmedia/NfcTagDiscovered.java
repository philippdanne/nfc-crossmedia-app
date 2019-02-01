package de.dhbw.mosbach.nfccrossmedia;

import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class NfcTagDiscovered extends AppCompatActivity {

    protected String nfcPayloadString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

                nfcPayloadString = new String(dataRecord.getPayload());

                // Hier muss die andere Activity gestartet werden
                Intent nfcIntent = new Intent(NfcTagDiscovered.this, ShowProductActivity.class);
                nfcIntent.putExtra("nfcPayload", nfcPayloadString);
                NfcTagDiscovered.this.startActivity(nfcIntent);
            }
        }
    }
}
