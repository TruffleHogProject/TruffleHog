package edu.kit.trufflehog.model.network.graph.components.node;

import edu.kit.trufflehog.model.filter.IFilter;
import edu.kit.trufflehog.model.network.graph.IComponent;
import edu.kit.trufflehog.model.network.graph.IUpdater;
import edu.kit.trufflehog.util.ICopyCreator;

import java.awt.*;
import java.util.NavigableMap;
import java.util.TreeMap;

/**
 * //TODO document
 * @author Mark Giraud
 */
public class FilterPropertiesComponent implements IComponent {

    private final NavigableMap<IFilter, Color> filterColors = new TreeMap<>();

    public void addFilterColor(IFilter filter, Color color) {
        filterColors.put(filter, color);
    }

    public void removeFilterColor(IFilter filter) {
        filterColors.remove(filter);
    }

    /**
     * <p>
     *     This method gets the current filter color that should be rendered.
     * </p>
     */
    public Color getFilterColor() {
        return filterColors.lastEntry().getValue();
    }

    @Override
    public String name() {
        return "Filter properties";
    }

    @Override
    public IComponent createDeepCopy(ICopyCreator copyCreator) {
        if (copyCreator == null) throw new NullPointerException("copyCreator must not be null!");
        return copyCreator.createDeepCopy(this);
    }

    @Override
    public boolean isMutable() {
        return true;
    }

    @Override
    public boolean update(IComponent instance, IUpdater updater) {
        if (instance == null) throw new NullPointerException("instance must not be null!");
        if (updater == null) throw new NullPointerException("updater must not be null!");
        return updater.update(this, instance);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof FilterPropertiesComponent;
    }
}