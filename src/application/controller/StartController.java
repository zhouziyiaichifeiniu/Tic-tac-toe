package application.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.sql.*;

public class StartController {
    private Connection con = null;
    private String host = "localhost";
    private String dbname = "postgres";
    private String user = "checker";
    private String pwd = "123456";
    private String port = "5432";
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;

    @FXML
    protected void LoginClick() throws SQLException, IOException {
        String name = username.getText();
        String word = password.getText();
        try {
            Class.forName("org.postgresql.Driver");
        } catch (Exception e) {
            System.out.println("driver fails.");
        }
        try {
            String url = "jdbc:postgresql://" + host + ":" + port + "/" + dbname;
            con = DriverManager.getConnection(url, user, pwd);
        } catch (Exception e) {
            System.out.println("connection fails.");
        }
        PreparedStatement pst = con.prepareStatement("select username,password from users_209 where username=?;");
        pst.setString(1,name);
        ResultSet rs = pst.executeQuery();
        while (rs.next()){
            if (rs.getString(2).equals(word)){
                Stage primaryStage = new Stage();
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getClassLoader().getResource("mainUI.fxml"));
                Pane root = fxmlLoader.load();
                primaryStage.setTitle("Tic Tac Toe");
                primaryStage.setScene(new Scene(root));
                primaryStage.setResizable(false);
                primaryStage.show();
            }
            else if (!rs.getString(1).equals(name) || !rs.getString(2).equals(word)){
            Stage secondStage = new Stage();
                Label label = new Label("wrong username or password");
            StackPane second = new StackPane(label);
                Scene scene = new Scene(second,200,200);
                secondStage.setScene(scene);
                secondStage.show();
            }
        }

    }
}
