package edu.kit.trufflehog.model.network.graph;

import edu.kit.trufflehog.model.network.IAddress;
import edu.kit.trufflehog.model.network.INetworkReadingPort;
import javafx.beans.property.IntegerProperty;

/**
 * Created by jan on 22.02.16.
 */
public class NetworkReadingPort implements INetworkReadingPort {


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
        return 0;
    }


    @Override
    public IntegerProperty getMaxConnectionSizeProperty() {
        return null;
    }

    @Override
    public int getMaxThroughput() {
        return 0;
    }


    @Override
    public IntegerProperty getMaxThroughputProperty() {
        return null;
    }
}
