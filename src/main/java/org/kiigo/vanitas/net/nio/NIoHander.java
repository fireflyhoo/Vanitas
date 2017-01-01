package org.kiigo.vanitas.net.nio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;

import io.mycat.backend.postgresql.packet.PostgreSQLPacket;
import io.mycat.backend.postgresql.utils.PacketUtils;

public  class NIoHander implements IoHander{

	@Override
	public void hander(ByteBuffer bf,int length) {
		//读取数据
		try {
			List<PostgreSQLPacket> bj = PacketUtils.parseClientPacket(bf, 0, length);			
			System.out.println(bj); 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
