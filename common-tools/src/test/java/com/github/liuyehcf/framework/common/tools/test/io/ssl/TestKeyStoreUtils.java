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

import java.io.InputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

/**
 * @author hechenfeng
 * @date 2020/4/18
 */
public class TestKeyStoreUtils {

    @Test
    public void test() throws Exception {
        ChannelFuture channelFuture = HttpServer.start();

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

        private static final KeyStore KEY_STORE;

        static {
            try {
                KEY_STORE = KeyStoreUtils.createMemoryKeyStore(null,
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

        public static ChannelFuture start() throws Exception {
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
                            pipeline.addLast(createSslHandlerUsingNetty(pipeline));
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

        private static ChannelHandler createSslHandlerUsingNetty(ChannelPipeline pipeline) throws Exception {
            PrivateKey key = (PrivateKey) KEY_STORE.getKey(KeyStoreUtils.DEFAULT_ALIAS, KeyStoreUtils.DEFAULT_PASSWORD.toCharArray());
            X509Certificate certificate = (X509Certificate) KEY_STORE.getCertificate(KeyStoreUtils.DEFAULT_ALIAS);

            return SslContextBuilder.forServer(key, certificate).build()
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
