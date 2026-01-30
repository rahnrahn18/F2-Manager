// SPDX-License-Identifier: Apache-2.0

package io.github.muntashirakon.AppManager.server;

import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;

import io.github.muntashirakon.AppManager.server.common.IFileExplorer;

public class FileExplorerService extends IFileExplorer.Stub {
    private static final String TAG = "FileExplorerService";

    @Override
    public ParcelFileDescriptor openFile(String path, int mode) throws RemoteException {
        try {
            File file = new File(path);
            return ParcelFileDescriptor.open(file, mode);
        } catch (FileNotFoundException e) {
            Log.e(TAG, "Failed to open file: " + path, e);
            // In a production environment, we might want to throw a service-specific exception
            // or return a specific error code. For this "no standard security validation"
            // implementation, returning null or letting the exception propagate (if the client expects it)
            // is acceptable, but returning null is safer for the binder transaction if we catch it here.
            // However, AIDL methods declared with 'throws RemoteException' will wrap runtime exceptions.
            // Let's rethrow as a RuntimeException to bubble up or return null?
            // The prompt asked for "no validation", so we just try to open it.
            return null;
        }
    }
}
