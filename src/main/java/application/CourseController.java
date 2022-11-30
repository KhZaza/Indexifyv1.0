package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.event.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.*;
import java.util.HashMap;
import java.util.Objects;
import java.util.Scanner;
import application.LoginController;


public class CourseController {
    @FXML
    Button addButton;
    @FXML
    Button renameConfirmB;
    @FXML
    Button RenameButton;
    @FXML
    Label renameLabel;
    @FXML
    Button courseBox;
    @FXML
    GridPane grid1Pane;
    @FXML
    Label warningLabel;
    @FXML
    TextField newName;
    @FXML
    TextField textField1;
    @FXML
    Integer indexRow = 0;
    @FXML
    Integer indexCol = 0;
    @FXML
    int counter;
    @FXML
    Button logoutButton;
    @FXML
    TabPane tabCourses;
    @FXML
    public Label currentUser;
    @FXML
    Button modButton;
    @FXML
    Label renameLabelText;
    @FXML
    TextField renameText;
    @FXML
    Button delButton;
    @FXML
    Button renameCancelB;
    @FXML
    Button openCourseSetB;

    @FXML
    Button accountSettings;

    public String currentUserName;
    public String currentCourseName;



    @FXML
    /**
     * Method for the creation of courses also provides each tab with the openCourseSet button.
     */
    protected void CreateClick(ActionEvent event) throws IOException
    {
        File makeDirectory = new File ("userData/"+currentUserName+"/");
        makeDirectory.mkdirs();
        File newUserFile = new File("userData/"+currentUserName+"/"+currentUserName+".TXT");


        BufferedWriter buffW = new BufferedWriter(new FileWriter(newUserFile,true));
        if(!addButton.isPressed())
        {
            counter++;
            Tab tab1 = new Tab("New Course " + counter);
            Button B = new Button();
            VBox vbox = new VBox();
            vbox.setAlignment(Pos.CENTER);
            B.setText(openCourseSetB.getText().toUpperCase());
            B.setStyle(openCourseSetB.getStyle());
            B.setOnAction(openCourseSetB.getOnAction());
            B.setTextAlignment(openCourseSetB.getTextAlignment());
            B.setTextFill(openCourseSetB.getTextFill());
            B.setFont(openCourseSetB.getFont());
            vbox.getChildren().add(B);
            tab1.setContent(vbox);
            tabCourses.getTabs().add(tab1);
            try {
                buffW.write(tab1.getText() + "\n");

                buffW.close();
                currentCourseName = tabCourses.getSelectionModel().getSelectedItem().getText();
            }
            catch (IOException ex) {
                throw new RuntimeException(ex);
            }


        }

    }


    /**
     * Initializes all the courses and adds the buttons to them.
     * @throws IOException
     */
    @FXML
    public void initialize() throws IOException
    {
        BufferedReader buffR = new BufferedReader(new FileReader("currentUser.txt"));
        currentUserName = buffR.readLine();
        buffR.close();
        File existingFile = new File("userData/"+currentUserName+"/"+currentUserName + ".txt");
        if(existingFile.exists())
        {
            BufferedReader courseReader = new BufferedReader(new FileReader(existingFile));
            String line = null;
            while ((line = courseReader.readLine()) != null) {
                Tab tab1 = new Tab(line);
                VBox vbox = new VBox();
                vbox.setAlignment(Pos.CENTER);
                Button B = new Button();
                B.setText("OPEN COURSE SET");
                B.setId(tab1.getText()+"Button");
                B.setStyle(openCourseSetB.getStyle());
                B.setTextFill(openCourseSetB.getTextFill());
                B.setFont(openCourseSetB.getFont());
                B.setOnAction(openCourseSetB.getOnAction());
                vbox.getChildren().add(B);
                tab1.setContent(vbox);
                tabCourses.getTabs().add(tab1);
            }
            courseReader.close();
        }


    }

    /**
     * Opens the account settings page.
     */
    @FXML
    public void accountSettingsClick() {
        Parent root;
        try
        {
            root = FXMLLoader.load(Main.class.getResource("accountSettings.fxml"));
            Stage stage = (Stage) accountSettings.getScene().getWindow();
            stage.setTitle("Account Settings");
            stage.setScene(new Scene(root,620, 400));
            stage.setResizable(false);
            stage.show();

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }

    @FXML
    protected void DeleteClick()
    {
        if(!delButton.isPressed())
        {
            Tab tab1 = tabCourses.getSelectionModel().getSelectedItem();
            File deleteFile = new File ("userData/"+currentUserName+"/"+currentUserName+tab1.getText()+".txt");
            if(deleteFile.exists())
            {
                deleteFile.delete();
            }
            removeLineFromFile(tab1.getText());
            tabCourses.getTabs().remove(tab1);
        }
    }


    @FXML
    protected void RenameClick(ActionEvent event)
    {
        if(!modButton.isPressed())
        {
            renameConfirmB.setVisible(true);
            renameLabelText.setVisible(true);
            renameText.setVisible(true);
            renameCancelB.setVisible(true);
        }
    }

    /**
     * The main function of the renaming a course.
     * @throws IOException
     */
    @FXML
    protected void renameConfirmButtonPress() throws IOException
    {
        if(!renameConfirmB.isPressed())
        {
            File readCoursesFile = new File("userData/" + currentUserName + "/" + currentUserName + ".txt");

            BufferedReader buffR = new BufferedReader(new FileReader("currentUser.txt"));

            String currentUserName = buffR.readLine();

            Scanner courseReader = new Scanner(readCoursesFile);

            StringBuffer buffer = new StringBuffer();

            while(courseReader.hasNextLine())
            {
                buffer.append(courseReader.nextLine()+System.lineSeparator());

            }

            String listOfCourses = buffer.toString();
            String oldName = tabCourses.getSelectionModel().getSelectedItem().getText();
            String newName = renameText.getText();
            listOfCourses = listOfCourses.replaceFirst(oldName, newName);

            FileWriter writer = new FileWriter("userData/"+currentUserName+"/"+currentUserName+".txt");
            writer.write(listOfCourses);
            writer.flush();
            writer.close();

            //This is to check if the file already has a course file saved to the user Data
            File ifFileExists = new File("userData/"+currentUserName+"/"+currentUserName+oldName+".txt");

            Tab tab1 = tabCourses.getSelectionModel().getSelectedItem();
            tab1.setText(renameText.getText());

            //The above is important for this block because then we can rename the course file to the newly renamed course without losing data.
            if(ifFileExists.exists())
            {
                File nameFileChange = new File("userData/"+currentUserName+"/" + currentUserName+tab1.getText() + ".txt");
                ifFileExists.renameTo(nameFileChange);
            }
            buffR.close();
            courseReader.close();
            renameConfirmB.setVisible(false);
            renameLabelText.setVisible(false);
            renameText.setVisible(false);
            renameCancelB.setVisible(false);
        }
    }


    @FXML
    public void renameCancel()
    {
        renameConfirmB.setVisible(false);
        renameLabelText.setVisible(false);
        renameText.setVisible(false);
        renameCancelB.setVisible(false);
    }

    /**
     * Provides the functionality for logging out, and wipes all data holding so that nothing carries over to the next user that logs in.
     */
    @FXML
    public void logoutButton() {
        Parent root;
        try
        {
            File file1 = new File("currentUser.txt");
            File file2 = new File("currentCourseSelected.txt");
            BufferedWriter buff2 = new BufferedWriter(new FileWriter(file2));
            BufferedWriter buff1 = new BufferedWriter(new FileWriter(file1));
            buff2.write("");
            buff1.write("");
            buff1.close();
            buff2.close();
            root = FXMLLoader.load(Main.class.getResource("login.fxml"));
            Stage stage = (Stage) logoutButton.getScene().getWindow();
            stage.setTitle("Indexify");
            stage.setScene(new Scene(root,530, 400));
            stage.setResizable(false);
            stage.show();

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }

    public void removeLineFromFile(String courseToRemove) {

        try {

            BufferedReader buffR = new BufferedReader(new FileReader("currentUser.txt"));
            currentUserName = buffR.readLine();


            File inFile = new File("userData/"+currentUserName+"/"+currentUserName +".txt");
            BufferedReader br = new BufferedReader(new FileReader(inFile));

            if (!inFile.isFile()) {
                System.out.println("Parameter is not an existing file");
                return;
            }

            //Construct the new file that will later be renamed to the original filename.
            File tempFile = new File(inFile.getAbsolutePath() + ".tmp");

            //BufferedReader br = new BufferedReader(new FileReader());
            PrintWriter pw = new PrintWriter(new FileWriter(tempFile));

            String line = null;

            //Read from the original file and write to the new
            //unless content matches data to be removed.
            while ((line = br.readLine()) != null) {

                if (line.trim().indexOf(courseToRemove) == -1) {

                    pw.println(line);
                    pw.flush();
                }


            }
            pw.close();
            br.close();

            //Delete the original file
            inFile.delete();
            //Rename the new file to the original file
            tempFile.renameTo(inFile);


        }
        catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Opens the course set for the selected course, also creates the file that it will be saved under.
     * @param event
     * @throws Exception
     */
    @FXML
    public void openCourseSet(ActionEvent event) throws Exception {
        try {

            String courseFileName = tabCourses.getSelectionModel().getSelectedItem().getText();

            File currentCourse = new File("currentCourseSelected.txt");

            FileWriter courseWriter = new FileWriter(currentCourse);
            courseWriter.write(courseFileName);
            courseWriter.close();


            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("indexCardViewer.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle(currentCourseName+" Viewer");
            stage.setScene(new Scene(root1));
            stage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }


}