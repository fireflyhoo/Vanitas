package cn.yayatao.vanitas.net.nio;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public interface IoHander {
	/**
	 * 连接完成
	 * @param channel
	 */
	void onConnected(SocketChannel channel); 

	/**
	 * 有数据写入
	 * @param bf
	 * @param length
	 * @param source
	 */
	void hander(ByteBuffer bf ,int length ,FrontConnection source);
}
