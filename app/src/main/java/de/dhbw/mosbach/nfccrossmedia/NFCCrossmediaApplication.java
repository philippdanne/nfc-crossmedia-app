package de.dhbw.mosbach.nfccrossmedia;

import android.app.Application;
import android.util.Log;

import java.util.ArrayList;

import de.dhbw.mosbach.nfccrossmedia.data.CartObject;
import de.dhbw.mosbach.nfccrossmedia.data.Product;

public class NFCCrossmediaApplication extends Application {
    private ArrayList<CartObject> cartList;
    private int cartCount;
    private boolean alreadyExists;

    public NFCCrossmediaApplication(){
        cartList = new ArrayList<>();
        cartCount = 0;
        alreadyExists = false;
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

    public void removeFromCart(Product product) {
        cartList.remove(product);
    }

    public String getCartCount(){
        return Integer.toString(cartCount);
    }
}
