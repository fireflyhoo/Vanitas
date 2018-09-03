package cn.yayatao.vanitas.net.nio;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public  class EchoIoHander implements IoHander{

	@Override
	public void hander(ByteBuffer bf,int length ,FrontConnection source) {
		//读取数据
		try {		
			System.out.println("收到数据包"+bf);
			source.write(bf);
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}

	@Override
	public void onConnected(SocketChannel channel) {
	}
}
