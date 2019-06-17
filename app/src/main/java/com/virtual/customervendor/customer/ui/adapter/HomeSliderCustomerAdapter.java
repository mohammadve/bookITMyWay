package com.virtual.customervendor.customer.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.virtual.customer_vendor.utill.AppUtill;
import com.virtual.customervendor.R;
import com.virtual.customervendor.customer.ui.ViewPagerItemClickedCustomer;
import com.virtual.customervendor.model.BannerModel;
import com.virtual.customervendor.utills.AppLog;

import java.util.ArrayList;

public class HomeSliderCustomerAdapter extends PagerAdapter {
    private LayoutInflater layoutInflater;
    Activity activity;
    ArrayList<BannerModel> imgList;
    ViewPagerItemClickedCustomer itemClicked;
    Boolean fromEdit;

    public HomeSliderCustomerAdapter(Activity activity, ArrayList<BannerModel> imgList, ViewPagerItemClickedCustomer itemClicked, boolean fromEdit) {
        this.activity = activity;
        this.imgList = imgList;
        this.itemClicked = itemClicked;
        this.fromEdit = fromEdit;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = layoutInflater.inflate(R.layout.customer_dashboard_slider_layout_home, container, false);
        ImageView im_slider = (ImageView) view.findViewById(R.id.im_slider);
        ImageView img_cancel = (ImageView) view.findViewById(R.id.img_cancel);
        im_slider.setId(position);
        img_cancel.setId(position);


        if (fromEdit) img_cancel.setVisibility(View.VISIBLE);
        else img_cancel.setVisibility(View.GONE);


//        DisplayMetrics displayMetrics = new DisplayMetrics();
//        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
//        int height = (int) (displayMetrics.heightPixels * .85);
//        int width = ((displayMetrics.widthPixels));
//
//        AppLog.e("CheckDimens", imgList.get(position).getUrl() + "height =" + height + "width=" + width);

//        RequestOptions options = new RequestOptions();
////        options.fitCenter();
////        options.override(height, width);
//        options.placeholder(R.drawable.place_holder_banner);
//        Glide.with(im_slider.getContext())
//                .load(imgList.get(position).getUrl())
//                .apply(options)
//                .into(im_slider);

        AppUtill.Companion.loadImageHomeBanner(im_slider.getContext(),imgList.get(position).getUrl(),im_slider);

        im_slider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClicked != null) {
                    int index = v.getId();
                    itemClicked.onPagerItemClicked(imgList.get(index));
                }
            }
        });
        container.addView(view);
        return view;
    }

//    public void refrehList(ArrayList<BusinessImage> imgList) {
//        this.imgList = imgList;
//        notifyDataSetChanged();
//
//    }

    @Override
    public int getCount() {
        return imgList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object obj) {
        return view == obj;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        container.removeView(view);
    }
}
