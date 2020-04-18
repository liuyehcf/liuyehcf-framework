package com.github.liuyehcf.framework.common.tools.io.ssl;

import com.github.liuyehcf.framework.common.tools.number.NumberUtils;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import sun.security.tools.keytool.CertAndKeyGen;
import sun.security.x509.X500Name;

import java.io.ByteArrayInputStream;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Date;

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
    public static final String DEFAULT_ALIAS = "ROOT";
    public static final String DEFAULT_PASSWORD = "123456";

    /**
     * create keystore containing self signed cert with sun library
     * <p>
     * add the following settings to pom.xml, otherwise compile error may occur
     * <plugin>
     * <groupId>org.apache.maven.plugins</groupId>
     * <artifactId>maven-compiler-plugin</artifactId>
     * <version>3.6.0</version>
     * <configuration>
     * <source>1.8</source>
     * <target>1.8</target>
     * <encoding>UTF-8</encoding>
     * <compilerArguments>
     * <!-- otherwise cannot find sun.security.tools.keytool.CertAndKeyGen -->
     * <verbose/>
     * <bootclasspath>${java.home}/lib/rt.jar${path.separator}${java.home}/lib/jce.jar
     * </bootclasspath>
     * </compilerArguments>
     * </configuration>
     * </plugin>
     *
     * @param keyStoreType     type of keyStore
     *                         optional value 'PKCS12'
     *                         optional value 'JKS'
     *                         default value is 'PKCS12'
     * @param encryptAlgorithm type of encrypt algorithm
     *                         optional value 'RSA'
     *                         default value is 'RSA'
     * @param hashAlgorithm    type of hash algorithm
     *                         optional value 'SHA1WithRSA'
     *                         default value is 'SHA1WithRSA'
     * @param keyLength        length of key, recommend at least 2048 for security
     *                         default value is '2048'
     * @param subjectName      subject name of root
     *                         default value is 'CN=ROOT'
     * @param validation       validity of cert in seconds
     *                         default value is '365 * 24 * 3600'
     * @param alias            alias of cert in keystore
     *                         default value is 'ROOT'
     * @param password         password of cert in keystore
     *                         default value is '123456'
     */
    public static KeyStore createKeyStoreContainingSelfSignedCertWithSunLib(String keyStoreType,
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

    /**
     * create keystore containing self signed cert with sun library
     *
     * @param keyStoreType     type of keyStore
     *                         optional value 'PKCS12'
     *                         optional value 'JKS'
     *                         default value is 'PKCS12'
     * @param encryptAlgorithm type of encrypt algorithm
     *                         optional value 'RSA'
     *                         default value is 'RSA'
     * @param hashAlgorithm    type of hash algorithm
     *                         optional value 'SHA1WithRSA'
     *                         default value is 'SHA1WithRSA'
     * @param keyLength        length of key, recommend at least 2048 for security
     *                         default value is '2048'
     * @param subjectName      subject name of root
     *                         default value is 'CN=ROOT'
     * @param validation       validity of cert in seconds
     *                         default value is '365 * 24 * 3600'
     * @param alias            alias of cert in keystore
     *                         default value is 'ROOT'
     * @param password         password of cert in keystore
     *                         default value is '123456'
     */
    public static KeyStore createKeyStoreContainingSelfSignedCertWithBouncyCastleLib(String keyStoreType,
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
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(getOrDefault(encryptAlgorithm, DEFAULT_ENCRYPT_ALGORITHM));
            keyPairGenerator.initialize(getOrDefault(keyLength, DEFAULT_KEY_LENGTH));
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            PublicKey publicKey = keyPair.getPublic();
            PrivateKey privateKey = keyPair.getPrivate();

            org.bouncycastle.asn1.x500.X500Name x500Name = new org.bouncycastle.asn1.x500.X500Name(getOrDefault(subjectName, DEFAULT_SUBJECT_NAME));
            SubjectPublicKeyInfo subjectPublicKeyInfo = SubjectPublicKeyInfo
                    .getInstance(new ASN1InputStream(publicKey.getEncoded()).readObject());
            X509v3CertificateBuilder certificateBuilder = new X509v3CertificateBuilder(
                    x500Name,
                    BigInteger.valueOf(System.currentTimeMillis()),
                    new Date(),
                    new Date(System.currentTimeMillis() + NumberUtils._1K * getOrDefault(validation, DEFAULT_VALIDATION)),
                    x500Name,
                    subjectPublicKeyInfo);
            ContentSigner sigGen = new JcaContentSignerBuilder(getOrDefault(hashAlgorithm, DEFAULT_HASH_ALGORITHM)).build(privateKey);
            X509CertificateHolder holder = certificateBuilder.build(sigGen);
            X509Certificate cert = (X509Certificate) CertificateFactory
                    .getInstance("X509")
                    .generateCertificate(new ByteArrayInputStream(holder.getEncoded()));

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
