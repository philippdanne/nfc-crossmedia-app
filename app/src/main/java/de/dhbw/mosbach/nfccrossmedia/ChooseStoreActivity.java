package de.dhbw.mosbach.nfccrossmedia;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.text.DecimalFormat;

public class ChooseStoreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_store);
        calculationByDistance(49.350019, 9.147702, 51.366970, 7.926860);
    }

    // Berechnet die Distanz zwischen NFC-Standort & Store. Mit dieser Lösung brauchen wir keine Anbindung an die Google Maps-API.
    public double calculationByDistance(double latStart, double lonStart, double latEnd, double lonEnd) {
        int Radius = 6371;// radius of earth in Km
        double dLat = Math.toRadians(latEnd - latStart);
        double dLon = Math.toRadians(lonEnd - lonStart);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(latStart))
                * Math.cos(Math.toRadians(latEnd)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult = Radius * c;
        Log.i("Radius Value", "" + valueResult);

        return Radius * c;
    }
}
