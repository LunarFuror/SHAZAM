import java.util.Scanner;
import java.util.Stack;
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
	private static Register r1 = new Register();
	private static Register r2 = new Register();
	private static Register r3 = new Register();
	private static Register r4 = new Register();
	private static Register r5 = new Register();
	private static Stack<DataMemoryPart> stack = new Stack<DataMemoryPart>();
	
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
		r1.parseString("000");
		r2.parseString("000");
		r3.parseString("000");
		r4.parseString("000");
		r5.parseString("000");
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
					writer.print(dataMemory[i][j].ToString().toUpperCase() + " ");
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
			writer.print("B = " + b.ToString() + " T = " + t.ToString() + " P = " + p.ToString() + "\r\n");
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
					writer.print(instructionMemory[i][j].ToString().toUpperCase() + " ");
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
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
			String[] insLine = thisLine.toLowerCase().split(" ");
			String fieldL = "";
			String fieldN = "";
			String fieldNC = "";
			int location = 0;
			int subLocation = 0;
			int numInstructions = 0;
			
			switch (insLine[0]){
				//parse instruction loading
				case "i": 
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
							if((i*5)+5 < fieldNC.length()-1){
								instructionMemory[location][subLocation + i].parseString(fieldNC.substring(i*5, (i*5)+5));
							}
							else{
								instructionMemory[location][subLocation + i].parseString(fieldNC.substring(i*5));
							}
						}
					}
					break;
					
					//parse data loading
				case "d": 
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
				default: inputs.add(thisLine); break;
			}
		}
		fileIn.close();
	}

	public void run(){
		String output = "";
		boolean done = false;
		//print trace out
		output += "Interpreter --- Begin at location " + p.toString() + "  B = " + b.toString() + "  T = " + t.toString() + "\n"
				+ "Trace is... On\n";
		//while !done
		while(!done){
			//IR = instructionMemory[p]
			ir = instructionMemory[p.getRow()][p.getColumn()];
			//p++
			p.parseString(Integer.toHexString(p.getMemoryValue()+1));
			//parse code
			switch(ir.getIB1()){
				case (byte)0x0:
					//push ib3,4,5 signed int onto stack
					break;
				case (byte)0x1:
					//opr chart thing in interprate pdf
					break;
				case (byte)0x2:
					//
					break;
				case (byte)0x3:
					//
					break;
				case (byte)0x4:
					//call subroutine
					break;
				case (byte)0x5:
					//incriment t by ir345's amount (ir345 is 11 bits so 7FF = -1)
					break;
				case (byte)0x6:
						switch(ir.getIB2()){
							case (byte)0x0:
								//don't pop stack, but get next instruction from ir345
								break;
							case (byte)0x1:
								//pop stack, if value is not 0 get instruction at that address, else get next instruction
								break;
							case (byte)0x2:
								//pop stack and load p with it
								break;
							case (byte)0x3:
								//load p with ir345 and halt program
								p.parseString(Integer.toHexString(ir.getIB3()) + Integer.toHexString(ir.getIB4()) +Integer.toHexString(ir.getIB5()));
								done = true;
								break;
						}
					break;
				case (byte)0x7:
					//
					break;
			}
			//print output
			//loop
		}
	}
	
}
