package com.virtual.customervendor.commonActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.virtual.customervendor.R;
import com.virtual.customervendor.adapter.TimeManagerAdapter;
import com.virtual.customervendor.customer.ui.dialogFragment.TimeDialogFragment;
import com.virtual.customervendor.customview.CustomTextView;
import com.virtual.customervendor.model.DayAviliability;

import java.util.ArrayList;

public class TimeManagerActivity extends BaseActivity implements TimeManagerAdapter.SlotsListener {
    private static final String TAG= TimeDialogFragment.class.getSimpleName();
    public static final int REQUEST_CODE=201;
    public static final int RESULT_CODE=202;
    public static final String KEY_Multi_Slots="isMultiSlots";
    public static final String KEY_TIME_SLOTS_LIST="selectedTimeSlots";

    private RecyclerView recyclerView;
    private TimeManagerAdapter adapter;
    private Boolean isMultiSlots=false;
    private ArrayList<DayAviliability> aviliabilities=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_time_manager);

        aviliabilities.add(new DayAviliability("Mon",false));
        aviliabilities.add(new DayAviliability("Tue",false));
        aviliabilities.add(new DayAviliability("Wed",false));
        aviliabilities.add(new DayAviliability("Thu",false));
        aviliabilities.add(new DayAviliability("Fri",false));
        aviliabilities.add(new DayAviliability("Sat",false));
        aviliabilities.add(new DayAviliability("Sun",false));

        init();

    }

    private void init(){
        try {
            isMultiSlots=getIntent().getExtras().getBoolean(KEY_Multi_Slots);
        }catch (Exception e){
            e.printStackTrace();
        }
        CustomTextView tv_toolbarTitleText=findViewById(R.id.tv_toolbarTitleText);
        tv_toolbarTitleText.setText("Select Aviliability");
        recyclerView=findViewById(R.id.recyclerView);
        adapter=new TimeManagerAdapter(aviliabilities,this,isMultiSlots);
        LinearLayoutManager manager=new LinearLayoutManager(getBaseContext(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onSlotSelection() {

    }

    @Override
    public void onBackPressed() {
        Intent data=new Intent();
        data.putExtra(KEY_TIME_SLOTS_LIST,aviliabilities);
        setResult(RESULT_CODE,data);
        super.onBackPressed();
    }
}
