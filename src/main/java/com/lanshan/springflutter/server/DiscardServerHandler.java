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
package com.lanshan.springflutter.server;

import com.lanshan.springflutter.message.ChannelContain;
import com.lanshan.springflutter.message.Message;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.HashSet;
import java.util.Set;

/**
 * Handles a server-side channel.
 */
public class DiscardServerHandler extends SimpleChannelInboundHandler<Message> {

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        ChannelContain.getChannels().add(ctx.channel());
        super.channelRegistered(ctx);
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, Message msg) throws Exception {
        // discard
        Set<Channel> ctxs = ChannelContain.getChannels();
            System.out.println(msg.getMsg());
            System.out.println("ctxs.size()"+ctxs.size());

        for (Channel c:ctxs) {
            if(!c.equals(ctx.channel())){
                c.writeAndFlush(msg);
                System.out.println("回复");
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }
}
