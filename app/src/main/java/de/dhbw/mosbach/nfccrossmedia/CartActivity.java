package de.dhbw.mosbach.nfccrossmedia;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import de.dhbw.mosbach.nfccrossmedia.data.CartObject;
import de.dhbw.mosbach.nfccrossmedia.data.Product;
import de.dhbw.mosbach.nfccrossmedia.utilities.CartAdapter;

public class CartActivity extends AppCompatActivity {

    private ArrayList<CartObject> cartList;
    private RecyclerView cartRecyclerView;
    private RecyclerView.Adapter cartAdapter;
    private RecyclerView.LayoutManager cartLayoutManager;
    protected TextView cartCount;
    protected Button toReserveInstore;
    private TextView cartSum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        Toolbar toolbar = findViewById(R.id.toolbarApp);
        setSupportActionBar(toolbar);

        cartRecyclerView = (RecyclerView) findViewById(R.id.cart_recycler_view);

        cartList = ((NFCCrossmediaApplication) this.getApplication()).getCart();

        cartLayoutManager = new LinearLayoutManager(this);
        cartRecyclerView.setLayoutManager(cartLayoutManager);

        cartAdapter = new CartAdapter(Glide.with(this), cartList);
        cartRecyclerView.setAdapter(cartAdapter);

        cartCount = findViewById(R.id.cart_count);
        cartCount.setText(((NFCCrossmediaApplication) this.getApplication()).getCartCount() + "  Artikel");

        toReserveInstore = findViewById(R.id.to_reserve_instore);

        toReserveInstore.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CartActivity.this.startActivity(new Intent(CartActivity.this, ChooseStoreActivity.class));
            }
        });

        cartSum = findViewById(R.id.cart_sum);
        cartSum.setText("Gesamtsumme: " + ((NFCCrossmediaApplication) this.getApplication()).getCartPrice());
    }
}
