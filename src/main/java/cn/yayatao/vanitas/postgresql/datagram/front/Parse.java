package cn.yayatao.vanitas.postgresql.datagram.front;

//		Parse (F)
//		Byte1('P')
//		标识消息是一条 Parse 命令。
//		
//		Int32
//		以字节计的消息内容长度，包括长度本身。
//		
//		String
//		目的预备语句的名字(空字符串表示选取了未命名的预备语句)
//		
//		String
//		要分析的查询字符串
//		
//		Int16
//		声明的参数数据类型的数目(可以为零)。请注意这个参数并不意味着可能在查询字符串里出现的参数个数的意思，只是前端希望预先声明的类型的数目。
//		
//		然后，对每个参数，有下面的东西：
//		
//				Int32
//				声明参数数据类型的对象 ID 。在这里放一个零等效于不声明该类型。


public class Parse implements IFrontDatagram {
	private char mark = 'E';

	private int length;
	
	private String name;
	
	private String sql;
	
	/***
	 * 声明的参数数据类型的数目
	 */
	private short argumentTypeOidsNumber;
	
	/***
	 * 声明参数数据类型的对象 ID 
	 */
	private int argumentTypeOids;

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

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public short getArgumentTypeOidsNumber() {
		return argumentTypeOidsNumber;
	}

	public void setArgumentTypeOidsNumber(short argumentTypeOidsNumber) {
		this.argumentTypeOidsNumber = argumentTypeOidsNumber;
	}

	public int getArgumentTypeOids() {
		return argumentTypeOids;
	}

	public void setArgumentTypeOids(int argumentTypeOids) {
		this.argumentTypeOids = argumentTypeOids;
	}

	
	@Override
	public byte[] toByteArrays() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void reviseLength() {
		// TODO Auto-generated method stub
		
	}
}
