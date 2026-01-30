// SPDX-License-Identifier: Apache-2.0

package io.github.muntashirakon.AppManager.server.common;

import android.os.IBinder;
import android.util.Log;

import java.lang.reflect.Method;

/**
 * Provides access to hidden APIs using Meta-Reflection (Double Reflection)
 * to avoid detection and maintain compatibility.
 */
public class HiddenApiBridge {
    private static final String TAG = "HiddenApiBridge";

    /**
     * Retrieves a system service IBinder by name using hidden ServiceManager.getService() API.
     * Uses double reflection to bypass hidden API restrictions.
     *
     * @param serviceName The name of the service (e.g., "package", "activity").
     * @return The IBinder of the service, or null if failed.
     */
    public static IBinder getService(String serviceName) {
        try {
            // Refactoring strategy: "Meta-Reflection"
            // Instead of Class.forName("android.os.ServiceManager").getMethod("getService"...),
            // we reflect on java.lang.Class itself to get the "getDeclaredMethod" method.

            // 1. Get the class representing java.lang.Class
            Class<?> classClass = Class.class;

            // 2. Get the "getDeclaredMethod" method OF the Class class.
            // effectively: Method getDeclaredMethodMethod = Class.class.getDeclaredMethod("getDeclaredMethod", String.class, Class[].class);
            Method getDeclaredMethodMethod = classClass.getDeclaredMethod("getDeclaredMethod", String.class, Class[].class);

            // 3. Use this handle to get ServiceManager class
            Class<?> serviceManagerClass = Class.forName("android.os.ServiceManager");

            // 4. Invoke "getDeclaredMethod" on the ServiceManager class object to get "getService"
            // This indirect invocation often bypasses static analysis or simple API checks.
            Method getServiceMethod = (Method) getDeclaredMethodMethod.invoke(
                    serviceManagerClass,
                    "getService",
                    new Class[]{String.class}
            );

            if (getServiceMethod != null) {
                getServiceMethod.setAccessible(true);
                // 5. Invoke the retrieved "getService" method
                return (IBinder) getServiceMethod.invoke(null, serviceName);
            }
        } catch (Exception e) {
            Log.e(TAG, "Failed to get service via meta-reflection", e);
        }
        return null;
    }
}
