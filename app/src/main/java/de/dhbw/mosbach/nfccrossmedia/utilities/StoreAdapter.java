package de.dhbw.mosbach.nfccrossmedia.utilities;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;

import java.util.ArrayList;
import java.util.HashMap;

import de.dhbw.mosbach.nfccrossmedia.CartActivity;
import de.dhbw.mosbach.nfccrossmedia.ChooseStoreActivity;
import de.dhbw.mosbach.nfccrossmedia.NFCCrossmediaApplication;
import de.dhbw.mosbach.nfccrossmedia.R;
import de.dhbw.mosbach.nfccrossmedia.ShowProductActivity;
import de.dhbw.mosbach.nfccrossmedia.data.CartObject;
import de.dhbw.mosbach.nfccrossmedia.data.Product;
import de.dhbw.mosbach.nfccrossmedia.data.Store;

public class StoreAdapter extends RecyclerView.Adapter<StoreAdapter.StoreViewHolder> {

    private ArrayList<Store> storeList;
    private ArrayList<CartObject> cartList;
    private RequestManager glide;




    public static class StoreViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView storeNameTextView;
        public TextView storeAddressTextView;
        public TextView storeDistanceTextView;
        public ImageView storeImageView;
        public TextView storeAvailabilityView;
        public ConstraintLayout storeItemView;
        public StoreViewHolder(View v) {
            super(v);
            storeNameTextView = v.findViewById(R.id.store_name);
            storeAddressTextView = v.findViewById(R.id.store_address);
            storeDistanceTextView = v.findViewById(R.id.store_distance);
            storeImageView = v.findViewById(R.id.store_image);
            storeAvailabilityView = v.findViewById(R.id.store_availability);
            storeItemView = v.findViewById(R.id.item_store);
        }
    }

    public StoreAdapter(RequestManager glide, ArrayList<Store> sList, ArrayList<CartObject> cList){
        this.storeList = sList;
        this.glide = glide;
        this.cartList = cList;
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public StoreAdapter.StoreViewHolder onCreateViewHolder(ViewGroup parent,
                                                          int viewType) {
        // create a new view
        View v = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_store, parent, false);
        StoreViewHolder vh = new StoreViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @NonNull
    @Override
    public void onBindViewHolder(StoreViewHolder holder, int position) {
        Store thisStore = storeList.get(position);
        holder.storeNameTextView.setText(thisStore.getStoreName());
        glide.load(thisStore.getStoreImage()).transition(DrawableTransitionOptions.withCrossFade()).into(holder.storeImageView);
        holder.storeAddressTextView.setText(thisStore.getStoreAddress());
        holder.storeDistanceTextView.setText(thisStore.getDistanceString());
        holder.storeAvailabilityView.setText(Html.fromHtml(getInventoryInfo(thisStore)));
    }

    public String getInventoryInfo(Store store){
        HashMap<String, Integer> inventory = store.getStoreInventory();
        int thisProductInventory = 0;
        String thisProductName = "";
        String htmlString = "";

        for (int i = 0; i < cartList.size(); i++) {
            Log.i("cartList", cartList.toString());
            thisProductInventory = inventory.get(cartList.get(i).getProduct().prdctId);
            thisProductName = cartList.get(i).getProduct().productName;
            if (thisProductInventory < 1){
                htmlString += "<font color='#b24b39'>" + thisProductName + " nicht verfügbar</font><br/>";
            }
            else {
                htmlString += "<font color='#58b23a'>" + thisProductName + " noch " + thisProductInventory + " mal verfügbar</font><br/>";
            }
        }

        Log.i("VerfügbarkeitHtml", htmlString);

        return htmlString;
    }

    // Return the size of your dataset (invoked by the layout manager)
    @NonNull
    @Override
    public int getItemCount() {
        return storeList.size();
    }
}
