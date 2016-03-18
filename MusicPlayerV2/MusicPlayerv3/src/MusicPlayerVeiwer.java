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
import java.util.*;

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
    private final double TEXT_SPACING = RESOLUTION_X / 3; // Spacing between the labels and Music buttons
    private final double BUTTONINDENT = 0.5;
    
    
    // Javafx Panes for all the differnt sections in the program
    private Pane playListPane   = new Pane();
    private Pane songPane       = new Pane();
    private Pane musicPane      = new Pane();
    private Group defaultLayout = new Group();
    private Scene mainScene     = new Scene(musicPane, RESOLUTION_X, RESOLUTION_Y);
    
    
    
    // Geometry for panes
    
    // Basic Layout
    private final Color     BLACK = Color.BLACK;
    private final Color     WHITE = Color.WHITE;
    private final Color     GREY  = Color.DIMGRAY;
    private final Rectangle TOPBAR = new Rectangle(0,0, RESOLUTION_X, RESOLUTION_Y / SCALE );
    private final Rectangle BOTTOMBAR = new Rectangle(0, RESOLUTION_Y / SCALE, RESOLUTION_X, RESOLUTION_Y / SCALE);
    private final labelButton<Pane>     PLAYLISTLABEL = new labelButton<>("Playlist", playListPane, BUTTONINDENT);
    private final labelButton<Pane>     MUSICLABEL    = new labelButton<>("Music", musicPane, BUTTONINDENT);
    private final labelButton<Pane>     SONGLABEL     = new labelButton<>("Songs", songPane, BUTTONINDENT);
    
    @Override
    public void start(Stage stage)
    {
        // Setting the color of the geometry
        PLAYLISTLABEL.setTextFill(WHITE);
        SONGLABEL.setTextFill(WHITE);
        MUSICLABEL.setTextFill(WHITE);
        TOPBAR.setFill(BLACK);
        BOTTOMBAR.setFill(GREY);
        System.out.print(RESOLUTION_Y / ( 3 * SCALE));
        System.out.println(TEXT_SPACING);
        setPos(SONGLABEL, TEXT_SPACING * 2,     (RESOLUTION_Y / ( SCALE * 3 )));
        setPos(MUSICLABEL, TEXT_SPACING * 1.5,  (RESOLUTION_Y / ( SCALE * 3 )));
        setPos(PLAYLISTLABEL, TEXT_SPACING,     (RESOLUTION_Y / ( SCALE * 3 )));
        setScale(SONGLABEL, SCALE / 2);
        setScale(MUSICLABEL, SCALE / 2);
        setScale(PLAYLISTLABEL, SCALE / 2);
        
        mainScene.setRoot(musicPane);
        stage.setScene(mainScene);
        
        PLAYLISTLABEL.setOnMouseClicked( me ->
        {
            System.out.print("Clicked");
        });
        
        addToLayout(TOPBAR);
        addToLayout(BOTTOMBAR);
        addToLayout(PLAYLISTLABEL);
        addToLayout(SONGLABEL);
        addToLayout(MUSICLABEL);
        addToPane(songPane, defaultLayout);
        addToPane(playListPane, defaultLayout);
        addToPane(musicPane, defaultLayout);
        
        
        
        System.out.println(defaultLayout.getChildren());
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

// labelButton class covers all the GUI functions of when the button is pressed
class labelButton<E> extends Label
{
    E e; // Object associated with this button
    double indent = 0.2;
    final Color ORIGINAL = Color.WHITE;
    final Color PRESSED  = Color.LIGHTGRAY; 
    
    public labelButton(String lbl)
    {
        super(lbl);
        setIndent(indent);
    }
    
    public labelButton(String lbl, E e)
    {
        super(lbl);
        this.e = e;
        setIndent(indent);
    }
    
    public labelButton(String lbl, E e, double indent)
    {
        super(lbl);
        this.e = e;
        this.indent = indent;
        setIndent(indent);
    }
    
    public E getObject()
    {
        return e;
    }
    
    private void setIndent(double indent)
    {
        this.indent = indent;
        setOnMousePressed( ev ->
        {
            setScaleX(getScaleX() - indent);
            setScaleY(getScaleY() - indent);
            setTextFill(PRESSED);
        });
        
        setOnMouseReleased( ev -> 
        {
            setScaleX(getScaleX() + indent);
            setScaleY(getScaleY() + indent);
            setTextFill(ORIGINAL);
        });
    }
}
