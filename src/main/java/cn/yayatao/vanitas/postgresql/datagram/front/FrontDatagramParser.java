package cn.yayatao.vanitas.postgresql.datagram.front;

import java.nio.ByteBuffer;

import cn.yayatao.vanitas.postgresql.DatagramFrames;
import cn.yayatao.vanitas.postgresql.DatagramParser;
import cn.yayatao.vanitas.postgresql.datagram.Datagram;
import cn.yayatao.vanitas.postgresql.datagram.IBuilder;
import cn.yayatao.vanitas.postgresql.datagram.front.analysis.AnalysisCancelRequestBuilder;
import cn.yayatao.vanitas.postgresql.datagram.front.analysis.AnalysisSSLRequestBuilder;
import cn.yayatao.vanitas.postgresql.datagram.front.analysis.AnalysisStartupMessageBuilder;
import cn.yayatao.vanitas.postgresql.utils.PIOUtils;

/**
 * 前端数据包解析
 * 
 * @author Huyahui
 *
 */
public class FrontDatagramParser implements DatagramParser {
	private final static IBuilder analysisCancelRequestBuilder = new AnalysisCancelRequestBuilder();
	private final static IBuilder analysisSSLRequestBuilder = new AnalysisSSLRequestBuilder();
	private final static IBuilder analysisStartupMessageBuilder = new AnalysisStartupMessageBuilder();

	private static enum FrontDatagramSign {
		;
		private char mark;
		private IBuilder builder;

		FrontDatagramSign(char mark, IBuilder builder) {
			this.mark = mark;
			this.builder = builder;
		}

		public static FrontDatagramSign valueOf(char mark) {
			for (FrontDatagramSign sign : FrontDatagramSign.values()) {
				if (sign.mark == mark) {
					return sign;
				}
			}
			return null;
		}
	}

	@Override
	public Datagram parse(byte[] frame) {
		FrontDatagramSign sign = FrontDatagramSign.valueOf((char) frame[0]);
		if (sign != null) {
			return sign.builder.build(frame);
		}
		// CancelRequest (F) , SSLRequest (F), StartupMessage (F)
		// 这三个数据包要单独处理
		int length = PIOUtils.bytesToInt(frame[0], frame[1], frame[2], frame[3]);
		int magic = PIOUtils.bytesToInt(frame[4], frame[5], frame[6], frame[7]);
		if (length == 16) {
			// CancelRequest
			return analysisCancelRequestBuilder.build(frame);
		}

		if (length == 8) {
			// SSLRequest
			return analysisSSLRequestBuilder.build(frame);
		}

		if (magic == 196608) {
			// StartupMessage
			return analysisStartupMessageBuilder.build(frame);
		}

		return null;
	}

	@Override
	public DatagramFrames split(ByteBuffer buffer, int offset) {
		buffer.flip();
		int position = buffer.position();
		DatagramFrames datagramFrames = new DatagramFrames();

		for (int i = 0; i < position - offset;) {
			char mark = buffer.getChar(offset + i);
			FrontDatagramSign sign = FrontDatagramSign.valueOf(mark);
			if (sign != null) {
				// 正常包
				if (i + 4 + offset + 1 < position) {
					int length = buffer.getInt(i + offset + 1);
					if ((i + 4 + 1 + offset + length) < position) {// 完整数据包
						byte[] frame = new byte[length + 1];
						buffer.get(frame, i + offset, length + 1);
						datagramFrames.addFrame(frame);
						i = i + length + 1;
						// 进入下一个循环
						continue;
					}
				}
			} else {
				// 坑爹三剑客 CancelRequest (F) , SSLRequest (F), StartupMessage (F)
				if (offset + i + 4 < position) {
					int length = buffer.getInt(i + offset);
					if ((offset + i + 4 + length) < position) {// 完整数据包
						byte[] frame = new byte[length + 1];
						buffer.get(frame, i + offset, length + 1);
						datagramFrames.addFrame(frame);
						i = i + length + 1;
						// 进入下一个循环
						continue;
					}
				}
			}
			// 异常包
			byte[] offcut = new byte[position - (offset + i)];
			buffer.get(offcut, offset + i, offcut.length);
			datagramFrames.setOffcut(offcut);
			break;
		}
		return datagramFrames;
	}

}