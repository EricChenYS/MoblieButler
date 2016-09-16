package com.chhd.mobliebutler.adapter;

import cn.sharesdk.framework.authorize.AuthorizeAdapter;

/**
 * Created by CWQ on 2016/8/26.
 */
public class MyAuthorizeAdapter extends AuthorizeAdapter {

    @Override
    public void onCreate() {
        super.onCreate();
        hideShareSDKLogo();
    }
}
