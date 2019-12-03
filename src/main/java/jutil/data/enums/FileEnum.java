package jutil.data.enums;

public enum FileEnum 
{
	
	UM_BYTE("8", 8),
	UM_KILOBYTE("1024", 1024),
	UM_MEGABYTE("1048576", 1048576),
	UM_GIGABYTE("1073741824", 1073741824),
	UM_TERABYTE("1099511627776", 1099511627776L),
	UM_PETABYTE("1125899906842624", 1125899906842624L),
	
	SULFIX_BYTE("b",0),
	SULFIX_KILOBYTE("Kb",0),
	SULFIX_MEGABYTE("Mb",0),
	SULFIX_GIGABYTE("Gb",0),
	SULFIX_TERABYTE("Tb",0),
	SULFIX_PETABYTE("Pb",0),
	
	ORDER_BY_FOLDER("0",0),
	ORDER_BY_FILES("1",1),
	
	EXTENSAO_ARQUIVO_SERIALIZADO (".ser",0)
	;
	
	String stringValue;
	long longValue;
	
	private FileEnum(String stringValue, long longValue) 
	{
		this.stringValue = stringValue;
		this.longValue = longValue;
	}

	public String getStringValue() {
		return stringValue;
	}

	public void setStringValue(String stringValue) {
		this.stringValue = stringValue;
	}

	public long getLongValue() {
		return longValue;
	}

	public void setLongValue(long longValue) {
		this.longValue = longValue;
	}
}
