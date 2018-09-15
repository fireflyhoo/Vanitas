package cn.yayatao.vanitas.postgresql.datagram.front;

import cn.yayatao.vanitas.postgresql.datagram.general.ByteUtils;

//		Execute (F)
//		Byte1('E')
//		标识消息识一个 Execute 命令。
//		
//		Int32
//		以字节记的消息内容的长度，包括长度自身。
//		
//		String
//		要执行的入口的名字（空字串选定未命名的入口）。
//		
//		Int32
//		要返回的最大行数，如果入口包含返回行的查询(否则忽略)。 零标识"没有限制"。
public class Execute implements IFrontDatagram {
	private char mark;

	private int length;
	
	private String name;
	
	private int maxReturnRowSize;

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getMaxReturnRowSize() {
		return maxReturnRowSize;
	}

	public void setMaxReturnRowSize(int maxReturnRowSize) {
		this.maxReturnRowSize = maxReturnRowSize;
	}

	@Override
	public byte[] toByteArrays() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int size() {
		return length + 1;
	}

	@Override
	public void reviseLength() {
		int currLength = 4 /*lenth*/ 
				    +ByteUtils.getStringLength(name)
				    +4 /**/;	
	}
	
}
