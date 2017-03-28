package io.mycat.backend.postgresql.packet;

/**
 * Sync (F) Byte1('S') 表示该消息为一条 Sync命令。
 * 
 * Int32(4) 以字节记的消息内容的长度，包括长度自身。
 */
public class Sync extends PostgreSQLPacket {
	private char marker = PacketMarker.F_Sync.getValue();

	@Override
	public int getLength() {
		return 4;
	}

	@Override
	public char getMarker() {
		return marker;
	}

}
