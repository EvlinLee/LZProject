package com.by.lizhiyoupin.app.component_umeng.share;


import com.by.lizhiyoupin.app.common.ComponentManager;
import com.by.lizhiyoupin.app.common.IComponent;
import com.by.lizhiyoupin.app.component_umeng.share.manager.IShareManager;
import com.by.lizhiyoupin.app.component_umeng.share.manager.ShareManager;

/**
 *    final IShareManager shareManager = (IShareManager) ComponentManager.getInstance()
 *                              .getManager(IShareManager.class.getName());
 */

public class SocialComponent implements IComponent {

    @Override
    public void onCreate() {
        final ComponentManager componentManager = ComponentManager.getInstance();
        componentManager.addManager(IShareManager.class.getName(), new ShareManager());
    }

    @Override
    public void onDestroy() {
        final ComponentManager componentManager = ComponentManager.getInstance();
        componentManager.removeManager(IShareManager.class.getName());
    }
}
