package de.dhbw.mosbach.nfccrossmedia;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.dhbw.mosbach.nfccrossmedia.data.Store;

public class ConfirmActivtiy extends AppCompatActivity {

    private TextView storeNameTextView;
    private ImageView storeImageView;
    private Button confirmButton;
    private String storeId;
    private double storeLat;
    private double storeLon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_activtiy);
        Intent intent = getIntent();
        storeId = intent.getStringExtra("storeId");

        storeNameTextView = findViewById(R.id.store_info_name);
        storeImageView = findViewById(R.id.store_info_image);
        confirmButton = findViewById(R.id.reserve_button);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Bundle extras = new Bundle();

                extras.putString("STORE_ID", storeId);
                extras.putDouble("STORE_LAT", storeLat);
                extras.putDouble("STORE_LON", storeLon);

                Intent intent = new Intent(ConfirmActivtiy.this, ThankYouActivity.class);
                intent.putExtras(extras);
                ConfirmActivtiy.this.startActivity(intent);
            }
        });

        getStoreInfo();
    }

    public void fillStoreInfo(Store store){
        storeNameTextView.setText(store.getStoreName());
        Glide.with(this).load(store.getStoreImage()).into(storeImageView);
    }

    public void getStoreInfo(){
        DatabaseReference fbSpecStoreReference = FirebaseDatabase.getInstance().getReference().child("stores").child(storeId);

        ValueEventListener storeInfoListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                Store store = dataSnapshot.getValue(Store.class);
                if (store != null){
                    fillStoreInfo(store);
                    storeLat = store.getStoreLatitude();
                    storeLon = store.getStoreLongitude();
                }
                else{
                    Log.e("Store not available!", dataSnapshot.toString() + storeId);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("TAG", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        fbSpecStoreReference.addListenerForSingleValueEvent(storeInfoListener);
    }
}
