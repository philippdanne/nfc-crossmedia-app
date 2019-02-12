package de.dhbw.mosbach.nfccrossmedia;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.dhbw.mosbach.nfccrossmedia.data.Store;

public class ConfirmActivtiy extends AppCompatActivity {

    private TextView storeNameTextView;
    private ImageView storeImageView;
    private TextView storeAddressTextView;
    private Button confirmButton;
    private CheckBox checkBoxAgb;
    private CheckBox checkBoxConditions;
    private TextView totalPrice;
    private String storeId;
    private String storeName;
    private double storeLat;
    private double storeLon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_activtiy);
        Intent intent = getIntent();
        storeId = intent.getStringExtra("storeId");

        storeName = "";

        storeNameTextView = findViewById(R.id.store_info_name);
        storeImageView = findViewById(R.id.store_info_image);
        confirmButton = findViewById(R.id.reserve_button);
        checkBoxAgb = findViewById(R.id.agb_check);
        checkBoxConditions = findViewById(R.id.conditions_check);
        storeAddressTextView = findViewById(R.id.store_info_address);
        totalPrice = findViewById(R.id.total_confirm);
        Button editCartButton = findViewById(R.id.confirm_edit_cart);


        editCartButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ConfirmActivtiy.this.startActivity(new Intent(ConfirmActivtiy.this, CartActivity.class));
            }
        });

        totalPrice.setText(((NFCCrossmediaApplication) this.getApplication()).getCartPrice());

        confirmButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(checkBoxAgb.isChecked() && checkBoxConditions.isChecked()){
                    Bundle extras = new Bundle();

                    extras.putString("STORE_NAME", storeName);
                    extras.putDouble("STORE_LAT", storeLat);
                    extras.putDouble("STORE_LON", storeLon);

                    Intent intent = new Intent(ConfirmActivtiy.this, ThankYouActivity.class);
                    intent.putExtras(extras);
                    ConfirmActivtiy.this.startActivity(intent);
                }
                else{
                    Toast.makeText(ConfirmActivtiy.this, "Du musst den Bedingungen zustimmen, bevor du bestellen kannst.", Toast.LENGTH_LONG).show();
                }
            }
        });

        getStoreInfo();
    }

    public void fillStoreInfo(Store store){
        storeNameTextView.setText(store.getStoreName());
        storeAddressTextView.setText(store.getStoreAddress());
        Glide.with(this).load(store.getStoreImage()).transition(DrawableTransitionOptions.withCrossFade()).into(storeImageView);
        storeName = store.getStoreName();
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
