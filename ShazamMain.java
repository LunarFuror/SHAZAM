import java.util.Scanner;
import java.io.File;
public class ShazamMain{
	
	public static void main(String[] args){

		
		
		int choice = 0;
		Scanner userIn = new Scanner(System.in);
		while(choice != 4){
			printMenu();
			choice = userIn.nextInt();
			switch(choice){
			case 1: clear(); break;
			case 2: dump(); break;
			case 3: load(); break;
			case 4: break;
			default: System.out.println("Selection out of bounds"); break;
			}
		}
	}
	public static void printMenu(){
		System.out.println("Type your choice and hit enter\n" +
				"1: Clear\n" +
				"2: Dump\n" +
				"3: Load\n" +
				"4: Exit");
	}
	
	public static void clear(){
		//clear all the memory!
	}
	
	public static void dump(){
		//write memory to output.txt
	}
	
	public static void load(){
		File input = null;
		try{
			input = new File("Load.in.txt");
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
}
