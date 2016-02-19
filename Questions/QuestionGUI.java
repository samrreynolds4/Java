import java.util.*;
import java.io.*;
import javafx.application.Application;
import javafx.stage.*;
import javafx.scene.shape.*;
import javafx.scene.*;
import javafx.stage.*;
import javafx.stage.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.*;
import javafx.scene.input.*;
import javafx.scene.paint.*;
import javafx.scene.text.*;

// Personal Project:
// A question system that asks the user for a question, then formats the question and looks to see if theres an entry
// in the map data structure. If there is not, then it will prompt the user for the answer to the question
// I plan on it writing all the answers and questions to a .dat file so users exit the program and come back
// later to ask/write more questions. 

// I meant for this to be a journal in a sense, allowing people to ask their past selves specefic questions to see the response they made.

class FutureQuestions
{  
   public Map<String, String> questionMap = new HashMap<>();
   public FileOutputStream fileOutput;
   public DataOutputStream output;
   public int size;
   
   public FutureQuestions()
   {
      size = 0;
      try
      {
         fileOutput = new FileOutputStream( new File("C:\\Users\\My Pc\\Desktop\\Questions\\Questions.dat"), true);
         output = new DataOutputStream(fileOutput);
      } catch(Exception ex) { ex.printStackTrace(); }   
   }
   
   public String ask(String msg)
   {
      String copy = parseString(msg);
      
      if(questionMap.containsKey(copy))
      {
         return questionMap.get(copy);
      } else
      {
         return "";
      }
   }
   
   public void addAnswer(String question, String answer)
   {
      System.out.println(parseString(question));
      String writeTo = question + "-+-" + answer;
      System.out.println(writeTo);
      try 
      {
         output.writeUTF(writeTo);
      } catch(Exception ex) { ex.printStackTrace(); }
      questionMap.put(parseString(question), answer);
   }
   
   public boolean hasAnswer(String msg)
   {
      
      return questionMap.containsKey(parseString(msg));
   }
   
   public String parseString(String msg)
   {
      String copy = "";
      char[] charArray = msg.toCharArray();
      
      for( int i = 0; i < charArray.length - 1; i++)
      {
         if(charArray[i] != charArray[i+1])
         {
           copy = copy + charArray[i];
         }
      }  
      
      
      copy = copy.toUpperCase();
      copy = copy.replaceAll("'", "");
      //copy = copy.replaceAll("?", "");
      copy = copy.replaceAll("WHATS", "WHAT");
      copy = copy.replaceAll("HOWS", "HOW");
      copy = copy.replaceAll("WHENS", "WHEN");
      copy = copy.replaceAll(" ", "");
      copy = copy.replaceAll("AM I", "ARE YOU");
      copy = copy.replaceAll(" U ", "YOU");
      System.out.println(copy);
      
      return copy;
   }
}

public class QuestionGUI extends Application
{

   public BorderPane mainPane;
   public VBox textList;
   public Scene scene;
   public FutureQuestions answerMachine;
   public String copy;
   public boolean prompting, addAnswer;
   
   //Text Objects
   public TextField questionArea;
   public TextField answerArea;
   public String question;
   
   //Button objects
   public Button yes_b;
   public Button no_b;
   public Button enter_b;
   
   public void start(Stage primStage)
   {
      //***** Setting up the GUI Area
      mainPane = new BorderPane();
      scene = new Scene(mainPane, 600, 800);
      
      mainPane.setStyle("-fx-background-color: black;");
      primStage.setScene(scene);
      primStage.show();
      
      answerMachine = new FutureQuestions();
      mainPane.setPadding(new Insets(12.0, 12.0, 12.0, 12.0));
      
      //******Text objects
      questionArea = new TextField();
      questionArea.setEditable(true);      
      questionArea.setBackground(new Background(new BackgroundFill(Color.BLACK, new CornerRadii(1.0), Insets.EMPTY))); 
      questionArea.setStyle("-fx-background-color: white;");
      answerArea = new TextField();
     
     
      mainPane.setTop(answerArea);
      mainPane.setBottom(questionArea);
      
      //******Button objects
      textList = new VBox();
      mainPane.setCenter(textList);
      //Insets(top, right, bottom, left)
      textList.setPadding(new Insets(12.0, 48.0, 12.0, 48.0));
      textList.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY))); 
      
      
      questionArea.setOnKeyPressed( e ->
      {
         if(e.getCode() == KeyCode.ENTER)
         {
                
                if(prompting) 
                { 
                   answerMachine.addAnswer(copy, questionArea.getText());
                   prompting = false;
                   answerArea.setText("");
                }
                else 
                {
                
                   if(!prompting && answerMachine.hasAnswer(questionArea.getText()))
                   {
                     answerArea.setText(answerMachine.ask(questionArea.getText()));
                     addText(answerMachine.ask(questionArea.getText()));
                   }
                   else 
                   {
                      
                      answerArea.setText("No Answer found. Please type your answer and hit enter");
                      copy = questionArea.getText();
                      prompting = true;
                   }
                }
                
                questionArea.setText("");
                
         }
            
            
          
         });
      
      primStage.show();
   }
   
   public void addText(String msg)
   {
      Text t_msg = new Text(msg);
      t_msg.setScaleX(2.0);
      t_msg.setScaleY(2.0);
      t_msg.setFill(Color.WHITE);
      textList.getChildren().add(t_msg);
      textList.setMargin(t_msg, new Insets(5.0, 1.0, 5.0, 1.0));
   }
   
   public QuestionGUI()
   {}
   
   public static void main(String[] args)
   {
      Application.launch(args);
   }
}