import java.util.List;
import java.util.ArrayList;
import java.io.Serializable;

public class Neuron implements Serializable {
	private float data;
	private List<Integer> connections;
	private static final long serialVersionUID = 1L;

	public Neuron() {
		data = 0;
		connections = new ArrayList<Integer>();
	}

	public void sigma(float x) {
		data = 1 / (1 + (float) Math.pow(Math.E, -x));
	}

	public float getData() {
		return data;
	}

	public void setData(float data) {
		this.data = data;
	}

	public void addConnection(int id) {
		connections.add(id);
	}

	public List<Integer> getConnections() {
		return connections;
	}
}
