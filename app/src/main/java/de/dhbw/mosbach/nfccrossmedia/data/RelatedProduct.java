package de.dhbw.mosbach.nfccrossmedia.data;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class RelatedProduct {
    public String productName;
    public String imgUrl;
    public ArrayList<String> productImages;

    public RelatedProduct() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public RelatedProduct(String productName, ArrayList<String> productImages) {
        this.productName = productName;
        this.productImages = productImages;
    }

    public String getProductImages(int imgNo) {
        try {
            JSONArray imgsJson = new JSONArray(productImages);
            ArrayList<String> imgsArrayList = new ArrayList<String>();
            if (imgsJson != null) {
                for (int i = 0; i < imgsJson.length(); i++) {
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
