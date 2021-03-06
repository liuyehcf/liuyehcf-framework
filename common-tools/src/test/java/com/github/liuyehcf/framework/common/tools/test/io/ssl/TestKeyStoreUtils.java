package com.github.liuyehcf.framework.common.tools.test.io.ssl;

import com.github.liuyehcf.framework.common.tools.io.ssl.KeyStoreUtils;
import com.github.liuyehcf.framework.common.tools.number.NumberUtils;
import com.github.liuyehcf.framework.common.tools.time.TimeUtils;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;
import org.junit.Assert;
import org.junit.Test;

import javax.net.ssl.KeyManagerFactory;
import java.io.InputStream;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author hechenfeng
 * @date 2020/4/18
 */
public class TestKeyStoreUtils {

    @Test
    public void testSaveKeyStore() throws Exception {
        KeyStore keyStore;
        X509Certificate certificate;
        Date notBefore, notAfter;

        keyStore = KeyStoreUtils.createKeyStoreContainingSelfSignedCertWithSunLib(null,
                null,
                null,
                null,
                null,
                null,
                null,
                null);
        certificate = (X509Certificate) keyStore.getCertificate(KeyStoreUtils.DEFAULT_KEY_ALIAS);
        notBefore = certificate.getNotBefore();
        notAfter = certificate.getNotAfter();
        Assert.assertTrue(Math.abs(System.currentTimeMillis() - notBefore.getTime()) < 2000);
        Assert.assertTrue(Math.abs(new Date(System.currentTimeMillis() + NumberUtils.THOUSAND * 86400 * 365L).getTime() - notAfter.getTime()) < 2000);

        keyStore = KeyStoreUtils.createKeyStoreContainingSelfSignedCertWithBouncyCastleLib(null,
                null,
                null,
                null,
                null,
                null,
                null,
                null);
        certificate = (X509Certificate) keyStore.getCertificate(KeyStoreUtils.DEFAULT_KEY_ALIAS);
        notBefore = certificate.getNotBefore();
        notAfter = certificate.getNotAfter();
        Assert.assertTrue(Math.abs(System.currentTimeMillis() - notBefore.getTime()) < 2000);
        Assert.assertTrue(Math.abs(new Date(System.currentTimeMillis() + NumberUtils.THOUSAND * 86400 * 365L).getTime() - notAfter.getTime()) < 2000);
    }

    @Test
    public void testSign() {
        KeyPair rootKeyPair = KeyStoreUtils.createKeyPair(null, null);
        PrivateKey rootPrivateKey = rootKeyPair.getPrivate();
        PublicKey rootPublicKey = rootKeyPair.getPublic();
        X509Certificate rootCert = KeyStoreUtils.x509SelfSign(rootPrivateKey,
                rootPublicKey,
                null,
                null,
                null);

        KeyPair keyPair = KeyStoreUtils.createKeyPair(null, null);
        PublicKey publicKey = keyPair.getPublic();
        X509Certificate cert = KeyStoreUtils.x509Sign(rootPrivateKey, rootCert, publicKey, "CN=liuyehcf", null, null);

        System.out.println(cert);
    }

    @Test
    public void testSunLibSelfSignedCertPKCS12() throws Exception {
        testHttpServer(KeyStoreUtils.createKeyStoreContainingSelfSignedCertWithSunLib(null,
                null,
                null,
                null,
                null,
                null,
                null,
                null));
    }

    @Test
    public void testSunLibSelfSignedCertJKS() throws Exception {
        testHttpServer(KeyStoreUtils.createKeyStoreContainingSelfSignedCertWithSunLib("JKS",
                null,
                null,
                null,
                null,
                null,
                null,
                null));
    }

    @Test
    public void testBouncyCastleLibSelfSignedCertPKCS12() throws Exception {
        testHttpServer(KeyStoreUtils.createKeyStoreContainingSelfSignedCertWithBouncyCastleLib(null,
                null,
                null,
                null,
                null,
                null,
                null,
                null));
    }

    @Test
    public void testBouncyCastleLibSelfSignedCertJKS() throws Exception {
        testHttpServer(KeyStoreUtils.createKeyStoreContainingSelfSignedCertWithBouncyCastleLib("JKS",
                null,
                null,
                null,
                null,
                null,
                null,
                null));
    }

    @Test
    public void testBouncyCastleLibSiteCert() throws Exception {
        testHttpServer(getSiteCertKeyStore());
    }

    private void testHttpServer(KeyStore keyStore) throws Exception {
        ChannelFuture channelFuture = HttpServer.start(keyStore);

        TimeUtils.sleep(1);

        Process exec = Runtime.getRuntime().exec("curl https://localhost:8080 -k -s");
        InputStream inputStream = exec.getInputStream();
        exec.waitFor();

        byte[] bytes = new byte[inputStream.available()];
        Assert.assertEquals(bytes.length, inputStream.read(bytes));

        Assert.assertEquals("hello world", new String(bytes));

        channelFuture.channel().close();
        channelFuture.sync();
    }

    private KeyStore getSiteCertKeyStore() {
        try {
            // create empty keystore without keystore password
            KeyStore keyStore = KeyStore.getInstance(KeyStoreUtils.DEFAULT_KEY_STORE_TYPE);
            keyStore.load(null, null);

            KeyPair rootKeyPair = KeyStoreUtils.createKeyPair(null, null);
            PrivateKey rootPrivateKey = rootKeyPair.getPrivate();
            PublicKey rootPublicKey = rootKeyPair.getPublic();
            X509Certificate rootCert = KeyStoreUtils.x509SelfSign(rootPrivateKey,
                    rootPublicKey,
                    null,
                    null,
                    null);

            KeyPair keyPair = KeyStoreUtils.createKeyPair(null, null);
            PublicKey publicKey = keyPair.getPublic();
            PrivateKey privateKey = keyPair.getPrivate();
            X509Certificate cert = KeyStoreUtils.x509Sign(rootPrivateKey, rootCert, publicKey, "CN=liuyehcf", null, null);

            keyStore.setKeyEntry(KeyStoreUtils.DEFAULT_KEY_ALIAS,
                    privateKey,
                    KeyStoreUtils.DEFAULT_KEY_PASSWORD.toCharArray(),
                    new X509Certificate[]{cert});

            return keyStore;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static final class HttpServer {
        private static final String HOST = "localhost";
        private static final int PORT = 8080;

        public static ChannelFuture start(KeyStore keyStore) throws Exception {
            final EventLoopGroup boss = new NioEventLoopGroup();
            final EventLoopGroup worker = new NioEventLoopGroup();

            final ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(boss, worker)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            pipeline.addLast(new IdleStateHandler(0, 0, 60, TimeUnit.SECONDS));
                            pipeline.addLast(createSslHandlerUsingNetty(pipeline, keyStore));
                            pipeline.addLast(new HttpServerCodec());
                            pipeline.addLast(new HttpObjectAggregator((int) NumberUtils.THOUSAND));
                            pipeline.addLast(new ChunkedWriteHandler());
                            pipeline.addLast(new ServerHandler());
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    .childOption(ChannelOption.SO_REUSEADDR, true);

            final ChannelFuture future = bootstrap.bind(PORT).sync();

            return future.channel().closeFuture();
        }

        private static ChannelHandler createSslHandlerUsingNetty(ChannelPipeline pipeline, KeyStore keyStore) throws Exception {
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(keyStore, KeyStoreUtils.DEFAULT_KEY_PASSWORD.toCharArray());

            return SslContextBuilder.forServer(keyManagerFactory).build()
                    .newHandler(pipeline.channel().alloc(), HOST, PORT);
        }
    }

    private static final class ServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) {
            FullHttpResponse response = new DefaultFullHttpResponse(
                    HttpVersion.HTTP_1_1,
                    HttpResponseStatus.OK,
                    Unpooled.wrappedBuffer(Unpooled.wrappedBuffer("hello world".getBytes())));
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain;charset=UTF-8");
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());

            ctx.channel().writeAndFlush(response);
        }
    }
}
