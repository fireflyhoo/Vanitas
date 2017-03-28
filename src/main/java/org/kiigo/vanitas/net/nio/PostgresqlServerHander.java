package org.kiigo.vanitas.net.nio;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.List;

import io.mycat.backend.postgresql.packet.AuthenticationPacket;
import io.mycat.backend.postgresql.packet.AuthenticationPacket.AuthType;
import io.mycat.backend.postgresql.packet.Bind;
import io.mycat.backend.postgresql.packet.BindComplete;
import io.mycat.backend.postgresql.packet.CommandComplete;
import io.mycat.backend.postgresql.packet.EmptyQueryResponse;
import io.mycat.backend.postgresql.packet.Parse;
import io.mycat.backend.postgresql.packet.ParseComplete;
import io.mycat.backend.postgresql.packet.PasswordMessage;
import io.mycat.backend.postgresql.packet.PostgreSQLPacket;
import io.mycat.backend.postgresql.packet.ReadyForQuery;
import io.mycat.backend.postgresql.packet.ReadyForQuery.TransactionState;
import io.mycat.backend.postgresql.packet.StartupMessage;
import io.mycat.backend.postgresql.utils.PacketUtils;

public class PostgresqlServerHander implements IoHander {

	@Override
	public void hander(ByteBuffer bf, int length, FrontConnection source) {
		System.out.println(bf);
		try {
			List<PostgreSQLPacket> pgs = PacketUtils.parseClientPacket(bf, 0, length); 
			for(PostgreSQLPacket packet : pgs){
				if(packet instanceof StartupMessage){
					//发送一个需要密码的包给前端.
					AuthenticationPacket authe = new AuthenticationPacket();				
					authe.setSalt(new byte[]{10,15,12,11}); //TODO 先写死
					authe.setAuthType(AuthType.MD5Password);
					source.write(authe.writeBuffer());
				}else if (packet instanceof PasswordMessage) {
					AuthenticationPacket ok = new AuthenticationPacket();
					ok.setAuthType(AuthType.Ok);
					source.write(ok.writeBuffer());
					//写入一个   ReadyForQuery 包, 完成认证
					
					ReadyForQuery readyForQuery = new ReadyForQuery();
					readyForQuery.setState(TransactionState.NOT_IN);
					source.write(readyForQuery.writeBuffer());
				}else if (packet instanceof Parse) {
					ParseComplete complete = new ParseComplete();
					source.write(complete.writeBuffer());
				}else if (packet instanceof Bind) {
					
					System.out.println("绑定参数");
					BindComplete bindComplete = new BindComplete();
					source.write(bindComplete.writeBuffer());
					
					EmptyQueryResponse emp = new EmptyQueryResponse();
					source.write(emp.writeBuffer());
					
					CommandComplete commandComplete = new CommandComplete();
					commandComplete.setCommandResponse("SELECT 0");
					source.write(commandComplete.writeBuffer()); 
					
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
