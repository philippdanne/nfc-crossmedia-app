package de.dhbw.mosbach.nfccrossmedia.data;

public class RelatedProduct {
    public String productName;

    public RelatedProduct() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public RelatedProduct(String productName){
        this.productName = productName;
    }
}
