package edu.kit.trufflehog.model.network.graph.components.edge;

import edu.kit.trufflehog.model.network.graph.IComponent;

import java.awt.Color;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D;
import java.time.Instant;

/**
 * Created by root on 26.02.16.
 */
public interface IRendererComponent extends IComponent {

    Shape getShape();
    Color getColorUnpicked();
    Color getColorPicked();

    Stroke getStroke();
}
