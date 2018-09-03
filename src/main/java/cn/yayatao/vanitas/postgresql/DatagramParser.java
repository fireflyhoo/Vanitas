package cn.yayatao.vanitas.postgresql;

import java.nio.ByteBuffer;

import cn.yayatao.vanitas.postgresql.datagram.Datagram;

/******
 * 包解析器
 * @author Huyahui
 *
 */
public interface DatagramParser {
	/**
	 * 解析包
	 * @param frame
	 * @return
	 */
	public Datagram parse(byte[] frame);
	
	/**
	 * 拆解包
	 * @param buffer
	 * @param offset
	 * @return
	 */
	public DatagramFrames split(ByteBuffer buffer, int offset);
	
}
