package com.mandala;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Paths;
import java.util.Scanner;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class in {
    static final protected String path = Paths.get("pom.xml").toAbsolutePath().getParent().toString();
    static protected Scene scene;
    static protected Stage stage = new Stage();
    protected static TextField user = new TextField();
    protected static PasswordField pass = new PasswordField();
    static crud Crud = new crud();
    final protected Text username = new Text("Username");
    final protected Text password = new Text("Password");
    final protected Text welcome = new Text();
    final protected Hyperlink createNew = new Hyperlink("Doesn't have account? Create now");
    final protected Hyperlink inNew = new Hyperlink("Have an account? SignIn now");
    protected int index;
    protected Button button = new Button();;
    protected GridPane layout = new GridPane();;
    protected BorderPane pane = new BorderPane();
    protected Label status = new Label("");

    public void logIn() {
        layout = new GridPane();
        pane = new BorderPane();

        button.setText("Login");
        welcome.setText("Welcome to Mandala");
        layout.setAlignment(Pos.CENTER);
        layout.setVgap(10);
        layout.setHgap(10);
        welcome.setFont(Font.font("Quicksand Medium", FontWeight.BOLD, 100));
        status.setTextFill(Color.RED);
        createNew.setTextFill(Color.BLUE);
        user.setPrefSize(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY);

        layout.add(status, 1, 2);
        layout.addRow(3, username, user);
        layout.addRow(4, password, pass);
        layout.addRow(5, button);
        layout.add(createNew, 1, 5);

        BorderPane.setAlignment(welcome, Pos.BOTTOM_CENTER);
        BorderPane.setAlignment(layout, Pos.TOP_CENTER);
        pane.setTop(welcome);
        pane.setCenter(layout);
        BorderPane.setMargin(welcome, new Insets(50, 0, 0, 0));
        BorderPane.setMargin(layout, new Insets(-50, 0, 0, 0));

        scene = new Scene(pane);
        button.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(final ActionEvent arg0) {
                if (isFieldCorrect(user, pass)) {
                    scene.setRoot(Crud.panel());
                }
            };
        });

        createNew.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                user.clear();
                pass.clear();
                status.setText("");
                signUp();
            }
        });

        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(final KeyEvent keyevent) {
                if (keyevent.getCode().isWhitespaceKey() && pass.getText().isBlank() && user.getText().isBlank()) {
                    status.setText("ISI FORM ANDA DULU LAHHH");
                } else if (keyevent.getCode().isWhitespaceKey() && user.getText().isBlank()) {
                    status.setText("ISI USERNAME ANDA DULU LAHHH");
                } else if (keyevent.getCode().isWhitespaceKey() && pass.getText().isBlank()) {
                    status.setText("ISI PASSWORD ANDA DULU LAHHH");
                }
            }
        });

        stage.setScene(scene);
        stage.sizeToScene();
        stage.setTitle("Login");
        stage.setFullScreen(true);
        stage.show();
    }

    public void signUp() {
        layout = new GridPane();
        pane = new BorderPane();

        button.setText("SignUp");
        welcome.setText("SignUp for Mandala");
        layout.setAlignment(Pos.CENTER);
        layout.setVgap(10);
        layout.setHgap(10);
        welcome.setFont(Font.font("Quicksand Medium", FontWeight.BOLD, 32));
        status.setTextFill(Color.RED);
        inNew.setTextFill(Color.BLUE);
        user.setPrefSize(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY);

        layout.add(status, 1, 2);
        layout.addRow(3, username, user);
        layout.addRow(4, password, pass);
        layout.addRow(5, button);
        layout.add(inNew, 1, 5);

        BorderPane.setAlignment(welcome, Pos.BOTTOM_CENTER);
        BorderPane.setAlignment(layout, Pos.TOP_CENTER);
        pane.setTop(welcome);
        pane.setCenter(layout);
        BorderPane.setMargin(welcome, new Insets(50, 0, 0, 0));
        BorderPane.setMargin(layout, new Insets(-50, 0, 0, 0));

        button.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(final ActionEvent arg0) {
                if (!user.getText().isBlank() && !pass.getText().isBlank()) {
                    try (FileWriter file = new FileWriter(
                            new File(path + "/src/main/resources/com/mandala/userpass.txt"), true);
                            BufferedWriter buffer = new BufferedWriter(file)) {
                        buffer.write(user.getText() + "\n");
                        buffer.write(pass.getText() + "\n");
                    } catch (Exception e) {
                    }
                    status.setText("");
                    logIn();
                }
            };
        });

        inNew.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                user.clear();
                pass.clear();
                status.setText("");
                logIn();
            }
        });

        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(final KeyEvent keyevent) {
                if (keyevent.getCode().isWhitespaceKey() && pass.getText().isBlank() && user.getText().isBlank()) {
                    status.setText("ISI FORM ANDA DULU LAHHH");
                } else if (keyevent.getCode().isWhitespaceKey() && user.getText().isBlank()) {
                    status.setText("ISI USERNAME ANDA DULU LAHHH");
                } else if (keyevent.getCode().isWhitespaceKey() && pass.getText().isBlank()) {
                    status.setText("ISI PASSWORD ANDA DULU LAHHH");
                }
            }
        });

        scene.setRoot(pane);
    }

    public boolean isFieldCorrect(TextField user, PasswordField pass) {
        try (Scanner scan = new Scanner(new File(path + "/src/main/resources/com/mandala/userpass.txt"))) {
            while (scan.hasNextLine()) {
                if (scan.nextLine().equals(user.getText()) && scan.nextLine().equals(pass.getText())) {
                    System.out.println("benarr anjayy");
                    return true;
                }
                scan.nextLine();
            }
            status.setText("Username dan Password anda salah");
            pass.clear();
        } catch (final Exception e) {
        }
        return false;
    }
}