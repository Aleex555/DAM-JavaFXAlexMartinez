package com.project;

import java.net.URL;
import java.util.ResourceBundle;

import org.json.JSONArray;
import org.json.JSONObject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class ControllerMobile0 implements Initializable {

  @FXML
  private VBox panelVBox;
  @FXML
  private Label texto_titulo;
  @FXML
  private AnchorPane informacion;
  @FXML
  private ScrollPane eleccionPJC;
  @FXML
  private Button boton1, boton2;
  String opcions[] = { "Personatges", "Jocs", "Consoles" };
  String texto;

  @FXML
  private void volver(ActionEvent event) {
    if (panelVBox.isVisible()) {
      texto_titulo.setText("Nintendo DB");
      try {
        showList();
      } catch (Exception e) {
        System.out.println(e);
      }
    } else {
      texto_titulo.setText(texto);
      informacion.getChildren().clear();
      panelVBox.setVisible(true);
      eleccionPJC.setVisible(true);
    }
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    loadList("Jocs");
    loadList("Consoles");
    try {
      showList();
    } catch (Exception e) {
      System.out.println(e);
    }
  }

  public void showList() throws Exception {
    boton2.setVisible(false);
    informacion.setVisible(false);
    URL resource = this.getClass().getResource("/assets/listItem2.fxml");
    panelVBox.getChildren().clear();
    for (int i = 0; i < 3; i++) {
      FXMLLoader loader = new FXMLLoader(resource);
      Parent itemTemplate = loader.load();
      ControllerListItem2 itemController = loader.getController();
      itemController.setText(opcions[i]);
      final String category = opcions[i];

      itemTemplate.setOnMouseClicked(event -> {
        texto_titulo.setText(category);
        boton2.setVisible(true);
        panelVBox.getChildren().clear();
        try {
          mostrar(category);
        } catch (Exception e) {
          System.out.println(e);
        }
        return;
      });
      panelVBox.getChildren().add(itemTemplate);
    }
  }

  public void loadList(String opcion) {
    AppData appData = AppData.getInstance();
    appData.load(opcion, (result) -> {
      if (result == null) {
        System.out.println("Error loading data.");
      }
    });
  }

  public void mostrar(String category) throws Exception {
    texto = category;
    JSONArray datos = AppData.getInstance().getData(category);
    URL resource = this.getClass().getResource("/assets/template_list_item.fxml");
    panelVBox.getChildren().clear();
    for (int i = 0; i < datos.length(); i++) {
      final int numero = i;
      JSONObject object = datos.getJSONObject(i);
      if (object.has("nom")) {
        FXMLLoader loader = new FXMLLoader(resource);
        Parent itemTemplate = loader.load();
        ControllerListItem itemController = loader.getController();
        itemController.setText(object.getString("nom"));
        itemController.setImage("assets/images/" + object.getString("imatge"));
        itemTemplate.setOnMouseClicked(event -> {
          texto_titulo.setText(object.getString("nom"));
          informacion.setVisible(true);
          showInfo(category, numero);
        });
        panelVBox.getChildren().add(itemTemplate);
      }
    }
  }

  void showInfo(String category, int index) {
    panelVBox.setVisible(false);
    eleccionPJC.setVisible(false);
    AppData appData = AppData.getInstance();
    JSONObject datos = appData.getItemData(category, index);
    URL resource = this.getClass().getResource("/assets/template_info_item.fxml");
    informacion.getChildren().clear();
    try {
      FXMLLoader loader = new FXMLLoader(resource);
      Parent itemTemplate = loader.load();
      ControllerInfoItem itemController = loader.getController();
      itemController.setImage("assets/images/" + datos.getString("imatge"));
      itemController.setTitle(datos.getString("nom"));
      switch (category) {
        case "Consoles":
          itemController.setText(datos.getString("procesador"));
          break;
        case "Jocs":
          itemController.setText(datos.getString("descripcio"));
          break;
        case "Personatges":
          itemController.setText(datos.getString("nom_del_videojoc"));
          break;
      }
      informacion.getChildren().add(itemTemplate);
    } catch (Exception e) {
      System.out.println(e);
    }
  }
}