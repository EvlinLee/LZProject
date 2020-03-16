package com.by.lizhiyoupin.app;

import com.by.lizhiyoupin.app.common.ComponentManager;
import com.by.lizhiyoupin.app.common.IComponent;
import com.by.lizhiyoupin.app.component_ui.manager.IDialogManager;
import com.by.lizhiyoupin.app.component_ui.manager.IWechatManager;
import com.by.lizhiyoupin.app.component_ui.web.Scheme;
import com.by.lizhiyoupin.app.io.manager.IAccountManager;
import com.by.lizhiyoupin.app.io.manager.ISchemeManager;
import com.by.lizhiyoupin.app.manager.DiaLogManagerImpl;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/9/23 10:19
 * Summary:
 * final IAccountManager accountManager = (IAccountManager) ComponentManager.getInstance()
 *  *                                     .getManager(IAccountManager.class.getName());
 * */
public class LiZhiComponent implements IComponent {
    @Override
    public void onCreate() {
        final ComponentManager componentManager = ComponentManager.getInstance();
        final LiZhiApplication liZhiApplication = LiZhiApplication.getApplication();
        componentManager.addManager(IAccountManager.class.getName(), liZhiApplication.getAccountManager());
        componentManager.addManager(ISchemeManager.class.getName(), new Scheme());
        componentManager.addManager(IDialogManager.class.getName(), new DiaLogManagerImpl());
        componentManager.addManager(IWechatManager.class.getName(),new WechatManager());

    }

    @Override
    public void onDestroy() {
        final ComponentManager componentManager = ComponentManager.getInstance();
        componentManager.removeManager(IAccountManager.class.getName());
        componentManager.removeManager(ISchemeManager.class.getName());
        componentManager.removeManager(IDialogManager.class.getName());
        componentManager.removeManager(IWechatManager.class.getName());
    }
}
