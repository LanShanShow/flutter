/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package com.lanshan.springflutter.client;

import com.lanshan.springflutter.message.Message;
import com.lanshan.springflutter.server.DecodeHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;

import java.util.Scanner;

/**
 * Keeps sending random data to the specified address.
 */
public final class DiscardClient2 {

    static final boolean SSL = System.getProperty("ssl") != null;
    static final String HOST = System.getProperty("host", "127.0.0.1");
    static final int PORT = Integer.parseInt(System.getProperty("port", "9999"));
    static final int SIZE = Integer.parseInt(System.getProperty("size", "256"));

    public static void main(String[] args) throws Exception {
        // Configure SSL.
        final SslContext sslCtx;
        if (SSL) {
            sslCtx = SslContextBuilder.forClient()
                .trustManager(InsecureTrustManagerFactory.INSTANCE).build();
        } else {
            sslCtx = null;
        }

        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
             .channel(NioSocketChannel.class)
             .handler(new ChannelInitializer<SocketChannel>() {
                 @Override
                 protected void initChannel(SocketChannel ch) throws Exception {
                     ChannelPipeline p = ch.pipeline();
                     if (sslCtx != null) {
                         p.addLast(sslCtx.newHandler(ch.alloc(), HOST, PORT));
                     }
                     p.addLast(new DecodeHandler());
                     p.addLast(new DiscardClientHandler());
                     p.addLast(new EncodeHandler());
                 }
             });

            // Make the connection attempt.
            ChannelFuture f = b.connect(HOST, PORT).sync();
            if(f.isSuccess()){
                Channel channel = f.channel();
                new Thread(() -> {
                    while (!Thread.interrupted()){
                        System.out.println("输入");
                        Scanner scanner = new Scanner(System.in);
                        String in = scanner.nextLine();
                        Message message = new Message();
                        message.setMsg(in);
                        channel.writeAndFlush(message);
                    }
                }).start();
            }
            // Wait until the connection is closed.
            f.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }
    }
}
