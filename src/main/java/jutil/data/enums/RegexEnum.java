package jutil.data.enums;

public enum RegexEnum 
{
	FIND_FILE_EXTENSION			("\\..{3,5}$"),
	REPLACE_ALL_BRACKETS 		("(\\[|\\])"),
    FIND_INI_SECTION          	("^\\[.*\\]"),
    FIND_INI_PROPERTIES       	("^(?!;).*\\=.*"),
    CHECK_IP_NUMBER				("(([0-1]?[0-9]{1,2}\\.)|(2[0-4][0-9]\\.)|(25[0-5]\\.)){3}(([0-1]?[0-9]{1,2})|(2[0-4][0-9])|(25[0-5]))"),
    FIND_NON_DIGIT    	("\\D"),
    ONLY_NUMBERS       	("^\\d+$"),
    TRIM_SPACES_RIGHT         	("\\s+$"),
    TRIM_SPACES_LEFT          	("^\\s+"),
    REPLACE_ESPACOS_DUPLICADOS 	("\\s+")
    ;

	String stringValue;

	private RegexEnum(String stringValue) {
		this.stringValue = stringValue;
	}

	public String getStringValue() {
		return stringValue;
	}

	public void setStringValue(String stringValue) {
		this.stringValue = stringValue;
	}
}
