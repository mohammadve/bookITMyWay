package com.virtual.customervendor.datasourcefactory;
import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.DataSource;
import android.arch.paging.PageKeyedDataSource;

import com.virtual.customervendor.datasource.RestaurantDataSource;
import com.virtual.customervendor.model.CustomerBookingListModel;

/**
 * Created by dalvendrakumar on 13/12/18.
 */

public class RestaurantDataSourceFactory extends DataSource.Factory {

    //creating the mutable live data
    private MutableLiveData<PageKeyedDataSource<Integer, CustomerBookingListModel>> restaurantLiveDataSource = new MutableLiveData<>();

    @Override
    public DataSource<Integer, CustomerBookingListModel> create() {
        //getting our data source object
        RestaurantDataSource itemDataSource = new RestaurantDataSource();

        //posting the datasource to get the values
        restaurantLiveDataSource.postValue(itemDataSource);

        //returning the datasource
        return itemDataSource;
    }


    //getter for itemlivedatasource
    public MutableLiveData<PageKeyedDataSource<Integer, CustomerBookingListModel>> getRestaurantLiveDataSource() {
        return restaurantLiveDataSource;
    }
}
