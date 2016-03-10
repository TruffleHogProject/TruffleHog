package edu.kit.trufflehog.command.queue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * <p>
 *     The command queue manager makes it possible for multiple command queues to be managed so that only once an
 *     element is present in one of the queues the calling thread is notified and can fetch the element.
 *     Essentially the CommandQueueManager is a BlockingQueue of CommandQueues.
 * </p>
 * <p>
 *     If multiple command queues have elements in them, each call to getNextQueue() will return a different one
 *     until every queue was at least popped once (round-robin style).
 * </p>
 */
public class CommandQueueManager {

    private int registeredQueues;

    private int currentQueue;

    private final List<ICommandQueue> queues = new ArrayList<>();

    private final BlockingQueue<Boolean> availableElementQueue = new LinkedBlockingQueue<>();

    /**
     * Creates a new CommandQueueManager
     */
    public CommandQueueManager() {
        registeredQueues = 0;
        currentQueue = 0;
    }

    /**
     * <p>
     *     Registers a {@link ICommandQueue} with the CommandQueueManager.
     * </p>
     *
     * @param queue The {@link ICommandQueue} to add.
     */
    protected void registerQueue(final ICommandQueue queue) {
        queues.add(queue);
        registeredQueues++;
    }

    /**
     * <p>
     *     Gets the next non empty queue.
     * </p>
     *
     * <p>
     *     This method gets the next non empty {@link ICommandQueue}. If there are multiple queues registered
     *     the queues are cycled each call until every queue was returned at least once.
     *     If all queues are empty this method blocks, until one of the queues receives an element.
     * </p>
     * @return The next command queue. Returns null if no queues are registered.
     * @throws InterruptedException
     */
    public synchronized ICommandQueue getNextQueue() throws InterruptedException {

        //TODO: Profile and optimize?

        if (registeredQueues == 0) {
            //TODO: return empty queue
            return null;
        }

        while (availableElementQueue.isEmpty()) {
            this.wait();
        }

        ICommandQueue current = queues.get(currentQueue);

        int timesLooped = 0;

        // Check for other queues if the current one is empty but don't do busy waiting (only loop over all once)
        while (current.isEmpty() && timesLooped <= registeredQueues) {
            currentQueue = (currentQueue + 1) % registeredQueues;
            timesLooped++;
            current = queues.get(currentQueue);
        }

        // next time we want a different queue if other queues have elements in them
        if (availableElementQueue.size() != 1)
            currentQueue = (currentQueue + 1) % registeredQueues;

        return current;
    }

    /**
     * <p>
     *     Notifies the manager that a new element was pushed onto a queue.
     * </p>
     *
     * <p>
     *     This method should always be called by any {@link ICommandQueue} implementation whenever
     *     an element is added to it.
     * </p>
     *
     * @throws InterruptedException Thrown if queue is interrupted while waiting
     */
    protected synchronized void notifyNewElement() throws InterruptedException {
        availableElementQueue.put(Boolean.TRUE);

        this.notifyAll();
    }

    /**
     * <p>
     *     Notifies the manager that an element was removed from the queue.
     * </p>
     *
     * <p>
     *     This method should always be called by any {@link ICommandQueue} implementation whenever
     *     an element is removed from it.
     * </p>
     *
     * @throws InterruptedException Thrown if queue is interrupted while waiting
     */
    protected void notifyRemovedElement() throws InterruptedException {
        availableElementQueue.take();
    }
}