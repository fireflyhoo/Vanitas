package cn.yayatao.vanitas.postgresql.parser;

import java.nio.ByteBuffer;

import cn.yayatao.vanitas.postgresql.DatagramFrames;
import cn.yayatao.vanitas.postgresql.DatagramParser;
import cn.yayatao.vanitas.postgresql.datagram.Datagram;
import cn.yayatao.vanitas.postgresql.datagram.IBuilder;
import cn.yayatao.vanitas.postgresql.datagram.server.analysis.AuthenticationBuilder;
import cn.yayatao.vanitas.postgresql.datagram.server.analysis.BackendKeyDataBuilder;
import cn.yayatao.vanitas.postgresql.datagram.server.analysis.BindCompleteBuilder;
import cn.yayatao.vanitas.postgresql.datagram.server.analysis.CloseCompleteBuilder;
import cn.yayatao.vanitas.postgresql.datagram.server.analysis.CommandCompleteBuilder;

public class ServerDatagramParser  implements DatagramParser{
	
	private static enum ServerDatagramSign{
		/**
		 * 认证信息
		 */
		AUTHENTICATION('R',new AuthenticationBuilder()),
		
		/***
		 * 后端的密钥
		 */
		BACKEND_KEY_DATA('K',new BackendKeyDataBuilder()),
		
		/***
		 * 参数绑定成功
		 */
		BIND_COMPLETE('2',new BindCompleteBuilder()),
		
		
		/***
		 * 关闭完成响应
		 */
		CLOSE_COMPLETE('3',new CloseCompleteBuilder()),
		
		
		/******
		 * 命令完成响应
		 */
		COMMAND_COMPLETE('C',new CommandCompleteBuilder())
		;
		
		private char mark;
		private IBuilder builder;
		
		private ServerDatagramSign(char mark,IBuilder builder) {
			this.mark = mark;
			this.builder = builder;
		}
		
		public static ServerDatagramSign valueOf(char mark) {
			for (ServerDatagramSign sign : ServerDatagramSign.values()) {
				if (sign.mark == mark) {
					return sign;
				}
			}
			return null;
		}
		
	}

	@Override
	public Datagram parse(byte[] frame) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DatagramFrames split(ByteBuffer buffer, int offset) {
		// TODO Auto-generated method stub
		return null;
	}

}
