
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
 * You should have received a copy of the GNU General Public License
 * along with TruffleHog.  If not, see <http://www.gnu.org/licenses/>.
 */

package edu.kit.trufflehog.presenter;

import edu.kit.trufflehog.Main;
import edu.kit.trufflehog.model.configdata.ConfigDataModel;
import edu.kit.trufflehog.model.network.INetworkViewPort;
import edu.kit.trufflehog.view.*;
import edu.kit.trufflehog.view.controllers.IWindowController;
import edu.kit.trufflehog.view.elements.FilterOverlayMenu;
import edu.kit.trufflehog.view.elements.ImageButton;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.apache.commons.lang3.ObjectUtils;

import java.io.File;

/**
 * <p>
 *     The ViewBuilder builds an instance of the TruffleHog view. It connects all view components with each other and
 *     with other vital components like the saving/loading of configurations.
 * </p>
 *
 * @author Julian Brendl, Maximilian Diez
 * @version 1.0
 */
public class ViewBuilder {
    // General variables
    private ConfigDataModel configDataModel;

    // View related variables
    private final Stage primaryStage;
    private final MainViewController mainViewController = new MainViewController("main_view.fxml");
    private final AnchorPane primaryView = new AnchorPane();

    private OverlayViewController recordOverlayViewController;
    private OverlayViewController filterOverlayViewController;
    private OverlayViewController settingsOverlayViewController;

    private TableView tableView;

    /**
     * <p>
     *     Creates the ViewBuilder, which builds the entire view.
     * </p>
     *
     * @param configDataModel The {@link ConfigDataModel} that is necessary to save and load configurations, like filters
     *                        or settings.
     */
    public ViewBuilder(final ConfigDataModel configDataModel, final Stage primaryStage) {

        this.configDataModel = configDataModel;
        this.primaryStage = primaryStage;

        if (this.primaryStage == null || this.configDataModel == null) {
            throw new NullPointerException("primaryStage and configDataModel must not be null");
        }
    }

    /**
     * <p>
     *     Builds the entire view. That means it connects all view components with each other and with other necessary
     *     components as well.
     * </p>
     * @param viewPort
     */
    public void build(INetworkViewPort viewPort) {
        loadFonts();

        final Node node = new NetworkViewScreen(viewPort, 10);

        final MenuBarViewController menuBar = buildMenuBar();

        AnchorPane.setLeftAnchor(primaryView, 0d);
        AnchorPane.setRightAnchor(primaryView, 0d);
        AnchorPane.setTopAnchor(primaryView, 29d);
        AnchorPane.setBottomAnchor(primaryView, 0d);

        primaryView.getChildren().add(node);
        AnchorPane.setBottomAnchor(node, 0d);
        AnchorPane.setTopAnchor(node, 0d);
        AnchorPane.setLeftAnchor(node, 0d);
        AnchorPane.setRightAnchor(node, 0d);

        // Set up scene
        final Scene mainScene = new Scene(mainViewController);
        final IWindowController rootWindow = new RootWindowController(primaryStage, mainScene, "icon.png", menuBar);

        mainViewController.setCenter(primaryView);

        rootWindow.show();

        // Set min. dimensions
        primaryStage.setMinWidth(720d);
        primaryStage.setMinHeight(480d);

        primaryStage.getScene().getAccelerators().put(new KeyCodeCombination(KeyCode.Q, KeyCombination.CONTROL_DOWN),
                primaryStage::close);
        primaryStage.getScene().getAccelerators().put(new KeyCodeCombination(KeyCode.F11), () -> {primaryStage.setFullScreen(!primaryStage.isFullScreen()); menuBar.setVisible(!menuBar.isVisible());});


        buildToolbar();

        // add keyboard shortcut: F11 for fullscreen
        primaryStage.getScene().getAccelerators().put(new KeyCodeCombination(KeyCode.Q, KeyCombination.CONTROL_DOWN),
                primaryStage::close);

        buildGeneralStatisticsOverlay();
        buildNodeStatisticsOverlay();
        buildSettingsOverlay();
        buildFilterMenuOverlay();
        buildRecordOverlay();
    }

    private MenuBarViewController buildMenuBar() {
        // Get MenuBar
        final MenuBarViewController menuBarViewController = new MenuBarViewController("menu_bar.fxml");

        return menuBarViewController;
    }

    /**
     * <p>
     *     Builds the settings overlay.
     * </p>
     */
    private void buildSettingsOverlay() {
        settingsOverlayViewController = new OverlayViewController("local_settings_overlay.fxml");
        primaryView.getChildren().add(settingsOverlayViewController);
        AnchorPane.setBottomAnchor(settingsOverlayViewController, 60d);
        AnchorPane.setLeftAnchor(settingsOverlayViewController, 18d);
        settingsOverlayViewController.setVisible(false);
    }

    /**
     * <p>
     *     Builds the filter menu overlay.
     * </p>
     */
    private void buildFilterMenuOverlay() {
        // Build filter menu
        FilterOverlayMenu filterOverlayMenu = new FilterOverlayMenu(configDataModel);
        filterOverlayViewController = filterOverlayMenu.setUpOverlayViewController();
        tableView = filterOverlayMenu.setUpTableView();
        BorderPane borderPane = filterOverlayMenu.setUpMenu(tableView);

        // Add menu to overlay
        filterOverlayViewController.getChildren().add(borderPane);

        // Set up overlay on screen
        primaryView.getChildren().add(filterOverlayViewController);
        AnchorPane.setBottomAnchor(filterOverlayViewController, 60d);
        AnchorPane.setLeftAnchor(filterOverlayViewController, 18d);
        filterOverlayViewController.setMaxSize(330d, 210d);
        filterOverlayViewController.setVisible(false);
    }

    /**
     * <p>
     *     Builds the record menu overlay.
     * </p>
     */
    private void buildRecordOverlay() {
        recordOverlayViewController = new OverlayViewController("node_statistics_overlay.fxml");
        primaryView.getChildren().add(recordOverlayViewController);
        AnchorPane.setBottomAnchor(recordOverlayViewController, 60d);
        AnchorPane.setLeftAnchor(recordOverlayViewController, 18d);
        recordOverlayViewController.setVisible(false);
    }

    /**
     * <p>
     *     Builds the node statistics overlay.
     * </p>
     */
    private void buildNodeStatisticsOverlay() {
        OverlayViewController nodeStatisticsOverlay = new OverlayViewController("node_statistics_overlay.fxml");
        primaryView.getChildren().add(nodeStatisticsOverlay);
        AnchorPane.setTopAnchor(nodeStatisticsOverlay, 10d);
        AnchorPane.setRightAnchor(nodeStatisticsOverlay, 10d);
        nodeStatisticsOverlay.setVisible(false);
    }

    /**
     * <p>
     *     Builds the general statistics overlay.
     * </p>
     */
    private void buildGeneralStatisticsOverlay() {
        OverlayViewController generalStatisticsOverlay = new OverlayViewController("general_statistics_overlay.fxml");
        primaryView.getChildren().add(generalStatisticsOverlay);
        AnchorPane.setBottomAnchor(generalStatisticsOverlay, 10d);
        AnchorPane.setRightAnchor(generalStatisticsOverlay, 10d);
    }

    /**
     * <p>
     *     Builds the toolbar (3 buttons on the bottom left corner).
     * </p>
     */
    private void buildToolbar() {
        Button settingsButton = buildSettingsButton();
        Button filterButton = buildFilterButton();
        Button recordButton = buildRecordButton();

        MainToolBarController mainToolBarController = new MainToolBarController("main_toolbar.fxml", settingsButton,
                filterButton, recordButton);
        primaryView.getChildren().add(mainToolBarController);
        AnchorPane.setBottomAnchor(mainToolBarController, 5d);
        AnchorPane.setLeftAnchor(mainToolBarController, 5d);
    }

    /**
     * <p>
     *     Builds the settings button.
     * </p>
     */
    private Button buildSettingsButton() {
        Button settingsButton = new ImageButton("gear.png");
        settingsButton.setOnAction(event -> {
            settingsOverlayViewController.setVisible(!settingsOverlayViewController.isVisible());

            // Hide the filter menu if it is visible
            if (filterOverlayViewController.isVisible()) {
                filterOverlayViewController.setVisible(false);
            }

//            Stage settingsStage = new Stage();
//            SettingsViewController settingsView = new SettingsViewController("settings_view.fxml");
//            Scene settingsScene = new Scene(settingsView);
//            settingsStage.setScene(settingsScene);
//            settingsStage.show();
//
//            // CTRL+W for closing
//            settingsStage.getScene().getAccelerators().put(new KeyCodeCombination(KeyCode.W, KeyCombination.CONTROL_DOWN)
//                    , settingsStage::close);
//
//            // CTRL+S triggers info about program settings saving
//            settingsStage.getScene().getAccelerators().put(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN),
//                    () -> {
//                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
//                        alert.setTitle("Relax, no need to save anything here");
//                        alert.setHeaderText(null);
//                        alert.setContentText("Oops. Seems you wanted to save the configuration by pressing CTRL+S. This" +
//                                " is not necessary thanks to the awesome always up-to-date saving design of TruffleHog.");
//                        alert.showAndWait();
//                    });
        });

        primaryStage.getScene().getAccelerators().put(new KeyCodeCombination(KeyCode.S, KeyCombination.ALT_DOWN),
                settingsButton::fire);

        settingsButton.setScaleX(0.8);
        settingsButton.setScaleY(0.8);

        return settingsButton;
    }

    /**
     * <p>
     *     Builds the filter button.
     * </p>
     */
    private Button buildFilterButton() {
        Button filterButton = new ImageButton("filter.png");
        filterButton.setOnAction(event -> {
            filterOverlayViewController.setVisible(!filterOverlayViewController.isVisible());

            // Deselect anything that was selected
            if (!filterOverlayViewController.isVisible()) {
                tableView.getSelectionModel().clearSelection();
            }

            // Hide the settings menu if it is visible
            if (settingsOverlayViewController.isVisible()) {
                settingsOverlayViewController.setVisible(false);
            }

            // Hide the record menu if it is visible
            if (recordOverlayViewController.isVisible()) {
                recordOverlayViewController.setVisible(false);
            }
        });

        filterButton.setScaleX(0.8);
        filterButton.setScaleY(0.8);
        filterButton.setMaxSize(20, 20);
        filterButton.setMinSize(20, 20);

        return filterButton;
    }

    /**
     * <p>
     *     Builds the record button.
     * </p>
     */
    private Button buildRecordButton() {
        ImageButton recordButton = new ImageButton("record.png");

        recordButton.setOnAction(event -> {
            recordOverlayViewController.setVisible(!recordOverlayViewController.isVisible());

            // Hide the settings menu if it is visible
            if (settingsOverlayViewController.isVisible()) {
                settingsOverlayViewController.setVisible(false);
            }

            // Hide the filter menu if it is visible
            if (filterOverlayViewController.isVisible()) {
                filterOverlayViewController.setVisible(false);
            }
        });

        recordButton.setScaleX(0.8);
        recordButton.setScaleY(0.8);

        return recordButton;
    }

    /**
     * <p>
     *     Loads all custom fonts.
     * </p>
     */
    private void loadFonts() {
        Font.loadFont(Main.class.getClassLoader().getResourceAsStream("fonts" + File.separator + "DroidSans" +
                File.separator + "DroidSans.ttf"), 12);
    }
}