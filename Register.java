
public class Register {
	byte rb1, rb2, rb3;
	
	public Register(){
		rb1=0x0;
		rb2=0x0;
		rb3=0x0;
	}
	
	public Register(byte nb1,byte nb2,byte nb3){
		rb1 = nb1;
		rb2 = nb2;
		rb3 = nb3;
	}

	public byte getRb1() {
		return rb1;
	}

	public void setRb1(byte rb1) {
		this.rb1 = rb1;
	}

	public byte getRb2() {
		return rb2;
	}

	public void setRb2(byte rb2) {
		this.rb2 = rb2;
	}

	public byte getRb3() {
		return rb3;
	}

	public void setRb3(byte rb3) {
		this.rb3 = rb3;
	}
	
	public int getRow(){
		String output = "";
		output = Integer.toHexString(rb1) + Integer.toHexString(rb2);
		return Integer.parseUnsignedInt(output);
	}
	
	public int getColumn(){
		String output = "";
		output = Integer.toHexString(rb3);
		return Integer.parseUnsignedInt(output);
	}

	public byte[] getByteArray(){
		byte[] output = {rb1, rb2, rb3};
		return output;
	}
	
	public int getMemoryValue(){
		int output = Integer.parseUnsignedInt(ToString(), 16);
		return output;
	}
	
	public void parseString(String input){
		int length = input.length();
		switch(length){
			case 1: 
				setRb1((byte)0); 
				setRb2((byte)0); 
				setRb3((byte)Integer.parseUnsignedInt(input, 16)); 
				break;
			case 2:
				setRb1((byte)0); 
				setRb2((byte)Integer.parseUnsignedInt(input.substring(0, 1), 16));
				setRb3((byte)Integer.parseUnsignedInt(input.substring(1), 16));
				break;
			case 3: 
				setRb1((byte)Integer.parseUnsignedInt(input.substring(0, 1), 16));
				setRb2((byte)Integer.parseUnsignedInt(input.substring(1, 2), 16));
				setRb3((byte)Integer.parseUnsignedInt(input.substring(2), 16));
				break;
			default: break;
		}
	}
	
	public String ToString(){
		String output = "";
		output = Integer.toHexString(rb1) + Integer.toHexString(rb2) + Integer.toHexString(rb3);
		return output;
	}
}
