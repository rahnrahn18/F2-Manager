package com.f2manager.core.apk;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * Implements "Simple Edit" logic.
 * Replaces files in APK (ZIP) without full rebuild.
 */
public class SimpleEditor {

    /**
     * Replaces a file inside the APK.
     *
     * @param apkFile    The original APK file.
     * @param pathInApk  The path of the file to replace inside the APK (e.g., "res/drawable/icon.png").
     * @param newFile    The new file to insert.
     * @param outputFile The destination for the modified APK.
     * @throws IOException If I/O error occurs.
     */
    public void replaceFile(File apkFile, String pathInApk, File newFile, File outputFile) throws IOException {
        try (ZipInputStream zis = new ZipInputStream(new BufferedInputStream(new FileInputStream(apkFile)));
             ZipOutputStream zos = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(outputFile)))) {

            ZipEntry entry;
            boolean replaced = false;
            byte[] buffer = new byte[8192];

            while ((entry = zis.getNextEntry()) != null) {
                String entryName = entry.getName();

                if (entryName.equals(pathInApk)) {
                    // Replace this entry
                    ZipEntry newEntry = new ZipEntry(entryName);
                    // Use STORED if possible for no compression (faster/safe), or DEFLATED.
                    // For simplicity, we let ZipOutputStream handle it, usually DEFLATED.
                    // Copy time/extra if needed? newEntry.setTime(System.currentTimeMillis());
                    zos.putNextEntry(newEntry);

                    try (InputStream fis = new BufferedInputStream(new FileInputStream(newFile))) {
                        int len;
                        while ((len = fis.read(buffer)) > 0) {
                            zos.write(buffer, 0, len);
                        }
                    }
                    zos.closeEntry();
                    replaced = true;
                } else {
                    // Copy existing entry
                    // We must create a new ZipEntry to avoid setting compressed size mismatch if we use putNextEntry directly with old entry
                    ZipEntry newEntry = new ZipEntry(entryName);
                    zos.putNextEntry(newEntry);

                    int len;
                    while ((len = zis.read(buffer)) > 0) {
                        zos.write(buffer, 0, len);
                    }
                    zos.closeEntry();
                }
            }

            if (!replaced) {
                // If file didn't exist, maybe add it?
                // For "Replace", strictly speaking we might want to throw error or just add.
                // Let's add it if it wasn't found.
                ZipEntry newEntry = new ZipEntry(pathInApk);
                zos.putNextEntry(newEntry);
                try (InputStream fis = new BufferedInputStream(new FileInputStream(newFile))) {
                    int len;
                    while ((len = fis.read(buffer)) > 0) {
                        zos.write(buffer, 0, len);
                    }
                }
                zos.closeEntry();
            }
        }
    }
}
