import java.util.Scanner;
import java.util.Vector;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class ShazamUI {
	private static Vector<String> inputs = new Vector<String>();
	private static InstructionMemoryPart[][] instructionMemory = new InstructionMemoryPart[0x40][0xF+1];
	private static DataMemoryPart[][] dataMemory = new DataMemoryPart[0x80][0xF+1];
	private static InstructionMemoryPart ir = new InstructionMemoryPart();
	private static Register p = new Register();
	private static Register b = new Register();
	private static Register t = new Register();
	private static DataMemoryPart r1 = new DataMemoryPart();
	private static DataMemoryPart r2 = new DataMemoryPart();
	private static DataMemoryPart r3 = new DataMemoryPart();
	private static DataMemoryPart r4 = new DataMemoryPart();
	private static DataMemoryPart r5 = new DataMemoryPart();
	
	//Start all the things!
	//Still gotta find a space for Tucker and Dale reference...
	public ShazamUI(){
		clear();
		String choice = "";
		Scanner userIn = new Scanner(System.in);
		
		//Basically run all the things
		while(choice.compareTo("exit") != 0){
			printMenu();
			choice = userIn.next().toLowerCase();
			
			switch(choice){
				case "clear": 
					clear();
					System.out.println("Memory Cleared");
					break;
				case "dump": 
					dump();
					System.out.println("Memory Dumped");
					break;
				case "load": 
					load();
					System.out.println("Memory Loaded");
					break;
				case "run": 
					run();
					break;
				case "exit": 
					break;
				default: break;
			}
		}
		
		userIn.close();
	}
	
	//Print all the things!
	public void printMenu(){
		System.out.println("Type your choice and hit enter\n" +
				"Clear\n" +
				"Dump\n" +
				"Load\n" +
				"Run\n" +
				"Exit");
	}
	
	//Clear all the things!
	public void clear(){
		b = new Register();
		p = new Register();
		t = new Register((byte)0x0,(byte)0x0,(byte)0x2);
		r1.parseString("0000");
		r2.parseString("0000");
		r3.parseString("0000");
		r4.parseString("0000");
		r5.parseString("0000");
		ir.parseString("00000");
		//clear data
		for(int i = 0x0; i < dataMemory.length; i++){
			for(int j = 0x0; j < dataMemory[i].length; j++){
				dataMemory[i][j] = new DataMemoryPart((byte)0x0,(byte)0x0,(byte)0x0,(byte)0x0);
			}
		}
		
		//clear instructions
		for(int i = 0x0; i < instructionMemory.length; i++){
			for(int j = 0x0; j < instructionMemory[i].length; j++){
				instructionMemory[i][j] = new InstructionMemoryPart((byte)0x6,(byte)0x3,(byte)0x0,(byte)0x0,(byte)0x0);
			}
		}
		
	}
	
	//Dump all the things!
	public void dump(){
		//write memory to output.txt
		LocalDateTime time = LocalDateTime.now();
		ZoneId zoneId = ZoneId.systemDefault();
		PrintWriter writer;
		
		try {
			writer = new PrintWriter("Dump"+time.atZone(zoneId).toEpochSecond()+time.getNano()+".txt", "UTF-8");
			//start writing
			writer.println("Memory Dump--Data Memory\tPage 1");
			
			//print data
			for(int i = 0x0; i < dataMemory.length; i++){
				//print address
				if(i<0x10){
					writer.print("0");
				}
				writer.print(Integer.toHexString(i).toUpperCase()+"0 ");
				
				//loop through data
				for(int j = 0x0; j < dataMemory[i].length; j++){
					writer.print(dataMemory[i][j].ToString().toUpperCase().toUpperCase() + " ");
				}
				
				//print page
				writer.print("\r\n");
				if(i==0x48){
					writer.println("Memory Dump--Data Memory\tPage 2");
				}
			}
			
			//print endings
			writer.println("End Memory Dump--Data Memory\r\n");
			writer.print("Memory Dump--Instruction Memory   ");
			writer.print("B = " + b.ToString().toUpperCase() + " T = " + t.ToString().toUpperCase() + " P = " + p.ToString().toUpperCase() + "\r\n");
			writer.println("Memory Dump--Instruction Memory\t\tPage 1");
			
			//print instructions
			for(int i = 0x0; i < instructionMemory.length; i++){
				//print address
				if(i<0x10){
					writer.print("0");
				}
				writer.print(Integer.toHexString(i).toUpperCase()+"0 ");
				
				//loop through data
				for(int j = 0x0; j < instructionMemory[i].length; j++){
					writer.print(instructionMemory[i][j].ToString().toUpperCase().toUpperCase() + " ");
				}
				
				//print page
				writer.print("\r\n");
				if(i==0x0D){
					writer.println("Memory Dump--Instruction Memory\t\tPage 2");
				}
			}
			//print ending
			writer.print("End Memory Dump--Instruction Memory");
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	//Load all the things!
	public void load(){
		File input = null;
		Scanner fileIn = null;
		try{
			input = new File("Load.in.txt");
			fileIn = new Scanner(input);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		//read instructions
		while(fileIn.hasNextLine()){
			String thisLine = fileIn.nextLine();
			String[] insLine = thisLine.split(" ");
			String fieldL = "";
			String fieldN = "";
			String fieldNC = "";
			String fullLocation = "";
			int location = 0;
			int subLocation = 0;
			int numInstructions = 0;
			int newLocation = 0;
			int newSubLocation = 0;
			
			switch (insLine[0]){
				//parse instruction loading
				case "I": 
					fieldL = insLine[1];
					fieldN = insLine[2];
					if(insLine.length == 4){
						fieldNC = insLine[3];
					}
					
					location = Integer.parseUnsignedInt(fieldL.substring(0, 2),16);
					subLocation = Integer.parseUnsignedInt(fieldL.substring(2),16);
					
					numInstructions = Integer.parseUnsignedInt(fieldN, 16);
					
					if(numInstructions > 0xE){
						throw new IndexOutOfBoundsException();
					}
					else if(numInstructions == 0){
						p.parseString(fieldL);
					}
					else{
						for(int i = 0; i <= numInstructions; i++){
							
							fullLocation = Integer.toHexString(Integer.parseUnsignedInt((Integer.toHexString(location) + Integer.toHexString(subLocation)), 16) + i);
							if(fullLocation.length() == 1){
								fullLocation = "00" + fullLocation;
							}
							if(fullLocation.length() == 2){
								fullLocation = "0" + fullLocation;
							}
							
							newLocation = Integer.parseUnsignedInt(fullLocation.substring(0, 2),16);
							newSubLocation = Integer.parseUnsignedInt(fullLocation.substring(2),16);
							
							if((i*5)+5 < fieldNC.length()-1){
								instructionMemory[newLocation][newSubLocation].parseString(fieldNC.substring(i*5, (i*5)+5));
							}
							else{
								instructionMemory[newLocation][newSubLocation].parseString(fieldNC.substring(i*5));
							}
						}
					}
					break;
					
					//parse data loading
				case "D": 
					fieldL = insLine[1];

					if(insLine[2].length() > 2){
						fieldN = insLine[2].substring(0, 2);
						fieldNC = insLine[2].substring(2);
					}
					else{
						fieldN = insLine[2];
					}
					
					location = Integer.parseUnsignedInt(fieldL.substring(0, 2),16);
					subLocation = Integer.parseUnsignedInt(fieldL.substring(2),16);
					
					numInstructions = Integer.parseUnsignedInt(fieldN, 16);
					
					if(numInstructions > 0xE){
						throw new IndexOutOfBoundsException();
					}
					else if(numInstructions == 0){
						t.parseString(fieldL);
					}
					else{
						for(int i = 0; i <= numInstructions; i++){
							if((i*4)+4 < fieldNC.length()-1){
								dataMemory[location][subLocation + i].parseString(fieldNC.substring(i*4, (i*4)+4));
							}
							else{
								dataMemory[location][subLocation + i].parseString(fieldNC.substring(i*4));
							}
						}
					}
					break;
					
					//dump the rest into input
				default: 
					inputs.add(thisLine); 
					break;
			}
		}
		fileIn.close();
	}

	public void run(){
		LocalDateTime time = LocalDateTime.now();
		ZoneId zoneId = ZoneId.systemDefault();
		PrintWriter writer;
		String resultState = "";
		String resultAction = "";
		
		try {
			writer = new PrintWriter("Trace"+time.atZone(zoneId).toEpochSecond()+time.getNano()+".txt", "UTF-8");
			//start writing\
			
			//Register effAddr = new Register();
			boolean done = false;
			//print trace out
			System.out.print("Interpreter --- Begin at location " + p.ToString().toUpperCase() + "  B = " + b.ToString().toUpperCase() + "  T = " + t.ToString().toUpperCase() + "\r\n"
					+ "Trace is... On\r\n");
			//while !done
			while(!done){
				resultAction = "";
				//write instruction number
				System.out.print(p.ToString().toUpperCase() + ": ");
				//IR = instructionMemory[p]
				ir = instructionMemory[p.getRow()][p.getColumn()];
				//p++
				p.parseString(Integer.toHexString(p.getMemoryValue()+1));
				//parse code
				switch(ir.getIB1()){
					case (byte)0x0: //LIT
						//push address signed int onto stack
						//put data on the stack
						t.parseString(Integer.toHexString(t.getMemoryValue()+1));
						dataMemory[t.getRow()][t.getColumn()].parseString(ir.getAddress());
						resultAction += ("DATA(" + t.ToString().toUpperCase() + ") <-- " + dataMemory[t.getRow()][t.getColumn()].ToString().toUpperCase() + " ");
						break;
					case (byte)0x1: //OPR
						//opr chart thing in interprate pdf
						switch(ir.getOpCode().toUpperCase()){
							case "00"://RET
								//p of calling routine
								//pop top of stack
								r1.parseString(dataMemory[t.getRow()][t.getColumn()].ToString().toUpperCase());
								t.parseString(Integer.toHexString(t.getMemoryValue()-1));
								//b of calling routine
								//pop top of stack
								r2.parseString(dataMemory[t.getRow()][t.getColumn()].ToString().toUpperCase());
								t.parseString(Integer.toHexString(t.getMemoryValue()-1));
								//b of calling routine 
								//pop top of stack
								r3.parseString(dataMemory[t.getRow()][t.getColumn()].ToString().toUpperCase());
								t.parseString(Integer.toHexString(t.getMemoryValue()-1));
								
								//set addresses for the return
								b.parseString(r3.getAddress());
								p.parseString(r1.getAddress());
								break;
							case "01"://NEG
								//pop top of the stack, make number negative
								//pop top of stack
								r1.parseString(dataMemory[t.getRow()][t.getColumn()].ToString().toUpperCase());
								t.parseString(Integer.toHexString(t.getMemoryValue()-1));
								//do math magic
								r1.parseString(Integer.toBinaryString(r1.getSignedValue() * -1));
								
								//push result to the stack
								t.parseString(Integer.toHexString(t.getMemoryValue()+1));
								dataMemory[t.getRow()][t.getColumn()].parseString(r1.ToString());
								resultAction += ("DATA(" + t.ToString().toUpperCase() + ") <-- " + dataMemory[t.getRow()][t.getColumn()].ToString().toUpperCase() + " ");
								break;
							case "02"://ADD
								//pop top of stack
								r1.parseString(dataMemory[t.getRow()][t.getColumn()].ToString().toUpperCase());
								t.parseString(Integer.toHexString(t.getMemoryValue()-1));
								//pop top of stack
								r2.parseString(dataMemory[t.getRow()][t.getColumn()].ToString().toUpperCase());
								t.parseString(Integer.toHexString(t.getMemoryValue()-1));
								//push result of r1 + r2 to the stack
								t.parseString(Integer.toHexString(t.getMemoryValue()+1));
								dataMemory[t.getRow()][t.getColumn()].parseString(Integer.toHexString(r1.getSignedValue() + r2.getSignedValue()));
								resultAction += ("DATA(" + t.ToString().toUpperCase() + ") <-- " + dataMemory[t.getRow()][t.getColumn()].ToString().toUpperCase() + " ");
								break;
							case "03"://SUB
								//pop top of stack
								r1.parseString(dataMemory[t.getRow()][t.getColumn()].ToString().toUpperCase());
								t.parseString(Integer.toHexString(t.getMemoryValue()-1));
								//pop top of stack
								r2.parseString(dataMemory[t.getRow()][t.getColumn()].ToString().toUpperCase());
								t.parseString(Integer.toHexString(t.getMemoryValue()-1));
								//push result of r1 - r2 to the stack
								t.parseString(Integer.toHexString(t.getMemoryValue()+1));
								dataMemory[t.getRow()][t.getColumn()].parseString(Integer.toHexString(r2.getSignedValue() - r1.getSignedValue()));
								resultAction += ("DATA(" + t.ToString().toUpperCase() + ") <-- " + dataMemory[t.getRow()][t.getColumn()].ToString().toUpperCase() + " ");
								break;
							case "04"://MUL
								//pop top of stack
								r1.parseString(dataMemory[t.getRow()][t.getColumn()].ToString().toUpperCase());
								t.parseString(Integer.toHexString(t.getMemoryValue()-1));
								//pop top of stack
								r2.parseString(dataMemory[t.getRow()][t.getColumn()].ToString().toUpperCase());
								t.parseString(Integer.toHexString(t.getMemoryValue()-1));
								//push result of r1 * r2 to the stack
								t.parseString(Integer.toHexString(t.getMemoryValue()+1));
								dataMemory[t.getRow()][t.getColumn()].parseString(Integer.toHexString(r1.getSignedValue() * r2.getSignedValue()));
								resultAction += ("DATA(" + t.ToString().toUpperCase() + ") <-- " + dataMemory[t.getRow()][t.getColumn()].ToString().toUpperCase() + " ");
								break;
							case "05"://DIV
								//pop top of stack
								r1.parseString(dataMemory[t.getRow()][t.getColumn()].ToString().toUpperCase());
								t.parseString(Integer.toHexString(t.getMemoryValue()-1));
								//pop top of stack
								r2.parseString(dataMemory[t.getRow()][t.getColumn()].ToString().toUpperCase());
								t.parseString(Integer.toHexString(t.getMemoryValue()-1));
								//push result of r1 / r2 to the stack
								t.parseString(Integer.toHexString(t.getMemoryValue()+1));
								dataMemory[t.getRow()][t.getColumn()].parseString(Integer.toHexString(r2.getSignedValue() / r1.getSignedValue()));
								resultAction += ("DATA(" + t.ToString().toUpperCase() + ") <-- " + dataMemory[t.getRow()][t.getColumn()].ToString().toUpperCase() + " ");
								break;
							case "06"://DUP
								//pop top of stack
								r1.parseString(dataMemory[t.getRow()][t.getColumn()].ToString().toUpperCase());
								t.parseString(Integer.toHexString(t.getMemoryValue()-1));
								//push r1 onto stack
								t.parseString(Integer.toHexString(t.getMemoryValue()+1));
								dataMemory[t.getRow()][t.getColumn()].parseString(r1.ToString().toUpperCase());
								resultAction += ("DATA(" + t.ToString().toUpperCase() + ") <-- " + dataMemory[t.getRow()][t.getColumn()].ToString().toUpperCase() + " ");
								//push another r1 onto stack
								t.parseString(Integer.toHexString(t.getMemoryValue()+1));
								dataMemory[t.getRow()][t.getColumn()].parseString(r1.ToString().toUpperCase());
								resultAction += ("DATA(" + t.ToString().toUpperCase() + ") <-- " + dataMemory[t.getRow()][t.getColumn()].ToString().toUpperCase() + " ");
								break;
							case "07"://EQL
								//pop top of stack
								r1.parseString(dataMemory[t.getRow()][t.getColumn()].ToString().toUpperCase());
								t.parseString(Integer.toHexString(t.getMemoryValue()-1));
								//pop top of stack
								r2.parseString(dataMemory[t.getRow()][t.getColumn()].ToString().toUpperCase());
								t.parseString(Integer.toHexString(t.getMemoryValue()-1));
								//push result of r1 == r2 to the stack
								if(r1.getMemoryValue() == r2.getMemoryValue()){
									t.parseString(Integer.toHexString(t.getMemoryValue()+1));
									dataMemory[t.getRow()][t.getColumn()].parseString("1");
									resultAction += ("DATA(" + t.ToString().toUpperCase() + ") <-- " + dataMemory[t.getRow()][t.getColumn()].ToString().toUpperCase() + " ");
								}
								else{
									t.parseString(Integer.toHexString(t.getMemoryValue()+1));
									dataMemory[t.getRow()][t.getColumn()].parseString("0");
									resultAction += ("DATA(" + t.ToString().toUpperCase() + ") <-- " + dataMemory[t.getRow()][t.getColumn()].ToString().toUpperCase() + " ");
								}
								break;
							case "08"://NEQ
								//pop top of stack
								r1.parseString(dataMemory[t.getRow()][t.getColumn()].ToString().toUpperCase());
								t.parseString(Integer.toHexString(t.getMemoryValue()-1));
								//pop top of stack
								r2.parseString(dataMemory[t.getRow()][t.getColumn()].ToString().toUpperCase());
								t.parseString(Integer.toHexString(t.getMemoryValue()-1));
								//push result of r1 == r2 to the stack
								if(r1.getMemoryValue() != r2.getMemoryValue()){
									t.parseString(Integer.toHexString(t.getMemoryValue()+1));
									dataMemory[t.getRow()][t.getColumn()].parseString("1");
									resultAction += ("DATA(" + t.ToString().toUpperCase() + ") <-- " + dataMemory[t.getRow()][t.getColumn()].ToString().toUpperCase() + " ");
								}
								else{
									t.parseString(Integer.toHexString(t.getMemoryValue()+1));
									dataMemory[t.getRow()][t.getColumn()].parseString("0");
									resultAction += ("DATA(" + t.ToString().toUpperCase() + ") <-- " + dataMemory[t.getRow()][t.getColumn()].ToString().toUpperCase() + " ");
								}
								break;
							case "09"://LSS
								//pop top of stack
								r1.parseString(dataMemory[t.getRow()][t.getColumn()].ToString().toUpperCase());
								t.parseString(Integer.toHexString(t.getMemoryValue()-1));
								//pop top of stack
								r2.parseString(dataMemory[t.getRow()][t.getColumn()].ToString().toUpperCase());
								t.parseString(Integer.toHexString(t.getMemoryValue()-1));
								//push result of r1 == r2 to the stack
								if(r1.getMemoryValue() > r2.getMemoryValue()){
									t.parseString(Integer.toHexString(t.getMemoryValue()+1));
									dataMemory[t.getRow()][t.getColumn()].parseString("1");
									resultAction += ("DATA(" + t.ToString().toUpperCase() + ") <-- " + dataMemory[t.getRow()][t.getColumn()].ToString().toUpperCase() + " ");
								}
								else{
									t.parseString(Integer.toHexString(t.getMemoryValue()+1));
									dataMemory[t.getRow()][t.getColumn()].parseString("0");
									resultAction += ("DATA(" + t.ToString().toUpperCase() + ") <-- " + dataMemory[t.getRow()][t.getColumn()].ToString().toUpperCase() + " ");
								}
								break;
							case "0A"://LEQ
								//pop top of stack
								r1.parseString(dataMemory[t.getRow()][t.getColumn()].ToString().toUpperCase());
								t.parseString(Integer.toHexString(t.getMemoryValue()-1));
								//pop top of stack
								r2.parseString(dataMemory[t.getRow()][t.getColumn()].ToString().toUpperCase());
								t.parseString(Integer.toHexString(t.getMemoryValue()-1));
								//push result of r1 == r2 to the stack
								if(r1.getMemoryValue() >= r2.getMemoryValue()){
									t.parseString(Integer.toHexString(t.getMemoryValue()+1));
									dataMemory[t.getRow()][t.getColumn()].parseString("1");
									resultAction += ("DATA(" + t.ToString().toUpperCase() + ") <-- " + dataMemory[t.getRow()][t.getColumn()].ToString().toUpperCase() + " ");
								}
								else{
									t.parseString(Integer.toHexString(t.getMemoryValue()+1));
									dataMemory[t.getRow()][t.getColumn()].parseString("0");
									resultAction += ("DATA(" + t.ToString().toUpperCase() + ") <-- " + dataMemory[t.getRow()][t.getColumn()].ToString().toUpperCase() + " ");
								}
								break;
							case "0B"://GEQ
								//pop top of stack
								r1.parseString(dataMemory[t.getRow()][t.getColumn()].ToString().toUpperCase());
								t.parseString(Integer.toHexString(t.getMemoryValue()-1));
								//pop top of stack
								r2.parseString(dataMemory[t.getRow()][t.getColumn()].ToString().toUpperCase());
								t.parseString(Integer.toHexString(t.getMemoryValue()-1));
								//push result of r1 == r2 to the stack
								if(r1.getMemoryValue() <= r2.getMemoryValue()){
									t.parseString(Integer.toHexString(t.getMemoryValue()+1));
									dataMemory[t.getRow()][t.getColumn()].parseString("1");
									resultAction += ("DATA(" + t.ToString().toUpperCase() + ") <-- " + dataMemory[t.getRow()][t.getColumn()].ToString().toUpperCase() + " ");
								}
								else{
									t.parseString(Integer.toHexString(t.getMemoryValue()+1));
									dataMemory[t.getRow()][t.getColumn()].parseString("0");
									resultAction += ("DATA(" + t.ToString().toUpperCase() + ") <-- " + dataMemory[t.getRow()][t.getColumn()].ToString().toUpperCase() + " ");
								}
								break;
							case "0C"://GTR
								//pop top of stack
								r1.parseString(dataMemory[t.getRow()][t.getColumn()].ToString().toUpperCase());
								t.parseString(Integer.toHexString(t.getMemoryValue()-1));
								//pop top of stack
								r2.parseString(dataMemory[t.getRow()][t.getColumn()].ToString().toUpperCase());
								t.parseString(Integer.toHexString(t.getMemoryValue()-1));
								//push result of r1 == r2 to the stack
								if(r1.getMemoryValue() < r2.getMemoryValue()){
									t.parseString(Integer.toHexString(t.getMemoryValue()+1));
									dataMemory[t.getRow()][t.getColumn()].parseString("1");
									resultAction += ("DATA(" + t.ToString().toUpperCase() + ") <-- " + dataMemory[t.getRow()][t.getColumn()].ToString().toUpperCase() + " ");
								}
								else{
									t.parseString(Integer.toHexString(t.getMemoryValue()+1));
									dataMemory[t.getRow()][t.getColumn()].parseString("0");
									resultAction += ("DATA(" + t.ToString().toUpperCase() + ") <-- " + dataMemory[t.getRow()][t.getColumn()].ToString().toUpperCase() + " ");
								}
								break;
							case "0D"://GET
									t.parseString(Integer.toHexString(t.getMemoryValue()+1));
									
									if(inputs.get(0).length()>4){
										dataMemory[t.getRow()][t.getColumn()].parseString(inputs.get(0).substring(0, 4));
										inputs.remove(0);
										resultAction += ("DATA(" + t.ToString().toUpperCase() + ") <-- " + dataMemory[t.getRow()][t.getColumn()].ToString().toUpperCase() + " ");
									}
									else{
										dataMemory[t.getRow()][t.getColumn()].parseString(inputs.get(0));
										inputs.remove(0);
										resultAction += ("DATA(" + t.ToString().toUpperCase() + ") <-- " + dataMemory[t.getRow()][t.getColumn()].ToString().toUpperCase() + " ");
									}
								break;
							case "0E"://PUT
								//pop top of stack
								r1.parseString(dataMemory[t.getRow()][t.getColumn()].ToString().toUpperCase());
								t.parseString(Integer.toHexString(t.getMemoryValue()-1));
								resultAction += ("\r\nPut ------> " + r1.ToString().toUpperCase());
								break;
							case "0F"://LDA
								//pop top of stack
								r1.parseString(dataMemory[t.getRow()][t.getColumn()].ToString().toUpperCase());
								t.parseString(Integer.toHexString(t.getMemoryValue()-1));
								//put data on the stack
								t.parseString(Integer.toHexString(t.getMemoryValue()+1));
								dataMemory[t.getRow()][t.getColumn()].parseString(dataMemory[r1.getRow()][r1.getColumn()].ToString().toUpperCase());
								resultAction += ("DATA(" + t.ToString().toUpperCase() + ") <-- " + dataMemory[t.getRow()][t.getColumn()].ToString().toUpperCase() + " ");
								break;
							case "10"://STA
								break;
						}
						break;
					case (byte)0x2: //LOD
						switch(ir.getIB2()){
							case (byte)0x0: //curent base +/- address = eff.addr.
								//push value at eff.addr. onto stack 
								//create the effective address
								r1.parseString( Integer.toHexString(b.getMemoryValue() + ir.getSignedAddress()) );
								//put data on the stack
								t.parseString(Integer.toHexString(t.getMemoryValue()+1));
								dataMemory[t.getRow()][t.getColumn()].parseString(dataMemory[r1.getRow()][r1.getColumn()].ToString().toUpperCase());
								resultAction += ("DATA(" + t.ToString().toUpperCase() + ") <-- " + dataMemory[t.getRow()][t.getColumn()].ToString().toUpperCase() + " ");
								break;
							case (byte)0x1: //caller's base +/- address = eff.addr.
								//push value at eff.addr. onto stack 
								//get current base address
								r1.parseString( Integer.toHexString(b.getMemoryValue()) );
								//get the previous base address from the current base address, use that value to create the effective address
								r2.parseString(Integer.toHexString(Integer.parseUnsignedInt(dataMemory[r1.getRow()][r1.getColumn()].getAddress(), 16) + ir.getSignedAddress()) );
								//put data on the stack
								t.parseString(Integer.toHexString(t.getMemoryValue()+1));
								dataMemory[t.getRow()][t.getColumn()].parseString(dataMemory[r2.getRow()][r2.getColumn()].ToString().toUpperCase());
								resultAction += ("DATA(" + t.ToString().toUpperCase() + ") <-- " + dataMemory[t.getRow()][t.getColumn()].ToString().toUpperCase() + " ");
								break;
							case (byte)0x2: //caller's caller's base +/- address = eff.addr.
								//push value at eff.addr. onto stack 
								//get current base address
								r1.parseString( Integer.toHexString(b.getMemoryValue()) );
								//get the previous base address from the current base address
								r2.parseString( Integer.toHexString(Integer.parseUnsignedInt(dataMemory[r1.getRow()][r1.getColumn()].getAddress(), 16)) );
								//use the value at the previous base to get the previous... previous base and use it to create the effective address
								r3.parseString(Integer.toHexString(Integer.parseUnsignedInt(dataMemory[r2.getRow()][r2.getColumn()].getAddress(), 16) + ir.getSignedAddress()) );
								//put data on the stack
								t.parseString(Integer.toHexString(t.getMemoryValue()+1));
								dataMemory[t.getRow()][t.getColumn()].parseString(dataMemory[r3.getRow()][r3.getColumn()].ToString().toUpperCase());
								resultAction += ("DATA(" + t.ToString().toUpperCase() + ") <-- " + dataMemory[t.getRow()][t.getColumn()].ToString().toUpperCase() + " ");
								break;
						}
						break;
					case (byte)0x3: //STO
						switch(ir.getIB2()){
							case (byte)0x0: //curent base +/- address = eff.addr.
								//pop top of stack, put at eff.addr.
								//create the effictive address
								r1.parseString( Integer.toHexString(b.getMemoryValue() + ir.getSignedAddress()) );
								//put data from the top of stack at the effective address
								dataMemory[r1.getRow()][r1.getColumn()].parseString(dataMemory[t.getRow()][t.getColumn()].ToString().toUpperCase());
								t.parseString(Integer.toHexString(t.getMemoryValue()-1));
								resultAction += ("DATA(" + r1.getAddress() + ") <-- " + dataMemory[r1.getRow()][r1.getColumn()].ToString().toUpperCase() + " ");
								break;
							case (byte)0x1: //caller's base +/- address = eff.addr.
								//pop top of stack, put at eff.addr.
								//get current base address
								r1.parseString( Integer.toHexString(b.getMemoryValue()) );
								//get the previous base address from the current base address, use that value to create the effective address
								r2.parseString(Integer.toHexString(Integer.parseUnsignedInt(dataMemory[r1.getRow()][r1.getColumn()].getAddress(), 16) + ir.getSignedAddress()) );
								//put data from the top of stack at the effective address
								dataMemory[r2.getRow()][r2.getColumn()].parseString(dataMemory[t.getRow()][t.getColumn()].ToString().toUpperCase());
								t.parseString(Integer.toHexString(t.getMemoryValue()-1));
								resultAction += ("DATA(" + r2.getAddress() + ") <-- " + dataMemory[r2.getRow()][r2.getColumn()].ToString().toUpperCase() + " ");
								break;
							case (byte)0x2: //caller's caller's base +/- address = eff.addr.
								//pop top of stack, put at eff.addr.
								//get current base address
								r1.parseString( Integer.toHexString(b.getMemoryValue()) );
								//get the previous base address from the current base address
								r2.parseString( Integer.toHexString(Integer.parseUnsignedInt(dataMemory[r1.getRow()][r1.getColumn()].getAddress(), 16)) );
								//use the value at the previous base to get the previous... previous base and use it to create the effective address
								r3.parseString(Integer.toHexString(Integer.parseUnsignedInt(dataMemory[r2.getRow()][r2.getColumn()].getAddress(), 16) + ir.getSignedAddress()) );
								//put data from the top of stack at the effective address
								dataMemory[r3.getRow()][r3.getColumn()].parseString(dataMemory[t.getRow()][t.getColumn()].ToString().toUpperCase());
								t.parseString(Integer.toHexString(t.getMemoryValue()-1));
								resultAction += ("DATA(" + r3.getAddress() + ") <-- " + dataMemory[r3.getRow()][r3.getColumn()].ToString().toUpperCase() + " ");
								break;
						}
						break;
					case (byte)0x4: //CAL
						//call subroutine
						//push b onto stack
						t.parseString(Integer.toHexString(t.getMemoryValue()+1));
						dataMemory[t.getRow()][t.getColumn()].parseString(b.ToString().toUpperCase());
						resultAction += ("DATA(" + t.ToString() + ") <-- " + dataMemory[t.getRow()][t.getColumn()].ToString().toUpperCase() + " ");
						//store t to use as b later
						r1.parseString(t.ToString());
						//push another b onto stack
						t.parseString(Integer.toHexString(t.getMemoryValue()+1));
						dataMemory[t.getRow()][t.getColumn()].parseString(b.ToString().toUpperCase());
						resultAction += ("DATA(" + t.ToString() + ") <-- " + dataMemory[t.getRow()][t.getColumn()].ToString().toUpperCase() + " ");
						//push p
						t.parseString(Integer.toHexString(t.getMemoryValue()+1));
						dataMemory[t.getRow()][t.getColumn()].parseString(p.ToString().toUpperCase());
						resultAction += ("DATA(" + t.ToString() + ") <-- " + dataMemory[t.getRow()][t.getColumn()].ToString().toUpperCase() + " ");
						
						b.parseString(r1.getAddress());
						p.parseString(ir.getAddress());
						break;
					case (byte)0x5: //INT
						//incriment t by address amount (address is 11 bits so 7FF = -1)
						t.parseString(Integer.toHexString(t.getMemoryValue() + ir.getSignedAddress()));
						break;
					case (byte)0x6: //JMP
						switch(ir.getIB2()){
							case (byte)0x0: //JPU
								//don't pop stack, but get next instruction from address
								p.parseString(ir.getAddress());
								break;
							case (byte)0x1: //JPC
								//pop stack, if value is not 0 get instruction at that address, else get next instruction
								r1.parseString(dataMemory[t.getRow()][t.getColumn()].ToString().toUpperCase());
								t.parseString(Integer.toHexString(t.getMemoryValue()-1));
								
								if(r1.getMemoryValue() != 0x0000){
									p.parseString(ir.getAddress());
								}
								else
								{
								}
								break;
							case (byte)0x2: //JPT
								//pop stack and load p with it
								r1.parseString(dataMemory[t.getRow()][t.getColumn()].ToString().toUpperCase());
								t.parseString(Integer.toHexString(t.getMemoryValue()-1));
								
								p.parseString(r1.ToString().toUpperCase());
								break;
							case (byte)0x3: //HLT
								//load p with address and halt program
								p.parseString(ir.getAddress());
								done = true;
								break;
						}
						break;
					case (byte)0x7: //ADR push the combined level and address onto the stack.
						t.parseString(Integer.toHexString(t.getMemoryValue()+1));
						dataMemory[t.getRow()][t.getColumn()].parseString(Integer.toHexString(ir.getIB2()) + ir.getAddress());
						writer.print("DATA(" + t.ToString() + ") <-- " + dataMemory[t.getRow()][t.getColumn()].ToString().toUpperCase() + " ");
						break;
				}
				resultState = ir.ToString().toUpperCase() + " B = " + b.ToString().toUpperCase() + " T = " + t.ToString().toUpperCase() + " ";
				System.out.print(resultState + resultAction);
				System.out.print("\r\n");
				//loop
			}
			writer.close();
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
		System.out.println("Program Run");
	}
	
}
