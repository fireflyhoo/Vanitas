package io.mycat.backend.postgresql.packet;

import java.io.IOException;
import java.nio.ByteBuffer;

import io.mycat.backend.postgresql.utils.PIOUtils;

//		Parse (F)
//		Byte1('P')
//		标识消息是一条 Parse 命令。
//		
//		Int32
//		以字节记的消息内容的长度，包括长度自身。
//		
//		String
//		目的准备好语句的名字（空字串表示选取了未命名的准备好语句）。
//		
//		String
//		要分析的查询字串。
//		
//		Int16
//		
//			声明的参数数据类型的数目(可以为零)。请注意这个参数并不意味着可能在查询字串里出现的参数个数的意思， 只是前端希望预先声明的类型的数目。
//		
//		然后，对每个参数，有下面的东西：
//		
//		Int32
//		声明参数数据类型的对象 ID。在这里放一个零等效于不声明该类型。

/**
 * 解析sql语句
 * 
 * @author Coollf
 *
 */
public class Parse extends PostgreSQLPacket {

	private char marker = PacketMarker.F_Parse.getValue();
	// private int length;
	private String name;
	private short parameterNumber;
	private DateType[] parameterTypes;
	private String sql;

	public Parse() {
	}

	public Parse(String name, String sql, DateType... parameterTypes) {
		this.name = (name == null) ? "\0" : (name.trim() + "\0");
		this.sql = (sql == null) ? "\0" : (sql.trim() + "\0");
		this.parameterNumber = (short) parameterTypes.length;
		this.parameterTypes = parameterTypes;

	}

	@Override
	public int getLength() {
		return 4 + name.getBytes(UTF8).length +1+ sql.getBytes(UTF8).length+1  + 2 + 4
				* parameterNumber; //参数为空时仍然需要多个类型为0的int
	}

	@Override
	public char getMarker() {
		return marker;
	}

	public String getName() {
		return name;
	}

	public short getParameterNumber() {
		return parameterNumber;
	}

	public DateType[] getParameterTypes() {
		return parameterTypes;
	}

	public String getSql() {
		return sql;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setParameterNumber(short parameterNumber) {
		this.parameterNumber = parameterNumber;
	}

	public void setParameterTypes(DateType[] parameterTypes) {
		this.parameterTypes = parameterTypes;
	}
	
	public void setSql(String sql) {
		this.sql = sql;
	}

	public void write(ByteBuffer buffer) throws IOException {
		PIOUtils.SendChar(marker, buffer);
		PIOUtils.SendInteger4(getLength(), buffer);
		PIOUtils.SendString(name, buffer);
		PIOUtils.SendString(sql, buffer);
		PIOUtils.SendInteger2(parameterNumber, buffer);
		for (DateType tp : parameterTypes) {
			PIOUtils.SendInteger4(tp.getValue(), buffer);
		}
		
	}
}
