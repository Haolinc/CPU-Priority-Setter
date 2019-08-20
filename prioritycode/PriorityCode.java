import java.io.PrintWriter;
import java.io.File;
import javafx.scene.layout.Pane;
import javafx.scene.Scene;
import javafx.application.Application;
import javafx.scene.control.Button;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.FileChooser;
import javafx.event.*;
import javafx.stage.Stage;
import javafx.scene.control.ChoiceBox;
import javafx.scene.text.Text;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;

public class PriorityCode extends Application {
  final String currpath = System.getProperty("user.dir");
  public void shortcut(String dir, String fileName, String priority) throws Exception {
    Runtime run = Runtime.getRuntime();

    PrintWriter script= new PrintWriter ("shortcut.vbs");
    script.println ("Set sh = CreateObject(\"WScript.Shell\")");
    script.println ("Set shortcut = sh.CreateShortcut (\""+currpath+"\\"+fileName+"lnk\")");
    script.println ("shortcut.TargetPath = \"C:\\Windows\\System32\\cmd.exe\"");
    script.print ("shortcut.Arguments = \"/c start \"\"\"\" ");
    if (priority.equals("High")) {
      script.println ("/High \"\""+dir+"\"\"\"");
    }
    if (priority.equals("Above Normal")) {
      script.println ("/AboveNormal \"\""+dir+"\"\"\"");
    }
    if (priority.equals("Normal")) {
      script.println ("/Normal \"\""+dir+"\"\"\"");
    }
    if (priority.equals("Below Normal")) {
      script.println ("/BelowNormal \"\""+dir+"\"\"\"");
    }
    if (priority.equals("Low")){
      script.println ("/Low \"\""+dir+"\"\"\"");
    }
    script.println ("shortcut.IconLocation = \""+dir+"\"");
    script.println ("shortcut.Save");
    script.close();
    try {
      Process proc = run.exec("cmd /c cd "+currpath+" && cscript shortcut.vbs && del shortcut.vbs");
      Stage sucstg = new Stage();
      Button ok = new Button();
      ok = okBut(sucstg);
      Text suctxt = new Text("Success");
      suctxt.setFont(new Font(13.5));
      VBox sucpane = new VBox(10);
      sucpane.setAlignment(Pos.CENTER);
      sucpane.getChildren().addAll(suctxt, ok);
      Scene sucscene = new Scene(sucpane, 155, 100);
      sucstg.setTitle("Success");
      sucstg.setScene(sucscene);
      sucstg.show();
    } catch (Exception e) {
      e.printStackTrace();
    }
    script.checkError();
  }

  public Button okBut(Stage stg) {
    Button ok = new Button("Ok");
    ok.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent e) {
        stg.close();
      }
    });
    return ok;
  }

  public void start(Stage primaryStage) throws Exception {
    FileChooser fc = new FileChooser();
    fc.setTitle("Choose Applicaiton");
    fc.getExtensionFilters().addAll (
      new ExtensionFilter ("Exe Files", "*.exe")
    );
    File [] file = new File [1];

    TextField appLoc = new TextField (currpath);
    appLoc.setEditable(false);
    appLoc.setLayoutX(65);
    appLoc.setLayoutY(5);

    Button browse = new Button ("Browse");
    browse.setLayoutX(5);
    browse.setLayoutY(5);
    browse.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle (ActionEvent e) {
        file[0]=fc.showOpenDialog(primaryStage);
        if (file[0]!=null)
          appLoc.setText(file[0].getPath());
      }
    });

    Text pritxt = new Text("CPU Priority:");
    pritxt.setLayoutY(65);
    pritxt.setLayoutX(10);
    pritxt.setFont(new Font(13));
    ChoiceBox<String> cb = new ChoiceBox();
    cb.getItems().addAll("High", "Above Normal", "Normal", "Below Normal", "Low");
    cb.setLayoutY(50);
    cb.setLayoutX(85);

    Button process = new Button("Process");
    process.setLayoutY(50);
    process.setLayoutX(230);
    process.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent e) {
        try {
          String priority = cb.getValue();
          String dir = file[0].getPath();
          System.out.println(dir);
          String fileName = file[0].getName().substring(0, file[0].getName().length()-3);
          System.out.println(fileName);
          shortcut(dir, fileName, priority);
        }
        catch (Exception ea) {
          Stage errwin = new Stage();
          Button ok = new Button();
          ok = okBut(errwin);
          VBox errpane = new VBox(10);
          errpane.setAlignment(Pos.CENTER);
          Text errtxt = new Text("You have to select an\n Exe file and prioirty");
          errtxt.setFont(new Font(13.5));
          errpane.getChildren().addAll(errtxt, ok);
          Scene errscene = new Scene(errpane, 155, 100);
          errwin.setTitle("Error");
          errwin.setScene(errscene);
          errwin.show();
        }
      }
    });

    Pane pane = new Pane();
    pane.getChildren().addAll(cb, process, browse, pritxt, appLoc);

    Scene scene = new Scene(pane, 300, 100);
    primaryStage.setTitle("Setting Applicaiton Priority");
    primaryStage.setScene(scene);
    primaryStage.show();
  }

  public static void main (String [] args){
    launch(args);
  }
}
