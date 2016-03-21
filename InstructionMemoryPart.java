public class InstructionMemoryPart {
	byte IB1,IB2,IB3,IB4,IB5;
	InstructionMemoryPart(){
		IB1 = 0x0;
		IB2 = 0x0;
		IB3 = 0x0;
		IB4 = 0x0;
		IB5 = 0x0;
	}
	
	InstructionMemoryPart(byte nib1,byte nib2,byte nib3,byte nib4,byte nib5){
		IB1 = nib1;
		IB2 = nib2;
		IB3 = nib3;
		IB4 = nib4;
		IB5 = nib5;
	}

	public byte getIB1() {
		return IB1;
	}

	public void setIB1(byte IB1) {
		if(IB1 > (byte)0x7){
			throw new InternalError();
		}
		this.IB1 = IB1;
	}

	public byte getIB2() {
		return IB2;
	}

	public void setIB2(byte IB2) {
		this.IB2 = IB2;
	}

	public byte getIB3() {
		return IB3;
	}

	public void setIB3(byte IB3) {
		if(IB3 > (byte)0x7){
			throw new InternalError();
		}
		this.IB3 = IB3;
	}

	public byte getIB4() {
		return IB4;
	}

	public void setIB4(byte IB4) {
		this.IB4 = IB4;
	}

	public byte getIB5() {
		return IB5;
	}

	public void setIB5(byte IB5) {
		this.IB5 = IB5;
	}
	
	public byte[] getByteArray(){
		byte[] output = {IB1,IB2,IB3,IB4,IB5};
		return output;
	}
	
	public int getMemoryValue(){
		int output = Integer.parseUnsignedInt(ToString(), 16);
		return output;
	}
	
	public int getSignedAddress(){
		int output = Integer.parseUnsignedInt((Integer.toHexString(getIB3()) + Integer.toHexString(getIB4()) + Integer.toHexString(getIB5())), 16);
		if(output > 1023){
			output -= 2048;
		}
		return output;
	}
	
	public void parseString(String input){
		int length = input.length();
		switch(length){
			case 1: 
				setIB1((byte)0); 
				setIB2((byte)0); 
				setIB3((byte)0); 
				setIB4((byte)0); 
				setIB5((byte)Integer.parseUnsignedInt(input, 16)); 
				break;
			case 2: 
				setIB1((byte)0); 
				setIB2((byte)0); 
				setIB3((byte)0); 
				setIB4((byte)Integer.parseUnsignedInt(input.substring(0, 1), 16));
				setIB5((byte)Integer.parseUnsignedInt(input.substring(1), 16));
				break;
			case 3: 
				setIB1((byte)0); 
				setIB2((byte)0); 
				setIB3((byte)Integer.parseUnsignedInt(input.substring(0, 1), 16));
				setIB4((byte)Integer.parseUnsignedInt(input.substring(1, 2), 16));
				setIB5((byte)Integer.parseUnsignedInt(input.substring(2), 16));
				break;
			case 4: 
				setIB1((byte)0); 
				setIB2((byte)Integer.parseUnsignedInt(input.substring(0, 1), 16));
				setIB3((byte)Integer.parseUnsignedInt(input.substring(1, 2), 16));
				setIB4((byte)Integer.parseUnsignedInt(input.substring(2, 3), 16));
				setIB5((byte)Integer.parseUnsignedInt(input.substring(3), 16));
				break;
			case 5: 
				setIB1((byte)Integer.parseUnsignedInt(input.substring(0, 1), 16));
				setIB2((byte)Integer.parseUnsignedInt(input.substring(1, 2), 16));
				setIB3((byte)Integer.parseUnsignedInt(input.substring(2, 3), 16));
				setIB4((byte)Integer.parseUnsignedInt(input.substring(3, 4), 16));
				setIB5((byte)Integer.parseUnsignedInt(input.substring(4), 16));
				break;
			default: break;
		}
	}

	public String getAddress(){
		String output = "";
		output = Integer.toHexString(IB3) + Integer.toHexString(IB4) + Integer.toHexString(IB5);
		return output;
	}
	
	public String getOpCode(){
		String output = "";
		output = Integer.toHexString(IB4) + Integer.toHexString(IB5);
		return output;
	}
	
	public String ToString(){
		String output = "";
		output = Integer.toHexString(IB1) + Integer.toHexString(IB2) + Integer.toHexString(IB3) + Integer.toHexString(IB4) + Integer.toHexString(IB5);
		return output;
	}
}