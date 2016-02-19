import javafx.scene.*;
import javafx.scene.paint.*;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.*;
import javafx.scene.layout.*;
import javafx.scene.image.*;
import javafx.scene.input.*;
import javafx.scene.media.*;
import javafx.application.Application;
import javafx.stage.*;
import javafx.event.*;
import javafx.geometry.*;
import java.awt.Toolkit;
import java.awt.Dimension;
import java.io.File;

import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;


public class MusicPlayerVeiwer extends Application
{
    private File musicFile = new File("C:\\Users\\My Pc\\Music\\iTunes\\iTunes Media\\Music\\Built to Spill\\Untethered Moon");
    private MusicPlayerProcessor musicProcessor = new MusicPlayerProcessor(musicFile);
    private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private final double RESOLUTION_X = screenSize.getWidth();
    private final double RESOLUTION_Y = screenSize.getHeight();
    private final double SCALE = 8; // Scalling the geometry
    private final double TEXT_SPACING = RESOLUTION_X / 4; // Spacing between the labels and Music buttons
    
    // Javafx Panes for all the differnt sections in the program
    private Pane playListPane   = new Pane();
    private Pane songPane       = new Pane();
    private Pane explorePane    = new Pane();
    private Group defaultLayout = new Group();
    private Scene mainScene     = new Scene(explorePane, RESOLUTION_X, RESOLUTION_Y);
    
    
    
    // Geometry for panes
    
    // Basic Layout
    private final Color     BLACK = Color.BLACK;
    private final Color     WHITE = Color.WHITE;
    private final Color     GREY  = Color.DIMGRAY;
    private final Rectangle TOPBAR = new Rectangle(0,0, RESOLUTION_X, RESOLUTION_Y / SCALE );
    private final Rectangle BOTTOMBAR = new Rectangle(0, RESOLUTION_Y - RESOLUTION_Y / SCALE, RESOLUTION_X, RESOLUTION_Y / SCALE);
    private final Label     PLAYLISTLABEL = new Label("Playlist");
    private final Label     MUSICLABEL    = new Label("Music");
    private final Label     SONGLABEL     = new Label("Songs");
    
    @Override
    public void start(Stage stage)
    {
        // Setting the color of the geometry
        PLAYLISTLABEL.setTextFill(WHITE);
        SONGLABEL.setTextFill(WHITE);
        MUSICLABEL.setTextFill(WHITE);
        TOPBAR.setFill(GREY);
        BOTTOMBAR.setFill(GREY);
        
        setPos(SONGLABEL, TEXT_SPACING * 4,     (RESOLUTION_Y / TEXT_SPACING) + TEXT_SPACING / SCALE);
        setPos(MUSICLABEL, TEXT_SPACING * 3,    (RESOLUTION_Y / TEXT_SPACING) + TEXT_SPACING / SCALE);
        setPos(PLAYLISTLABEL, TEXT_SPACING * 2, (RESOLUTION_Y / TEXT_SPACING) + TEXT_SPACING / SCALE);
        setScale(SONGLABEL, SCALE);
        setScale(MUSICLABEL, SCALE);
        setScale(PLAYLISTLABEL, SCALE);
        
        
        
        stage.setScene(mainScene);
        addToLayout(TOPBAR);
        addToLayout(BOTTOMBAR);
        addToLayout(PLAYLISTLABEL);
        addToLayout(SONGLABEL);
        addToLayout(MUSICLABEL);
        addToPane(explorePane, defaultLayout);
        
        stage.show();
    }
    
    public void addToPane(Pane pane, javafx.scene.Node node)
    {
        pane.getChildren().add(node);
    }
    
    public void setScale(javafx.scene.Node node, double scale)
    {
        node.setScaleX(scale);
        node.setScaleY(scale);
    }
    
    public void setPos(javafx.scene.Node node, double x, double y)
    {
        node.setLayoutX(x);
        node.setLayoutY(y);
    }
    
    public void addToLayout(javafx.scene.Node node)
    {
        defaultLayout.getChildren().add(node);
    }
    
    public static void main(String[] args)
    {
        Application.launch(args);
    }
    
    
}
