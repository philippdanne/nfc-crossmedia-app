package de.dhbw.mosbach.nfccrossmedia.utilities;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

import java.util.ArrayList;
import java.util.List;

import de.dhbw.mosbach.nfccrossmedia.NfcTagDiscovered;
import de.dhbw.mosbach.nfccrossmedia.R;
import de.dhbw.mosbach.nfccrossmedia.data.RelatedProduct;

public class RelatedProductsAdapter extends
        RecyclerView.Adapter<RelatedProductsAdapter.RPViewHolder> {
    private ArrayList<RelatedProduct> vRelatedProducts;
    private RequestManager glide;

    // Pass in the contact array into the constructor
    public RelatedProductsAdapter(RequestManager glide, ArrayList<RelatedProduct> relatedProducts) {
        this.glide = glide;
        vRelatedProducts = relatedProducts;
    }

    @NonNull
    @Override
    public RPViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View relatedProductView = inflater.inflate(R.layout.item_related_product, parent, false);

        // Return a new holder instance
        RPViewHolder viewHolder = new RPViewHolder(relatedProductView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RPViewHolder holder, int position) {
        RelatedProduct relatedProduct = vRelatedProducts.get(position);

        // Set item views based on your views and data model
        holder.nameTextView.setText(relatedProduct.productName);
        glide.load(relatedProduct.getProductImages(0)).into(holder.imageImageView);
    }

    @Override
    public int getItemCount() {
        return vRelatedProducts.size();
    }

    public class RPViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView nameTextView;
        public ImageView imageImageView;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public RPViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            nameTextView = (TextView) itemView.findViewById(R.id.relatedProductNameTextView);
            imageImageView = (ImageView) itemView.findViewById(R.id.relatedProductImageImageView);
        }
    }
}
