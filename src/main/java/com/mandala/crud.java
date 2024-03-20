package com.mandala;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import javafx.animation.FillTransition;
import javafx.animation.TranslateTransition;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class crud {
    static private int index = 1;
    static private TextField[] fields = new TextField[5];
    static private Text[] texts = new Text[5];
    static private Text status = new Text();
    static private Text title = new Text();
    static private TableColumn<crud, String> dosen, matkul, waktu, gkb, ruangan, no;
    static private ObservableList<crud> content = FXCollections.observableArrayList();
    private Button button = new Button();
    private GridPane layout = new GridPane();
    private BorderPane pane = new BorderPane();
    private SimpleStringProperty dosenProperty, matkulProperty, waktuProperty, gkbProperty, ruanganProperty, noProperty;
    private ArrayList<String> database;
    private File dataContent = new File(in.path + "/src/main/resources/com/mandala/database.txt");
    private TableView<crud> table = new TableView<>();
    private Alert alert = new Alert(AlertType.WARNING);
    private String splitter = "-,-";

    public crud(String no, String dosen, String matkul, String waktu, String gkb, String ruangan) {
        noProperty = new SimpleStringProperty(no);
        dosenProperty = new SimpleStringProperty(dosen);
        matkulProperty = new SimpleStringProperty(matkul);
        waktuProperty = new SimpleStringProperty(waktu);
        gkbProperty = new SimpleStringProperty(gkb);
        ruanganProperty = new SimpleStringProperty(ruangan);
    }

    public crud() {
        in.stage.setTitle("Mandala Application");
        in.stage.sizeToScene();

        alert.setTitle("WARNING ANJAYYYY");
        alert.setHeaderText("ISI YANG BENAR YAAA");

        pane.setLeft(panel());
        BorderPane.setMargin(title, new Insets(100, 0, 0, 0));

        no = new TableColumn<>("NO");
        no.setMinWidth(50);
        no.setMaxWidth(50);
        no.setPrefWidth(50);
        dosen = new TableColumn<>("Dosen");
        dosen.setMinWidth(200);
        dosen.setMaxWidth(200);
        dosen.setPrefWidth(200);
        matkul = new TableColumn<>("Mata Kuliah");
        matkul.setMinWidth(200);
        matkul.setMaxWidth(200);
        matkul.setPrefWidth(200);
        waktu = new TableColumn<>("Waktu");
        gkb = new TableColumn<>("GKB");
        ruangan = new TableColumn<>("Ruangan");

        no.setCellValueFactory(new PropertyValueFactory<crud, String>("noProperty"));
        dosen.setCellValueFactory(new PropertyValueFactory<crud, String>("dosenProperty"));
        matkul.setCellValueFactory(new PropertyValueFactory<crud, String>("matkulProperty"));
        waktu.setCellValueFactory(new PropertyValueFactory<crud, String>("waktuProperty"));
        gkb.setCellValueFactory(new PropertyValueFactory<crud, String>("gkbProperty"));
        ruangan.setCellValueFactory(new PropertyValueFactory<crud, String>("ruanganProperty"));

        table.getColumns().addAll(Arrays.asList(no, dosen, matkul, waktu, gkb, ruangan));

        getDatafromDatabase();
        table.setItems(content);
    }

    public String getDosenProperty() {
        return dosenProperty.get();
    }

    public String getMatkulProperty() {
        return matkulProperty.get();
    }

    public String getWaktuProperty() {
        return waktuProperty.get();
    }

    public String getGkbProperty() {
        return gkbProperty.get();
    }

    public String getRuanganProperty() {
        return ruanganProperty.get();
    }

    public String getNoProperty() {
        return noProperty.get();
    }

    public void create_data() {
        layout = new GridPane();

        setFormInput();

        setWindowContext(layout, status, title, "-- Create Your Schedule --", button, "Submit");

        for (int i = 0; i < 5; i++) {
            layout.add(texts[i], 2, i + 2);
            layout.add(fields[i], 3, i + 2);
        }
        layout.add(button, 2, 7);
        layout.add(status, 3, 7);
        layout.setAlignment(Pos.CENTER);

        button.setOnAction((actionEvent) -> {
            if (fields[0].getText().isEmpty() || fields[1].getText().isEmpty() || fields[2].getText().isEmpty()
                    || fields[3].getText().isEmpty() || fields[4].getText().isEmpty()) {
                alert.setContentText("ISI FIELD NYA SEMUANYA LAHH ANJAYY");
                alert.show();
                return;
            } else {
                try (FileWriter fWriter = new FileWriter(dataContent, true);
                        BufferedWriter bWriter = new BufferedWriter(fWriter)) {
                    Integer.parseInt(fields[3].getText());
                    String temp = "";
                    for (int i = 0; i < 5; i++) {
                        temp += fields[i].getText() + splitter;
                    }
                    fWriter.write(temp + "\n");
                    content.add(new crud(Integer.toString(index),
                            fields[0].getText(),
                            fields[1].getText(),
                            fields[2].getText(),
                            fields[3].getText(),
                            fields[4].getText()));
                    for (int i = 0; i < 5; i++) {
                        fields[i].clear();
                    }
                    index++;

                } catch (NumberFormatException e) {
                    alert.setContentText("ISI FIELD GKB NYA DENGAN ANGKA LAHH ANJAYY");
                    alert.show();
                    fields[3].clear();
                } catch (IOException e) {
                }
            }
        });

        pane.setTop(title);
        pane.setCenter(layout);
        BorderPane.setAlignment(layout, Pos.CENTER);

        in.scene.setRoot(pane);
    }

    public void read_data() {
        getDatafromDatabase();

        layout = new GridPane();

        title.setText("-- Your Schedule --");
        title.setFont(Font.font("Quicksand Medium", FontWeight.EXTRA_BOLD, FontPosture.REGULAR, 100));
        title.setFill(Color.rgb(59, 64, 60));

        table.setItems(content);

        layout.add(table, 0, 0);
        GridPane.setMargin(table, new Insets(0, 20, 20, 20));

        pane.setTop(title);
        BorderPane.setAlignment(title, Pos.BOTTOM_CENTER);
        pane.setCenter(layout);
        BorderPane.setAlignment(layout, Pos.CENTER);

        in.scene.setRoot(pane);
    }

    public void update_data() {
        getDatafromDatabase();

        layout = new GridPane();

        setFormInput();

        setWindowContext(layout, status, title, "-- Update Your Schedule --", button, "Update");

        setTableFormShape();

        table.getSelectionModel().selectedItemProperty().addListener((ChangeListener<Object>) (arg0, arg1, arg2) -> {
            int selected = (table.getSelectionModel().getSelectedIndex() + 1);
            String selectes = table.getSelectionModel().getSelectedCells().get(0).getTableColumn().getText();
            int indeks = 0;
            database = new ArrayList<String>();
            getSelectedCellMEEEEE(selected, selectes, indeks);

            button.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent arg0) {
                    if (fields[0].getText().isEmpty() || fields[1].getText().isEmpty() || fields[2].getText().isEmpty()
                            || fields[3].getText().isEmpty() || fields[4].getText().isEmpty()) {
                        alert.setContentText("ISI FIELD NYA SEMUANYA LAHH ANJAYY");
                        alert.show();
                        return;
                    }
                    try (FileWriter fWriter = new FileWriter(dataContent)) {
                        Integer.parseInt(fields[3].getText());
                        for (int i = 0; i < database.size(); i++) {
                            if (database.get(i).equals("GANTIIIIIII")) {
                                String temp = "";
                                for (int j = 0; j < 5; j++) {
                                    temp += fields[j].getText() + splitter;
                                }
                                database.set(i, temp);
                            }
                        }
                        for (int i = 0; i < database.size(); i++) {
                            fWriter.write(database.get(i) + "\n");
                        }
                        fWriter.close();

                        getDatafromDatabase();
                        for (int i = 0; i < 5; i++) {
                            fields[i].clear();
                        }
                    } catch (Exception e) {
                        System.out.println(e);
                        alert.setContentText("ISI FIELD GKB NYA DENGAN ANGKA LAHH ANJAYY");
                        alert.show();
                        fields[3].clear();
                        return;
                    } finally {
                    }
                }
            });
        });

        HBox group = new HBox(table, layout);

        group.setAlignment(Pos.CENTER_LEFT);
        HBox.setMargin(table, new Insets(0, 20, 20, 20));

        pane.setTop(title);
        pane.setCenter(group);

        in.scene.setRoot(pane);
    }

    public void delete_data() {
        layout = new GridPane();

        setFormInput();

        setWindowContext(layout, status, title, "-- Delete Your Schedule --", button, "Delete");

        setTableFormShape();

        table.getSelectionModel().selectedItemProperty().addListener((ChangeListener<Object>) (arg0, arg1, arg2) -> {
            int selected = (table.getSelectionModel().getSelectedIndex() + 1);
            String selectes = table.getSelectionModel().getSelectedCells().get(0).getTableColumn().getText();
            int indeks = 0;
            database = new ArrayList<String>();

            getSelectedCellMEEEEE(selected, selectes, indeks);

            button.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent arg0) {
                    if (fields[0].getText().isEmpty() || fields[1].getText().isEmpty() || fields[2].getText().isEmpty()
                            || fields[3].getText().isEmpty() || fields[4].getText().isEmpty()) {
                        alert.setContentText("ISI FIELD NYA SEMUANYA LAHH ANJAYY");
                        alert.show();
                        return;
                    }
                    try (FileWriter fWriter = new FileWriter(dataContent)) {
                        Integer.parseInt(fields[3].getText());
                        for (int i = 0; i < database.size(); i++) {
                            if (database.get(i).equals("GANTIIIIIII")) {
                                database.remove(i);
                            }
                        }
                        for (int i = 0; i < database.size(); i++) {
                            fWriter.write(database.get(i) + "\n");
                        }
                        fWriter.close();

                        getDatafromDatabase();
                        for (int i = 0; i < 5; i++) {
                            fields[i].clear();
                        }
                    } catch (Exception e) {
                        System.out.println(e);
                        alert.setContentText("ISI FIELD GKB NYA DENGAN ANGKA LAHH ANJAYY");
                        alert.show();
                        fields[3].clear();
                        return;
                    } finally {
                    }
                }
            });
            return;
        });

        HBox group = new HBox(table, layout);
        group.setAlignment(Pos.CENTER_LEFT);
        HBox.setMargin(table, new Insets(0, 20, 20, 20));

        pane.setTop(title);
        pane.setCenter(group);

        in.scene.setRoot(pane);
    }

    public StackPane panel() {
        Text[] CRUD = new Text[5];
        VBox panel = new VBox();
        Rectangle kotak = new Rectangle();
        Rectangle[] rounded = new Rectangle[4];
        StackPane[] circledPane = new StackPane[4];
        StackPane background = new StackPane();

        CRUD[0] = new Text("Create Data");
        CRUD[1] = new Text("Read Data");
        CRUD[2] = new Text("Update Data");
        CRUD[3] = new Text("Delete Data");

        for (int i = 0; i < 4; i++) {
            CRUD[i].setFont(Font.font("MesloLGS NF", FontWeight.EXTRA_BOLD, FontPosture.REGULAR, 16));
            CRUD[i].setFill(Color.WHITE);

            rounded[i] = new Rectangle(140, 30, Color.rgb(59, 64, 60));
            rounded[i].setArcHeight(30);
            rounded[i].setArcWidth(30);
            circledPane[i] = new StackPane(rounded[i], CRUD[i]);

            VBox.setMargin(circledPane[i], new Insets(0, 20, 20, 20));
        }

        circledPane[0].setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent arg0) {
                changePanelPosition(0, rounded, CRUD);
                create_data();
            }
        });
        circledPane[1].setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent arg0) {
                changePanelPosition(1, rounded, CRUD);
                read_data();
            }
        });
        circledPane[2].setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent arg0) {
                changePanelPosition(2, rounded, CRUD);
                update_data();
            }
        });
        circledPane[3].setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent arg0) {
                changePanelPosition(3, rounded, CRUD);
                delete_data();
            }
        });

        kotak.setHeight(250);
        kotak.setWidth(150);
        kotak.setArcHeight(30);
        kotak.setArcWidth(30);
        kotak.setFill(Color.rgb(59, 64, 60));

        panel.getChildren().add(kotak);
        panel.setAlignment(Pos.CENTER);
        panel.getChildren().addAll(circledPane[0], circledPane[1], circledPane[2], circledPane[3]);
        background.setAlignment(Pos.CENTER);

        background.getChildren().addAll(kotak, panel);

        return background;
    }

    public void changePanelPosition(int index, Rectangle[] rectangles, Text[] texts) {
        FillTransition shapeTransition = new FillTransition(Duration.seconds(0.4));
        FillTransition textTransition = new FillTransition(Duration.seconds(0.4));
        for (int i = 0; i < 4; i++) {
            if (i == index) {
                shapeTransition.setToValue(Color.WHITE);
                shapeTransition.setShape(rectangles[i]);
                textTransition.setToValue(Color.rgb(59, 64, 60));
                textTransition.setShape(texts[i]);

                shapeTransition.play();
                textTransition.play();
                continue;
            }
            rectangles[i].setFill(Color.rgb(59, 64, 60));
            texts[i].setFill(Color.WHITE);
        }
    }

    public void setTransition(Node node, String type) {
        TranslateTransition translate = new TranslateTransition();
        if (type.equals("translate")) {
            translate.setNode(node);
            translate.setAutoReverse(true);
            translate.setCycleCount(Integer.MAX_VALUE);
            translate.setByY(100);
            translate.play();
        }
    }

    private void getDatafromDatabase() {
        index = 1;
        content.clear();
        try (Scanner scan = new Scanner(dataContent)) {
            while (scan.hasNextLine()) {
                String[] temp = scan.nextLine().split(splitter);
                content.add(new crud(Integer.toString(index), temp[0], temp[1], temp[2], temp[3], temp[4]));
                index++;
            }
        } catch (Exception e) {
        }
    }

    private void getSelectedCellMEEEEE(int selected, String selectes, int indeks) {
        try (Scanner scan = new Scanner(dataContent)) {
            while (scan.hasNextLine()) {
                indeks++;
                String[] tempField = scan.nextLine().split(splitter);
                if (selectes.equals("NO")) {
                    if (selected == indeks) {
                        for (int i = 0; i < 5; i++) {
                            fields[i].setText(tempField[i]);
                        }
                        database.add("GANTIIIIIII");
                    } else {
                        database.add(String.join(splitter, tempField));
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private void setWindowContext(GridPane layout, Text status, Text title, String titleText, Button button,
            String buttonText) {
        title.setText(titleText);
        title.setFont(Font.font("Quicksand Medium", FontWeight.EXTRA_BOLD, FontPosture.REGULAR, 100));
        title.setFill(Color.rgb(59, 64, 60));
        status.setText("");
        status.setFill(Color.RED);

        button.setText(buttonText);
        layout.setHgap(10);
        layout.setVgap(10);
    }

    private void setFormInput() {
        texts[0] = new Text("Nama Dosen");
        fields[0] = new TextField();
        fields[0].setPromptText("Nama Dosen");
        texts[1] = new Text("Mata Kuliah");
        fields[1] = new TextField();
        fields[1].setPromptText("Mata Kuliah");
        texts[2] = new Text("Waktu");
        fields[2] = new TextField();
        fields[2].setPromptText("Waktu");
        texts[3] = new Text("GKB");
        fields[3] = new TextField();
        fields[3].setPromptText("GKB");
        texts[4] = new Text("Ruangan");
        fields[4] = new TextField();
        fields[4].setPromptText("Ruangan");
    }

    private void setTableFormShape() {
        for (int i = 0; i < 5; i++) {
            layout.add(texts[i], 2, i + 2);
            layout.add(fields[i], 3, i + 2);
        }
        layout.add(button, 2, 7);
        layout.add(status, 3, 7);
        layout.setAlignment(Pos.CENTER_LEFT);
    }
}