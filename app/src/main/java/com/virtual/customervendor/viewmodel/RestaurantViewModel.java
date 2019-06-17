//package com.virtual.customervendor.viewmodel;
//
//import android.arch.lifecycle.LiveData;
//import android.arch.lifecycle.ViewModel;
//import android.arch.paging.LivePagedListBuilder;
//import android.arch.paging.PageKeyedDataSource;
//import android.arch.paging.PagedList;
//
//import com.virtual.customervendor.datasource.RestaurantDataSource;
//import com.virtual.customervendor.datasourcefactory.RestaurantDataSourceFactory;
//import com.virtual.customervendor.model.CustomerBookingListModel;
//
///**
// * Created by dalvendrakumar on 13/12/18.
// */
//
//public class RestaurantViewModel extends ViewModel {
//    public LiveData<PagedList<CustomerBookingListModel>> itemPagedList;
//    public LiveData<PageKeyedDataSource<Integer, CustomerBookingListModel>> liveDataSource;
//
//    //constructor
//    public RestaurantViewModel() {
//        //getting our data source factory
//        RestaurantDataSourceFactory restaurantDataSourceFactory = new RestaurantDataSourceFactory();
//
//        //getting the live data source from data source factory
//        liveDataSource = restaurantDataSourceFactory.getRestaurantLiveDataSource();
//
//        //Getting PagedList config
//        PagedList.Config pagedListConfig =
//                (new PagedList.Config.Builder())
//                        .setEnablePlaceholders(false)
//                        .setPageSize(RestaurantDataSource.PAGE_SIZE).build();
//
//        //Building the paged list
//        itemPagedList = (new LivePagedListBuilder(restaurantDataSourceFactory, pagedListConfig))
//                .build();
//
//
//    }
//}
