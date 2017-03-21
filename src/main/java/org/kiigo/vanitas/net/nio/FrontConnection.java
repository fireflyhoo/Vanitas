package org.kiigo.vanitas.net.nio;

import java.io.Closeable;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.kiigo.vanitas.VanitasServer;

public class FrontConnection implements Closeable ,IOConnection{
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

	private IoHander ioHander = new NIoHander();

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
		int lenth = channel.read(readBuffer);
		if (lenth <= 0) {
			return;
		}
		ByteBuffer bf = ByteBuffer.wrap(readBuffer.array());
		readBuffer.clear();
		VanitasServer.getExecutor().execute(() -> {
			ioHander.hander(bf,lenth,this);
		});

	}

	public void write(ByteBuffer buffer) {
		writeQueue.offer(buffer);
		selectionKey.interestOps(selectionKey.interestOps() | SelectionKey.OP_WRITE);
	}

	public void write(byte... bs) {
		writeQueue.offer(ByteBuffer.wrap(bs));
		selectionKey.interestOps(selectionKey.interestOps() | SelectionKey.OP_WRITE);
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
		ByteBuffer v =null;;
		while ((v  = writeQueue.poll()) !=null) {
			while(0 < channel.write(v));
		}		
	}

}
