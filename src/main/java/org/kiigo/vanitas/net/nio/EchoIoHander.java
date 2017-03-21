package org.kiigo.vanitas.net.nio;

import java.nio.ByteBuffer;

public  class EchoIoHander implements IoHander{

	@Override
	public void hander(ByteBuffer bf,int length ,FrontConnection source) {
		//读取数据
		try {
		
			System.out.println("收到数据包"+bf);
			
			source.write(bf.array());
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}

}
