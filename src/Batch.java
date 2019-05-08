import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.io.Serializable;

public class Batch implements Serializable {
	private Map<List<Integer>, List<Float>> connections;
	private List<Float> scores;
	private static final long serialVersionUID = 1L;

	public Batch() {
		connections = new HashMap<List<Integer>, List<Float>>();
		scores = new ArrayList<Float>();
	}

	public Batch(Map<List<Integer>, List<Float>> connections, List<Float> scores) {
		this.connections = connections;
		this.scores = scores;
	}

	public void addBatch(Batch batch) {
		if (batch == null)
			return;
		for (List<Integer> key : batch.getConnections().keySet())
			if (connections.containsKey(key))
				for (float e : batch.getConnections().get(key))
					connections.get(key).add(e);
			else
				connections.put(key, batch.getConnections().get(key));
		for (float e : batch.getScores())
			addScore(e);
	}

	public Map<List<Integer>, List<Float>> getConnections() {
		return connections;
	}

	public float getScore(int index) {
		return scores.get(index);
	}

	public List<Float> getScores() {
		return scores;
	}

	public void addConnection(List<Integer> key, float value) {
		if (!connections.containsKey(key))
			connections.put(key, new ArrayList<Float>());
		connections.get(key).add(value);
	}

	public void addScore(float score) {
		scores.add(score);
	}

	public Object clone() {
		return new Batch(connections, scores);
	}

}
