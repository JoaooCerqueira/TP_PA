package pt.isec.pa.apoio_poe.model.FX.Phase5;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import pt.isec.pa.apoio_poe.fsm.EState;
import pt.isec.pa.apoio_poe.model.Data.ModelManager;
import pt.isec.pa.apoio_poe.model.FX.Helper.MyButton;

import java.util.Arrays;


public class QueryingFx extends BorderPane {
    ModelManager model;
    Data data;
    MyButton exit;
    ExportToFile exportToFile;

    PieChart estagios_projetos,proposals_assigned;
    BarChart<String,Integer> bar;

    public QueryingFx(ModelManager model) {
        this.model = model;
        createViews();
        registerHandlers();
        update();
    }

    private void update() {
        this.setVisible(model != null && model.getState() == EState.QUERYING_PHASE);

        ObservableList<PieChart.Data> data =
                FXCollections.observableArrayList(
                        new PieChart.Data("DA", model.getNumberDestiny("DA")),
                        new PieChart.Data("RAS", model.getNumberDestiny("RAS")),
                        new PieChart.Data("SI", model.getNumberDestiny("SI")));

        estagios_projetos.setData(data);


        if (model.getPercentage() != null){
            ObservableList<PieChart.Data> assigned =
                    FXCollections.observableArrayList(
                            new PieChart.Data("Assigned", model.getPercentage().get(0)),
                            new PieChart.Data("Not assigned", model.getPercentage().get(1)));

            proposals_assigned.setData(assigned);
        }
        model.top5();
    }

    private void registerHandlers() {
        model.addPropertyChangeListener(ModelManager.PROP_STATE, evt -> update());
        model.addPropertyChangeListener(ModelManager.PROP_DATA, evt -> update());

        exit.setOnAction(event -> {
            Platform.exit();
        });
    }

    private void createViews() {
        this.setStyle("-fx-background-color: #5F7161;");

        exit = new MyButton("Exit");
        exportToFile = new ExportToFile(model);
        
        estagios_projetos = new PieChart();
        proposals_assigned = new PieChart();
        data = new Data(model);
        data.setStyle("-fx-background-radius: 6;" + "-fx-background-color: #D0C9C0;");
        data.setPadding(new Insets(10));
        data.setMaxHeight(500);
        data.setMaxWidth(600);

        HBox center = new HBox(exportToFile,data,new VBox(estagios_projetos,proposals_assigned));
        center.setAlignment(Pos.CENTER);
        setCenter(center);
        setLeft(exit);
    }
}
