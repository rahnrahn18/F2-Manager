package com.f2manager.core.apk;

import com.reandroid.apk.ApkModule;
import com.reandroid.arsc.chunk.xml.AndroidManifestBlock;
import com.reandroid.arsc.chunk.xml.ResXmlElement;
import com.reandroid.arsc.chunk.xml.ResXmlAttribute;
import com.reandroid.arsc.value.ValueType;

import java.io.File;
import java.io.IOException;

/**
 * Implements "Common Edit" logic using ARSCLib.
 * Allows modifying Manifest attributes directly in binary XML.
 */
public class CommonEditor {

    private final ApkModule apkModule;

    public CommonEditor(File apkFile) throws IOException {
        this.apkModule = ApkModule.loadApkFile(apkFile);
    }

    public void setPackageName(String newPackageName) {
        if (apkModule.hasAndroidManifest()) {
            apkModule.getAndroidManifest().setPackageName(newPackageName);
        }
    }

    public void setVersionCode(int newVersionCode) {
        if (apkModule.hasAndroidManifest()) {
            apkModule.getAndroidManifest().setVersionCode(newVersionCode);
        }
    }

    public void setVersionName(String newVersionName) {
        if (apkModule.hasAndroidManifest()) {
            apkModule.getAndroidManifest().setVersionName(newVersionName);
        }
    }

    public void setInstallLocation(int installLocation) {
        if (apkModule.hasAndroidManifest()) {
             AndroidManifestBlock manifest = apkModule.getAndroidManifest();
             ResXmlElement manifestElement = manifest.getManifestElement();
             // 0x010102b7 is android:installLocation
             ResXmlAttribute attr = manifestElement.getOrCreateAndroidAttribute("installLocation", 0x010102b7);
             attr.setValueType(ValueType.DEC);
             attr.setData(installLocation);
        }
    }

    public void save(File outputFile) throws IOException {
        apkModule.writeApk(outputFile);
    }
}
