package cn.yayatao.vanitas.postgresql.datagram.server;

import java.nio.ByteBuffer;

import cn.yayatao.vanitas.postgresql.datagram.general.ByteUtils;

//		CommandComplete (B)
//		Byte1('C')
//		标识此消息是一个命令结束响应。
//		
//		Int32
//		以字节记的消息内容的长度，包括长度本身。
//		
//		String
//		命令标记。它通常是一个单字，标识那个命令完成。
//		
//		对于 INSERT 命令，标记是 INSERT oid rows ，这里的 rows 是插入的行数。oid 在 rows 为 1 并且目标表有 OID 的时候是插入行的对象 ID ；否则 oid 就是 0
//		
//		对于 DELETE 命令，标记是 DELETE rows ，这里的 rows 是删除的行数。
//		
//		对于 UPDATE 命令，标记是 UPDATE rows ，这里的 rows 是更新的行数。
//		
//		对于 MOVE 命令，标记是 MOVE rows ，这里的 rows 是游标未知改变的行数。
//		
//		对于 FETCH 命令，标记是 FETCH rows ，这里的 rows 是从游标中检索出来的行数。
//		
//		对于 COPY 命令，标记是 COPY rows ，这里的 rows 是拷贝的行数。
public class CommandComplete implements IServerDatagram {

	private char mark = 'C';

	private int length;

	private String note;

	@Override
	public byte[] toByteArrays() {
		if (length == 0) {
			reviseLength();
		}
		ByteBuffer buffer = ByteBuffer.allocate(size());
		buffer.put((byte) mark);
		buffer.putInt(length);
		buffer.put(ByteUtils.stringToBytes(note, true));
		return buffer.array();
	}

	@Override
	public int size() {
		return length + 1;
	}

	@Override
	public void reviseLength() {
		this.length = ByteUtils.getStringLength(note) + 4;
	}

}
