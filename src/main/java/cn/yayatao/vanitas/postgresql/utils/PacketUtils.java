package cn.yayatao.vanitas.postgresql.utils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import cn.yayatao.vanitas.postgresql.packet.AuthenticationPacket;
import cn.yayatao.vanitas.postgresql.packet.BackendKeyData;
import cn.yayatao.vanitas.postgresql.packet.Bind;
import cn.yayatao.vanitas.postgresql.packet.CommandComplete;
import cn.yayatao.vanitas.postgresql.packet.CopyInResponse;
import cn.yayatao.vanitas.postgresql.packet.CopyOutResponse;
import cn.yayatao.vanitas.postgresql.packet.DataRow;
import cn.yayatao.vanitas.postgresql.packet.EmptyQueryResponse;
import cn.yayatao.vanitas.postgresql.packet.ErrorResponse;
import cn.yayatao.vanitas.postgresql.packet.NoticeResponse;
import cn.yayatao.vanitas.postgresql.packet.ParameterStatus;
import cn.yayatao.vanitas.postgresql.packet.Parse;
import cn.yayatao.vanitas.postgresql.packet.ParseComplete;
import cn.yayatao.vanitas.postgresql.packet.PasswordMessage;
import cn.yayatao.vanitas.postgresql.packet.PostgreSQLPacket;
import cn.yayatao.vanitas.postgresql.packet.ReadyForQuery;
import cn.yayatao.vanitas.postgresql.packet.RowDescription;
import cn.yayatao.vanitas.postgresql.packet.StartupMessage;
import cn.yayatao.vanitas.postgresql.packet.Bind.DataParameter;
import cn.yayatao.vanitas.postgresql.packet.PostgreSQLPacket.DataProtocol;
import cn.yayatao.vanitas.postgresql.packet.PostgreSQLPacket.DateType;
import cn.yayatao.vanitas.postgresql.packet.StartupMessage.ParamsItme;

public class PacketUtils {

	public static List<PostgreSQLPacket> parseClientPacket(ByteBuffer buffer, int offset, int readLength)
			throws Exception {
		ArrayList<PostgreSQLPacket> pgs = new ArrayList<>();
		char _MAKE = (char) buffer.get(offset);
		switch (_MAKE) {
		case 'C':
			break;

		case 'p':
			pgs.add(paersPasswordMessage(buffer, offset, readLength));
			break;

		case 'P':
			pgs.addAll(paersParsePkg(buffer, offset, readLength));
			break;

		default:
			pgs.add(paersStartUp(buffer, offset, readLength));
			break;
		}

		return pgs;
	}

	/****************
	 * 解析编译sql包
	 * 
	 * @param buffer
	 * @param offset
	 * @param readLength
	 * @return
	 * @throws IOException
	 */
	private static List<PostgreSQLPacket> paersParsePkg(ByteBuffer buffer, int offset, int readLength)
			throws IOException {

		List<PostgreSQLPacket> pgs = new ArrayList<>();

		char mark = PIOUtils.redChar1(buffer, offset);
		int length = PIOUtils.redInteger4(buffer, offset + 1);
		String name = PIOUtils.redString(buffer, offset + 1 + 4, Charset.forName("utf-8"));
		String sql = PIOUtils.redString(buffer, offset + 1 + 4 + name.getBytes().length + 1, Charset.forName("utf-8"));

		int parmTypesLength = PIOUtils.redInteger2(buffer,
				offset + 1 + 4 + name.getBytes().length + 1 + sql.getBytes().length + 1);

		List<DateType> paramTypes = new ArrayList<>();

		int _offset = offset + 1 + 4 + name.getBytes().length + 1 + sql.getBytes().length + 1 + 2;
		for (int i = 0; i < parmTypesLength; i++) {
			paramTypes.add(DateType.valueOf(PIOUtils.redInteger4(buffer, _offset)));
			_offset = _offset + 4;
		}

		Parse parse = new Parse();
		parse.setName(name);
		parse.setParameterNumber((short) parmTypesLength);
		parse.setParameterTypes(paramTypes.toArray(new DateType[parmTypesLength]));
		parse.setSql(sql);
		pgs.add(parse);

		int leg = parse.getLength();
		char make = PIOUtils.redChar1(buffer, offset + (leg + 1));
		_offset = offset + leg + 1;
		if (make == 'B') {// 绑定包
			Charset utf8 = Charset.forName("utf-8");

			Bind bind = new Bind();
			length = PIOUtils.redInteger4(buffer, _offset + 1);
			String _name = PIOUtils.redString(buffer, _offset + 1 + 4, utf8);
			_offset = _offset + 1 + 4 + _name.getBytes().length + 1;
			String _sql = PIOUtils.redString(buffer, _offset, utf8);
			_offset = offset + _sql.getBytes().length + 1;
			short parameterProtocolNumber = PIOUtils.redInteger2(buffer, _offset);
			_offset = offset + 2;
			DataProtocol[] parameterProtocol = new DataProtocol[parameterProtocolNumber];
			for (int i = 0; i < parameterProtocolNumber; i++) {
				parameterProtocol[i] = DataProtocol.valueOf(PIOUtils.redInteger2(buffer, _offset));
				_offset += 2;
			}

			short parameterNumber = PIOUtils.redInteger2(buffer, _offset);
			DataParameter[] parameter = new DataParameter[parameterNumber];
			for (int i = 0; i < parameterNumber; i++) {
				parameter[i] = new DataParameter();
				parameter[i].setLength(PIOUtils.redInteger4(buffer, _offset));
				if (parameter[i].getLength() != -1) {
					parameter[i].setData(PIOUtils.redByteArray(buffer, _offset + 4, parameter[i].getLength()));
					_offset += (parameter[i].getLength() + 4);
				} else {
					offset += 4;
					parameter[i].setNull(true);
				}
			}
			short resultNumber = PIOUtils.redInteger2(buffer, _offset);
			_offset += 2;
			DataProtocol[] resultProtocol = new DataProtocol[resultNumber];
			for (int i = 0; i < resultNumber; i++) {
				resultProtocol[i] = DataProtocol.valueOf(PIOUtils.redInteger2(buffer, _offset));
				_offset += 2;
			}
			bind.setName(_name);
			bind.setSql(_sql);
			bind.setParameter(parameter);
			bind.setParameterNumber(parameterNumber);
			bind.setParameterProtocol(parameterProtocol);
			bind.setParameterProtocolNumber(parameterProtocolNumber);
			bind.setResultNumber(resultNumber);
			bind.setResultProtocol(resultProtocol);
			pgs.add(bind);

		}
		return pgs;
	}

	/**
	 * 解析密码包
	 * 
	 * @param buffer
	 * @param offset
	 * @param readLength
	 * @return
	 */
	private static PasswordMessage paersPasswordMessage(ByteBuffer buffer, int offset, int readLength) {
		PasswordMessage password = new PasswordMessage();
		char mark = PIOUtils.redChar1(buffer, offset);
		int length = PIOUtils.redInteger4(buffer, offset + 1);
		byte[] pass = PIOUtils.redByteArray(buffer, offset + 1 + 4, length - 4);
		password.setPassword(pass);
		return password;
	}

	/*************
	 * 解析 客户段发来的 启动包
	 * 
	 * @param buffer
	 * @param offset
	 * @param readLength
	 * @return
	 * @throws Exception
	 */
	private static StartupMessage paersStartUp(ByteBuffer buffer, int offset, int readLength) throws Exception {
		int length = PIOUtils.redInteger4(buffer, offset); // 包长度
		short protocol_major = PIOUtils.redInteger2(buffer, offset + 4);
		short protocol_minor = PIOUtils.redInteger2(buffer, offset + 4 + 2);
		int _offset = offset + 4 + 2 + 2;
		List<String> params = new ArrayList<>();
		while (_offset < (offset + length)) {
			String param = PIOUtils.redString(buffer, _offset, Charset.forName("utf-8"));
			_offset = _offset + (param.getBytes().length + 1);
			if (param != null) {
				params.add(param);
			}
		}
		StartupMessage message = new StartupMessage();
		message.setMajor(protocol_major);
		message.setMinor(protocol_minor);
		List<ParamsItme> paramsItmes = new ArrayList<>();
		ParamsItme item = null;
		for (int i = 0; i < params.size(); i++) {
			if (i % 2 == 0 && (i + 1 < params.size())) {

				item = new ParamsItme(params.get(i), params.get(i + 1));
				paramsItmes.add(item);
			}
		}
		message.setParams(paramsItmes);
		return message;
	}

	public static List<PostgreSQLPacket> parsePacket(ByteBuffer buffer, int offset, int readLength) throws IOException {
		final ByteBuffer bytes = buffer;
		List<PostgreSQLPacket> pgs = new ArrayList<>();
		while (offset < readLength) {
			char MAKE = (char) bytes.get(offset);
			PostgreSQLPacket pg = null;
			switch (MAKE) {
			case 'R':
				pg = AuthenticationPacket.parse(bytes, offset);
				break;
			case 'E':
				pg = ErrorResponse.parse(bytes, offset);
				break;
			case 'K':
				pg = BackendKeyData.parse(bytes, offset);
				break;
			case 'S':
				pg = ParameterStatus.parse(bytes, offset);
				break;
			case 'Z':
				pg = ReadyForQuery.parse(bytes, offset);
				break;
			case 'N':
				pg = NoticeResponse.parse(bytes, offset);
				break;
			case 'C':
				pg = CommandComplete.parse(bytes, offset);
				break;
			case 'T':
				pg = RowDescription.parse(bytes, offset);
				break;
			case 'D':
				pg = DataRow.parse(bytes, offset);
				break;

			case 'I':
				pg = EmptyQueryResponse.parse(bytes, offset);
				break;

			case 'G':
				pg = CopyInResponse.parse(bytes, offset);
				break;
			case 'H':
				pg = CopyOutResponse.parse(bytes, offset);
				break;
			case '1':
				pg = ParseComplete.parse(bytes, offset);
				break;
			default:
				throw new RuntimeException("Unknown packet");
			}
			if (pg != null) {
				offset = offset + pg.getLength() + 1;
				pgs.add(pg);
			}

		}
		return pgs;
	}

	@Deprecated
	private static List<PostgreSQLPacket> parsePacket(byte[] bytes, int offset, int readLength) throws IOException {
		List<PostgreSQLPacket> pgs = new ArrayList<>();
		while (offset < readLength) {
			char MAKE = (char) bytes[offset];
			PostgreSQLPacket pg = null;
			switch (MAKE) {
			case 'R':
				pg = AuthenticationPacket.parse(ByteBuffer.wrap(bytes), offset);
				break;
			case 'E':
				pg = ErrorResponse.parse(ByteBuffer.wrap(bytes), offset);
				break;
			case 'K':
				pg = BackendKeyData.parse(ByteBuffer.wrap(bytes), offset);
				break;
			case 'S':
				pg = ParameterStatus.parse(ByteBuffer.wrap(bytes), offset);
				break;
			case 'Z':
				pg = ReadyForQuery.parse(ByteBuffer.wrap(bytes), offset);
				break;
			case 'N':
				pg = NoticeResponse.parse(ByteBuffer.wrap(bytes), offset);
				break;
			case 'C':
				pg = CommandComplete.parse(ByteBuffer.wrap(bytes), offset);
				break;
			case 'T':
				pg = RowDescription.parse(ByteBuffer.wrap(bytes), offset);
				break;
			case 'D':
				pg = DataRow.parse(ByteBuffer.wrap(bytes), offset);
				break;

			case 'I':
				pg = EmptyQueryResponse.parse(ByteBuffer.wrap(bytes), offset);
				break;

			case 'G':
				pg = CopyInResponse.parse(ByteBuffer.wrap(bytes), offset);
				break;
			case 'H':
				pg = CopyOutResponse.parse(ByteBuffer.wrap(bytes), offset);
				break;
			case '1':
				pg = ParseComplete.parse(ByteBuffer.wrap(bytes), offset);
				break;
			default:
				throw new RuntimeException("Unknown packet");
			}
			if (pg != null) {
				offset = offset + pg.getLength() + 1;
				pgs.add(pg);
			}
		}

		return pgs;
	}

	/**
	 * Convert Java time zone to postgres time zone. All others stay the same
	 * except that GMT+nn changes to GMT-nn and vise versa.
	 * 
	 * @return The current JVM time zone in postgresql format.
	 */
	public static String createPostgresTimeZone() {
		String tz = TimeZone.getDefault().getID();
		if (tz.length() <= 3 || !tz.startsWith("GMT")) {
			return tz;
		}
		char sign = tz.charAt(3);
		String start;
		if (sign == '+') {
			start = "GMT-";
		} else if (sign == '-') {
			start = "GMT+";
		} else {
			// unknown type
			return tz;
		}
		return start + tz.substring(4);
	}

	public static ByteBuffer makeStartUpPacket(String user, String database) throws IOException {
		List<String[]> paramList = new ArrayList<String[]>();
		String appName = "MyCat-Server";
		paramList.add(new String[] { "user", user });
		paramList.add(new String[] { "database", database });
		paramList.add(new String[] { "client_encoding", "UTF8" });
		paramList.add(new String[] { "DateStyle", "ISO" });
		paramList.add(new String[] { "TimeZone", createPostgresTimeZone() });
		paramList.add(new String[] { "extra_float_digits", "3" });
		paramList.add(new String[] { "application_name", appName });
		String[][] params = paramList.toArray(new String[0][]);
		StringBuilder details = new StringBuilder();
		for (int i = 0; i < params.length; ++i) {
			if (i != 0) {
				details.append(", ");
			}
			details.append(params[i][0]);
			details.append("=");
			details.append(params[i][1]);
		}

		/*
		 * Precalculate message length and encode params.
		 */
		int length = 4 + 4;
		byte[][] encodedParams = new byte[params.length * 2][];
		for (int i = 0; i < params.length; ++i) {
			encodedParams[i * 2] = params[i][0].getBytes("UTF-8");
			encodedParams[i * 2 + 1] = params[i][1].getBytes("UTF-8");
			length += encodedParams[i * 2].length + 1 + encodedParams[i * 2 + 1].length + 1;
		}

		length += 1; // Terminating \0

		ByteBuffer buffer = ByteBuffer.allocate(length);

		/*
		 * Send the startup message.
		 */
		PIOUtils.SendInteger4(length, buffer);
		PIOUtils.SendInteger2(3, buffer); // protocol major
		PIOUtils.SendInteger2(0, buffer); // protocol minor
		for (byte[] encodedParam : encodedParams) {
			PIOUtils.Send(encodedParam, buffer);
			PIOUtils.SendChar(0, buffer);
		}
		PIOUtils.Send(new byte[] { 0 }, buffer);
		return buffer;
	}

}
