package com.virtual.customervendor.datasource;

import android.arch.paging.PageKeyedDataSource;
import android.support.annotation.NonNull;

import com.virtual.customervendor.managers.SharedPreferenceManager;
import com.virtual.customervendor.model.CustomerBookingListModel;
import com.virtual.customervendor.model.RegionModel;
import com.virtual.customervendor.model.response.CustomerDisplayListResponse;
import com.virtual.customervendor.networks.ApiClient;
import com.virtual.customervendor.networks.ApiInterface;
import com.virtual.customervendor.utills.AppConstants;
import com.virtual.customervendor.utills.AppKeys;

import java.util.HashMap;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by dalvendrakumar on 13/12/18.
 */

public class RestaurantDataSource extends PageKeyedDataSource<Integer, CustomerBookingListModel> {

    //the size of a page that we want
    public static final int PAGE_SIZE = 50;

    //we will start from the first page which is 1
    private static final int FIRST_PAGE = 1;

    //we need to fetch from stackoverflow
    private static final String SITE_NAME = "stackoverflow";


    //this will be called once to load the initial data
    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull final LoadInitialCallback<Integer, CustomerBookingListModel> callback) {
        getRestaurents(null,callback);

    }

    private void getRestaurents(RegionModel regionModel, final LoadInitialCallback<Integer, CustomerBookingListModel> callback) {
        ApiInterface apiService = ApiClient.INSTANCE.getClient().create(ApiInterface.class);

        HashMap<String, String> fieldMap = new HashMap<>();
        if (regionModel != null) {
            fieldMap.put(AppKeys.KEY_REGIONID, regionModel.getRegionid());
        }
        fieldMap.put(AppKeys.CATEGORY_ID, AppConstants.CAT_RESTAURANT_DINNIG + "");
        fieldMap.put(AppKeys.SUBCATEGORY_ID, AppConstants.SUBCAT_RESTAURANT_DINNIG + "");
        fieldMap.put(AppKeys.OFFSET, "0");
        apiService.getRestaurentsList("Bearer " + SharedPreferenceManager.getAuthToken(), fieldMap).
                subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CustomerDisplayListResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(CustomerDisplayListResponse customerDisplayListResponse) {

                        callback.onResult(customerDisplayListResponse.getData(),null,2);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    //this will load the previous page
    @Override
    public void loadBefore(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, CustomerBookingListModel> callback) {
//        RetrofitClient.getInstance()
//                .getApi().getAnswers(params.key, PAGE_SIZE, SITE_NAME)
//                .enqueue(new Callback<StackApiResponse>() {
//                    @Override
//                    public void onResponse(Call<StackApiResponse> call, Response<StackApiResponse> response) {
//
//                        //if the current page is greater than one
//                        //we are decrementing the page number
//                        //else there is no previous page
//                        Integer adjacentKey = (params.key > 1) ? params.key - 1 : null;
//                        if (response.body() != null) {
//
//                            //passing the loaded data
//                            //and the previous page key
//                            callback.onResult(response.body().items, adjacentKey);
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<StackApiResponse> call, Throwable t) {
//
//                    }
//                });
    }

    //this will load the next page
    @Override
    public void loadAfter(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, CustomerBookingListModel> callback) {
//        RetrofitClient.getInstance()
//                .getApi()
//                .getAnswers(params.key, PAGE_SIZE, SITE_NAME)
//                .enqueue(new Callback<StackApiResponse>() {
//                    @Override
//                    public void onResponse(Call<StackApiResponse> call, Response<StackApiResponse> response) {
//
//                        if (response.body() != null) {
//                            //if the response has next page
//                            //incrementing the next page number
//                            Integer key = response.body().has_more ? params.key + 1 : null;
//
//                            //passing the loaded data and next page value
//                            callback.onResult(response.body().items, key);
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<StackApiResponse> call, Throwable t) {
//
//                    }
//                });
    }

}
