package cn.yayatao.vanitas.net.nio;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.yayatao.vanitas.postgresql.DatagramFrames;
import cn.yayatao.vanitas.postgresql.datagram.Datagram;
import cn.yayatao.vanitas.postgresql.datagram.front.Execute;
import cn.yayatao.vanitas.postgresql.datagram.front.FrontDatagramParser;
import cn.yayatao.vanitas.postgresql.datagram.front.PasswordMessage;
import cn.yayatao.vanitas.postgresql.datagram.front.StartupMessage;
import cn.yayatao.vanitas.postgresql.datagram.front.Sync;
import cn.yayatao.vanitas.postgresql.datagram.server.AuthenticationMD5Password;
import cn.yayatao.vanitas.postgresql.datagram.server.AuthenticationOk;
import cn.yayatao.vanitas.postgresql.datagram.server.EmptyQueryResponse;
import cn.yayatao.vanitas.postgresql.datagram.server.ErrorResponse;
import cn.yayatao.vanitas.postgresql.datagram.server.ReadyForQuery;

public class PostgresqlServerHander implements IoHander {

	private final static Logger LOGGER = LoggerFactory.getLogger(PostgresqlServerHander.class);

	private final ByteArrayOutputStream buffer = new ByteArrayOutputStream();

	private FrontDatagramParser datagramParser = new FrontDatagramParser();

	@Override
	public void hander(ByteBuffer bf, int length, FrontConnection source) {
		try {
			buffer.write(bf.array());
			byte[] packetDatas = buffer.toByteArray();
			buffer.reset();
			DatagramFrames frames = datagramParser.split(ByteBuffer.wrap(packetDatas), 0);
			// 剩余的半包
			if(frames.getOffcut()!= null){
				buffer.write(frames.getOffcut());
			}
			for (byte[] frame : frames.getFrames()) {
				Datagram datagram = datagramParser.parse(frame);
				LOGGER.error("Datagram:{}",datagram);
				doProcess(datagram,source); //每个包处理一个 
			}
		} catch (Exception e) {
			LOGGER.error("解析包出现异常",e);
		}
	}

	private void doProcess(Datagram datagram,FrontConnection source) {
		if(datagram instanceof StartupMessage){
			AuthenticationMD5Password authenticationMD5Password = new AuthenticationMD5Password();
			source.write(authenticationMD5Password.toByteArrays());
		}
		
		if(datagram instanceof PasswordMessage){
			AuthenticationOk ok = new AuthenticationOk();
			source.write(ok.toByteArrays());
			// 写入一个 ReadyForQuery 包, 完成认证

			ReadyForQuery readyForQuery = new ReadyForQuery();
			readyForQuery.setState((byte) 'I');
			
			source.write(readyForQuery.toByteArrays());
		}
		
		if(datagram instanceof Execute){
			
//			ErrorResponse errorResponse = new ErrorResponse();
//			errorResponse.setErrReason("服务器心情不好");
//			errorResponse.setCode((byte)'S');
//			source.write(errorResponse.toByteArrays());
			
			
			EmptyQueryResponse empty  = new EmptyQueryResponse();
			source.write(empty.toByteArrays());
		}
		
		if(datagram instanceof Sync){
			ReadyForQuery readyForQuery = new ReadyForQuery();
			readyForQuery.setMark((char)'I');
			source.write(readyForQuery.toByteArrays());
		}
		
		
	}

	@Override
	public void onConnected(SocketChannel channel) {
	}

}
