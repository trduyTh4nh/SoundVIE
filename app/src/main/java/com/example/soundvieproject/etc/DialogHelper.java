package com.example.soundvieproject.etc;

import android.content.Context;
import android.view.View;

public class DialogHelper {
    Context context;
    public static final int DIALOG_CONFIRM = 1;
    public static final int DIALOG_ERROR = 2;
    public static final int DIALOG_INFO = 3;
    public static final int DIALOG_WARNING = 4;

    public DialogHelper(Context context) {
        this.context = context;
    }
    public void prepareDialog(int type){

    }
    public void setClickPositiveEvent(View.OnClickListener callback){

    }
    public void setClickNegativeEvent(View.OnClickListener callback){

    }
}
