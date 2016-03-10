package edu.kit.trufflehog.model.network.graph.components;

import edu.kit.trufflehog.model.network.graph.IComponent;
import edu.kit.trufflehog.util.DeepCopyable;
import edu.kit.trufflehog.util.Updatable;

import java.awt.Color;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D;
import java.time.Instant;

/**
 * Created by root on 26.02.16.
 */
public interface IRenderer extends DeepCopyable<IRenderer>, Updatable<IRenderer> {

    Shape getShape();

    Color getColorUnpicked();

    Color getColorPicked();

    Stroke getStroke();

    void setColorPicked(Color colorPicked);

    void setColorUnpicked(Color colorUnpicked);

    void setShape(Shape shape);

    void setStroke(Stroke stroke);

    void updateState();
}
