package edu.kit.trufflehog.model.graph;

import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;

/**
 * <p>
 *     Creates a {@link Layout} implementation of the Fruchterman-Reingold algorithm.
 * </p>
 */
public class FRLayoutFactory implements ILayoutFactory{

    @Override
    public Layout<INode, IConnection> transform(Graph<INode, IConnection> networkGraph) {

        return new FRLayout<>(networkGraph);
    }
}