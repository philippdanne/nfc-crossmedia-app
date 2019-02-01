package de.dhbw.mosbach.nfccrossmedia;

import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
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

public class ShowProductActivity extends AppCompatActivity {

    protected TextView productNameTextView;
    protected ProgressBar loadingApiProgressBar;
    protected TextView nfcErrorTextView;
    protected TextView productDescriptionTextView;
    protected ImageView productImageImageView;
    private DatabaseReference mProductReference;
    protected String payloadString;
    protected Button productBuyButton;
    protected RecyclerView relatedProductsRecyclerView;
    protected ScrollView productContentScrollView;
    protected Product product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        payloadString = intent.getStringExtra("nfcPayload");
        setContentView(R.layout.activity_show_product);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        productBuyButton = (Button) findViewById(R.id.productBuyButton);
        productNameTextView = (TextView) findViewById(R.id.productNameTextView);
        productImageImageView = (ImageView) findViewById(R.id.productImageImageView);
        loadingApiProgressBar = (ProgressBar) findViewById(R.id.loadingApiProgressBar);
        nfcErrorTextView = (TextView) findViewById(R.id.nfcErrorTextView);
        productDescriptionTextView = (TextView) findViewById(R.id.productDescriptionTextView);
        relatedProductsRecyclerView = (RecyclerView) findViewById(R.id.relatedProductsRecyclerView);
        productContentScrollView = (ScrollView) findViewById(R.id.productContentScrollView);

        this.getDataFromFirebase();

        productBuyButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                updateCart();
                Snackbar mySnackbar = Snackbar.make(findViewById(R.id.productContentScrollView),
                        R.string.add_to_cart, Snackbar.LENGTH_LONG);
                mySnackbar.setAction("Zum Warenkorb", new View.OnClickListener() {
                    public void onClick(View v) {
                        ShowProductActivity.this.startActivity(new Intent(ShowProductActivity.this, CartActivity.class));
                    }
                });
                mySnackbar.show();
            }
        });
    }

    private void updateCart(){
        ((NFCCrossmediaApplication) this.getApplication()).addToCart(product);
    }

    private void showJsonDataView(){
        loadingApiProgressBar.setVisibility(View.GONE);
        nfcErrorTextView.setVisibility(View.GONE);
        productContentScrollView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage(){
        loadingApiProgressBar.setVisibility(View.GONE);
        nfcErrorTextView.setVisibility(View.VISIBLE);
        productContentScrollView.setVisibility(View.GONE);
    }

    private void fillWithJsonData(String productName, String productColor, String productDescription, String productImage){
        productNameTextView.setText(productName);
        productDescriptionTextView.setText(productDescription);
        Glide.with(this).load(productImage).into(productImageImageView);
        getSupportActionBar().setTitle(productName);
    }

    private void setRelatedProducts(ArrayList<RelatedProduct> relatedProducts){
        RelatedProductsAdapter productsAdapter = new RelatedProductsAdapter(Glide.with(this), relatedProducts);
        relatedProductsRecyclerView.setAdapter(productsAdapter);
        relatedProductsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

    }

    private void getDataFromFirebase() {
        mProductReference = FirebaseDatabase.getInstance().getReference().child("products");

        ValueEventListener productListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                product = dataSnapshot.child(payloadString).getValue(Product.class);
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
