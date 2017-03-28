package io.mycat.backend.postgresql.packet;

import java.nio.ByteBuffer;

import io.mycat.backend.postgresql.utils.PIOUtils;

/*******
 * 
 * ParseComplete (B)
 *   Byte1('1')
 *       标识这条消息是一个 Parse 完成指示器。
 *   Int32(4)
 *       以字节记的消息内容长度，包括长度自身。
 *       
 *        编译完成数据库包 
 *        
 * @author Huyahui
 *
 */
public class ParseComplete extends PostgreSQLPacket {
	private char marker = PacketMarker.B_ParseComplete.getValue();
	private int length ;
	
	
	@Override
	public int getLength() {
		return length;
	}

	@Override
	public char getMarker() {
		return marker;
	}

	public static ParseComplete parse(ByteBuffer buffer, int offset) {
		if ((char) buffer.get(offset) != PacketMarker.B_ParseComplete.getValue()) {
			throw new IllegalArgumentException("this packet not is ParseComplete");
		}
		ParseComplete parse = new ParseComplete();
		parse.length = PIOUtils.redInteger4(buffer, offset+1);
		return parse;
	}

	@Override
	public ByteBuffer writeBuffer() {
		ByteBuffer buf  = ByteBuffer.allocate(5);
		PIOUtils.SendChar(this.getMarker(), buf);
		PIOUtils.SendInteger4(4, buf);
		return buf;
	}
}
