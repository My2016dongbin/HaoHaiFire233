package com.skyline.socket;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.vilyever.socketclient.SocketClient;
import com.vilyever.socketclient.helper.SocketClientDelegate;
import com.vilyever.socketclient.helper.SocketClientReceivingDelegate;
import com.vilyever.socketclient.helper.SocketClientSendingDelegate;
import com.vilyever.socketclient.helper.SocketPacket;
import com.vilyever.socketclient.helper.SocketPacketHelper;
import com.vilyever.socketclient.helper.SocketResponsePacket;
import com.vilyever.socketclient.util.CharsetUtil;

import java.util.Arrays;

/**
 * Created by HHT on 2018-01-15.
 */

public class MyClient {

    private static final String TAG = "MyClient";
    final MyClient self=this;

    String serverIp="192.168.1.249";
    String serverPort="9999";

    /**
     * 连接
     */
    public void connect() {
        self.getLocalSocketClient().connect();
        Log.i(TAG, "connect: "+self.getLocalSocketClient().getState());
    }
    public   boolean isConnected()
    {
        if (self.getLocalSocketClient().getState()== SocketClient.State.Disconnected)
        {
            return  false;
        }
        else
        {
            return  true;
        }
    }

    /**
     * 断开连接
     */
    public void disConnect()
    {
        if (self.getLocalSocketClient().getState()!= SocketClient.State.Disconnected)
        {
            self.getLocalSocketClient().disconnect();
        }
    }
    public  MyClient(String ip,String port)
    {
        this.serverIp=ip;
        this.serverPort=port;
    }
    public MyClient()
    {

    }

    /* Properties */
    private SocketClient localSocketClient;
    public SocketClient getLocalSocketClient() {
        if (this.localSocketClient == null) {
            this.localSocketClient = new SocketClient();

            setupAddress(this.localSocketClient);
            setupEncoding(this.localSocketClient);

            //setupConstantHeartBeat(this.localSocketClient);
            //发送数据
            setupReadByLengthForSender(this.localSocketClient);
            //接收数据
            setupReadByLengthForReceiver(this.localSocketClient);
            this.localSocketClient.registerSocketClientDelegate(new SocketClientDelegate() {
                @Override
                public void onConnected(SocketClient client) {
                    Log.i("onConnected", "SocketClient: onConnected");

                    if (client.getSocketPacketHelper().getReadStrategy() == SocketPacketHelper.ReadStrategy.Manually) {
                        client.readDataToLength(CharsetUtil.stringToData("Server accepted", CharsetUtil.UTF_8).length);
                        Log.i(TAG, "onConnected: "+client.getSocketPacketHelper().getReadStrategy().name());
                    }
                }

                @Override
                public void onDisconnected(final SocketClient client) {
                    Log.i(TAG, "onDisconnected: "+client.getState().name());
             /*       new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... params) {
                            try {
                                Thread.sleep(10 * 1000);
                            }
                            catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            client.connect();

                            return null;
                        }


                    }.execute();*/
                }

                @Override
                public void onResponse(final SocketClient client, @NonNull SocketResponsePacket responsePacket) {
                    Log.i(TAG, "onResponse: ");
                    if (responsePacket.isHeartBeat()) {
                        Log.i(TAG, "onResponse: "+responsePacket.isHeartBeat());
                        return;
                    }
                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... params) {
                            try {
                                Thread.sleep(3 * 1000);
                            }
                            catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            client.sendString("client on " + System.currentTimeMillis());
//                            client.sendString("我爱中国");

                            try {
                                Thread.sleep(3 * 1000);
                            }
                            catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            client.disconnect();
                            Log.i(TAG, "doInBackground: disconnect");

                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void aVoid) {
                            super.onPostExecute(aVoid);

                        }
                    }.execute();
                }
            });
            //发送数据
            this.localSocketClient.registerSocketClientSendingDelegate(new SocketClientSendingDelegate() {

                @Override
                public void onSendPacketBegin(SocketClient client, SocketPacket packet) {
                    Log.i(TAG, "SocketClient: onSendPacketBegin: " + packet.hashCode() + "   " + Arrays.toString(packet.getData()));
                }

                @Override
                public void onSendPacketCancel(SocketClient client, SocketPacket packet) {
                    Log.i(TAG, "SocketClient: onSendPacketCancel: " + packet.hashCode());
                }

                @Override
                public void onSendingPacketInProgress(SocketClient client, SocketPacket packet, float progress, int sendedLength) {
                    Log.i(TAG, "SocketClient: onSendingPacketInProgress: " + packet.hashCode() + " : " + progress + " : " + sendedLength);
                }

                @Override
                public void onSendPacketEnd(SocketClient client, SocketPacket packet) {
                    Log.i(TAG, "SocketClient: onSendPacketEnd: " + packet.hashCode());
                   // client.disconnect();
                   // Log.i(TAG, "onSendPacketEnd:  duankailianjie");
                }
            });
            //接收数据
            this.localSocketClient.registerSocketClientReceiveDelegate(new SocketClientReceivingDelegate() {
                @Override
                public void onReceivePacketBegin(SocketClient client, SocketResponsePacket packet) {
                    Log.i(TAG, "SocketClient: onReceivePacketBegin: " + packet.getHeaderData());
                }

                @Override
                public void onReceivePacketEnd(SocketClient client, SocketResponsePacket packet) {
                    Log.i(TAG, "SocketClient: onReceivePacketEnd: " + packet.getTrailerData());
                }

                @Override
                public void onReceivePacketCancel(SocketClient client, SocketResponsePacket packet) {
                    Log.i(TAG, "SocketClient: onReceivePacketCancel: " + packet.hashCode());
                }

                @Override
                public void onReceivingPacketInProgress(SocketClient client, SocketResponsePacket packet, float progress, int receivedLength) {
                    Log.i(TAG, "SocketClient: onReceivingPacketInProgress: " + packet.hashCode() + " : " + progress + " : " + receivedLength);
                }
            });
        }
        return this.localSocketClient;
    }
    /**
     * 设置远程端地址信息
     */
    private void setupAddress(SocketClient socketClient) {
        Log.i(TAG, "__i__setupAddress: 设置参数");
        socketClient.getAddress().setRemoteIP(serverIp); // 远程端IP地址
        socketClient.getAddress().setRemotePort(serverPort); // 远程端端口号
        socketClient.getAddress().setConnectionTimeout(15 * 1000); // 连接超时时长，单位毫秒
    }

    /**
     * 设置自动转换String类型到byte[]类型的编码
     * 如未设置（默认为null），将不能使用{@link SocketClient#sendString(String)}发送消息
     * 如设置为非null（如UTF-8），在接受消息时会自动尝试在接收线程（非主线程）将接收的byte[]数据依照编码转换为String，在{@link SocketResponsePacket#getMessage()}读取
     */
    private void setupEncoding(SocketClient socketClient) {
        socketClient.setCharsetName(CharsetUtil.UTF_8); // 设置编码为UTF-8
    }

    private void setupConstantHeartBeat(SocketClient socketClient) {
        /**
         * 设置自动发送的心跳包信息
         */
        socketClient.getHeartBeatHelper().setDefaultSendData(new byte[]{0x08});


        Log.i(TAG, "setupConstantHeartBeat: ");
        /**
         * 设置远程端发送到本地的心跳包信息内容，用于判断接收到的数据包是否是心跳包
         * 通过{@link SocketResponsePacket#isHeartBeat()} 查看数据包是否是心跳包
         */
        socketClient.getHeartBeatHelper().setDefaultReceiveData(CharsetUtil.stringToData("HeartBeat", CharsetUtil.UTF_8));
        socketClient.getHeartBeatHelper().setHeartBeatInterval(10 * 1000); // 设置自动发送心跳包的间隔时长，单位毫秒
        socketClient.getHeartBeatHelper().setSendHeartBeatEnabled(true); // 设置允许自动发送心跳包，此值默认为false
    }

    /**
     * 设置包长度转换器
     * 即每次发送数据时，将包头以外的数据长度转换为特定的byte[]发送个远程端用于解析还需要读取多少长度的数据
     *
     * 例：socketClient.sendData(new byte[]{0x01, 0x02})的步骤为
     * 1. socketClient向远程端发送包头（如果设置了包头信息）
     * 2. socketClient要发送的数据为{0x01, 0x02}，长度为2（若设置了包尾，还需加上包尾的字节长度），通过此转换器将int类型的2转换为4字节的byte[]，远程端也照此算法将4字节的byte[]转换为int值
     * 3. socketClient向远程端发送转换后的长度信息byte[]
     * 4. socketClient向远程端发送正文数据{0x01, 0x02}
     * 5. socketClient向远程端发送包尾（如果设置了包尾信息）
     *
     * 此转换器用于第二步
     *
     * 使用{@link com.vilyever.socketclient.helper.SocketPacketHelper.ReadStrategy}必须设置此项
     * 用于分隔多条消息
     */
    private void setupReadByLengthForSender(SocketClient socketClient) {

        socketClient.getSocketPacketHelper().setSendPacketLengthDataConvertor(new SocketPacketHelper.SendPacketLengthDataConvertor() {
            @Override
            public byte[] obtainSendPacketLengthDataForPacketLength(SocketPacketHelper helper, int packetLength) {

                 //简单将int转换为byte[]

                byte[] data = new byte[4];
                data[3] = (byte) (packetLength & 0xFF);
                data[2] = (byte) ((packetLength >> 8) & 0xFF);
                data[1] = (byte) ((packetLength >> 16) & 0xFF);
                data[0] = (byte) ((packetLength >> 24) & 0xFF);
                return data;

            }
        });

        /**
         * 根据连接双方协议设置自动发送的包头数据
         * 每次发送数据包（包括心跳包）都会在发送包内容前自动发送此包头
         *
         * 若无需包头可删除此行
         */
        //包头
        socketClient.getSocketPacketHelper().setSendHeaderData(new byte[]{0x03});

        Log.i(TAG, "setupReadByLengthForSender: ");
        /**
         * 根据连接双方协议设置自动发送的包尾数据
         * 每次发送数据包（包括心跳包）都会在发送包内容后自动发送此包尾
         *
         * 若无需包尾可删除此行
         * 注意：
         * 使用{@link com.vilyever.socketclient.helper.SocketPacketHelper.ReadStrategy.AutoReadByLength}时不依赖包尾读取数据
         */

        //包尾
        socketClient.getSocketPacketHelper().setSendTrailerData(new byte[]{0x05});

        /**
         * 设置分段发送数据长度
         * 即在发送指定长度后通过 {@link SocketClientSendingDelegate#onSendingPacketInProgress(SocketClient, SocketPacket, float, int)}回调当前发送进度
         * 注意：回调过于频繁可能导致设置UI过于频繁从而导致主线程卡顿
         *
         * 若无需进度回调可删除此二行，删除后仍有【发送开始】【发送结束】的回调
         */
        //socketClient.getSocketPacketHelper().setSendSegmentLength(8); // 设置发送分段长度，单位byte
        //socketClient.getSocketPacketHelper().setSendSegmentEnabled(false); // 设置允许使用分段发送，此值默认为false

        /**
         * 设置发送超时时长
         * 在发送每个数据包时，发送每段数据的最长时间，超过后自动断开socket连接
         * 通过设置分段发送{@link SocketPacketHelper#setSendSegmentEnabled(boolean)} 可避免发送大数据包时因超时断开，
         *
         * 若无需限制发送时长可删除此二行
         */
        socketClient.getSocketPacketHelper().setSendTimeout(10 * 1000); // 设置发送超时时长，单位毫秒
        socketClient.getSocketPacketHelper().setSendTimeoutEnabled(true); // 设置允许使用发送超时时长，此值默认为false
    }

    private void setupReadByLengthForReceiver(SocketClient socketClient) {
        /**
         * 设置读取策略为自动读取指定长度
         */
        socketClient.getSocketPacketHelper().setReadStrategy(SocketPacketHelper.ReadStrategy.AutoReadByLength);

        /**
         * 设置包长度转换器
         * 即每次接收数据时，将远程端发送到本地的长度信息byte[]转换为int，然后读取相应长度的值
         *
         * 例：自动接收远程端所发送的socketClient.sendData(new byte[]{0x01, 0x02})【{0x01, 0x02}为将要接收的数据】的步骤为
         * 1. socketClient接收包头（如果设置了包头信息）（接收方式为一直读取到与包头相同的byte[],即可能过滤掉包头前的多余信息）
         * 2. socketClient接收长度为{@link SocketPacketHelper#getReceivePacketLengthDataLength()}（此处设置为4）的byte[]，通过下面设置的转换器，将byte[]转换为int值，此int值暂时称为X
         * 3. socketClient接收长度为X的byte[]
         * 4. socketClient接收包尾（如果设置了包尾信息）（接收方式为一直读取到与包尾相同的byte[],如无意外情况，此处不会读取到多余的信息）
         * 5. socketClient回调数据包
         *
         * 此转换器用于第二步
         *
         * 使用{@link com.vilyever.socketclient.helper.SocketPacketHelper.ReadStrategy.AutoReadByLength}必须设置此项
         * 用于分隔多条消息
         */
        socketClient.getSocketPacketHelper().setReceivePacketLengthDataLength(4);
        socketClient.getSocketPacketHelper().setReceivePacketDataLengthConvertor(new SocketPacketHelper.ReceivePacketDataLengthConvertor() {
            @Override
            public int obtainReceivePacketDataLength(SocketPacketHelper helper, byte[] packetLengthData) {
                /**
                 * 简单将byte[]转换为int
                 */
                int length =  (packetLengthData[3] & 0xFF) + ((packetLengthData[2] & 0xFF) << 8) + ((packetLengthData[1] & 0xFF) << 16) + ((packetLengthData[0] & 0xFF) << 24);
                Log.i(TAG, "obtainReceivePacketDataLength: "+length);

                return length;
            }
        });

        /**
         * 根据连接双方协议设置的包头数据
         * 每次接收数据包（包括心跳包）都会先接收此包头
         *
         * 若无需包头可删除此行
         */
        //接收数据设置包头
       // socketClient.getSocketPacketHelper().setReceiveHeaderData(CharsetUtil.stringToData("SocketClient:", CharsetUtil.UTF_8));

        //socketClient.getSocketPacketHelper().setReceiveHeaderData(new byte[]{0x01, 0x02});

        /**
         * 根据连接双方协议设置的包尾数据
         *
         * 若无需包尾可删除此行
         * 注意：
         * 使用{@link com.vilyever.socketclient.helper.SocketPacketHelper.ReadStrategy.AutoReadByLength}时不依赖包尾读取数据
         */
        //接收数据接收包尾0x13 0x10
        socketClient.getSocketPacketHelper().setReceiveTrailerData(new byte[]{0x13, 0x10});

        /**
         * 设置接收超时时长
         * 在指定时长内没有数据到达本地自动断开
         *
         * 若无需限制接收时长可删除此二行
         */
        socketClient.getSocketPacketHelper().setReceiveTimeout(60 * 1000); // 设置接收超时时长，单位毫秒
        socketClient.getSocketPacketHelper().setReceiveTimeoutEnabled(true); // 设置允许使用接收超时时长，此值默认为false
    }
}
