package de.dhbw.mosbach.nfccrossmedia;

import android.app.Application;

import java.util.ArrayList;

import de.dhbw.mosbach.nfccrossmedia.data.CartObject;
import de.dhbw.mosbach.nfccrossmedia.data.Product;

public class NFCCrossmediaApplication extends Application {
    private ArrayList<CartObject> cartList;
    private int cartCount;
    private boolean alreadyExists;
    private double posterLatitude;
    private double posterLongitude;

    public NFCCrossmediaApplication(){
        cartList = new ArrayList<>();
        cartCount = 0;
        alreadyExists = false;
        posterLatitude = 0;
        posterLongitude =  0;
    }

    public void setPosterLatitude(String nfcLatitude){
        posterLatitude = Double.valueOf(nfcLatitude);
    }

    public void setPosterLongitude(String nfcLongitude){
        posterLongitude = Double.valueOf(nfcLongitude);
    }

    public double getPosterLatitude(){
        return posterLatitude;
    }

    public double getPosterLongitude(){
        return posterLongitude;
    }

    public ArrayList<CartObject> getCart() {
        return cartList;
    }

    public void addToCart(Product product) {
        int existingProductPos = 0;

        for (int i = 0; i < cartList.size(); i++){
            if(product.prdctId == cartList.get(i).getProduct().prdctId){
                alreadyExists = true;
                existingProductPos = i;
            }
        }

        if(alreadyExists) {
            cartList.get(existingProductPos).increaseCount();
        }
        else{
            CartObject thisCartObject = new CartObject(product);
            cartList.add(new CartObject(product));
        }

        cartCount += 1;
        alreadyExists = false;
    }

    public String getCartPrice(){
        double tmpPrice = 0;
        for (CartObject thisObject:
             cartList) {
            tmpPrice += thisObject.getProduct().productPrice * Double.valueOf(thisObject.getCount());
        }
        String tmpString = "";
        tmpString = Double.toString(tmpPrice);
        tmpString = tmpString.replace(".", ",");
        tmpString = tmpString.substring(0, tmpString.indexOf(",") + 3);
        tmpString += " €";
        return tmpString;
    }

    public void removeFromCart(Product product) {
        cartList.remove(product);
    }

    public String getCartCount(){
        return Integer.toString(cartCount);
    }
}
