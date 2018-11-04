package cn.yayatao.vanitas.postgresql.datagram.server;

//		RowDescription (B)
//		Byte1('T')
//		标识消息是一个行描述
//		
//		Int32
//		以字节计的消息内容长度，包括长度本身。
//		
//		Int16
//		声明在一个行里面的字段数目(可以为零)
//		
//		然后对于每个字段，有下面的东西：
//		
//				String
//				字段名字
//				
//				Int32
//				如果字段可以标识为一个特定表的字段，那么就是表的对象 ID ；否则就是零。
//				
//				Int16
//				如果该字段可以标识为一个特定表的字段，那么就是该表字段的属性号；否则就是零。
//				
//				Int32
//				字段数据类型的对象 ID
//				
//				Int16
//				数据类型尺寸(参阅 pg_type.typlen)。请注意负数表示变宽类型。
//				
//				Int32
//				类型修饰词(参阅 pg_attribute.atttypmod)。修饰词的含义是类型相关的。
//				
//				Int16
//				用于该字段的格式码。目前会是零(文本)或者一(二进制)。从语句变种 Describe 返回的 RowDescription 里，格式码还是未知的，因此总是零。
public class RowDescription {

	private char mark = 'R';

	private int length = 4;

	private short columnsNumber;

	private ColumnDescription[] columns;
	
	

	// 然后对于每个字段，有下面的东西：
	//
	// String
	// 字段名字
	//
	// Int32
	// 如果字段可以标识为一个特定表的字段，那么就是表的对象 ID ；否则就是零。
	//
	// Int16
	// 如果该字段可以标识为一个特定表的字段，那么就是该表字段的属性号；否则就是零。
	//
	// Int32
	// 字段数据类型的对象 ID
	//
	// Int16
	// 数据类型尺寸(参阅 pg_type.typlen)。请注意负数表示变宽类型。
	//
	// Int32
	// 类型修饰词(参阅 pg_attribute.atttypmod)。修饰词的含义是类型相关的。
	//
	// Int16
	// 用于该字段的格式码。目前会是零(文本)或者一(二进制)。从语句变种 Describe 返回的 RowDescription
	// 里，格式码还是未知的，因此总是零。
	public static class ColumnDescription {
		
		private String name;

		private int tableOid;

		private short columnOid;
		
		private int typeOid;
		
		// pg_type.typlen
		private short typeLent;
		
		// pg_attribute.atttypmod
		private int attrTypeMod;
		
		// 0: text format 1: binary format
		private int columType ;
		

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public int getTableOid() {
			return tableOid;
		}

		public void setTableOid(int tableOid) {
			this.tableOid = tableOid;
		}

		public short getColumnOid() {
			return columnOid;
		}

		public void setColumnOid(short columnOid) {
			this.columnOid = columnOid;
		}

		public int getTypeOid() {
			return typeOid;
		}

		public void setTypeOid(int typeOid) {
			this.typeOid = typeOid;
		}

		public short getTypeLent() {
			return typeLent;
		}

		public void setTypeLent(short typeLent) {
			this.typeLent = typeLent;
		}

		public int getAttrTypeMod() {
			return attrTypeMod;
		}

		public void setAttrTypeMod(int attrTypeMod) {
			this.attrTypeMod = attrTypeMod;
		}

		public int getColumType() {
			return columType;
		}

		public void setColumType(int columType) {
			this.columType = columType;
		}

	}



	public char getMark() {
		return mark;
	}



	public void setMark(char mark) {
		this.mark = mark;
	}



	public int getLength() {
		return length;
	}



	public void setLength(int length) {
		this.length = length;
	}



	public short getColumnsNumber() {
		return columnsNumber;
	}



	public void setColumnsNumber(short columnsNumber) {
		this.columnsNumber = columnsNumber;
	}



	public ColumnDescription[] getColumns() {
		return columns;
	}



	public void setColumns(ColumnDescription[] columns) {
		this.columns = columns;
	}
}
