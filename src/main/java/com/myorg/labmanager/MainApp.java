
package com.myorg.labmanager;

import com.myorg.labmanager.config.Container;
import com.myorg.labmanager.ui.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Container container = Container.bootstrap();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main.fxml"));
        loader.setControllerFactory(clazz -> {
            if (clazz == MainController.class) {
                return new MainController(container);
            } else {
                try { return clazz.getDeclaredConstructor().newInstance(); } catch (Exception e) { throw new RuntimeException(e); }
            }
        });
        Scene scene = new Scene(loader.load());
        stage.setTitle("Gestor de Inventario - Lab Manager");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
