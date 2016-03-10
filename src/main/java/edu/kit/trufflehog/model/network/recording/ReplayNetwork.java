package edu.kit.trufflehog.model.network.recording;

import edu.kit.trufflehog.model.network.*;
import edu.kit.trufflehog.model.network.graph.FRLayoutFactory;
import edu.kit.trufflehog.model.network.graph.IConnection;
import edu.kit.trufflehog.model.network.graph.INode;
import edu.kit.trufflehog.model.network.graph.IUpdater;
import edu.kit.trufflehog.util.ICopyCreator;
import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.LongProperty;
import org.apache.commons.collections15.Transformer;
import org.apache.commons.collections15.keyvalue.MultiKey;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * TODO IMPLEMENT having replay network is necessary, because there are several internal functionalities that
 * TODO are handled a bite differently in contrast to a live network
 */
public class ReplayNetwork implements INetwork {

    private final ReplayWritingPort port;
    private final ReplayViewPort viewPort;

    private Layout<INode, IConnection> delegate;

    private final Map<IAddress, INode> idNodeMap = new ConcurrentHashMap<>();
    private final Map<MultiKey<IAddress>, IConnection> idConnectionMap = new ConcurrentHashMap<>();

    private final AtomicInteger maxConnectionSize = new AtomicInteger(0);

    private final AtomicInteger maxTrafficCount = new AtomicInteger(0);


    private final IUpdater replayUpdater = new ReplayUpdater();

    private long viewTime = 0;

    private Transformer<Graph<INode, IConnection>, Layout<INode, IConnection>> layoutFactory;

    public ReplayNetwork(Graph<INode, IConnection> replayViewGraph) {

        this.delegate = new FRLayout<>(replayViewGraph);
        this.layoutFactory = new FRLayoutFactory();

        port = new ReplayWritingPort();
        viewPort = new ReplayViewPort();

    }

    @Override
    public INetworkReadingPort getReadingPort() {

        return port;
    }

    @Override
    public INetworkWritingPort getWritingPort() {
        return port;
    }

    @Override
    public INetworkViewPort getViewPort() {
        return viewPort;
    }

    @Override
    public String toString() {
        return port.toString();
    }

    @Override
    public NetworkCopy createDeepCopy(ICopyCreator copyCreator) {
        throw new UnsupportedOperationException("not supported for replay networks");
    }

    @Override
    public boolean isMutable() {
        return false;
    }




    private class ReplayViewPort implements INetworkViewPort {

        @Override
        public void setMaxConnectionSize(int count) {
            maxConnectionSize.set(count);
        }

        @Override
        public int getMaxConnectionSize() {
            return maxConnectionSize.get();
        }

        @Override
        public IntegerProperty getMaxConnectionSizeProperty() {
            return null;
        }

        @Override
        public int getMaxThroughput() {
            return maxTrafficCount.get();
        }

        @Override
        public void setMaxThroughput(int size) {
            maxTrafficCount.set(size);
        }

        @Override
        public IntegerProperty getMaxThroughputProperty() {
            return null;
        }

        @Override
        synchronized
        public void refreshLayout() {

            delegate = layoutFactory.transform(delegate.getGraph());
        }

        @Override
        synchronized
        public void setLayoutFactory(Transformer<Graph<INode, IConnection>, Layout<INode, IConnection>> factory) {

            layoutFactory = factory;
            refreshLayout();
        }

        @Override
        synchronized
        public long getViewTime() {
            return viewTime;
        }

        @Override
        synchronized
        public void initialize() {
            delegate.initialize();
        }

        @Override
        synchronized
        public void setInitializer(Transformer<INode, Point2D> transformer) {
            delegate.setInitializer(transformer);
        }

        @Override
        synchronized
        public void setGraph(Graph<INode, IConnection> graph) {
            delegate.setGraph(graph);
        }

        @Override
        synchronized
        public Graph<INode, IConnection> getGraph() {
            return delegate.getGraph();
        }

        @Override
        synchronized
        public void reset() {
            delegate.reset();
        }

        @Override
        synchronized
        public void setSize(Dimension dimension) {
            delegate.setSize(dimension);
        }

        @Override
        synchronized
        public Dimension getSize() {
            return delegate.getSize();
        }


        @Override
        synchronized
        public void lock(INode iNode, boolean b) {
            delegate.lock(iNode, b);
        }

        @Override
        synchronized
        public boolean isLocked(INode iNode) {
            return delegate.isLocked(iNode);
        }

        @Override
        synchronized
        public void setLocation(INode iNode, Point2D point2D) {
            delegate.setLocation(iNode, point2D);
        }

        @Override
        synchronized
        public Point2D transform(INode iNode) {
            return delegate.transform(iNode);
        }

        synchronized
        public void setViewTime(long viewingTime) {
            viewTime = viewingTime;

        }

        @Override
        public LongProperty getViewTimeProperty() {
            return null;
        }

        public void graphIntersection(Graph<INode, IConnection> graph) {

            final Collection<INode> filteredNodes = getGraph().getVertices().stream().filter(node -> !graph.containsVertex(node)).collect(Collectors.toList());
            //existingNodes.values().removeAll(filteredNodes);

            //getGraph().getVertices()

            filteredNodes.stream().forEach(deleteNode -> {

                final Collection<IConnection> incident = delegate.getGraph().getIncidentEdges(deleteNode);
                getGraph().removeVertex(deleteNode);
                //existingNodes.remove(deleteNode.getID());
                //existingEdges.values().removeAll(incident);
            });

            final Collection<IConnection> filteredEdges = getGraph().getEdges().stream().filter(edge -> !graph.containsEdge(edge)).collect(Collectors.toList());

            filteredEdges.stream().forEach(deleteEdge -> {


                getGraph().removeEdge(deleteEdge);
                //existingEdges.remove(deleteEdge.longHashCode());
            });


        }

        @Override
        public void graphIntersection(Collection<INode> vertices, Collection<IConnection> edges) {

            final Collection<INode> filteredNodes = getGraph().getVertices().stream().filter(node -> !vertices.contains(node)).collect(Collectors.toList());
            //existingNodes.values().removeAll(filteredNodes);

            //getGraph().getVertices()

            filteredNodes.stream().forEach(deleteNode -> {

                //final Collection<IConnection> incident = delegate.getGraph().getIncidentEdges(deleteNode);
                getGraph().removeVertex(deleteNode);
                idNodeMap.remove(deleteNode.getAddress());
            });

            final Collection<IConnection> filteredEdges = getGraph().getEdges().stream().filter(edge -> !edges.contains(edge)).collect(Collectors.toList());

            filteredEdges.stream().forEach(deleteEdge -> {

                final MultiKey<IAddress> connectionKey = new MultiKey<>(deleteEdge.getSrc().getAddress(), deleteEdge.getDest().getAddress());
                //final IConnection existing = idConnectionMap.get(connectionKey);

                getGraph().removeEdge(deleteEdge);
                idConnectionMap.remove(connectionKey);
            });
        }

        @Override
        public NetworkViewCopy createDeepCopy(ICopyCreator copyCreator) {

            throw new UnsupportedOperationException("Not supported on replay ports");
        }

        @Override
        public boolean isMutable() {
            return false;
        }
    }

    /**
     * \brief
     * \details
     * \date 04.03.16
     * \copyright GNU Public License
     *
     * @author Jan Hermes
     * @version 0.0.1
     */
    private class ReplayWritingPort implements INetworkIOPort {


        @Override
        public void writeConnection(IConnection connection) {

            final MultiKey<IAddress> connectionKey = new MultiKey<>(connection.getSrc().getAddress(), connection.getDest().getAddress());
            final IConnection existing = idConnectionMap.get(connectionKey);

            if (existing != null) {
                existing.update(connection, replayUpdater);
                return;
            }
            delegate.getGraph().addEdge(connection, connection.getSrc(), connection.getDest());
            idConnectionMap.put(connectionKey, connection);
        }

        @Override
        public void writeNode(INode node) {

            final INode existing = idNodeMap.get(node.getAddress());

            if (existing != null) {
                existing.update(node, replayUpdater);
                return;
            }
            delegate.getGraph().addVertex(node);
            idNodeMap.put(node.getAddress(), node);
        }

        @Override
        public INode getNetworkNodeByAddress(IAddress address) {
            return null;
        }

        @Override
        public IConnection getNetworkConnectionByAddress(IAddress source, IAddress dest) {
            return null;
        }

        @Override
        public int getMaxConnectionSize() {
            return maxConnectionSize.get();
        }

        @Override
        public IntegerProperty getMaxConnectionSizeProperty() {

            throw new UnsupportedOperationException("not supported on replay graphs");
        }

        @Override
        public int getMaxThroughput() {
            return maxTrafficCount.get();
        }

        @Override
        public IntegerProperty getMaxThroughputProperty() {
            throw new UnsupportedOperationException("not supported on replay graphs");
        }


        @Override
        public String toString() {
            return delegate.getGraph().toString();
        }

        @Override
        public boolean isMutable() {
            return false;
        }

        @Override
        public Collection<IConnection> createDeepCopy(ICopyCreator copyCreator) {
            throw new UnsupportedOperationException("not supported on replay graphs");
        }
    }

}
