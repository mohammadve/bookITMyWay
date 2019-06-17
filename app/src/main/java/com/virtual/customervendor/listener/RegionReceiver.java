package com.virtual.customervendor.listener;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.virtual.customervendor.model.RegionModel;


public class RegionReceiver extends BroadcastReceiver {
    private static final String TAG=RegionReceiver.class.getSimpleName();
    public static final String ACTION_REGION_RECEIVE="acton_receive_region";
    private RegionObserver observer;

    public RegionReceiver(RegionObserver observer) {
        this.observer = observer;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG,"onReceive");
        RegionModel  region= (RegionModel) intent.getSerializableExtra("region");
        observer.onReceiveRegion(region);
    }

    public static void sendBroadCast(RegionModel region,Context context){
        Log.d(TAG,"sendBroadCast");
        Intent intent=new Intent(ACTION_REGION_RECEIVE);
        intent.putExtra("region",region);
        context.sendBroadcast(intent);
    }

    public static RegionReceiver registerReceiver(RegionObserver observer, Context context){
        Log.d(TAG,"registerReceiver");
        RegionReceiver receiver=new RegionReceiver(observer);
        context.registerReceiver(receiver,new IntentFilter(ACTION_REGION_RECEIVE));
        return receiver;
    }

    public static void unRegisterReceiver(RegionReceiver receiver, Context context){
        Log.d(TAG,"unRegisterReceiver");
        context.unregisterReceiver(receiver);
    }

    public interface RegionObserver{
        void onReceiveRegion(RegionModel region);
    }

}
