package cn.yayatao.vanitas.net.nio;

import java.io.Closeable;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import cn.yayatao.vanitas.VanitasServer;

public class FrontConnection implements Closeable, IOConnection {
	private Queue<ByteBuffer> writeQueue = new ConcurrentLinkedQueue<ByteBuffer>();

	/**
	 * 连接
	 */
	private final SocketChannel channel;

	/**
	 * 连接读写缓存
	 */
	private volatile ByteBuffer readBuffer;

	/**
	 * 注册的事件key
	 */
	private SelectionKey selectionKey;

	private IoHander ioHander = new PostgresqlServerHander();

	public SelectionKey getSelectionKey() {
		return selectionKey;
	}

	public void setSelectionKey(SelectionKey selectionKey) {
		this.selectionKey = selectionKey;
	}

	public SocketChannel getChannel() {
		return channel;
	}

	public FrontConnection(SocketChannel channel) {
		this.channel = channel;
		VanitasServer.getExecutor().execute(() ->{
			ioHander.onConnected(channel);
		});
	}

	/***
	 * 同步读取数据，异步处理
	 * 
	 * @throws Throwable
	 */
	public void asynchronousRead() throws Throwable {
		if (readBuffer == null) {
			readBuffer = ByteBuffer.allocate(1024 * 1024 * 2);// 默认读取 2MB 数据
		}
		int length = this.channel.read(readBuffer);
		int position = readBuffer.position();
		
		readBuffer.flip();
		byte[] dst = new byte[length];
		readBuffer.get(dst, position-length, length);
//		System.out.println("读到数据长度:" + dst.length);
		readBuffer.clear();
		VanitasServer.getExecutor().execute(() -> {
			ioHander.hander(ByteBuffer.wrap(dst), 0, this);
		});
	}

	public void write(ByteBuffer buffer) {
		writeQueue.add(buffer);
		this.doWakeupIoSelector();
	}

	/**
	 * 通知select 当前有 写入事件
	 */
	private void doWakeupIoSelector() {
		System.out.println("唤醒...///////////////");
		if ((selectionKey.interestOps() & SelectionKey.OP_WRITE) > 0) {
			System.err.print("当前的状态就是可写状态");
		} else {
			selectionKey.interestOps(selectionKey.interestOps() | SelectionKey.OP_WRITE);
		} 
		
		selectionKey.selector().wakeup();// 写入数据要立刻唤醒.
	}

	public void write(byte... bs) {
		writeQueue.add(ByteBuffer.wrap(bs));
		this.doWakeupIoSelector();
	}

	public void close() throws IOException {
		if (selectionKey != null) {
			selectionKey.cancel();
		}
		if (channel != null) {
			channel.socket().close();
			channel.close();
		}
		if (writeQueue != null) {
			writeQueue.clear();
			writeQueue = null;
		}
	}

	@Override
	public void write0() throws Throwable {
		ByteBuffer v = null;
		while ((v = writeQueue.poll()) != null) {
			while (0 < channel.write(v))
				;
		}
	}

}
