import java.util.List;
import java.util.ArrayList;

public class SnakeGame implements Game {
	private Block[][] board;
	private List<Float> input;

	public SnakeGame() {
		board = new Block[15][];
		input = new ArrayList<Float>();
		for (int i = 0; i < board.length; i++)
			board[i] = new Block[15];
	}

	@Override
	public void setInput(List<Float> input) {
		this.input = new ArrayList<Float>();
		for (float e : input)
			this.input.add(e);
	}

	@Override
	public List<Float> getOutput() {
		return input;
	}

	@Override
	public float getScore() {
		return 0;
	}

	@Override
	public boolean isAlive() {
		return false;
	}

	@Override
	public void tick() {

	}

	@Override
	public int getInputAmount() {
		return 0;
	}

	@Override
	public int getOutputAmount() {
		return 0;
	}

	public Object clone() throws CloneNotSupportedException {
		return (SnakeGame) super.clone();
	}

}
