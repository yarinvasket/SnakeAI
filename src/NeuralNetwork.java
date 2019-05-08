import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.io.Serializable;

public class NeuralNetwork implements Serializable {
	private Map<Integer, Neuron> neurons;
	private Map<List<Integer>, Float> connections;
	private Map<Integer, List<Integer>> layers;
	private Map<Integer, Boolean> activeNeurons;
	private static final long serialVersionUID = 1L;

	public NeuralNetwork(int inputAmount, int outputAmount) {
		neurons = new HashMap<Integer, Neuron>();
		connections = new HashMap<List<Integer>, Float>();
		layers = new HashMap<Integer, List<Integer>>();
		activeNeurons = new HashMap<Integer, Boolean>();

		layers.put(0, new ArrayList<Integer>());
		layers.put(2147483647, new ArrayList<Integer>());

		for (int i = 0; i < inputAmount + 1; i++)
			addNeuron(0);

		for (int i = 0; i < outputAmount; i++)
			addNeuron(2147483647);
	}

	public NeuralNetwork(Map<Integer, Neuron> neurons, Map<List<Integer>, Float> connections,
			Map<Integer, List<Integer>> layers, Map<Integer, Boolean> activeNeurons) {
		this.neurons = neurons;
		this.connections = connections;
		this.layers = layers;
		this.activeNeurons = activeNeurons;
	}

	public List<Float> feedForward(List<Float> input) {
		for (int i = 0; i < layers.get(0).size() - 1; i++) {
			activeNeurons.put(i, true);
			neurons.get(i).setData(input.get(i));
		}
		neurons.get(layers.get(0).get(layers.get(0).size() - 1)).setData(1);
		activeNeurons.put(layers.get(0).get(layers.get(0).size() - 1), true);
		List<Float> output = new ArrayList<Float>();

		for (int i = 0; i < layers.size() - 1; i++) {
			Map<Integer, Boolean> afterNeurons = new HashMap<Integer, Boolean>();
			Map<Integer, Float> neuronsState = new HashMap<Integer, Float>();
			for (int j : activeNeurons.keySet()) {
				if (neurons.get(j).getData() == 0)
					continue;
				for (int k : neurons.get(j).getConnections()) {
					List<Integer> key = new ArrayList<Integer>();
					key.add(j);
					key.add(k);
					if (connections.get(key) == 0)
						continue;
					afterNeurons.put(k, true);
					float curr = neuronsState.containsKey(k) ? neuronsState.get(k) : 0;
					neuronsState.put(k, curr + neurons.get(j).getData() * connections.get(key));
				}
			}
			for (int j : activeNeurons.keySet())
				for (int k : neurons.get(j).getConnections())
					if (neuronsState.containsKey(k))
						neurons.get(k).sigma(neuronsState.get(k));
			activeNeurons = afterNeurons;
		}
		for (int j : layers.get(2147483647))
			output.add(neurons.get(j).getData());

		return output;
	}

	public Batch addConnectionMutation() {
		int possibleConnections = 0;
		for (int i = 0; i < layers.size() - 1; i++) {
			int nextLayer = (i == layers.size() - 2) ? 2147483647 : i + 1;
			possibleConnections += layers.get(i).size() * layers.get(nextLayer).size();
		}
		if (connections.size() >= possibleConnections)
			return null;

		int inputLayer = (int) (Math.random() * (layers.size() - 2));
		int outputLayer = (int) (Math.random() * (layers.size() - inputLayer - 1)) + inputLayer + 1;
		if (outputLayer == layers.size() - 1)
			outputLayer = 2147483647;

		List<Integer> key = new ArrayList<Integer>();
		key.add(layers.get(inputLayer).get((int) (Math.random() * (layers.get(inputLayer).size()))));
		key.add(layers.get(outputLayer).get((int) (Math.random() * (layers.get(outputLayer).size()))));

		if (!connections.containsKey(key)) {
			float value = addConnection(key);
			Batch res = new Batch();
			res.addConnection(key, value);
			return res;
		} else
			return addConnectionMutation();
	}

	public Batch addInConnectionMutation(int in, int inputLayer) {
		int outputLayer = (int) (Math.random() * (layers.size() - inputLayer - 1)) + inputLayer + 1;
		if (outputLayer == layers.size() - 1)
			outputLayer = 2147483647;

		List<Integer> key = new ArrayList<Integer>();
		key.add(in);
		key.add(layers.get(outputLayer).get((int) (Math.random() * (layers.get(outputLayer).size()))));

		if (!connections.containsKey(key)) {
			float value = addConnection(key);
			Batch res = new Batch();
			res.addConnection(key, value);
			return res;
		} else
			return addInConnectionMutation(in, inputLayer);
	}

	public Batch addOutConnectionMutation(int out, int outputLayer) {
		int inputLayer = (int) (Math.random() * (layers.size() - 2));

		List<Integer> key = new ArrayList<Integer>();
		key.add(layers.get(inputLayer).get((int) (Math.random() * (layers.get(inputLayer).size()))));
		key.add(out);

		if (!connections.containsKey(key)) {
			float value = addConnection(key);
			Batch res = new Batch();
			res.addConnection(key, value);
			return res;
		} else
			return addOutConnectionMutation(out, outputLayer);
	}

	public Batch mutateConnections() {
		Batch res = new Batch();
		for (List<Integer> key : connections.keySet()) {
			float value = (float) Math.random() * 2 - 1;
			nudgeConnection(key, value);
			res.addConnection(key, connections.get(key) + value);
		}
		return res;
	}

	public int addNeuronMutation() {
		if (layers.size() == 2)
			addLayer();

		int layerNum = (int) (Math.random() * (layers.size() - 2) + 1);
		addNeuron(layerNum);
		return layerNum;
	}

	public float addConnection(List<Integer> key) {
		float res = (float) Math.random() * 2 - 1;
		connections.put(key, res);
		neurons.get(key.get(0)).addConnection(key.get(1));
		return res;
	}

	public void addNeuron(int layerNum) {
		neurons.put(neurons.size(), new Neuron());
		layers.get(layerNum).add(neurons.size() - 1);
	}

	public void addLayer() {
		layers.put(layers.size() - 1, new ArrayList<Integer>());
	}

	public Map<Integer, Neuron> getNeurons() {
		return neurons;
	}

	public Map<List<Integer>, Float> getConnections() {
		return connections;
	}

	public Map<Integer, List<Integer>> getLayers() {
		return layers;
	}

	public void nudgeConnection(List<Integer> key, float value) {
		connections.put(key, connections.get(key) + value);
	}

	public Object clone() {
		return new NeuralNetwork(neurons, connections, layers, activeNeurons);
	}
}
