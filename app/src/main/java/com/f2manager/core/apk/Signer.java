package com.f2manager.core.apk;

import com.android.apksig.ApkSigner;
import com.android.apksig.ApkVerifier;

import java.io.File;
import java.io.IOException;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Implements "Auto-Signing" logic using apksig.
 */
public class Signer {

    /**
     * Signs the APK with the provided key and certificate.
     *
     * @param apkFile     The APK file to sign.
     * @param outputFile  The signed APK output.
     * @param privateKey  The private key.
     * @param certificate The certificate.
     * @throws Exception If signing fails.
     */
    public void sign(File apkFile, File outputFile, PrivateKey privateKey, X509Certificate certificate) throws Exception {
        ApkSigner.SignerConfig signerConfig = new ApkSigner.SignerConfig.Builder(
                "CERT", privateKey, Collections.singletonList(certificate))
                .build();

        List<ApkSigner.SignerConfig> signerConfigs = new ArrayList<>();
        signerConfigs.add(signerConfig);

        ApkSigner.Builder builder = new ApkSigner.Builder(signerConfigs);
        builder.setInputApk(apkFile);
        builder.setOutputApk(outputFile);

        // V1 + V2 + V3 signing
        builder.setV1SigningEnabled(true);
        builder.setV2SigningEnabled(true);
        builder.setV3SigningEnabled(true);

        ApkSigner signer = builder.build();
        signer.sign();
    }
}
