import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		Game snakeGame = new SnakeGame();
		System.out.println("Is there a file saved? (y/n)");
		Scanner scanner = new Scanner(System.in);
		String ans = scanner.nextLine();
		System.out.println("File path: ");
		String filePath = scanner.nextLine();
		scanner.close();
		Population population;
		if (ans.compareToIgnoreCase("y") == 0)
			population = (Population) ObjectIO.readObjFromFile(filePath);
		else
			population = new Population(1000, snakeGame);
		while (true)
			population.killMutate();
	}

}
