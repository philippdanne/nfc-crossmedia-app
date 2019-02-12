package de.dhbw.mosbach.nfccrossmedia;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import de.dhbw.mosbach.nfccrossmedia.data.Store;
import de.dhbw.mosbach.nfccrossmedia.utilities.RecyclerItemClickListener;
import de.dhbw.mosbach.nfccrossmedia.utilities.StoreAdapter;

public class ChooseStoreActivity extends AppCompatActivity {

    private RecyclerView storeRecyclerView;
    private RecyclerView.Adapter storeAdapter;
    private RecyclerView.LayoutManager storeLayoutManager;
    private ArrayList<Store> storeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_store);
        Toolbar toolbar = findViewById(R.id.toolbarApp);
        setSupportActionBar(toolbar);

        storeList = new ArrayList<>();

        storeRecyclerView = (RecyclerView) findViewById(R.id.store_recycler_view);

        storeRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(ChooseStoreActivity.this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(ChooseStoreActivity.this, "Clicked on: " + storeList.get(position).getStoreName(), Toast.LENGTH_SHORT).show();
                Intent nfcIntent = new Intent(ChooseStoreActivity.this, ConfirmActivtiy.class);
                nfcIntent.putExtra("storeId", storeList.get(position).getKey());
                ChooseStoreActivity.this.startActivity(nfcIntent);
            }
        }));

        this.getStoreList();

    }

    public void sortStoreList(){
        Collections.sort(storeList, new Comparator<Store>() {
            @Override
            public int compare(Store o1, Store o2) {
                int distance1 = (int) o1.getDistance();
                int distance2 = (int) o2.getDistance();
                return Integer.valueOf(distance1).compareTo(distance2);
            }
        });
        for (Store thisStore:
             storeList) {
            Log.i("Nach Sortierung", thisStore.getStoreName());
        }
        }

    public void showErrorMessage(){
    }

    public void fillRecyclerViewWithData(){
        storeLayoutManager = new LinearLayoutManager(this);
        storeRecyclerView.setLayoutManager(storeLayoutManager);

        storeAdapter = new StoreAdapter(Glide.with(this), storeList, ((NFCCrossmediaApplication) this.getApplication()).getCart());
        storeRecyclerView.setAdapter(storeAdapter);
    }

    public void getStoreList(){
        DatabaseReference fbStoreReference = FirebaseDatabase.getInstance().getReference().child("stores");

        ValueEventListener storeListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                int loopCounter = 0;
                for (DataSnapshot storesChild:
                     dataSnapshot.getChildren()) {
                    Store store = storesChild.getValue(Store.class);
                    store.addKey(storesChild.getKey());
                    if (store != null) {
                        store.calculateDistance(49.350910, 9.148020);
                        storeList.add(store);
                    } else {
                        showErrorMessage();
                    }
                    loopCounter++;
                    if(loopCounter == dataSnapshot.getChildrenCount()){
                        sortStoreList();
                        fillRecyclerViewWithData();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("TAG", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        fbStoreReference.addListenerForSingleValueEvent(storeListener);
    }
}
