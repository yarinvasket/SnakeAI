import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		Game snakeGame = new SnakeGame(17);
		System.out.println("Path to save the population file: ");
		Scanner scanner = new Scanner(System.in);
		String filePath = scanner.nextLine();
		scanner.close();
		Population population = new Population(1000, snakeGame, filePath);
		while(true)
			population.killMutate();
	}

}
