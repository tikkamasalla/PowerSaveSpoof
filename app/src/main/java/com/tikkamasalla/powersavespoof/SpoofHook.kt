package com.tikkamasalla.powersavespoof

import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage

class SpoofHook : IXposedHookLoadPackage {

    companion object {
        private const val TAG = "PowerSaveSpoof"
        private val TARGETS = setOf(
            "com.google.android.apps.maps",
            "org.ungoogled.android.apps.maps",
            "org.ungoogled.android.apps.maps.microg",
            "com.google.earth",
        )
    }

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        if (lpparam.packageName !in TARGETS) return
        try {
            XposedHelpers.findAndHookMethod(
                "android.os.PowerManager",
                lpparam.classLoader,
                "isPowerSaveMode",
                object : XC_MethodHook() {
                    override fun afterHookedMethod(param: MethodHookParam) {
                        param.result = false
                    }
                },
            )
            XposedBridge.log("$TAG: spoof installed in ${lpparam.packageName}")
            val refreshHook =
                object : XC_MethodHook() {
                    override fun afterHookedMethod(param: MethodHookParam) {
                        try {
                            val activity = param.thisObject as android.app.Activity
                            val win = activity.window ?: return
                            val attrs = win.attributes ?: return
                            if (attrs.preferredRefreshRate != 120f) {
                                attrs.preferredRefreshRate = 120f
                                win.attributes = attrs
                            }
                        } catch (t: Throwable) {
                            XposedBridge.log("$TAG refresh hook failed: ${t.message}")
                        }
                    }
                }
            XposedHelpers.findAndHookMethod(
                "android.app.Activity",
                lpparam.classLoader,
                "onResume",
                refreshHook,
            )
            // Re-apply when the window regains focus: map/globe views reset
            // window attributes on surface changes, which is where the
            // micro-stutters come from mid-session.
            XposedHelpers.findAndHookMethod(
                "android.app.Activity",
                lpparam.classLoader,
                "onWindowFocusChanged",
                Boolean::class.javaPrimitiveType,
                refreshHook,
            )
            XposedBridge.log("$TAG: 120Hz request installed in ${lpparam.packageName}")
        } catch (t: Throwable) {
            XposedBridge.log("$TAG install failed: ${t.message}")
        }
    }
}
