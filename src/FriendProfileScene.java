import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class FriendProfileScene {
  private Model model;
  private Scene scene;
  private String name;
  private ApiQuery api;

  @FXML
  private Button profile_back_button;

  @FXML
  private Label username_text;

  @FXML
  private Label friends_name_textfield;

  @FXML
  private ListView services_list;

  @FXML
  private VBox movie_ratings_vbox;

  @FXML
  private AnchorPane anchor;

  @FXML
  private ImageView profile_picture;

  @FXML
  private VBox desired_movies_vbox;

  @FXML
  private AnchorPane anchorDesired;

  public FriendProfileScene(Model newModel, String friendName, Scene scene) {
    model = newModel;
    name = friendName;
    this.scene = scene;
    api = new ApiQuery();
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("FriendProfileScene.fxml"));

      loader.setController(this);

      model.stage.setScene(new Scene(loader.load()));

      model.stage.setTitle("MovieNight - FriendProfileScene");
    } catch (IOException e) {
      e.printStackTrace();
    }

    setUpProfileDetails();
    setUpStreamingServices();
    setUpMovieRatingsBox();
    setUpDesiredMovieBox();

  }

  public void pressedBackButton(ActionEvent event) throws IOException {
    model.stage.setScene(scene);
  }

  public void setUpProfileDetails() {
    Map<String, Object> friendInfo = Server.getAttributes(name);
    System.out.println(friendInfo);
    username_text.setText(friendInfo.get("username").toString());
    friends_name_textfield.setText(friendInfo.get("first_name").toString() + " " +
        friendInfo.get("last_name").toString() + "'s Profile");
    profile_picture.setImage(ProfilePicture.getProfilePic(name));
  }

  private void setUpMovieRatingsBox(){
    HashMap<String, Map<String, Object>> fav_movies =
        Server.getUsersFavouriteMovies(name);

    Iterator movie_it = fav_movies.entrySet().iterator();
    while (movie_it.hasNext()) {
      Map.Entry pair = (Map.Entry) movie_it.next();
      Movie movie = api.getMovie(pair.getKey().toString());

      HBox movie_collection = new HBox();
      ImageView movie_poster =
          new ImageView(new Image(movie.getMoviePosterUrl(), 75, 112, false, false));
      Button movie_button = new Button();
      movie_button.setGraphic(movie_poster);
      movie_button.setMaxSize(75,112);
      movie_button.setOnAction(
          actionEvent -> {altMovieScene movieScene = new altMovieScene(model, movie, model.stage.getScene());

          });
      VBox info = new VBox();
      Label movie_title = new Label(pair.getKey().toString());
      Label movie_rating = new Label(fav_movies.get(pair.getKey()).get("rating").toString());

      movie_title.setMinSize(400, 10);
      movie_title.setFont(new Font(20));
      info.getChildren().addAll(movie_title, movie_rating);
      info.setSpacing(15);

      movie_collection.getChildren().addAll(movie_button, info);

      anchor.setMinHeight(anchor.getMinHeight() + 120);
      movie_ratings_vbox.setMinHeight(movie_ratings_vbox.getMinHeight() + 120);
      movie_ratings_vbox.getChildren().add(movie_collection);

    }
  }

  private void setUpDesiredMovieBox(){
    HashMap<String, Map<String, Object>> fav_movies =
        Server.getUsersWantToWatchMovies(name);

    Iterator movie_it = fav_movies.entrySet().iterator();
    while (movie_it.hasNext()) {
      Map.Entry pair = (Map.Entry) movie_it.next();
      Movie movie = api.getMovie(pair.getKey().toString());
      HBox movie_collection = new HBox();
      ImageView movie_poster =
          new ImageView(new Image(movie.getMoviePosterUrl(), 75, 112, false, false));
      Button movie_button = new Button();
      movie_button.setGraphic(movie_poster);
      movie_button.setMaxSize(75,112);
      movie_button.setOnAction(
          actionEvent -> {altMovieScene movieScene = new altMovieScene(model, movie, model.stage.getScene());

          });
      VBox info = new VBox();

      Label movie_title = new Label(pair.getKey().toString());
      if(User.getMovieList().movieNames.contains(pair.getKey().toString())){
        movie_title.setStyle("-fx-font-weight: bold");
      }
      movie_title.setMinSize(400, 10);
      movie_title.setFont(new Font(20));
      info.getChildren().addAll(movie_title);

      movie_collection.getChildren().addAll(movie_button, info);

      anchorDesired.setMinHeight(anchorDesired.getMinHeight() + 120);
      desired_movies_vbox.setMinHeight(desired_movies_vbox.getMinHeight() + 120);
      desired_movies_vbox.getChildren().add(movie_collection);
    }
  }

  private void setUpStreamingServices(){
    ObservableList<String> services = FXCollections.observableArrayList();
    services.addAll(Server.getUsersStreamingServices(name).keySet());
    services_list.setItems(services);
  }
}
