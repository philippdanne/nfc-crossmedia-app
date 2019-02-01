package de.dhbw.mosbach.nfccrossmedia.data;

public class CartObject {
    private Product product;
    private int count;

    public CartObject(Product pProduct){
        product = pProduct;
    }

    public Product getProduct(){
        return product;
    }

    public String getCount(){
        return Integer.toString(count + 1);
    }

    public void increaseCount(){
        count += 1;
    }
}
