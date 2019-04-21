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
	private int xHead, yHead, xTail, yTail;
	private int score;
	private int tailLast;
	private boolean isAlive;
	private static final long serialVersionUID = 1L;

	public SnakeGame(int boardSize) {
		board = new Block[boardSize][];
		for (int i = 0; i < boardSize; i++)
			board[i] = new Block[boardSize];
		for (int i = 0; i < boardSize; i++)
			for (int j = 0; j < boardSize; j++)
				if (i == 0 || j == 0 || i == boardSize - 1 || j == boardSize - 1)
					board[i][j] = Block.WALL;
				else
					board[i][j] = Block.NA;

		xHead = 4;
		yHead = 5;
		xTail = xHead - 2;
		yTail = yHead;
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
		tailLast = 1;
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
		return null;
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
				xHead++;
				latestDirection = Direction.RIGHT;
				List<Integer> key = new ArrayList<Integer>();
				key.add(yHead);
				key.add(xHead);
				directions.put(key, Direction.RIGHT);
			} else if (latestDirection == Direction.LEFT) {
				yHead++;
				latestDirection = Direction.DOWN;
				List<Integer> key = new ArrayList<Integer>();
				key.add(yHead);
				key.add(xHead);
				directions.put(key, Direction.DOWN);
			} else if (latestDirection == Direction.RIGHT) {
				yHead--;
				latestDirection = Direction.UP;
				List<Integer> key = new ArrayList<Integer>();
				key.add(yHead);
				key.add(xHead);
				directions.put(key, Direction.UP);
			} else {
				xHead--;
				latestDirection = Direction.LEFT;
				List<Integer> key = new ArrayList<Integer>();
				key.add(yHead);
				key.add(xHead);
				directions.put(key, Direction.LEFT);
			}
			break;
		case 2:
			if (latestDirection == Direction.DOWN) {
				xHead--;
				latestDirection = Direction.LEFT;
				List<Integer> key = new ArrayList<Integer>();
				key.add(yHead);
				key.add(xHead);
				directions.put(key, Direction.LEFT);
			} else if (latestDirection == Direction.LEFT) {
				yHead--;
				latestDirection = Direction.UP;
				List<Integer> key = new ArrayList<Integer>();
				key.add(yHead);
				key.add(xHead);
				directions.put(key, Direction.UP);
			} else if (latestDirection == Direction.RIGHT) {
				yHead++;
				latestDirection = Direction.DOWN;
				List<Integer> key = new ArrayList<Integer>();
				key.add(yHead);
				key.add(xHead);
				directions.put(key, Direction.DOWN);
			} else {
				xHead++;
				latestDirection = Direction.RIGHT;
				List<Integer> key = new ArrayList<Integer>();
				key.add(yHead);
				key.add(xHead);
				directions.put(key, Direction.RIGHT);
			}
			break;
		default:
			if (latestDirection == Direction.DOWN) {
				yHead++;
				List<Integer> key = new ArrayList<Integer>();
				key.add(yHead);
				key.add(xHead);
				directions.put(key, Direction.DOWN);
			} else if (latestDirection == Direction.LEFT) {
				xHead--;
				List<Integer> key = new ArrayList<Integer>();
				key.add(yHead);
				key.add(xHead);
				directions.put(key, Direction.LEFT);
			} else if (latestDirection == Direction.RIGHT) {
				xHead++;
				List<Integer> key = new ArrayList<Integer>();
				key.add(yHead);
				key.add(xHead);
				directions.put(key, Direction.RIGHT);
			} else {
				yHead--;
				List<Integer> key = new ArrayList<Integer>();
				key.add(yHead);
				key.add(xHead);
				directions.put(key, Direction.DOWN);
			}
			break;
		}
	}

	@Override
	public int getInputAmount() {
		return 0;
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
		return (SnakeGame) super.clone();
	}

}
