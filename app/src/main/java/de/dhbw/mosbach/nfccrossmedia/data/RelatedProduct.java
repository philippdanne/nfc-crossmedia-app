package de.dhbw.mosbach.nfccrossmedia.data;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class RelatedProduct {
    public String productId;
    public String productName;
    public String imgUrl;
    public String productImage;

    public RelatedProduct() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public RelatedProduct(String productName, String productImage, String prdctId) {
        this.productId = prdctId;
        this.productName = productName;
        this.productImage = productImage;
    }
}
