package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

import static application.controller.Controller.*;

@SuppressWarnings("all")
public class Client extends Application {
  private Stage primaryStage;

  @Override
  public void start(Stage primaryStage) throws Exception {
    try {
      System.out.println("User login interface");
      FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getClassLoader().getResource("startup.fxml"));
      Pane root = fxmlLoader.load();
       primaryStage.setTitle("start");
      primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
      primaryStage.show();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void stop() {
    printWriter.println("Disconnect");
    System.out.println("Not today");
      printWriter.flush();
    printWriter.close();
      try {
      bufferedReader.close();
    } catch (IOException e) {
      System.out.println("Bufferedreader connot be closed");
    }
    try {
      socket.close();
    } catch (IOException e) {
      System.out.println("Socket cannot be closed");
    }

  }

  public static void main(String[] args) {
    launch(args);
  }
}
