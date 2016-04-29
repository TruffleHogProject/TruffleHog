/*
 * This file is part of TruffleHog.
 *
 * TruffleHog is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * TruffleHog is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with TruffleHog.  If not, see <http://www.gnu.org/licenses/>.
 */
package edu.kit.trufflehog.view.jung.visualization;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

/**
 * \brief
 * \details
 * \date 23.03.16
 * \copyright GNU Public License
 *
 * @author Jan Hermes
 * @version 0.0.1
 */
public class CanvasGestures {
    private EventHandler<? super MouseEvent> onMousePressedEventHandler;
    private EventHandler<? super MouseEvent> onMouseDraggedEventHandler;
    private EventHandler<? super MouseEvent> onMouseReleasedEventHandler;

    public CanvasGestures(PannableCanvas canvas) {

    }

    public EventHandler<? super MouseEvent> getOnMousePressedEventHandler() {
        return onMousePressedEventHandler;
    }

    public EventHandler<? super MouseEvent> getOnMouseDraggedEventHandler() {
        return onMouseDraggedEventHandler;
    }

    public EventHandler<? super MouseEvent> getOnMouseReleasedEventHandler() {
        return onMouseReleasedEventHandler;
    }
}
