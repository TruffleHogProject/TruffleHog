package edu.kit.trufflehog.model.network.graph;

import edu.kit.trufflehog.model.network.INetworkViewPort;
import edu.kit.trufflehog.model.network.graph.jungconcurrent.ConcurrentFRLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import org.apache.commons.collections15.Transformer;

import java.awt.*;
import java.awt.geom.Point2D;
import java.time.Instant;

/**
 * Created by jan on 22.02.16.
 */
public class NetworkViewPort implements INetworkViewPort {


    private Layout<INode, IConnection> delegate;
    private Transformer<Graph<INode, IConnection>, Layout<INode, IConnection>> layoutFactory;

    private final IntegerProperty maxThroughputProperty = new SimpleIntegerProperty(0);

    private final IntegerProperty maxConnectionSizeProperty = new SimpleIntegerProperty(0);

    private final LongProperty viewTimeProperty = new SimpleLongProperty(Instant.now().toEpochMilli());

    public NetworkViewPort(final Graph<INode, IConnection> delegate) {

        this.delegate = new ConcurrentFRLayout<>(delegate);
        this.layoutFactory = new FRLayoutFactory();

    }

    @Override
    public void initialize() {
        delegate.initialize();
    }

    @Override
    public void setInitializer(Transformer<INode, Point2D> initializer) {
        delegate.setInitializer(initializer);
    }

    @Override
    public void setGraph(Graph<INode, IConnection> graph) {
        delegate.setGraph(graph);
    }

    @Override
    public Graph<INode, IConnection> getGraph() {
        return delegate.getGraph();
    }

    @Override
    public void reset() {
        delegate.reset();
    }

    @Override
    public void setSize(Dimension d) {
        delegate.setSize(d);
    }

    @Override
    public Dimension getSize() {
        return delegate.getSize();
    }

    @Override
    public void lock(INode iNode, boolean state) {
        delegate.lock(iNode, state);
    }

    @Override
    public boolean isLocked(INode iNode) {
        return delegate.isLocked(iNode);
    }

    @Override
    public void setLocation(INode iNode, Point2D location) {
        delegate.setLocation(iNode, location);
    }

    @Override
    public Point2D transform(INode iNode) {
        return delegate.transform(iNode);
    }


    @Override
    public int getMaxConnectionSize() {
        return maxConnectionSizeProperty.get();
    }

    @Override
    public void setMaxConnectionSize(int size) {
        maxConnectionSizeProperty.set(size);
    }

    @Override
    public IntegerProperty getMaxConnectionSizeProperty() {
        return maxConnectionSizeProperty;
    }

    @Override
    public int getMaxThroughput() {
        return maxThroughputProperty.get();
    }

    @Override
    public void setMaxThroughput(int size) {
        maxThroughputProperty.set(size);
    }

    @Override
    public IntegerProperty getMaxThroughputProperty() {
        return maxThroughputProperty;
    }

    @Override
    public long getViewTime() {
        return viewTimeProperty.get();
    }

    @Override
    public void setViewTime(long time) {
        viewTimeProperty.set(time);
    }

    @Override
    public LongProperty getViewTimeProperty() {
        return viewTimeProperty;
    }

    @Override
    public void refreshLayout() {

        delegate = layoutFactory.transform(getGraph());
    }

    @Override
    public void setLayoutFactory(Transformer<Graph<INode, IConnection>, Layout<INode, IConnection>> layoutFactory) {

        this.layoutFactory = layoutFactory;
    }

    @Override
    public void graphIntersection(Graph<INode, IConnection> graph) {

        throw new UnsupportedOperationException("not supported, yet not know if in future");
    }
}
