// SPDX-License-Identifier: Apache-2.0

package io.github.muntashirakon.AppManager.ipc;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import io.github.muntashirakon.AppManager.server.common.IFileExplorer;
import rikka.shizuku.Shizuku;

public class FileExplorerClient {
    private static final String TAG = "FileExplorerClient";

    // Shizuku service name as defined in the manifest and expected by the UserServiceArgs
    private static final String SERVICE_NAME = "io.github.muntashirakon.AppManager.server.FileExplorerService";
    // Version of the service
    private static final int SERVICE_VERSION = 1;

    private static IFileExplorer sService;
    private static final CountDownLatch sLatch = new CountDownLatch(1);

    private static final ServiceConnection sConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.i(TAG, "FileExplorerService connected");
            sService = IFileExplorer.Stub.asInterface(service);
            sLatch.countDown();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i(TAG, "FileExplorerService disconnected");
            sService = null;
        }
    };

    /**
     * Binds to the Shizuku service if not already bound.
     */
    private static void ensureServiceBound() {
        if (sService != null && sService.asBinder().pingBinder()) {
            return;
        }

        if (Shizuku.checkSelfPermission() != android.content.pm.PackageManager.PERMISSION_GRANTED) {
             Log.e(TAG, "Shizuku permission not granted");
             return;
        }

        Shizuku.bindUserService(
                new Shizuku.UserServiceArgs(new ComponentName(
                        "io.github.muntashirakon.AppManager",
                        SERVICE_NAME))
                        .version(SERVICE_VERSION)
                        .processNameSuffix("file_explorer")
                        .debuggable(true)
                        .daemon(false),
                sConnection
        );

        try {
            // Wait for connection
            sLatch.await(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Log.e(TAG, "Interrupted while waiting for service connection", e);
        }
    }

    /**
     * Open a file using the privileged service and return an InputStream.
     * This is designed for UI modules like SoraEditor.
     *
     * @param path Absolute path to the file.
     * @return InputStream of the file, or null if failed.
     */
    public static InputStream openFileStream(String path) {
        ensureServiceBound();
        if (sService == null) {
            Log.e(TAG, "Service is not connected");
            return null;
        }

        try {
            // mode 268435456 is ParcelFileDescriptor.MODE_READ_ONLY (0x10000000)
            ParcelFileDescriptor pfd = sService.openFile(path, ParcelFileDescriptor.MODE_READ_ONLY);
            if (pfd != null) {
                return new ParcelFileDescriptor.AutoCloseInputStream(pfd);
            }
        } catch (RemoteException e) {
            Log.e(TAG, "Remote exception calling openFile", e);
        }
        return null;
    }

    /**
     * Open a file using the privileged service and return a ParcelFileDescriptor.
     *
     * @param path Absolute path to the file.
     * @param mode Access mode (e.g., ParcelFileDescriptor.MODE_READ_ONLY)
     * @return ParcelFileDescriptor of the file, or null if failed.
     */
    public static ParcelFileDescriptor openFileDescriptor(String path, int mode) {
        ensureServiceBound();
        if (sService == null) {
            Log.e(TAG, "Service is not connected");
            return null;
        }

        try {
            return sService.openFile(path, mode);
        } catch (RemoteException e) {
            Log.e(TAG, "Remote exception calling openFile", e);
        }
        return null;
    }
}
