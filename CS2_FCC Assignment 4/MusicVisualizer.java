import javafx.application.Application;
import javafx.stage.*;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.event.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.shape.*;
import javafx.scene.layout.*;
import java.io.File;
import javafx.scene.paint.*;
import java.util.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.input.*;
import javafx.scene.media.*;
import javafx.beans.property.*;

// Sam Reynolds
// Most of the GUI proabably wont display correctly as I did not
// have enough time to implement a scaling function to the GUI
public class MusicVisualizer extends Application
{ 
   private Pane listPane;
   private Pane musicPane;
   private Pane songPane;
   private Group data = new Group(); // PlayList data
   private Group songs = new Group(); // Group of buttons for song pane
   private ObjectButton<File> b_up;
   private MenuItem newPlay;
   private Circle addTo;              // Add to new Playlist
   private MediaPlayer player;        // Mediaplayer for playing songs
   private boolean visible = true;
   private File playingFile;         // Currently playing file
   private Label playingLabel;
   private Label play;              // Play button
   private MediaPlayer queuePlayer;
   
   private boolean playWithQueue; // Bool to check if playWithQueue is checked
   private final double PANEX = 900.0;
   private final double PANEY = 900.0;
   private double          list_x = 40.0; // 
   private double          list_y = 400.0;
   private int          num_song  = 0;
   private int             song_x = 40;
   private int             song_y = 400;
   private Color on = Color.rgb(200, 200, 200);
   private Color off = Color.rgb(100, 100, 100);
   private CheckBox cb_queue;
   
   private BST<musicFile>         searchTree       = new BST<>(); // search tree for searching for files
   private TwoWayLinkedList<File> songHistory      = new TwoWayLinkedList<>(); // every song played will be added to this list
   private Queue<File>            songQueue        = new LinkedList<>(); // Queue for queueing playlists
   private final ContextMenu      MENU             = new ContextMenu(); // Context menu for adding ne playlists
   private Queue<File>            playQueue        = new LinkedList<>(); // Queue for queueing specific songs
   
   
   
   
   public void start(Stage primaryStage)
   {
      File file = new File("C:\\Users\\My Pc\\Music\\iTunes\\iTunes Media\\Music" ); // Change this if you want a different directory
      
      listPane  = new Pane(); // Playlist pane
      musicPane = new Pane(); // Pane for displaying music
      songPane  = new Pane(); // Song pane
      listPane.getChildren().add(data);
   
      
      Group layout   = new Group(); // default layout for all the panes
      Scene scene    = new Scene(musicPane, PANEX,PANEY);
      newPlay = new MenuItem("New PlayList");
      MENU.getItems().add(newPlay);
      
      
      primaryStage.setScene(scene);
      primaryStage.show();
      musicPane.setStyle("-fx-background-color: black;");
      listPane.setStyle("-fx-background-color: black;");
      songPane.setStyle("-fx-background-color: black;");
      
      // ----- getting Files from lists and dding to panes ----- \\
      searchFiles(file, songs);
      songPane.getChildren().add(songs);
      
      if (num_song > 40)
      {
         ScrollBar sbVertical = new ScrollBar(); 
         sbVertical.setOrientation(Orientation.VERTICAL);
         songPane.getChildren().add(sbVertical);
         sbVertical.setLayoutX(3000.0);
         sbVertical.setLayoutY(200.0);
         sbVertical.toFront();
         sbVertical.setPrefHeight(1400.0);
         sbVertical.setScaleX(2.0);
         final int S = num_song;
         sbVertical.valueProperty().addListener( 
               new ChangeListener<Number>() {
                  public void changed(ObservableValue<? extends Number> ov,
                              Number old_val, Number new_val)
                  {
                     songs.setLayoutY(-new_val.doubleValue()*S/3);
                  }
                           
               });
                  
      }
   
      // ----- Black bar
      Rectangle bar = new Rectangle(0.0, 0.0, 4000.0, 190.0);
      bar.setFill(Color.rgb(50, 50, 50));
      bar.toBack();
      layout.getChildren().add(bar);
      
      // ----- Bottom Bar
      Rectangle bottomBar = new Rectangle(0.0, scene.getHeight() + 700, 4000.0, scene.getHeight() + 490);  
      bottomBar.setFill(Color.rgb(50, 50, 50));
      bottomBar.toBack();
      layout.getChildren().add(bottomBar); 
      
      // ---- Play Button **new**
      play = new Label("Pause");
      play.setLayoutX(2800.0);
      play.setLayoutY(100.0);
      play.setScaleX(3.0);
      play.setScaleY(3.0);
      play.setTextFill(off);
      layout.getChildren().add(play);
      
      
      // ---- Playing Label
      playingLabel = new Label("");
      playingLabel.setLayoutX(100.0);
      playingLabel.setLayoutY(scene.getHeight() + 650);
      playingLabel.setTextFill(Color.rgb(255, 255, 255));
      playingLabel.toFront();
      layout.getChildren().add(playingLabel);
      
      // ----- PlayList label
      Label listLabel = new Label("PlayLists");
      layout.getChildren().add(listLabel);
      listLabel.setLayoutX(200.0);
      listLabel.setLayoutY(100.0);
      listLabel.setScaleX(3.0);
      listLabel.setScaleY(3.0);
      listLabel.setTextFill(off);
      
      // ----- Music Label
      Label musicLabel = new Label("Music");
      layout.getChildren().add(musicLabel);
      musicLabel.setLayoutX(800.0);
      musicLabel.setLayoutY(100.0);
      musicLabel.setScaleX(3.0);
      musicLabel.setScaleY(3.0);
      musicLabel.setTextFill(on);
      
      // ------ Songs label
      Label songLabel = new Label("Songs");
      layout.getChildren().add(songLabel);
      songLabel.setLayoutX(1400.0);
      songLabel.setLayoutY(100.0);
      songLabel.setScaleX(3.0);
      songLabel.setScaleY(3.0);
      songLabel.setTextFill(off);
      
      // ---- Checkbox for adding stuff to the queue
      cb_queue = new CheckBox("Play using a queue");
      cb_queue.setLayoutX(2750.0);
      cb_queue.setLayoutY(30.0);
      layout.getChildren().add(cb_queue);
      cb_queue.setTextFill(Color.rgb(200, 200, 200));
            
      // ----- Search Button
      TextField search = new TextField();
      search.setPrefColumnCount(12);
      search.setLayoutX(2200.0);
      search.setLayoutY(100.0);
      Button search_b = new Button("Search");
      search_b.setLayoutX(2450.0);
      search_b.setLayoutY(100.0);
      layout.getChildren().addAll(search, search_b);
      
      
      search_b.setOnAction( 
            s_e ->
            {
               String search_t = search.getText();
               musicFile sfile = searchTree.findClosest(new musicFile(search_t));
               System.out.println(sfile);
               musicPane.getChildren().clear();
               showFiles(sfile.getFile(), musicPane, layout);
            });
      
      
      
      try {
         b_up = new ObjectButton<>(file.getParentFile(), 1700.0, 100.0, 20, Color.rgb(0, 250, 0), file.getParentFile().getName());
         System.out.println(listPane.getChildren().size());
            
                        // The "File up" button
         b_up.setOnMousePressed( 
               e ->
               {
                  musicPane.getChildren().clear();
                  showFiles(b_up.getObject(), musicPane, layout );
                  MENU.hide();
               });
         
         // ----- Displaying the number of files
         layout.getChildren().add(b_up);
         layout.getChildren().add(b_up.getLabel());
      
            
      } 
      catch (Exception e) 
      { } //Folder above the file most likely not found or allowed
      
      showFiles(file, musicPane, layout);
      
      // ----- If playlistLabel is pressed, show PlaylistPane
      listLabel.setOnMousePressed( 
            e -> 
            {
               musicLabel.setTextFill(off);
               listLabel.setTextFill(on);
               songLabel.setTextFill(off);
               b_up.setOn(false);
               visible = false;
               addTo.setVisible(visible);
                  
               listPane.getChildren().add(layout);
               scene.setRoot(listPane);
            });
         
      // ----- if Music label is pressed, show Music pane
      musicLabel.setOnMousePressed( 
            e ->
            {
               musicLabel.setTextFill(on);
               listLabel.setTextFill(off);
               songLabel.setTextFill(off);
               musicPane.getChildren().add(layout);
               b_up.setOn(true);
               visible = true;
               addTo.setVisible(visible);
            
               scene.setRoot(musicPane);
            });
      
      // ------ if songLabel is pressed, show songPane
      songLabel.setOnMousePressed( 
            e ->
            {
               musicLabel.setTextFill(off);
               listLabel.setTextFill(off);
               songLabel.setTextFill(on); 
               b_up.setOn(false);
               visible = false;
               addTo.setVisible(visible);
               songPane.getChildren().add(layout);
                  
               scene.setRoot(songPane);
            });
      
      play.setOnMousePressed( e ->
      {
         try{
         if (play.getText().equals("Pause"))
         {
            player.pause();
            queuePlayer.pause();
            play.setText("Play");
         }
         else {
            player.play();
            queuePlayer.play();
            play.setText("Pause");
         }
        
         } catch(NullPointerException nullPointer) { }            
      });
   }
   // --- Pos for last button added
   private int b_x;
   private int b_y;
   
   //Recursive method that will build the pane depending on the file given
   public void showFiles(File file, Pane pane, Group layout)
   {
      
      int b_x, b_y, r, size;
      Group g_buttons = new Group();
      Color col = Color.rgb(255, 0, 0);
      r = 20;
      b_x = 40;
      b_y = 400; 
      
      size = 0;
      File[] files = file.listFiles();
      
      b_up.setObject(file.getParentFile());
      b_up.setName(file.getName());
      
      // Do not do anything if there are no files in the file \\
      if (files != null )
      {
         for (int i = 0; i < files.length; i++)
         {
            size++;
            // create a new ObjectButton for every file found
            ObjectButton<File> button = new ObjectButton<>(files[i], b_x, b_y, r, col, files[i].getName());
            button.setOnMousePressed( 
                  ev ->
                  {
                     if (button.getObject().listFiles() != null)
                     {
                        pane.getChildren().clear();
                        showFiles(button.getObject(), pane, layout); // Direct Recursion
                     } else { if (isSong(button.getObject())) { playSong(button.getObject()); } }
                  });
            // add object button to the layout
            g_buttons.getChildren().addAll(button, button.getLabel());
            if	(b_x	< 2000)
               b_x = b_x	+ 400;
            else { 
               b_x = 40;
               b_y = b_y	+ 200;
            }
         }
         
       // ----- If list becomes too big, create a scroll bar -----
         if (b_y > 1240)
         {
            ScrollBar sbVertical = new ScrollBar(); 
            sbVertical.setOrientation(Orientation.VERTICAL);
            pane.getChildren().add(sbVertical);
            sbVertical.setLayoutX(2400.0);
            sbVertical.setLayoutY(200.0);
            sbVertical.toFront();
            sbVertical.setPrefHeight(1400.0);
            sbVertical.setScaleX(2.0);
            
            
            final int S = size;
            sbVertical.valueProperty().addListener( 
                  new ChangeListener<Number>() {
                     public void changed(ObservableValue<? extends Number> ov,
                     Number old_val, Number new_val)
                     {
                        g_buttons.setLayoutY(-new_val.doubleValue()*S/4);
                     }
                  
                  });
         }
      
      
         addTo = new Circle(20, Color.rgb(0,0,255));
         addTo.setLayoutX(b_x+400);
         addTo.setLayoutY(b_y);
         addTo.setVisible(visible);
         g_buttons.getChildren().add(addTo);
         Label l_addTo = new Label("PlayLists");
         l_addTo.setLayoutX(addTo.getLayoutX() + 30.0);
         l_addTo.setLayoutY(addTo.getLayoutY() + 50.0);
         l_addTo.setScaleX(2.0);
         l_addTo.setScaleY(2.0);
         g_buttons.getChildren().add(l_addTo);
         pane.getChildren().add(g_buttons);
         pane.getChildren().add(layout);
            
         addTo.setOnMousePressed( 
               pev ->
               {
                  MENU.show(addTo, addTo.getLayoutX(), addTo.getLayoutY());
                  newPlay.setOnAction( 
                        e ->
                        {
                        
                           ObjectButton<File> button = new ObjectButton<>(file, list_x, list_y, 20, Color.rgb(0, 255, 255), file.getName());
                           list_x = list_x + 400.0;
                           
                        
                           data.getChildren().add(button.getLabel());
                           data.getChildren().add(button);
                           
                           button.setOnMousePressed( 
                                 ev ->
                                 {
                                    if (playingFile == null || playingFile != button.getObject()) 
                                    {
                                       songQueue.clear();
                                       playingFile = button.getObject();
                                       queueSongs(button.getObject(), songQueue);
                                       
                                    }
                                    
                                       
                                    File songFile = songQueue.poll();
                                    songHistory.add(songFile);
                                    playingLabel.setText(songFile.getName());
                                    
                                    playSong(songFile);
                                       
                                 });
                        
                        });
                  
               });
                  
         
      }  
      
      
   }
   
   public void playSong(File file)
   {
      Media song;
      // Checks if the queue is selecteed
      if (!cb_queue.isSelected()) {
      if (queuePlayer != null) // checks if a queuePlayer has been instantiated 
      { 
         queuePlayer.dispose(); 
         playQueue.clear();
         queuePlayer = null;
      }
      
      playingLabel.setText(file.getName());
      if (player == null) // checks if the regular player has been instantiated
      {
         song = new Media(file.toURI().toString());
         player = new MediaPlayer(song);
         
         player.play();
         play.setTextFill(on);
         
      } else 
      {
         player.stop();
         song = new Media(file.toURI().toString());
         player = new MediaPlayer(song);
         player.play();
         play.setTextFill(on);
      }
   
   } else {
      // --- If the play using queue is checked --- \\
      if (player != null) { player.dispose(); }
      System.out.print(cb_queue.isSelected());
      if (queuePlayer == null)
      {
         playQueue.add(file);
         playingLabel.setText(file.getName());
         song = new Media(playQueue.poll().toURI().toString());
         queuePlayer = new MediaPlayer(song);
         queuePlayer.play();
         System.out.println("null");
      } else 
      
         playQueue.add(file);
         System.out.println(playQueue.size());

            queuePlayer.setOnEndOfMedia(new Runner(playQueue, queuePlayer, playingLabel));
      }
   }
   
   // ---- take the file and add any songs found in the file to the queue
   public void queueSongs(File file, Queue<File> queue)
   {
      if (file.isDirectory())
      {
         File[] files = file.listFiles();
         
         for (int i = 0; i < files.length; i++)
         {
            if (isSong(files[i]))
            {
               queue.offer(files[i]);
            } 
            else { queueSongs(files[i], queue); }
         }
      }
   }
   
   // --- Test if the file is a song by the extension
   public boolean isSong(File file)
   {
      
      String s = file.getName();
      String ext = "";
      if (s.contains("."))
      {
         ext = s.substring(s.indexOf("."));
      } 
      else { 
         return false; }
      
      if (ext.equals(".mp3") || ext.equals(".mp4") || ext.equals(".wav") || ext.equals(".m4a"))
         return true;
      else { 
         return false; }
   }
   
   // Searches for songs recursively
   public void searchFiles(File file, Group g)
   {
      if ( file.isDirectory() )
      {
         File[] files = file.listFiles();
         Color col = Color.rgb(255, 0, 0);
         searchTree.insert(new musicFile(file.getName(), file)); // adds all the files to the search tree
         for (int i = 0; i < files.length; i++)
         {
            if (!files[i].getName().equals("desktop.ini"))
            { 
            
            
               if (isSong(files[i]))   
               {
                  num_song++;
                  ObjectButton<File> button = new ObjectButton<>(files[i], song_x, song_y, 20, col, files[i].getName());
                  g.getChildren().addAll(button, button.getLabel());
               
                  if(song_x	< 2000)
                     song_x = song_x	+ 400;
                  else { 
                     song_x = 40;
                     song_y = song_y	+ 200;
                  }  
                  
                  button.setOnMousePressed( e ->
                  {
                     playSong(button.getObject());
                  });
               }   
               else {searchFiles(files[i], g);}
            }
            
         }
      } // end of for loop
   
   } //  end of Search Files method
   
   
   public static void main(String[] args)
   {
      Application.launch(args);   
   }


   class ObjectButton<E> extends Circle
   {
      private Label label;              // Label for button
      private E     object;             // Object inside of the button
      private final double SCALE = 7.0; // Scale Factor
      private Random ran = new Random();
      private String name;
   
      public ObjectButton()
      {
      
      }
      
      public ObjectButton(E object, double x, double y, double r, Color col, String name )
      {
         super( x, y, r, col);
         this.object = object;
         this.name = name;
         Color c = Color.rgb(ran.nextInt(200) + 55, ran.nextInt(200) + 55, ran.nextInt(200) + 55);
      
         int length = name.length() > 10 ? 10 : name.length();
         
         if (name.length() > 20)
         {
            name = name.substring(0, 20) + "...";
         }
         this.name = name;
         
         label = new Label(name);
         setScale(SCALE);
         setFill(c);
         label.setLayoutX(getCenterX() + 100.0);
         label.setLayoutY(getCenterY());
         label.setTextFill(Color.rgb(ran.nextInt(155) + 100, ran.nextInt(155) + 100, ran.nextInt(155) + 100));
         
         
      }
   
      public ObjectButton(E object, double x, double y, double r, Color col )
      {
         super( x, y, r, col);
         this.object = object;
         Color c = Color.rgb(ran.nextInt(200) + 55, ran.nextInt(200) + 55, ran.nextInt(200) + 55);
      
         label = new Label(name);
         setScale(SCALE);
         setFill(c);
         label.setLayoutX(getTranslateX() + getCenterX() + 100.0);
         label.setLayoutY(getTranslateY() + getCenterY());
         label.setTextFill(Color.rgb(ran.nextInt(155) + 100, ran.nextInt(155) + 100, ran.nextInt(155) + 100));
         
      }
      
      public ObjectButton(E object, Color col, String name)
      {
         setFill(col);
         this.object = object;
         this.name = name;
         label = new Label(name);
         setScale(SCALE);
         label.setTextFill(Color.rgb(ran.nextInt(155) + 100, ran.nextInt(155) + 100, ran.nextInt(155) + 100));
      
         
      }
      
      public E getObject()
      {
         return object;
      }
      
      
      // Sets the name of the label 
      public void setName(String name)
      {
         if (name.length() > 10)
         {
            name = name.substring(0, 10) + "...";
         }
         this.name = name;
         label.setText(name);
      }
      
      public Label getLabel()
      {
         return label;
      }
      // Sets the scale of the GUI button
      public void setScale( double n )
      {
         setScaleX(n/2);
         setScaleY(n/2);
         int scale = label.getText().length() > 10 ? 10 : label.getText().length();
         label.setScaleX(n*1.5 / scale);
         label.setScaleY(n*1.5 / scale);
      }
      
      // Sets the object this button holds
      public void setObject(E object)
      {
         this.object = object;
      }
      
      // sets the visibility
      public void setOn(boolean v)
      {
         setVisible(v);
         setDisabled(!v);
         label.setVisible(v);
      }
   
    
   }
   
}

// Runner class for when the MediaPlayer is done playing Media
// This was required because in order to play new Media, a new MediaPlayer has to be created
// which means everytime a new song is played, the new MediaPlayer has to have the same thread or Runnable class
// activate when it reaches the end of media
class Runner implements Runnable
{
   Queue<File> queue;
   MediaPlayer player;
   Label label;
   
   public Runner(Queue<File> queue, MediaPlayer player, Label label)
   {
      this.player = player;
      this.queue = queue;
      this.label = label;
   }
   
   public void run()
   {
      label.setText(queue.peek().getName());
      Media song = new Media(queue.poll().toURI().toString());
      player = new MediaPlayer(song);
      player.setOnEndOfMedia(this);
      player.play();
   }
   
}
