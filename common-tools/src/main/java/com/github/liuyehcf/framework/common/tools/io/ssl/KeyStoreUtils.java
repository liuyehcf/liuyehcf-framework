package com.github.liuyehcf.framework.common.tools.io.ssl;

import sun.security.tools.keytool.CertAndKeyGen;
import sun.security.x509.X500Name;

import java.security.Key;
import java.security.KeyStore;
import java.security.cert.X509Certificate;

/**
 * @author hechenfeng
 * @date 2020/4/18
 */
public class KeyStoreUtils {

    public static final String DEFAULT_KEY_STORE_TYPE = "PKCS12";
    public static final String DEFAULT_ENCRYPT_ALGORITHM = "RSA";
    public static final String DEFAULT_HASH_ALGORITHM = "SHA1WithRSA";
    public static final int DEFAULT_KEY_LENGTH = 2048;
    public static final String DEFAULT_SUBJECT_NAME = "CN=ROOT";
    public static final long DEFAULT_VALIDATION = 365 * 24 * 3600;
    public static final String DEFAULT_ALIAS = "ALIAS";
    public static final String DEFAULT_PASSWORD = "PASSWORD";

    /**
     * create memory keystore
     *
     * @param keyStoreType     type of keyStore
     *                         optional value 'PKCS12' (default if null)
     *                         optional value 'JKS'
     * @param encryptAlgorithm type of encrypt algorithm
     *                         optional value 'RSA' (default if null)
     * @param hashAlgorithm    type of hash algorithm
     *                         optional value 'SHA1WithRSA'
     * @param keyLength        length of key, recommend at least 2048 for security
     * @param subjectName      subject name of root
     * @param validation       validity of cert in seconds
     * @param alias            alias of cert in keystore
     * @param password         password of cert in keystore
     */
    public static KeyStore createMemoryKeyStore(String keyStoreType,
                                                String encryptAlgorithm,
                                                String hashAlgorithm,
                                                Integer keyLength,
                                                String subjectName,
                                                Long validation,
                                                String alias,
                                                String password) {
        try {
            // init the key store
            KeyStore keyStore = KeyStore.getInstance(getOrDefault(keyStoreType, DEFAULT_KEY_STORE_TYPE));
            keyStore.load(null, null);

            // create private entry and cert chain
            CertAndKeyGen gen = new CertAndKeyGen(
                    getOrDefault(encryptAlgorithm, DEFAULT_ENCRYPT_ALGORITHM),
                    getOrDefault(hashAlgorithm, DEFAULT_HASH_ALGORITHM)
            );
            gen.generate(getOrDefault(keyLength, DEFAULT_KEY_LENGTH));
            Key privateKey = gen.getPrivateKey();
            X509Certificate cert = gen.getSelfCertificate(
                    new X500Name(
                            getOrDefault(subjectName, DEFAULT_SUBJECT_NAME)),
                    getOrDefault(validation, DEFAULT_VALIDATION)
            );
            X509Certificate[] certChain = new X509Certificate[]{cert};
            keyStore.setKeyEntry(
                    getOrDefault(alias, DEFAULT_ALIAS),
                    privateKey,
                    getOrDefault(password, DEFAULT_PASSWORD).toCharArray(),
                    certChain
            );

            return keyStore;
        } catch (
                Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static <T> T getOrDefault(T obj, T defaultObj) {
        if (obj == null) {
            return defaultObj;
        }
        return obj;
    }
}
