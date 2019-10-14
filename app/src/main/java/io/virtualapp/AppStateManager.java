package io.virtualapp;


import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.util.ArrayMap;
import android.widget.Toast;

import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.client.ipc.VActivityManager;

import io.virtualapp.abs.ui.VUiKit;
import io.virtualapp.home.models.PackageAppData;
import io.virtualapp.home.repo.PackageAppDataStorage;

public class AppStateManager {
    private static AppStateManager sInst = new AppStateManager();
    ArrayMap<String, Boolean> appMap = new ArrayMap<>();
    private Handler mainHandler = new Handler(Looper.getMainLooper());
    private final VActivityManager.UiCallback mUiCallback = new VActivityManager.UiCallback() {
        @Override
        public void onAppOpened(String packageName, int userId) {
            if (appListener != null) {
                appListener.onAppOpened(packageName, userId);
            }
        }

        @Override
        public void onAppDead(String packageName, int userId) {
            if (appListener != null) {
                appListener.onAppDead(packageName, userId);
            }
            if (isAppEnable(packageName)) {
                mainHandler.postDelayed(() -> doLunchApp(packageName, userId), 1000);
            }
        }
    };
    private AppStateListener appListener;

    public static AppStateManager inst() {
        return sInst;
    }

    public interface AppStateListener  {
        void onAppOpened(java.lang.String packageName, int userId);
        void onAppDead(java.lang.String packageName, int userId);
    }

    public AppStateManager() {
        VActivityManager.get().setUiCallback(mUiCallback);
    }

    private void doLunchApp(String packageName, int userId) {
        if (VirtualCore.get().isAppRunning(packageName, userId)) {
            Toast.makeText(VApp.getApp(), "程序正在运行", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = VirtualCore.get().getLaunchIntent(packageName, userId);
        PackageAppData appModel = PackageAppDataStorage.get().acquire(packageName);
        VUiKit.defer().when(() -> {
            if (!appModel.fastOpen) {
                try {
                    VirtualCore.get().preOpt(appModel.packageName);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            VActivityManager.get().startActivity(intent, userId);
        });
    }

    public void setAppListener(AppStateListener listener) {
        this.appListener = listener;
    }


    public synchronized void setEnableApp(String pkgName, boolean isEnable) {
        appMap.put(pkgName, isEnable);
        if (isEnable) {
            doLunchApp(pkgName, 0);
        } else {
            VirtualCore.get().killApp(pkgName, 0);
        }
    }

    public synchronized boolean isAppEnable(String pkgName) {
        if (!appMap.containsKey(pkgName)) {
            return false;
        }
        return appMap.get(pkgName);
    }


}
