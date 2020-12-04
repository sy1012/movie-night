import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddMovieToEventScene {
  private Model model;
  private Stage movieEventStage;
  private Map<String, Map<String, Object>> eventMap;
  private Map<String, String> reverseEventMap = new HashMap<>();
  private Movie movie;

  @FXML
  private Button back_button;

  @FXML
  private Label title_label;

  @FXML
  private ComboBox event_list;

  @FXML
  private Button add_button;

  private String sentRequestName;

  public AddMovieToEventScene(Model newModel, Movie movie) {
    model = newModel;

    this.movie = movie;

    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("AddMovieToEventScene.fxml"));

      loader.setController(this);

      movieEventStage = new Stage();

      movieEventStage.setScene(new Scene(loader.load()));
      movieEventStage.setTitle("MovieNight - AddMovieToEventScene");

      title_label.setText("Adding " + movie.getMovieName() + " to an Event:");

      eventMap = Server.getUsersOrganizingEvents(User.getUserName());
      ObservableList<String> eventNames = FXCollections.observableArrayList();

      for(String key : eventMap.keySet()){
        eventNames.add((String)(eventMap.get(key).get("eventName")));
        reverseEventMap.put(eventMap.get(key).get("eventName").toString(), key);
      }

      event_list.setItems(eventNames);

      movieEventStage.showAndWait();

    } catch (IOException e) {
      e.printStackTrace();
    }


  }

  public void pressedBackButton(ActionEvent event) throws IOException {
    movieEventStage.close();
  }

  public void addMovieToEvent(){
    String pickedEvent = event_list.getValue().toString();
    Server.addEventMovie(movie.getMovieName(), reverseEventMap.get(pickedEvent));
    System.out.println(Server.getEventMovies(reverseEventMap.get(pickedEvent)));
    movieEventStage.close();
  }


}