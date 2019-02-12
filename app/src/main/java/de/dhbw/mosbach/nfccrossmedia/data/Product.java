package de.dhbw.mosbach.nfccrossmedia.data;

import android.util.Log;

import com.google.firebase.database.IgnoreExtraProperties;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

@IgnoreExtraProperties
public class Product {

    public String productName;
    public String productDescription;
    public String productImage;
    public ArrayList<String> relatedProducts;
    public double productPrice;
    public String prdctId;
    public ArrayList<RelatedProduct> prdctsArrayList;

    public Product() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Product(String productName, double productPrice, String productDescription, String productImage, ArrayList<String> relatedProducts) {
        this.productName = productName;
        this.productDescription = productDescription;
        this.productImage = productImage;
        this.relatedProducts = relatedProducts;
        this.prdctsArrayList = new ArrayList<>();
        this.productPrice = productPrice;
    }

    public String getProductPriceString(){
        String returnString = "";
        returnString = Double.toString(productPrice);
        returnString = returnString.replace(".", ",");
        returnString += " €";
        return returnString;
    }

    public String getProductId(int prNo) {
        try {
            JSONArray prdctJson = new JSONArray(relatedProducts);
            ArrayList<String> prdctArrayList = new ArrayList<>();
            if (prdctJson != null) {
                for (int i = 0; i < prdctJson.length(); i++) {
                    prdctArrayList.add(prdctJson.getString(i));
                }
            }
            else{
                Log.i("TAG", "ArrayList ist leer!");
            }
            prdctId = prdctArrayList.get(prNo);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("prdctId", prdctId);
        return prdctId;
    }

    public int getRelatedProductsLength(){
        JSONArray prdctJson = new JSONArray(relatedProducts);
        return prdctJson.length();
    }

    public ArrayList<RelatedProduct> getRelatedProducts() {
        JSONArray prdctsJson = new JSONArray(relatedProducts);
        if (prdctsJson != null) {
            for (int i = 0; i < prdctsJson.length(); i++) {
                prdctsArrayList.add(new RelatedProduct());
            }
        }
        return prdctsArrayList;
    }
}
