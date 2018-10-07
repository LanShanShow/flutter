package com.lanshan.springflutter.message;

import io.netty.channel.Channel;

import java.util.HashSet;
import java.util.Set;

public class ChannelContain {
     private ChannelContain(){}
     private static class Channels{
          private static Set<Channel> ctxs = new HashSet<>();
     }
     public static Set<Channel> getChannels(){
          return Channels.ctxs;
     }
}
