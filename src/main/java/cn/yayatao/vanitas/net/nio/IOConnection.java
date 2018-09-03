package cn.yayatao.vanitas.net.nio;
/***
 * 简单连接
 * @author Huyahui
 *
 */
public interface IOConnection {
	
	/**
	 * 真正写出数据
	 * @throws Throwable 
	 */
	void write0() throws Throwable;
	
	
	/***
	 * 异步读取数据
	 * @throws Throwable
	 */
	public void asynchronousRead() throws Throwable; 
} 
