package cn.yayatao.vanitas.net.nio;

import java.io.Closeable;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.yayatao.vanitas.net.SocketIOReactor;

/***
 * NIO 反应器处理 NIO事件
 * 
 * @author fireflyhoo
 *
 */
public class NIOReactor implements SocketIOReactor {

	private final static Logger LOGGER = LoggerFactory.getLogger(NIOReactor.class);

	private String name;

	private Selector rwSelector;

	private final ConcurrentLinkedQueue<FrontConnection> registerQueue;

	private final NIORw rw;

	public void start() {
		this.rw.start();
	}

	public NIOReactor(String name) throws IOException {
		this.name = name;
		this.rwSelector = Selector.open();
		this.rw = new NIORw();
		this.registerQueue = new ConcurrentLinkedQueue<FrontConnection>();
	}

	class NIORw extends Thread {
		public NIORw() {
			super(name + "-" + "RW");
		}

		@Override
		public void run() {
			for (;;) {
				int n = 0;
				try {
					n = rwSelector.select();
				} catch (IOException e) {
					LOGGER.error("选择出错", e);
				}
				doRegister();
				if (n == 0) {
					continue;
				}
				Set<SelectionKey> keys = rwSelector.selectedKeys();
				try {
					Iterator<SelectionKey> keysIter = keys.iterator();
					while (keysIter.hasNext()) {
						SelectionKey key = keysIter.next();
						Object conn = key.attachment();
						try {
							if (key.isValid() && key.isReadable()) {
								LOGGER.debug("have Chand"); 
								doRead(key);
							}
							if (key.isValid() && key.isWritable()) {
								// 可写
								LOGGER.debug(key.channel() + "can writable");
								doWrit(key);
							}
							keysIter.remove();
						} catch (Throwable e) {
							LOGGER.error("处理读写事件出错", e);
							if (conn != null && conn instanceof FrontConnection) {
								try {
									((FrontConnection) conn).close();
								} catch (IOException e1) {
									LOGGER.error("关闭链接出错", e);
								}
							}
						}
					}
				} finally {
					keys.clear();
				}
			}
		}

	}

	public void doRead(SelectionKey key) {
		Object con = key.attachment();
		if(con != null && con instanceof FrontConnection){
			try {
				((FrontConnection)con).asynchronousRead();
			} catch (Throwable e) {
				LOGGER.error("读取数据出错", e);
				try {
					((FrontConnection) con).close(); 
				} catch (IOException e1) {
					LOGGER.error("关闭连接错误", e1);
				}
			}
		}else{
			LOGGER.error("读取数据出错  SelectionKey {}，con {}",key,con);
			try {
				key.channel().close();
				key.cancel();
			} catch (IOException e1) {
				LOGGER.error("关闭连接错误", e1);
			}
		}
		
		SocketChannel chann = (SocketChannel) key.channel();
		try {
			ByteBuffer dst = ByteBuffer.allocate(1024 * 1024);
			int n = chann.read(dst);
			if (n > 0) {
				byte[] bytes = new byte[n];
				dst.flip();
				dst.get(bytes, 0, n);
				System.err.println("读到数据啦:" + new String(bytes));				
			}
		} catch (Exception e) {
			LOGGER.error("读取数据出错", e);
			try {
				chann.close();
			} catch (IOException e1) {
				LOGGER.error("关闭连接错误", e1);
			}
		}
	}

	//
	protected void doRegister() {
		FrontConnection con = registerQueue.poll();
		while (con != null) {
			try {
				SocketChannel chan = con.getChannel();
				chan.configureBlocking(false);
				SelectionKey selectionKey = chan.register(rwSelector, SelectionKey.OP_READ,con);// 默认是可读
				con.setSelectionKey(selectionKey);
				con = registerQueue.poll();
			} catch (Throwable e) {
				LOGGER.error("注册连接出错", con, e);
				try {
					con.close();
				} catch (Exception e1) {
					LOGGER.error("注册出错关闭前端连接", e);
				}
			}

		}
	}

	public void doWrit(SelectionKey key) {
		LOGGER.debug("进行写处理......"); 
		Object attachment  = key.attachment();
		if(attachment instanceof IOConnection){
			try {
				((IOConnection) attachment).write0();
				LOGGER.debug("进行写处理完成......"); 
				if ((key.interestOps() & SelectionKey.OP_WRITE) != 0) {					
					key.interestOps(key.interestOps() & (~SelectionKey.OP_WRITE));
				}
			} catch (Throwable e) {
				LOGGER.error("写入数据出现错误,关闭连接",e);
				if(attachment instanceof Closeable){
					try {
						((Closeable) attachment).close();
					} catch (IOException exception) {
						LOGGER.error("close  attachment error",exception); 
					}
				}
			}
		}
		
	}

	public void register(FrontConnection con) {
		this.registerQueue.add(con);
		this.rwSelector.wakeup();
	}
}
