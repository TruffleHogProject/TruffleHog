package edu.kit.trufflehog.view;

import edu.kit.trufflehog.command.IUserCommand;
import edu.kit.trufflehog.interactor.IInteraction;
import edu.kit.trufflehog.util.IListener;
import javafx.scene.layout.BorderPane;

import java.util.Collection;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * The Basic class for alls BorderPane controllers... if needed we can also
 * implement a dispatch Command fucntion in here that sends all the commands
 * to the dispatch Thread
 *
 * @param <I>
 */
public abstract class BorderPaneController<I extends IInteraction> extends BorderPane
		implements IViewController<I> {

	private final Collection<IListener<IUserCommand<?>>> listeners =
			new ConcurrentLinkedQueue<>();

	@Override
	public void addListener(IListener<IUserCommand<?>> listener) {
		listeners.add(listener);
	}

	@Override
	public void removeListener(IListener<IUserCommand<?>> listener) {
		listeners.remove(listener);
	}

	@Override
	public void sendToListeners(IUserCommand<?> message) {

		listeners.forEach(listener -> {
			listener.receive(message);
		});
	}

}