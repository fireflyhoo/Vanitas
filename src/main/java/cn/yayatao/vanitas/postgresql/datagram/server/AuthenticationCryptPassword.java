package cn.yayatao.vanitas.postgresql.datagram.server;

import cn.yayatao.vanitas.postgresql.datagram.IServerDatagram;

import java.nio.ByteBuffer;

//AuthenticationCryptPassword (B)
//Byte1('R')
//标识该消息是一条认证请求。
//
//Int32(10)
//以字节记的消息内容的长度，包括长度本身。
//
//Int32(4)
//声明需要一个 crypt() 加密的口令。
//
//Byte2
//加密口令使用的盐粒(salt)。

public class AuthenticationCryptPassword  implements IServerDatagram {
	
	private char mark = 'R';

	private int length = 10;

	private int authType = 4;
	
	byte[] salt;
	

	@Override
	public byte[] toByteArrays() {
		ByteBuffer buffer= ByteBuffer.allocate(size());
		buffer.put((byte)mark);
		buffer.putInt(length);
		buffer.putInt(authType);	
		buffer.put(salt);
		return buffer.array();
	}

	@Override
	public int size() {
		return 11;
	}

	@Override
	public void reviseLength() {
	}

}
