package de.dhbw.mosbach.nfccrossmedia.data;

import android.util.Log;

import com.google.firebase.database.IgnoreExtraProperties;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

@IgnoreExtraProperties
public class Product {

    public String productName;
    public String productDescription;
    public ArrayList<String> productImages;
    public String imgUrl;

    public Product() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Product(String productName, String productDescription, ArrayList<String> productImages) {
        this.productName = productName;
        this.productDescription = productDescription;
        this.productImages = productImages;
    }

    public String getProductImages(int imgNo) {
        try {
            JSONArray imgsJson = new JSONArray(productImages);
            ArrayList<String> imgsArrayList = new ArrayList<String>();
            if (imgsJson != null) {
                for (int i=0;i<imgsJson.length();i++){
                    imgsArrayList.add(imgsJson.getString(i));
                }
            }
            imgUrl = imgsArrayList.get(imgNo);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return imgUrl;
    }
}
