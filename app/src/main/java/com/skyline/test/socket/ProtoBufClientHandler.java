package com.skyline.test.socket;


import com.bean.MessageBuf;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ProtoBufClientHandler extends SimpleChannelInboundHandler<MessageBuf.JMTransfer> {
    private NettyListener listener;

    public ProtoBufClientHandler(NettyListener listener){
        this.listener = listener;
    }
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, MessageBuf.JMTransfer book) throws Exception {

        listener.onMessageResponse(book);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        NettyClient.getInstance().setConnectStatus(false);
        listener.onServiceStatusConnectChanged(NettyListener.STATUS_CONNECT_CLOSED);
        NettyClient.getInstance().reconnect();
    }
}
