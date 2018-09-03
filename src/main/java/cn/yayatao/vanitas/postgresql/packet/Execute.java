package cn.yayatao.vanitas.postgresql.packet;

//字段值。
//
//Execute (F)
//Byte1('E')
//标识消息识一个 Execute 命令。
//
//Int32
//以字节记的消息内容的长度，包括长度自身。
//
//String
//要执行的入口的名字（空字串选定未命名的入口）。
//
//Int32
//要返回的最大行数，如果入口包含返回行的查询(否则忽略)。 零标识"没有限制"。

/**
 * 执行预编译SQL
 * 
 * @author Huyahui
 *
 */
public class Execute extends PostgreSQLPacket {

	@Override
	public int getLength() {
		return 0;
	}

	@Override
	public char getMarker() {
		return PostgreSQLPacket.PacketMarker.F_Execute.getValue();
	}

}
