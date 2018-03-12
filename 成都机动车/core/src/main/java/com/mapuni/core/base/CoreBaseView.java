package com.mapuni.core.base;

import android.content.Context;

/**
 * Created by lybin on 16/10/27.
 */

public interface CoreBaseView {
    Context getContext();

    void showError(String msg);
}
