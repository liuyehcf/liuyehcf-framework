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
import java.io.FileOutputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.util.concurrent.TimeUnit;

/**
 * @author hechenfeng
 * @date 2020/4/18
 */
public class TestKeyStoreUtils {

    @Test
    public void testSaveKeyStore() throws Exception {
        KeyStore keyStore1 = KeyStoreUtils.createKeyStoreContainingSelfSignedCertWithSunLib(null,
                null,
                null,
                null,
                null,
                null,
                null,
                null);
        keyStore1.store(new FileOutputStream("/tmp/keystore1"), "123456".toCharArray());

        KeyStore keyStore2 = KeyStoreUtils.createKeyStoreContainingSelfSignedCertWithBouncyCastleLib(null,
                null,
                null,
                null,
                null,
                null,
                null,
                null);
        keyStore2.store(new FileOutputStream("/tmp/keystore2"), "123456".toCharArray());
    }

    @Test
    public void testSunLib() throws Exception {
        ChannelFuture channelFuture = HttpServer.start(true);

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

    @Test
    public void testBouncyCastleLib() throws Exception {
        ChannelFuture channelFuture = HttpServer.start(false);

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

    private static final class HttpServer {
        private static final String HOST = "localhost";
        private static final int PORT = 8080;

        private static final KeyStore KEY_STORE_CONTAINING_SELF_SIGNED_CERT_WITH_SUN_LIB;
        private static final KeyStore KEY_STORE_CONTAINING_SELF_SIGNED_CERT_WITH_BOUNCY_CASTLE_LIB;

        static {
            try {
                KEY_STORE_CONTAINING_SELF_SIGNED_CERT_WITH_SUN_LIB = KeyStoreUtils.createKeyStoreContainingSelfSignedCertWithSunLib(null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null);

                KEY_STORE_CONTAINING_SELF_SIGNED_CERT_WITH_BOUNCY_CASTLE_LIB = KeyStoreUtils.createKeyStoreContainingSelfSignedCertWithBouncyCastleLib(null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null);
            } catch (Exception e) {
                throw new Error(e);
            }
        }

        public static ChannelFuture start(boolean usingSunLibToCreateCert) throws Exception {
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
                            if (usingSunLibToCreateCert) {
                                pipeline.addLast(createSslHandlerUsingNetty(pipeline, KEY_STORE_CONTAINING_SELF_SIGNED_CERT_WITH_SUN_LIB));
                            } else {
                                pipeline.addLast(createSslHandlerUsingNetty(pipeline, KEY_STORE_CONTAINING_SELF_SIGNED_CERT_WITH_BOUNCY_CASTLE_LIB));
                            }
                            pipeline.addLast(new HttpServerCodec());
                            pipeline.addLast(new HttpObjectAggregator(NumberUtils._1K));
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
