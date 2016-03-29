public class DataMemoryPart {
	byte DB1,DB2,DB3,DB4;
	DataMemoryPart(){
		DB1 = 0x0;
		DB2 = 0x0;
		DB3 = 0x0;
		DB4 = 0x0;
	}
	
	DataMemoryPart(byte nDB1,byte nDB2,byte nDB3,byte nDB4){
		DB1 = nDB1;
		DB2 = nDB2;
		DB3 = nDB3;
		DB4 = nDB4;
	}

	public byte getDB1() {
		return DB1;
	}

	public void setDB1(byte DB1) {
		this.DB1 = DB1;
	}

	public byte getDB2() {
		return DB2;
	}

	public void setDB2(byte DB2) {
		this.DB2 = DB2;
	}

	public byte getDB3() {
		return DB3;
	}

	public void setDB3(byte DB3) {
		this.DB3 = DB3;
	}

	public byte getDB4() {
		return DB4;
	}

	public void setDB4(byte DB4) {
		this.DB4 = DB4;
	}
	
	public int getRow(){
		String output = "";
		output = Integer.toHexString(DB2) + Integer.toHexString(DB3);
		return Integer.parseUnsignedInt(output);
	}
	
	public int getColumn(){
		String output = "";
		output = Integer.toHexString(DB4);
		return Integer.parseUnsignedInt(output);
	}
	
	public byte[] getByteArray(){
		byte[] output = {DB1,DB2,DB3,DB4};
		return output;
	}
	
	public int getMemoryValue(){
		int output = Integer.parseUnsignedInt(ToString(), 16);
		return output;
	}
	
	public int getSignedValue(){
		int output = getMemoryValue();
		if(output > 32767){
			output -= 65536;
		}
		return output;
	}
	
	public void parseString(String input){
		int length = input.length();
		switch(length){
			case 0:
				break;
			case 1: 
				setDB1((byte)0);
				setDB2((byte)0);
				setDB3((byte)0);
				setDB4((byte)Integer.parseUnsignedInt(input, 16));
				break;
			case 2: 
				setDB1((byte)0);
				setDB2((byte)0);
				setDB3((byte)Integer.parseUnsignedInt(input.substring(0, 1), 16));
				setDB4((byte)Integer.parseUnsignedInt(input.substring(1), 16));
				break;
			case 3: 
				setDB1((byte)0);
				setDB2((byte)Integer.parseUnsignedInt(input.substring(0, 1), 16));
				setDB3((byte)Integer.parseUnsignedInt(input.substring(1, 2), 16));
				setDB4((byte)Integer.parseUnsignedInt(input.substring(2), 16));
				break;
			case 4: 
				setDB1((byte)Integer.parseUnsignedInt(input.substring(0, 1), 16));
				setDB2((byte)Integer.parseUnsignedInt(input.substring(1, 2), 16));
				setDB3((byte)Integer.parseUnsignedInt(input.substring(2, 3), 16));
				setDB4((byte)Integer.parseUnsignedInt(input.substring(3), 16));
				break;
			default: 
				setDB1((byte)Integer.parseUnsignedInt(input.substring(input.length()-4, input.length()-3), 16));
				setDB2((byte)Integer.parseUnsignedInt(input.substring(input.length()-3, input.length()-2), 16));
				setDB3((byte)Integer.parseUnsignedInt(input.substring(input.length()-2, input.length()-1), 16));
				setDB4((byte)Integer.parseUnsignedInt(input.substring(input.length()-1), 16));
				break;
		}
	}
	
	public String getAddress(){
		String output = "";
		output = Integer.toHexString(DB2) + Integer.toHexString(DB3) + Integer.toHexString(DB4);
		return output;
	}
	
	public String ToString(){
		String output = "";
		output = Integer.toHexString(DB1) + Integer.toHexString(DB2) + Integer.toHexString(DB3) + Integer.toHexString(DB4);
		return output;
	}
}