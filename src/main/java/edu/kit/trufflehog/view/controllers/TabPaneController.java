package edu.kit.trufflehog.view.controllers;


import edu.kit.trufflehog.command.usercommand.IUserCommand;
import edu.kit.trufflehog.interaction.IInteraction;
import edu.kit.trufflehog.util.IListener;
import edu.kit.trufflehog.util.INotifier;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TabPane;

import java.io.IOException;


/**
 * <p>
 *      The Basic abstraction for all TabPane controllers. Every abstraction
 *      for javafx Components wraps a {@link ViewControllerNotifier} instance
 *      for implementation of the specific operations of the INotifier
 *      interface.
 * </p>
 *
 * @param <I> The type of interaction to be used in the ViewController
 */
public abstract class TabPaneController<I extends IInteraction> extends
        TabPane implements IViewController<I> {

    public TabPaneController(String fxmlFile) {

        final FXMLLoader fxmlLoader =
                new FXMLLoader(getClass().getResource(fxmlFile));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

    }

    /** The wrapped instance of view controller notifier. **/
    private final INotifier<IUserCommand> viewControllerNotifier =
            new ViewControllerNotifier();

	/**
     * {@inheritDoc}
     */
    @Override
    public final boolean addListener(final IListener<IUserCommand> listener) {

        return viewControllerNotifier.addListener(listener);
    }

	/**
     * {@inheritDoc}
     */
    @Override
    public final boolean removeListener(final IListener<IUserCommand> listener) {
        return viewControllerNotifier.removeListener(listener);
    }

	/**
     * {@inheritDoc}
     */
    @Override
    public final void notifyListeners(final IUserCommand message) {
        viewControllerNotifier.notifyListeners(message);
    }

}
