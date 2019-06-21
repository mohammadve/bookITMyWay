package com.virtual.customervendor.commonActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CompoundButton;

import com.virtual.customervendor.R;
import com.virtual.customervendor.adapter.TimeManagerAdapter;
import com.virtual.customervendor.customer.ui.dialogFragment.TimeDialogFragment;
import com.virtual.customervendor.customview.CustomTextView;
import com.virtual.customervendor.model.DayAviliability;

import java.util.ArrayList;

public class TimeManagerActivity extends BaseActivity {
    private static final String TAG= TimeDialogFragment.class.getSimpleName();
    public static final int REQUEST_CODE=201;
    public static final int RESULT_CODE=202;
    public static final String KEY_Multi_Slots="isMultiSlots";
    public static final String KEY_ALL_DAY_SAME="allDaySameSlots";
    public static final String KEY_TIME_SLOTS_LIST="selectedTimeSlots";

    private RecyclerView recyclerView;
    private TimeManagerAdapter adapter;
    private Boolean isMultiSlots=false,isSameSlots=false;
    private ArrayList<DayAviliability> aviliabilities=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_time_manager);

        init();

    }

    private void init(){
        try {
            isMultiSlots=getIntent().getExtras().getBoolean(KEY_Multi_Slots);
            if(getIntent().getExtras().containsKey(KEY_TIME_SLOTS_LIST)){
                aviliabilities = (ArrayList<DayAviliability>) getIntent().getExtras().getSerializable(KEY_TIME_SLOTS_LIST);
            }else
                setSlotsBlank(false);

        }catch (Exception e){
            e.printStackTrace();
        }
        CustomTextView tv_toolbarTitleText=findViewById(R.id.tv_toolbarTitleText);
        AppCompatCheckBox cbSameSlots=findViewById(R.id.cbSameSlots);
        recyclerView=findViewById(R.id.recyclerView);
        adapter=new TimeManagerAdapter(aviliabilities,isMultiSlots,isSameSlots);
        LinearLayoutManager manager=new LinearLayoutManager(getBaseContext(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        tv_toolbarTitleText.setText("Select Aviliability");

        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        cbSameSlots.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isSameSlots=isChecked;

                if(isChecked)
                    setSlotsBlank(true);
                else if(getIntent().getExtras().containsKey(KEY_TIME_SLOTS_LIST)){
                    aviliabilities = (ArrayList<DayAviliability>) getIntent().getExtras().getSerializable(KEY_TIME_SLOTS_LIST);
                }else
                    setSlotsBlank(false);
                adapter.setSameSlots(isSameSlots);
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void setSlotsBlank(boolean isSameSlots) {
        aviliabilities.clear();
        if (isSameSlots) {
            ArrayList<DayAviliability.TimeSlot> slots=new ArrayList<>();
            aviliabilities.add(new DayAviliability("Monday", true,slots));
            aviliabilities.add(new DayAviliability("Tuesday", true,slots));
            aviliabilities.add(new DayAviliability("Wednesday", true,slots));
            aviliabilities.add(new DayAviliability("Thursday", true,slots));
            aviliabilities.add(new DayAviliability("Friday", true,slots));
            aviliabilities.add(new DayAviliability("Saturday", true,slots));
            aviliabilities.add(new DayAviliability("Sunday", true,slots));

        } else {
            aviliabilities.add(new DayAviliability("Monday", false));
            aviliabilities.add(new DayAviliability("Tuesday", false));
            aviliabilities.add(new DayAviliability("Wednesday", false));
            aviliabilities.add(new DayAviliability("Thursday", false));
            aviliabilities.add(new DayAviliability("Friday", false));
            aviliabilities.add(new DayAviliability("Saturday", false));
            aviliabilities.add(new DayAviliability("Sunday", false));
        }
    }

    @Override
    public void onBackPressed() {
        Intent data=new Intent();
        data.putExtra(KEY_TIME_SLOTS_LIST,aviliabilities);
        data.putExtra(KEY_ALL_DAY_SAME,isSameSlots?1:0);
        setResult(RESULT_CODE,data);
        super.onBackPressed();
    }
}
