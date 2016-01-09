package com.shyling.healthmanager.activity;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.shyling.healthmanager.R;

/**
 * Created by Mars on 2016/1/7.
 */

/**
 * 设置中心自定义控件
 */
public class SettingItemView extends RelativeLayout {
    private static final String NAMESPACE = "http://schemas.android.com/apk/res-auto";
    TextView tvTitle, tvDesc;
    String mTitle, mDesc_on, mDesc_off;
    Switch stStatus;

    //有new 对象时调用此方法
    public SettingItemView(Context context) {
        super(context);
        initView();
    }

    //有属性调用此方法
    public SettingItemView(Context context, AttributeSet attrs) {
        super(context, attrs);

        //根据属性名称，获取属性内容
        mTitle = attrs.getAttributeValue(NAMESPACE, "tvTitle");
        mDesc_on = attrs.getAttributeValue(NAMESPACE, "desc_on");
        mDesc_off = attrs.getAttributeValue(NAMESPACE, "desc_off");
        initView();
    }

    //有style样式会调用此方法
    public SettingItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    /**
     * 初始化布局
     */
    private void initView() {
        //将自定义好的布局文件设置给当前的SettingItemView
        View.inflate(getContext(), R.layout.view_setting_item, this);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvDesc = (TextView) findViewById(R.id.tv_desc);
        stStatus = (Switch) findViewById(R.id.cb_status);
        //设置标题
        setTitle(mTitle);

    }

    public void setTitle(String title) {
        tvTitle.setText(title);
    }

    public void setDesc(String desc) {
        tvDesc.setText(desc);
    }

    /**
     * 返回勾选状态
     *
     * @return
     */
    public boolean isChecked() {
        return stStatus.isChecked();
    }

    public void setCheck(boolean check) {
        stStatus.setChecked(check);
        if (check){
            setDesc(mDesc_on);
        }else {
            setDesc(mDesc_off);
        }
    }
}

