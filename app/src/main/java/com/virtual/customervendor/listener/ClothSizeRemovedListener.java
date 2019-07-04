package com.virtual.customervendor.listener;

import com.virtual.customervendor.model.response.StoreClothSizeModel;

public interface ClothSizeRemovedListener {
    void onSizeRemoved(StoreClothSizeModel clothSizeModel  );
}
