package cn.yayatao.vanitas.postgresql.datagram.front;
//		Describe (F)
//		Byte1('D')
//		标识消息是一个 Describe(描述)命令。
//		
//		Int32
//		以字节记的消息内容的长度，包括字节本身。
//		
//		Byte1
//		'S'描述一个预备语句；或者'P'描述一个入口。
//		
//		String
//		要描述的预备语句或者入口的名字(或者一个空字符串，就会选取未命名的预备语句或者入口)。
public class Describe {

}
