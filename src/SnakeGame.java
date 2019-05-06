import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SnakeGame implements Game, Serializable {
	private Block[][] board;
	private Map<List<Integer>, Direction> directions;
	private List<Float> input;
	private Direction latestDirection;
	private int xHead, yHead, xTail, yTail, xFood, yFood;
	private int score;
	private int tailLast;
	private int hunger;
	private boolean isAlive;
	private static final long serialVersionUID = 1L;

	public SnakeGame() {
		board = new Block[17][];
		for (int i = 0; i < 17; i++)
			board[i] = new Block[17];
		for (int i = 0; i < 17; i++)
			for (int j = 0; j < 17; j++)
				if (i == 0 || j == 0 || i == 16 || j == 16)
					board[i][j] = Block.WALL;
				else
					board[i][j] = Block.NA;

		xHead = 4;
		yHead = 5;
		xTail = xHead - 2;
		yTail = yHead;
		generateFood();
		directions = new HashMap<List<Integer>, Direction>();
		for (int x = 2; x <= 4; x++) {
			board[yHead][x] = Block.SNAKE;
			if (x < 4) {
				List<Integer> key = new ArrayList<Integer>();
				key.add(yHead);
				key.add(x);
				directions.put(key, Direction.RIGHT);
			}
		}
		latestDirection = Direction.RIGHT;
		score = 3;
		tailLast = 2;
		hunger = 20;
		isAlive = true;
	}

	@Override
	public void setInput(List<Float> input) {
		this.input = new ArrayList<Float>();
		for (float e : input)
			this.input.add(e);
	}

	@Override
	public List<Float> getOutput() {
		List<Float> output = new ArrayList<Float>();

		int yDifference;
		int xDifference;

		if (latestDirection == Direction.UP) {
			yDifference = yHead - yFood;
			xDifference = xFood - xHead;
		} else if (latestDirection == Direction.DOWN) {
			yDifference = yFood - yHead;
			xDifference = xHead - xFood;
		} else if (latestDirection == Direction.LEFT) {
			yDifference = xHead - xFood;
			xDifference = yFood - yHead;
		} else {
			yDifference = xFood - xHead;
			xDifference = yHead - yFood;
		}

		float firstNeuron;
		if (yDifference == 0)
			firstNeuron = (float) Math.PI / 2;
		else if (xDifference == 0)
			firstNeuron = 0;
		else
			firstNeuron = (float) Math.atan((yDifference) / (xDifference));

		output.add(firstNeuron);
		return output;
	}

	@Override
	public float getScore() {
		return score;
	}

	@Override
	public boolean isAlive() {
		return isAlive;
	}

	@Override
	public void tick() {
		if (tailLast <= 0) {
			board[yTail][xTail] = Block.NA;
			List<Integer> key = new ArrayList<Integer>();
			key.add(yTail);
			key.add(xTail);
			Direction tailDirection = directions.get(key);
			if (tailDirection == Direction.DOWN)
				yTail++;
			else if (tailDirection == Direction.LEFT)
				xTail--;
			else if (tailDirection == Direction.RIGHT)
				xTail++;
			else
				yTail--;
			directions.put(key, null);
		} else {
			tailLast--;
			score++;
		}

		int maxIdx = 0;
		for (int i = 1; i < input.size(); i++)
			if (input.get(i) > input.get(maxIdx))
				maxIdx = i;
		switch (maxIdx) {
		case 1:
			if (latestDirection == Direction.DOWN) {
				List<Integer> key = new ArrayList<Integer>();
				key.add(yHead);
				key.add(xHead);
				directions.put(key, Direction.RIGHT);
				xHead++;
				latestDirection = Direction.RIGHT;
			} else if (latestDirection == Direction.LEFT) {
				List<Integer> key = new ArrayList<Integer>();
				key.add(yHead);
				key.add(xHead);
				directions.put(key, Direction.DOWN);
				yHead++;
				latestDirection = Direction.DOWN;
			} else if (latestDirection == Direction.RIGHT) {
				List<Integer> key = new ArrayList<Integer>();
				key.add(yHead);
				key.add(xHead);
				directions.put(key, Direction.UP);
				yHead--;
				latestDirection = Direction.UP;
			} else {
				List<Integer> key = new ArrayList<Integer>();
				key.add(yHead);
				key.add(xHead);
				directions.put(key, Direction.LEFT);
				xHead--;
				latestDirection = Direction.LEFT;
			}
			break;
		case 2:
			if (latestDirection == Direction.DOWN) {
				List<Integer> key = new ArrayList<Integer>();
				key.add(yHead);
				key.add(xHead);
				directions.put(key, Direction.LEFT);
				xHead--;
				latestDirection = Direction.LEFT;
			} else if (latestDirection == Direction.LEFT) {
				List<Integer> key = new ArrayList<Integer>();
				key.add(yHead);
				key.add(xHead);
				directions.put(key, Direction.UP);
				yHead--;
				latestDirection = Direction.UP;
			} else if (latestDirection == Direction.RIGHT) {
				List<Integer> key = new ArrayList<Integer>();
				key.add(yHead);
				key.add(xHead);
				directions.put(key, Direction.DOWN);
				yHead++;
				latestDirection = Direction.DOWN;
			} else {
				List<Integer> key = new ArrayList<Integer>();
				key.add(yHead);
				key.add(xHead);
				directions.put(key, Direction.RIGHT);
				xHead++;
				latestDirection = Direction.RIGHT;
			}
			break;
		default:
			if (latestDirection == Direction.DOWN) {
				List<Integer> key = new ArrayList<Integer>();
				key.add(yHead);
				key.add(xHead);
				directions.put(key, Direction.DOWN);
				yHead++;
			} else if (latestDirection == Direction.LEFT) {
				List<Integer> key = new ArrayList<Integer>();
				key.add(yHead);
				key.add(xHead);
				directions.put(key, Direction.LEFT);
				xHead--;
			} else if (latestDirection == Direction.RIGHT) {
				List<Integer> key = new ArrayList<Integer>();
				key.add(yHead);
				key.add(xHead);
				directions.put(key, Direction.RIGHT);
				xHead++;
			} else {
				List<Integer> key = new ArrayList<Integer>();
				key.add(yHead);
				key.add(xHead);
				directions.put(key, Direction.UP);
				yHead--;
			}
			break;
		}

		if (board[yHead][xHead] == Block.WALL || board[yHead][xHead] == Block.SNAKE || hunger <= 0) {
			isAlive = false;
			drawBoard();
			return;
		} else if (board[yHead][xHead] == Block.NA) {
			board[yHead][xHead] = Block.SNAKE;
			hunger--;
		} else {
			tailLast += 3;
			board[yHead][xHead] = Block.SNAKE;
			hunger = 20;
			generateFood();
		}
		drawBoard();
	}

	public void generateFood() {
		xFood = (int) (Math.random() * 16);
		yFood = (int) (Math.random() * 16);
		if (board[yFood][xFood] == Block.NA)
			board[yFood][xFood] = Block.FOOD;
		else
			generateFood();
	}

	@Override
	public int getInputAmount() {
		return 1;
	}

	@Override
	public int getOutputAmount() {
		return 3;
	}

	public void drawBoard() {
		for (Block[] y : board) {
			for (Block x : y)
				if (x == Block.NA)
					System.out.print(" ");
				else if (x == Block.WALL)
					System.out.print("█");
				else if (x == Block.SNAKE)
					System.out.print("*");
				else
					System.out.print("&");
			System.out.println();
		}
	}

	public Object clone() throws CloneNotSupportedException {
		// since clone is broken I'm returning a fresh object from the constructor
		return new SnakeGame();
	}

}
