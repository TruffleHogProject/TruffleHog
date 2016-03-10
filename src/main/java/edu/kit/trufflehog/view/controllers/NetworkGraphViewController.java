package edu.kit.trufflehog.view.controllers;

import edu.kit.trufflehog.command.usercommand.IUserCommand;
import edu.kit.trufflehog.interaction.GraphInteraction;
import edu.kit.trufflehog.model.network.graph.IConnection;
import edu.kit.trufflehog.model.network.graph.INode;
import edu.kit.trufflehog.util.IListener;
import edu.kit.trufflehog.util.INotifier;
import edu.uci.ics.jung.visualization.VisualizationServer;
import javafx.embed.swing.SwingNode;

/**
 * <p>
 * 		NetworkGraphViewController is the basic abstraction for every implementation of a network graph
 * 		view. By extending the Swing wrapper class {@link SwingNode} its possible to place
 * 		an instance of a NetworkGraphViewController into the existing javafx environment easily.
 * </p>
 */
public abstract class NetworkGraphViewController extends SwingNode implements VisualizationServer<INode, IConnection>,
		IViewController<GraphInteraction> {

	/**
	 * <p>
	 *     The wrapped instance of view controller notifier.
	 * </p>
	 */
	private final INotifier<IUserCommand> viewControllerNotifier = new ViewControllerNotifier();

	public abstract void setRefreshRate(int rate);

	public abstract void enableSmartRefresh(int maxRate);

	public abstract void disableSmartRefresh();

	@Override
	public final boolean addListener(final IListener<IUserCommand> listener) {

		return viewControllerNotifier.addListener(listener);
	}

	@Override
	public final boolean removeListener(final IListener<IUserCommand> listener) {
		return viewControllerNotifier.removeListener(listener);
	}

	@Override
	public final void notifyListeners(final IUserCommand message) {
		viewControllerNotifier.notifyListeners(message);
	}
}
