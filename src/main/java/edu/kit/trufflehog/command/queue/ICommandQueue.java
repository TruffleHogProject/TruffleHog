package edu.kit.trufflehog.command.queue;

import edu.kit.trufflehog.command.ICommand;

/**
 * <p>
 *     This interface supplies general methods for any CommandQueue implementation.
 * </p>
 */
public interface ICommandQueue {

    /**
     * <p>
     *     Pushes the supplied command onto the queue.
     * </p>
     *
     * <p>
     *     The element will always be added to the end of the list. Any element that was added will be returned after
     *     all elements that were added before it if pop is called.
     * </p>
     *
     * @param command The command to put onto the Queue.
     * @param <T>     The type of the command.
     * @throws InterruptedException
     * @throws NullPointerException If the command to add is null.
     */
    <T extends ICommand> void push(final T command) throws InterruptedException;

    /**
     * <p>
     *     Gets the first element of the queue and removes it from the list.
     * </p>
     *
     * @return The first command on the queue
     * @throws InterruptedException
     * @throws java.util.NoSuchElementException If there are no elements in this queue.
     */
    ICommand pop() throws InterruptedException;

    /**
     * <p>
     *     Returns true if the ICommandQueue is empty, else it returns false.
     * </p>
     *
     * @return True if the ICommandQueue is empty, else it returns false.
     */
    boolean isEmpty();
}
