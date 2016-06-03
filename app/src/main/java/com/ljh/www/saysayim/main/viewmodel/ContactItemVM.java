package com.ljh.www.saysayim.main.viewmodel;

import android.content.Context;
import android.databinding.ObservableField;
import com.ljh.www.saysayim.base.ViewModel;

/**
 * Created by ljh on 2016/6/2.
 */
public class ContactItemVM extends ViewModel {

    public final ObservableField<String> name =new ObservableField<>("name");
    public ContactItemVM(Context context) {
    }
}
