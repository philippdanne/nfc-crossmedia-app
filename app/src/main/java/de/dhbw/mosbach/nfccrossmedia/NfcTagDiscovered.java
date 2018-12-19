package de.dhbw.mosbach.nfccrossmedia;

import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.dhbw.mosbach.nfccrossmedia.data.Product;
import de.dhbw.mosbach.nfccrossmedia.data.RelatedProduct;
import de.dhbw.mosbach.nfccrossmedia.utilities.RelatedProductsAdapter;

public class NfcTagDiscovered extends AppCompatActivity {

    protected TextView productNameTextView;
    protected ProgressBar loadingApiProgressBar;
    protected TextView nfcErrorTextView;
    protected TextView productDescriptionTextView;
    protected ImageView productImageImageView;
    private DatabaseReference mProductReference;
    protected String nfcPayloadString;
    protected RecyclerView relatedProductsRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc_tag_discovered);

        productNameTextView = (TextView) findViewById(R.id.productNameTextView);
        productImageImageView = (ImageView) findViewById(R.id.productImageImageView);
        loadingApiProgressBar = (ProgressBar) findViewById(R.id.loadingApiProgressBar);
        nfcErrorTextView = (TextView) findViewById(R.id.nfcErrorTextView);
        productDescriptionTextView = (TextView) findViewById(R.id.productDescriptionTextView);
        relatedProductsRecyclerView = (RecyclerView) findViewById(R.id.relatedProductsRecyclerView);
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

                this.getDataFromFirebase();
            }
        }
    }

    private void showJsonDataView(){
        loadingApiProgressBar.setVisibility(View.GONE);
        nfcErrorTextView.setVisibility(View.GONE);
        productNameTextView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage(){
        loadingApiProgressBar.setVisibility(View.GONE);
        nfcErrorTextView.setVisibility(View.VISIBLE);
        productNameTextView.setVisibility(View.GONE);
    }

    private void fillWithJsonData(String productName, String productColor, String productDescription, String productImage){
        productNameTextView.setText(productName);
        productDescriptionTextView.setText(productDescription);
        Glide.with(this).load(productImage).into(productImageImageView);
    }

    private void setRelatedProducts(ArrayList<RelatedProduct> relatedProducts){
        RelatedProductsAdapter productsAdapter = new RelatedProductsAdapter(relatedProducts);
        relatedProductsRecyclerView.setAdapter(productsAdapter);
        relatedProductsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void getDataFromFirebase() {
        mProductReference = FirebaseDatabase.getInstance().getReference().child("products");

        ValueEventListener productListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                Product product = dataSnapshot.child(nfcPayloadString).getValue(Product.class);
                if(product != null){
                    showJsonDataView();
                    fillWithJsonData(product.productName, null, product.productDescription, product.getProductImages(0));
                    RelatedProduct relatedProduct;
                    ArrayList<RelatedProduct> relatedProducts = new ArrayList<>();
                    for (int a = 0; a < product.getRelatedProductsLength(); a++){
                        relatedProduct = dataSnapshot.child(product.getProductId(a)).getValue(RelatedProduct.class);
                        relatedProducts.add(relatedProduct);
                    }
                    setRelatedProducts(relatedProducts);
                }
                else {
                    showErrorMessage();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("TAG", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        mProductReference.addListenerForSingleValueEvent(productListener);
    }
}
