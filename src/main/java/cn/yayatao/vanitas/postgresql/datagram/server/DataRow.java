package cn.yayatao.vanitas.postgresql.datagram.server;

import java.nio.ByteBuffer;

//	DataRow (B)
//	Byte1('D')
//	标识这个消息是一个数据行。
//	
//	Int32
//	以字节记的消息内容的长度，包括长度自身。
//	
//	Int16
//	后面跟着的字段值的个数(可能是零)。
//	
//	然后，每个字段都会出现下面的数据域对：
//		
//		Int32
//		字段值的长度，以字节记(这个长度不包括它自己)。可以为零。一个特殊的情况是，-1 表示一个 NULL 的字段值。在 NULL 的情况下就没有跟着数据字段。
//		
//		Byten
//		一个字段的数值，以相关的格式代码表示的格式展现。n 是上面的长度。

public class DataRow implements IServerDatagram {

	private char mark = 'D';

	private int length;

	private short columnsNumber;

	private DataColumn[] columns;

	public static class DataColumn {
		/**
		 * 长度
		 */
		private int length;

		/**
		 * 数据
		 */
		private byte[] data;

		public int getLength() {
			return length;
		}

		public void setLength(int length) {
			this.length = length;
		}

		public byte[] getData() {
			return data;
		}

		public void setData(byte[] data) {
			this.data = data;
		}

	}

	public char getMark() {
		return mark;
	}

	public void setMark(char mark) {
		this.mark = mark;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public short getColumnsNumber() {
		return columnsNumber;
	}

	public void setColumnsNumber(short columnsNumber) {
		this.columnsNumber = columnsNumber;
	}

	public DataColumn[] getColumns() {
		return columns;
	}

	public void setColumns(DataColumn[] columns) {
		this.columns = columns;
	}

	@Override
	public byte[] toByteArrays() {
		if (this.length == 0) {
			reviseLength();
		}
		ByteBuffer buffer = ByteBuffer.allocate(size());
		buffer.put((byte) mark);
		buffer.putInt(length);
		buffer.putShort(columnsNumber);
		for (DataColumn c : columns) {
			buffer.putInt(c.getLength());
			buffer.put(c.getData());
		}
		return buffer.array();
	}

	@Override
	public int size() {
		return length + 1;
	}

	@Override
	public void reviseLength() {
		int _length = 4/* length */ + 2 /* columnsNumber */;
		for (DataColumn c : columns) {
			_length += (4/* length */ + c.getData().length);
		}

		this.length = _length;
	}
}
