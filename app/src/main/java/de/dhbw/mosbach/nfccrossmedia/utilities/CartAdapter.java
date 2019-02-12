package de.dhbw.mosbach.nfccrossmedia.utilities;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;

import java.util.ArrayList;

import de.dhbw.mosbach.nfccrossmedia.R;
import de.dhbw.mosbach.nfccrossmedia.data.CartObject;
import de.dhbw.mosbach.nfccrossmedia.data.Product;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private ArrayList<CartObject> cartList;
    private RequestManager glide;

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView productNameTextView;
        public TextView productCountTextView;
        public TextView addProduct;
        public TextView subtractProduct;
        public ImageView productImageImageView;
        public CartViewHolder(View v) {
            super(v);
            productCountTextView = v.findViewById(R.id.cart_product_count);
            productNameTextView = v.findViewById(R.id.cart_product_name);
            productImageImageView = v.findViewById(R.id.cart_product_image);
            addProduct = v.findViewById(R.id.cart_add);
            subtractProduct = v.findViewById(R.id.cart_subtract);
        }
    }

    public CartAdapter(RequestManager glide, ArrayList<CartObject> cList){
        cartList = cList;
        this.glide = glide;
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public CartAdapter.CartViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        View v = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cart, parent, false);
        CartViewHolder vh = new CartViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @NonNull
    @Override
    public void onBindViewHolder(CartViewHolder holder, int position) {
        holder.productNameTextView.setText(cartList.get(position).getProduct().productName);
        holder.productCountTextView.setText(cartList.get(position).getCount());
        glide.load(cartList.get(position).getProduct().productImage).into(holder.productImageImageView);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @NonNull
    @Override
    public int getItemCount() {
        return cartList.size();
    }
}
