package cn.yayatao.vanitas.net.nio;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.yayatao.vanitas.postgresql.DatagramFrames;
import cn.yayatao.vanitas.postgresql.datagram.Datagram;
import cn.yayatao.vanitas.postgresql.datagram.front.FrontDatagramParser;
import cn.yayatao.vanitas.postgresql.datagram.front.Sync;
import cn.yayatao.vanitas.postgresql.datagram.server.ErrorResponse;
import cn.yayatao.vanitas.postgresql.packet.AuthenticationPacket;
import cn.yayatao.vanitas.postgresql.packet.AuthenticationPacket.AuthType;
import cn.yayatao.vanitas.postgresql.packet.Bind;
import cn.yayatao.vanitas.postgresql.packet.BindComplete;
import cn.yayatao.vanitas.postgresql.packet.CommandComplete;
import cn.yayatao.vanitas.postgresql.packet.EmptyQueryResponse;
import cn.yayatao.vanitas.postgresql.packet.Parse;
import cn.yayatao.vanitas.postgresql.packet.ParseComplete;
import cn.yayatao.vanitas.postgresql.packet.PasswordMessage;
import cn.yayatao.vanitas.postgresql.packet.PostgreSQLPacket;
import cn.yayatao.vanitas.postgresql.packet.ReadyForQuery;
import cn.yayatao.vanitas.postgresql.packet.ReadyForQuery.TransactionState;
import cn.yayatao.vanitas.postgresql.packet.StartupMessage;
import cn.yayatao.vanitas.postgresql.utils.PacketUtils;

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
				System.out.println(datagram);
				doProcess(datagram,source); //每个包处理一个 
			}
		} catch (Exception e) {
			LOGGER.error("解析包出现异常",e);
		}
	}

	private void doProcess(Datagram datagram,FrontConnection source) {
		if(datagram instanceof cn.yayatao.vanitas.postgresql.datagram.front.StartupMessage){
			AuthenticationPacket authe = new AuthenticationPacket();
			authe.setSalt(new byte[] { 10, 15, 12, 11 }); // TODO 先写死
			authe.setAuthType(AuthType.MD5Password);
			source.write(authe.writeBuffer());
		}
		
		if(datagram instanceof cn.yayatao.vanitas.postgresql.datagram.front.PasswordMessage){
			AuthenticationPacket ok = new AuthenticationPacket();
			ok.setAuthType(AuthType.Ok);
			source.write(ok.writeBuffer());
			// 写入一个 ReadyForQuery 包, 完成认证

			ReadyForQuery readyForQuery = new ReadyForQuery();
			readyForQuery.setState(TransactionState.NOT_IN);
			source.write(readyForQuery.writeBuffer());
		}
		
		if(datagram instanceof Sync){
			BindComplete bindComplete = new BindComplete();
			source.write(bindComplete.writeBuffer());
			ErrorResponse err = new ErrorResponse();
			ByteBuffer buf = ByteBuffer.wrap(err.toByteArrays());
			source.write(buf);
		}
	}

	public void old(FrontConnection source, ByteBuffer bf) {
		try {
			List<PostgreSQLPacket> pgs = PacketUtils.parseClientPacket(bf, 0, 1);
			for (PostgreSQLPacket packet : pgs) {
				if (packet instanceof StartupMessage) {
					// 发送一个需要密码的包给前端.
					AuthenticationPacket authe = new AuthenticationPacket();
					authe.setSalt(new byte[] { 10, 15, 12, 11 }); // TODO 先写死
					authe.setAuthType(AuthType.MD5Password);
					source.write(authe.writeBuffer());
				} else if (packet instanceof PasswordMessage) {
					AuthenticationPacket ok = new AuthenticationPacket();
					ok.setAuthType(AuthType.Ok);
					source.write(ok.writeBuffer());
					// 写入一个 ReadyForQuery 包, 完成认证

					ReadyForQuery readyForQuery = new ReadyForQuery();
					readyForQuery.setState(TransactionState.NOT_IN);
					source.write(readyForQuery.writeBuffer());
				} else if (packet instanceof Parse) {
					LOGGER.debug("Parse sql:" + ((Parse) packet).getSql());
					ParseComplete complete = new ParseComplete();
					source.write(complete.writeBuffer());
				} else if (packet instanceof Bind) {

					LOGGER.debug("绑定参数", ((Bind) packet).getParameter());

					BindComplete bindComplete = new BindComplete();
					source.write(bindComplete.writeBuffer());

					EmptyQueryResponse emp = new EmptyQueryResponse();
					source.write(emp.writeBuffer());

					CommandComplete commandComplete = new CommandComplete();
					commandComplete.setCommandResponse("SELECT 0");
					source.write(commandComplete.writeBuffer());
				} else {
					System.out.println(packet);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void onConnected(SocketChannel channel) {
	}

}
