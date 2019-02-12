package de.dhbw.mosbach.nfccrossmedia.data;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

public class Store {
    private String storeId = "";
    private String storeName;
    private String storeImage;
    private String storeAddress;
    private double storeLatitude;
    private double storeLongitude;
    private double storeDistance;
    private HashMap<String, Integer> storeInventory;

    public Store() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Store(String storeName, String storeImage, String storeAddress, double storeLatitude, double storeLongitude, HashMap<String, Integer> storeInventory) {
        this.storeName = storeName;
        this.storeImage = storeImage;
        this.storeAddress = storeAddress;
        this.storeLatitude = storeLatitude;
        this.storeLongitude = storeLongitude;
        this.storeInventory = storeInventory;
    }

    public String getStoreAddress(){
        return storeAddress;
    }

    public double getStoreLatitude(){
        return storeLatitude;
    }

    public double getStoreLongitude() {
        return storeLongitude;
    }

    public String getStoreImage(){
        return storeImage;
    }

    public String getStoreName(){
        return storeName;
    }

    public HashMap<String, Integer> getStoreInventory(){
        return storeInventory;
    }

    public double getDistance(){
        return storeDistance;
    }

    public void addKey(String key){
        storeId = key;
    }

    public String getKey(){
        if(!storeId.equals("")){
            return storeId;
        }
        else{
            return null;
        }
    }

    public String getDistanceString(){

        String distanceToStore;
        String tmpStr = Double.toString(storeDistance);

        Log.i("Geht der hier rein?", tmpStr);

        if(storeDistance < 1){
            tmpStr = tmpStr.substring(2, 5);
            distanceToStore = tmpStr + " m";
        }
        else if(storeDistance < 0.1){
            tmpStr = tmpStr.substring(3, 5);
            distanceToStore = tmpStr + " m";
        }
        else if(storeDistance > 10){
            tmpStr = tmpStr.substring(0, 2);
            distanceToStore = tmpStr + " km";
        }
        else{
            tmpStr = tmpStr.substring(0, 3);
            distanceToStore = tmpStr.replace(".", ",") + " km";
        }

        return distanceToStore + " entfernt";
    }

    public void calculateDistance(double latStart, double lonStart){
        int Radius = 6371;// radius of earth in Km
        double dLat = Math.toRadians(storeLatitude - latStart);
        double dLon = Math.toRadians(storeLongitude - lonStart);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(latStart))
                * Math.cos(Math.toRadians(storeLatitude)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult = Radius * c;

        Log.i("valueResult", Double.toString(storeDistance));

        storeDistance = valueResult;
    }
}
