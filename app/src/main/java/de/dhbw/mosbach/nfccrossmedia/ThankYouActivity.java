package de.dhbw.mosbach.nfccrossmedia;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ThankYouActivity extends AppCompatActivity {

    private double storeLat;
    private double storeLon;
    public String storeName;
    private TextView thankYouContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thank_you);
        Intent intent = getIntent();

        Bundle extras = intent.getExtras();
        storeName = extras.getString("STORE_NAME");
        storeLat = extras.getDouble("STORE_LAT");
        storeLon = extras.getDouble("STORE_LON");

        thankYouContent = findViewById(R.id.thank_you_content);

        thankYouContent.setText("Wir haben deine Bestellung erhalten. " + storeName + " wird deine reservierte Ware schnellstmöglich für dich auswählen und dich per Mail informieren, sobald du deine Bestellung abholen kannst.");

        Button startRoutetoStore = findViewById(R.id.start_route_to_store);
        startRoutetoStore.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Uri gmmIntentUri = Uri.parse("google.navigation:q=" + Double.toString(storeLat) + ", " + Double.toString(storeLon) + "&mode=w");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });
    }
}
