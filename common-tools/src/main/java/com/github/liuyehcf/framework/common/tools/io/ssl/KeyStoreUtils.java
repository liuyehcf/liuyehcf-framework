package com.github.liuyehcf.framework.common.tools.io.ssl;

import com.github.liuyehcf.framework.common.tools.asserts.Assert;
import com.github.liuyehcf.framework.common.tools.bean.OptionalUtils;
import com.github.liuyehcf.framework.common.tools.number.NumberUtils;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import sun.security.tools.keytool.CertAndKeyGen;

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
    public static final String DEFAULT_KEY_ALIAS = "ROOT";
    public static final String DEFAULT_KEY_PASSWORD = "123456";

    private static final String CERTIFICATE_TYPE = "X509";

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
     * @param keyAlias         alias of key in keystore
     *                         default value is 'ROOT'
     * @param keyPassword      password of key in keystore
     *                         default value is '123456'
     */
    @SuppressWarnings("Duplicates")
    public static KeyStore createKeyStoreContainingSelfSignedCertWithSunLib(String keyStoreType,
                                                                            String encryptAlgorithm,
                                                                            String hashAlgorithm,
                                                                            Integer keyLength,
                                                                            String subjectName,
                                                                            Long validation,
                                                                            String keyAlias,
                                                                            String keyPassword) {
        keyStoreType = OptionalUtils.getOrDefault(keyStoreType, DEFAULT_KEY_STORE_TYPE);
        encryptAlgorithm = OptionalUtils.getOrDefault(encryptAlgorithm, DEFAULT_ENCRYPT_ALGORITHM);
        hashAlgorithm = OptionalUtils.getOrDefault(hashAlgorithm, DEFAULT_HASH_ALGORITHM);
        keyLength = OptionalUtils.getOrDefault(keyLength, DEFAULT_KEY_LENGTH);
        subjectName = OptionalUtils.getOrDefault(subjectName, DEFAULT_SUBJECT_NAME);
        validation = OptionalUtils.getOrDefault(validation, DEFAULT_VALIDATION);
        keyAlias = OptionalUtils.getOrDefault(keyAlias, DEFAULT_KEY_ALIAS);
        keyPassword = OptionalUtils.getOrDefault(keyPassword, DEFAULT_KEY_PASSWORD);

        try {
            // create empty keystore without keystore password
            KeyStore keyStore = createKeyStore(keyStoreType);

            // create private key and self signed cert
            CertAndKeyGen gen = new CertAndKeyGen(
                    encryptAlgorithm,
                    hashAlgorithm
            );
            gen.generate(keyLength);
            Key privateKey = gen.getPrivateKey();
            X509Certificate cert = gen.getSelfCertificate(
                    new sun.security.x509.X500Name(subjectName),
                    validation
            );

            // save private key and self signed cert to keystore with keyAlias and keyPassword
            keyStore.setKeyEntry(
                    keyAlias,
                    privateKey,
                    keyPassword.toCharArray(),
                    new X509Certificate[]{cert}
            );

            return keyStore;
        } catch (Exception e) {
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
     * @param keyAlias         alias of key in keystore
     *                         default value is 'ROOT'
     * @param keyPassword      password of key in keystore
     *                         default value is '123456'
     */
    @SuppressWarnings("Duplicates")
    public static KeyStore createKeyStoreContainingSelfSignedCertWithBouncyCastleLib(String keyStoreType,
                                                                                     String encryptAlgorithm,
                                                                                     String hashAlgorithm,
                                                                                     Integer keyLength,
                                                                                     String subjectName,
                                                                                     Long validation,
                                                                                     String keyAlias,
                                                                                     String keyPassword) {
        keyStoreType = OptionalUtils.getOrDefault(keyStoreType, DEFAULT_KEY_STORE_TYPE);
        encryptAlgorithm = OptionalUtils.getOrDefault(encryptAlgorithm, DEFAULT_ENCRYPT_ALGORITHM);
        hashAlgorithm = OptionalUtils.getOrDefault(hashAlgorithm, DEFAULT_HASH_ALGORITHM);
        keyLength = OptionalUtils.getOrDefault(keyLength, DEFAULT_KEY_LENGTH);
        subjectName = OptionalUtils.getOrDefault(subjectName, DEFAULT_SUBJECT_NAME);
        validation = OptionalUtils.getOrDefault(validation, DEFAULT_VALIDATION);
        keyAlias = OptionalUtils.getOrDefault(keyAlias, DEFAULT_KEY_ALIAS);
        keyPassword = OptionalUtils.getOrDefault(keyPassword, DEFAULT_KEY_PASSWORD);

        try {
            // create empty keystore without keystore password
            KeyStore keyStore = createKeyStore(keyStoreType);

            // create private key and public key
            KeyPair keyPair = createKeyPair(encryptAlgorithm, keyLength);
            PublicKey publicKey = keyPair.getPublic();
            PrivateKey privateKey = keyPair.getPrivate();

            // create self signed cert
            X509Certificate cert = x509SelfSign(privateKey, publicKey, subjectName, hashAlgorithm, validation);

            // save private key and self signed cert to keystore with keyAlias and keyPassword
            keyStore.setKeyEntry(
                    keyAlias,
                    privateKey,
                    keyPassword.toCharArray(),
                    new X509Certificate[]{cert}
            );

            return keyStore;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * create empty key store
     *
     * @param keyStoreType type of keyStore
     *                     optional value 'PKCS12'
     *                     optional value 'JKS'
     *                     default value is 'PKCS12'
     * @return KeyStore
     */
    public static KeyStore createKeyStore(String keyStoreType) {
        keyStoreType = OptionalUtils.getOrDefault(keyStoreType, DEFAULT_KEY_STORE_TYPE);
        try {
            // create empty keystore without keystore password
            KeyStore keyStore = KeyStore.getInstance(keyStoreType);
            keyStore.load(null, null);

            return keyStore;
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    /**
     * create key pair
     *
     * @param encryptAlgorithm type of encrypt algorithm
     *                         optional value 'RSA'
     *                         default value is 'RSA'
     * @param keyLength        length of key, recommend at least 2048 for security
     *                         default value is '2048'
     * @return KeyPair
     */
    public static KeyPair createKeyPair(String encryptAlgorithm, Integer keyLength) {
        encryptAlgorithm = OptionalUtils.getOrDefault(encryptAlgorithm, DEFAULT_ENCRYPT_ALGORITHM);
        keyLength = OptionalUtils.getOrDefault(keyLength, DEFAULT_KEY_LENGTH);

        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(encryptAlgorithm);
            keyPairGenerator.initialize(keyLength);
            return keyPairGenerator.generateKeyPair();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * sign with root private key and root cert
     *
     * @param rootPrivateKey private key of root
     * @param rootCert       cert of root
     * @param publicKey      public key to be signed
     * @param subjectName    subject name to be signed
     * @param hashAlgorithm  hash algorithm
     * @param validation     validation in seconds
     * @return X509Certificate
     */
    public static X509Certificate x509Sign(PrivateKey rootPrivateKey,
                                           X509Certificate rootCert,
                                           PublicKey publicKey,
                                           String subjectName,
                                           String hashAlgorithm,
                                           Long validation) {
        Assert.assertNotNull(rootCert, "rootCert");

        subjectName = OptionalUtils.getOrDefault(subjectName, DEFAULT_SUBJECT_NAME);
        hashAlgorithm = OptionalUtils.getOrDefault(hashAlgorithm, DEFAULT_HASH_ALGORITHM);
        validation = OptionalUtils.getOrDefault(validation, DEFAULT_VALIDATION);

        return x509Sign(rootPrivateKey, rootCert.getSubjectDN().getName(), publicKey, subjectName, hashAlgorithm, validation);
    }

    /**
     * @param privateKey    self private key
     * @param publicKey     self public key
     * @param subjectName   subject name
     * @param hashAlgorithm hash algorithm
     * @param validation    validation in seconds
     * @return X509Certificate
     */
    public static X509Certificate x509SelfSign(PrivateKey privateKey,
                                               PublicKey publicKey,
                                               String subjectName,
                                               String hashAlgorithm,
                                               Long validation) {

        subjectName = OptionalUtils.getOrDefault(subjectName, DEFAULT_SUBJECT_NAME);
        hashAlgorithm = OptionalUtils.getOrDefault(hashAlgorithm, DEFAULT_HASH_ALGORITHM);
        validation = OptionalUtils.getOrDefault(validation, DEFAULT_VALIDATION);

        return x509Sign(privateKey, subjectName, publicKey, subjectName, hashAlgorithm, validation);
    }

    /**
     * sign with root private key and root cert
     *
     * @param rootPrivateKey  private key of root
     * @param rootSubjectName subject name of root
     * @param publicKey       public key to be signed
     * @param subjectName     subject name to be signed
     * @param hashAlgorithm   hash algorithm
     * @param validation      validation in seconds
     * @return X509Certificate
     */
    private static X509Certificate x509Sign(PrivateKey rootPrivateKey,
                                            String rootSubjectName,
                                            PublicKey publicKey,
                                            String subjectName,
                                            String hashAlgorithm,
                                            Long validation) {
        Assert.assertNotNull(rootPrivateKey, "rootPrivateKey");
        Assert.assertNotNull(rootSubjectName, "rootSubjectName");
        Assert.assertNotNull(publicKey, "publicKey");
        Assert.assertNotNull(subjectName, "subjectName");
        Assert.assertNotNull(hashAlgorithm, "hashAlgorithm");
        Assert.assertNotNull(validation, "validation");

        try {
            X500Name issuer = new X500Name(rootSubjectName);
            X500Name subject = new X500Name(subjectName);

            SubjectPublicKeyInfo subjectPublicKeyInfo = SubjectPublicKeyInfo.getInstance(
                    new ASN1InputStream(publicKey.getEncoded()
                    ).readObject());
            X509v3CertificateBuilder certificateBuilder = new X509v3CertificateBuilder(
                    issuer,
                    BigInteger.valueOf(System.currentTimeMillis()),
                    new Date(),
                    new Date(System.currentTimeMillis() + NumberUtils.THOUSAND * validation),
                    subject,
                    subjectPublicKeyInfo);
            ContentSigner sigGen = new JcaContentSignerBuilder(hashAlgorithm).build(rootPrivateKey);
            X509CertificateHolder holder = certificateBuilder.build(sigGen);

            return (X509Certificate) CertificateFactory
                    .getInstance(CERTIFICATE_TYPE)
                    .generateCertificate(new ByteArrayInputStream(holder.getEncoded()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
