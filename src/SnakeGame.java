import java.io.Serializable;
import java.util.List;

public class SnakeGame implements Game, Serializable {
	private Block[][] board;
	private static final long serialVersionUID = 1L;

	public SnakeGame(int boardSize) {
		board = new Block[boardSize][];
		for(int i = 0; i<boardSize; i++)
			board[i] = new Block[boardSize];
	}

	@Override
	public void setInput(List<Float> input) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<Float> getOutput() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public float getScore() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isAlive() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void tick() {
		// TODO Auto-generated method stub

	}

	@Override
	public int getInputAmount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getOutputAmount() {
		// TODO Auto-generated method stub
		return 0;
	}

	public Object clone() throws CloneNotSupportedException {
		return (SnakeGame) super.clone();
	}

}
