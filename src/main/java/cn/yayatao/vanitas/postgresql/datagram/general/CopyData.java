package cn.yayatao.vanitas.postgresql.datagram.general;

//		CopyData (F & B)
//		Byte1('d')
//		标识这条消息是一个 COPY 数据。
//		
//		Int32
//		以字节记的消息内容的长度，包括长度本身。
//		
//		Byten
//		COPY 数据流的一部分的数据。从后端发出的消息总是对应一个数据行，但是前端发出的消息可以任意分割数据流。

public class CopyData {

}
