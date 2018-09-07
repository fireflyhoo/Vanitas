package cn.yayatao.vanitas.postgresql.datagram.front.analysis;

import java.nio.ByteBuffer;

import cn.yayatao.vanitas.postgresql.datagram.Datagram;
import cn.yayatao.vanitas.postgresql.datagram.IBuilder;
import cn.yayatao.vanitas.postgresql.datagram.front.CancelRequest;


//CancelRequest (F)
//Int32(16)
//以字节计的消息长度。包括长度本身。
//
//Int32(80877102)
//取消请求代码。选这个值是为了在高16位包含 1234， 低16位包含 5678。（为避免混乱，这个代码必须与协议版本号不同．）
//
//Int32
//目标后端的进程号（PID）。
//
//Int32
//目标后端的密钥（secret key）。
public class AnalysisCancelRequestBuilder implements IBuilder {

	@Override
	public Datagram build(byte[] data) {
		CancelRequest cancelRequest = new CancelRequest();
		ByteBuffer buffer = ByteBuffer.wrap(data);
		int length = buffer.getInt();
		int cancelCode = buffer.getInt();
		int pid  = buffer.getInt();
		int secretKey = buffer.getInt();
		cancelRequest.setLength(length);
		cancelRequest.setCancelCode(cancelCode);
		cancelRequest.setPid(pid);
		cancelRequest.setSecretKey(secretKey);		
		return cancelRequest;
	}

}
