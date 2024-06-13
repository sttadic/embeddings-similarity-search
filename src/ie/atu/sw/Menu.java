package ie.atu.sw;
// Static import
import static java.lang.System.out;

public class Menu {
	
	public Menu() {
		options();
	}
	
	public void options() {
		out.println("************************************************************");
		out.println("*     ATU - Dept. of Computer Science & Applied Physics    *");
		out.println("*                                                          *");
		out.println("*          Similarity Search with Word Embeddings          *");
		out.println("*                                                          *");
		out.println("************************************************************");
		out.println("(1) Specify Embedding File");
		out.println("(2) Specify an Output File (default: ./out.txt)");
		out.println("(3) Enter a Word or Text");
		out.println("(4) Configure Options");
		out.println("(?) Optional Extras...");
		out.println("(?) Quit");
		
		out.println("");
		out.println("Select Option (1-?) >");
	}
}
