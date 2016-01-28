package edu.kit.trufflehog.model.graph;

/**
 * <p>
 *     Graph deputy used to encapsulate the graph and thus make it interchangeable without having to update graph
 *     references of other objects.
 * </p>
 */
public class GraphProxy extends ANetworkGraph {

	private ANetworkGraph networkGraph;

    /**
     * <p>
     *     Sets the new reference graph.
     * </p>
     *
     * @param graph INetworkGraph to refer to
     */
	public void setGraph(ANetworkGraph graph) {

	}

    /**
     * {@inheritDoc}
     */
	public void addNetworkNode(NetworkNode node) {

	}

    /**
     * {@inheritDoc}
     */
	public void addNetworkEdge(NetworkNode from, NetworkNode to) {

	}

}