import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class ShazamUI {
	private static InstructionMemoryPart[][] instructionMemory = new InstructionMemoryPart[0x40][0xF+1];
	private static DataMemoryPart[][] dataMemory = new DataMemoryPart[0x80][0xF+1];
	private static Register p = new Register();
	private static Register b = new Register();
	private static Register t = new Register();
	private static Register ir = new Register();
	private static Register r1 = new Register();
	private static Register r2 = new Register();
	private static Register r3 = new Register();
	private static Register r4 = new Register();
	private static Register r5 = new Register();
	
	public ShazamUI(){
		//dormant Shaq goes here for further use
	}
	
	public void run(){
		String choice = "";
		Scanner userIn = new Scanner(System.in);
		
		//Basically run all the things
		while(choice.compareTo("exit") != 0){
			printMenu();
			choice = userIn.next().toLowerCase();
			
			switch(choice){
				case "clear": 
					clear();
					break;
				case "dump": 
					dump();
					break;
				case "load": 
					load();
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
				"Exit");
	}
	
	//Clear all the things!
	public void clear(){
		b = new Register();
		p = new Register();
		t = new Register();

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
		
		System.out.println("Memory Cleared");
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
					writer.print("Memory Dump--Instruction Memory\t\tPage 2");
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
			String[] insLine = fileIn.nextLine().toLowerCase().split(" ");
			switch (insLine[0]){
				case "i": 
					break;
				case "d": 
					break;
			}
		}
		fileIn.close();
	}
}
