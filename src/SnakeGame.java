import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import javax.imageio.ImageIO;

import java.util.HashMap;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.Serializable;

public class SnakeGame implements Game, Serializable {
	private Block[][] board;
	private List<Float> input;
	private Map<List<Integer>, Direction> directions;
	private Direction latestDirection;
	private int xHead, yHead, xTail, yTail;
	private boolean isAlive;
	private int score;
	private int tailLast;
	private static final long serialVersionUID = 1L;

	public SnakeGame(int boardSize) {
		board = new Block[boardSize][];
		input = new ArrayList<Float>();
		directions = new HashMap<List<Integer>, Direction>();
		latestDirection = Direction.RIGHT;
		for (int i = 0; i < board.length; i++) {
			board[i] = new Block[boardSize];
			for (int j = 0; j < board[i].length; j++)
				if (i == 0 || j == 0 || i == boardSize - 1 || j == boardSize - 1)
					board[i][j] = Block.WALL;
				else
					board[i][j] = Block.NA;
		}
		xHead = 4;
		yHead = 5;
		for (int i = 4; i <= 7; i++) {
			board[yHead][i] = Block.SNAKE;
			List<Integer> key = new ArrayList<Integer>();
			key.add(yHead);
			key.add(i);
			directions.put(key, Direction.RIGHT);
		}
		xTail = 2;
		yTail = yHead;
		generateFood();
		isAlive = true;
		score = 0;
		tailLast = 1;
	}

	public void generateFood() {
		int xFood = (int) (Math.random() * 15) + 1;
		int yFood = (int) (Math.random() * 15) + 1;
		if (board[yFood][xFood] == Block.NA)
			board[yFood][xFood] = Block.FOOD;
		else
			generateFood();
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
		return (float) score;
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
			if (directions.get(key) == Direction.DOWN)
				yTail--;
			else if (directions.get(key) == Direction.LEFT)
				xTail--;
			else if (directions.get(key) == Direction.RIGHT)
				xTail++;
			else
				yTail++;
			directions.put(key, null);

		} else
			tailLast--;
		int maxIdx = 0;
		for (int i = 1; i < input.size(); i++)
			if (input.get(i) > input.get(maxIdx))
				maxIdx = i;
		Direction value = latestDirection;
		if (maxIdx == 1)
			if (latestDirection == Direction.DOWN)
				value = Direction.RIGHT;
			else if (latestDirection == Direction.LEFT)
				value = Direction.DOWN;
			else if (latestDirection == Direction.RIGHT)
				value = Direction.UP;
			else
				value = Direction.LEFT;
		else if (maxIdx == 2)
			if (latestDirection == Direction.DOWN)
				value = Direction.LEFT;
			else if (latestDirection == Direction.LEFT)
				value = Direction.UP;
			else if (latestDirection == Direction.RIGHT)
				value = Direction.DOWN;
			else
				value = Direction.RIGHT;
		List<Integer> key = new ArrayList<Integer>();
		key.add(yHead);
		key.add(xHead);
		directions.put(key, value);
		latestDirection = value;
		if (latestDirection == Direction.DOWN)
			yHead--;
		else if (latestDirection == Direction.LEFT)
			xHead--;
		else if (latestDirection == Direction.RIGHT)
			xHead++;
		else
			yHead++;
		if (board[yHead][xHead] == Block.FOOD) {
			tailLast += 3;
			score++;
			generateFood();
		} else if (board[yHead][xHead] == Block.SNAKE || board[yHead][xHead] == Block.WALL) {
			isAlive = false;
			drawBoard();
			return;
		}
		board[yHead][xHead] = Block.SNAKE;
		drawBoard();
	}

	@Override
	public int getInputAmount() {
		return 0;
	}

	@Override
	public int getOutputAmount() {
		return 3;
	}

	public Object clone() throws CloneNotSupportedException {
		return (SnakeGame) super.clone();
	}

	public void drawBoard() {
		// TODO implement this with graphics on screen and not le console...
		/*
		 * BufferedImage straight; BufferedImage straightend; BufferedImage
		 * sidestraight; try { straight =
		 * ImageIO.read(getClass().getResourceAsStream("../res/straight.png"));
		 * straightend =
		 * ImageIO.read(getClass().getResourceAsStream("../res/straightend.png"));
		 * sidestraight =
		 * ImageIO.read(getClass().getResourceAsStream("../res/sidestraight.png")); }
		 * catch(IOException e) { e.printStackTrace(); }
		 */
		for (Block[] x : board) {
			for (Block y : x)
				if (y == Block.FOOD)
					System.out.print("&");
				else if (y == Block.NA)
					System.out.print(" ");
				else if (y == Block.SNAKE)
					System.out.print("*");
				else
					System.out.print("█");
			System.out.println();
		}
	}

}
