package cn.yayatao.vanitas.net.nio;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.yayatao.vanitas.postgresql.DatagramFrames;
import cn.yayatao.vanitas.postgresql.datagram.Datagram;
import cn.yayatao.vanitas.postgresql.datagram.front.Bind;
import cn.yayatao.vanitas.postgresql.datagram.front.Describe;
import cn.yayatao.vanitas.postgresql.datagram.front.Execute;
import cn.yayatao.vanitas.postgresql.datagram.front.Parse;
import cn.yayatao.vanitas.postgresql.datagram.front.PasswordMessage;
import cn.yayatao.vanitas.postgresql.datagram.front.StartupMessage;
import cn.yayatao.vanitas.postgresql.datagram.front.Sync;
import cn.yayatao.vanitas.postgresql.datagram.server.AuthenticationMD5Password;
import cn.yayatao.vanitas.postgresql.datagram.server.AuthenticationOk;
import cn.yayatao.vanitas.postgresql.datagram.server.BindComplete;
import cn.yayatao.vanitas.postgresql.datagram.server.CommandComplete;
import cn.yayatao.vanitas.postgresql.datagram.server.DataRow;
import cn.yayatao.vanitas.postgresql.datagram.server.DataRow.DataColumn;
import cn.yayatao.vanitas.postgresql.datagram.server.ParseComplete;
import cn.yayatao.vanitas.postgresql.datagram.server.ReadyForQuery;
import cn.yayatao.vanitas.postgresql.datagram.server.RowDescription;
import cn.yayatao.vanitas.postgresql.datagram.server.RowDescription.ColumnDescription;
import cn.yayatao.vanitas.postgresql.parser.FrontDatagramParser;

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
		
		
		
		if(datagram instanceof Parse){
			LOGGER.info("name:{}, parseSQL:{}",((Parse) datagram).getName(),((Parse) datagram).getSql());
			ParseComplete  parseComplete = new ParseComplete();
			source.write(parseComplete.toByteArrays());
		}
		
		if(datagram instanceof Bind){
			 LOGGER.info("target: {}", ((Bind) datagram).getTargetName());
			 BindComplete bindComplete = new BindComplete();
			 source.write(bindComplete.toByteArrays());
		}
		
		if(datagram instanceof Describe){
			LOGGER.info("Describe:{}", ((Describe) datagram).getName());
			System.out.println(datagram);
		}
		
		if(datagram instanceof Execute){			
//			ErrorResponse errorResponse = new ErrorResponse();
//			errorResponse.setErrReason("服务器心情不好");
//			errorResponse.setCode((byte)'S');
//			source.write(errorResponse.toByteArrays());
			//行描述
			RowDescription  row  = new RowDescription();
			ColumnDescription[] columns = new ColumnDescription[4];
			for(int i=0;i< columns.length;i++){
				ColumnDescription columnDesc = new ColumnDescription();
				columnDesc.setName("col"+i);
				columns[i]= columnDesc;
			}
			row.setColumns(columns);
			row.setColumnsNumber((short)4);
			source.write(row.toByteArrays());
			for(int j=0;j<10;j++){
				//数据
				DataRow dataRow = new DataRow();
				DataColumn[] colDatas = new DataColumn[4];
				for(int i =0;i< colDatas.length;i++){
					DataColumn column = new DataColumn();
					byte[] data = ("xxxdsfewa"+j).getBytes();
					column.setData(data);
					column.setLength(data.length);
					colDatas[i]= column;
				}
				dataRow.setColumns(colDatas);
				dataRow.setColumnsNumber((short)4);
				source.write(dataRow.toByteArrays());
			}
			//完成语句
			CommandComplete  cmdComple = new CommandComplete();
			cmdComple.setNote("FETCH "+4); //取到了4行
			source.write(cmdComple.toByteArrays());
		}
		
		if(datagram instanceof Sync){
			ReadyForQuery readyForQuery = new ReadyForQuery();
			readyForQuery.setState((byte)'I');
			source.write(readyForQuery.toByteArrays());
		}
	}

	@Override
	public void onConnected(SocketChannel channel) {
	}

}
