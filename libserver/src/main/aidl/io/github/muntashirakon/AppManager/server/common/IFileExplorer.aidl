// SPDX-License-Identifier: Apache-2.0

package io.github.muntashirakon.AppManager.server.common;

import android.os.ParcelFileDescriptor;

interface IFileExplorer {
    /**
     * Open a file with the specified mode.
     *
     * @param path The absolute path to the file.
     * @param mode The access mode (e.g., "r", "w", "rw", etc., mapped to ParcelFileDescriptor modes).
     * @return A ParcelFileDescriptor pointing to the file, or null if access failed.
     */
    ParcelFileDescriptor openFile(String path, int mode);
}
