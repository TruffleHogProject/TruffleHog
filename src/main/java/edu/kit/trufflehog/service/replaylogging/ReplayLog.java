package edu.kit.trufflehog.service.replaylogging;

import edu.kit.trufflehog.command.ICommand;
import edu.kit.trufflehog.model.network.INetwork;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

/**
 * <p>
 *     A ReplayLog object contains the snapshot of a given graph, a list of all commands that were executed for the next
 *     X seconds following that snapshot and a timestamp of when the ReplayLog starts to where it ends.
 * </p>
 * <p>
 *     ReplayLog objects are used to recreate a graph that already occurred. They capture all necessary information to
 *     display the full graph the way it was at any given point in time. This is done by applying the commands stored
 *     in it back on the snapshot until the end of the ReplayLog is reached, at which point the commands of the next
 *     replay log are applied and so on.
 * </p>
 */
public class ReplayLog implements Serializable {

    /**
     * <p>
     *     Creates a new ReplayLog object containing the given graph snapshot and the list of commands.
     * </p>
     *
     * @param snapshotGraph The snapshot of the graph to include in the replay log.
     * @param commands The list of commands to include in the replay log.
     */
    public ReplayLog(INetwork snapshotGraph, List<ICommand> commands) {
    }

    /**
     * <p>
     *     Gets the graph snapshot contained in this ReplayLog object.
     * </p>
     *
     * @return The graph snapshot contained in this ReplayLog object.
     */
    public INetwork getGraphSnapshot() {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    /**
     * <p>
     *     Gets the command list contained in this ReplayLog object.
     * </p>
     *
     * @return The command list contained in this ReplayLog object.
     */
    public List<ICommand> getCommands() {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    /**
     * <p>
     *     Gets the point in time of when this ReplayLog object starts to contain graph data.
     * </p>
     *
     * @return The point in time of when this ReplayLog object starts to contain graph data.
     */
    public Instant getStartInstant() {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    /**
     * <p>
     *     Gets the point in time of when this ReplayLog object stops to contain graph data.
     * </p>
     *
     * @return The point in time of when this ReplayLog object stops to contain graph data.
     */
    public Instant getEndInstant() {
        throw new UnsupportedOperationException("Not implemented yet!");
    }
}
