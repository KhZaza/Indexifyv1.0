package application;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;

public class IndexCardController implements Initializable {
    @FXML
    Rectangle card;

    @FXML
    ImageView editButton;

    @FXML
    CheckBox checkBox;

    @FXML
    Button createCard;

    @FXML
    Button nextButton;

    @FXML
    Button previousButton;

    @FXML
    Label text;

    @FXML
    TabPane courseTab;

    @FXML
    Text cardCount;

    @FXML
    Group scene;

    @FXML
    TextField counterBox;

    @FXML
    Button reviewLearnedB;
    @FXML
    Button deleteCardB;

    Set set;

    String currentUser;
    String currentCourse;
    Boolean currentCheck;


    /**
     * Main function for the edit button in the IndexCardViewer FXML file.  Allows the card to edit itself.
     * @param e
     */
    @FXML
    public void editButtonClicked(Event e) {

        Stage stage = new Stage();
        stage.setTitle("Edit Card");

        Pane root = new Pane();
        Scene scene = new Scene(root, 400, 300);

        Label termLabel = new Label("Term: ");
        TextField termField = new TextField(set.getCard().getFront());

        Label definitionLabel = new Label("Definition: ");
        TextField definitionField = new TextField(set.getCard().getBack());

        Button submit = new Button("Submit");
        submit.setStyle("-fx-text-fill: white; -fx-background-color: #a6051a");

        submit.setOnAction((actionEvent) -> {
            if(!(termField.getText().isEmpty() || definitionField.getText().isEmpty())){
                //boolean selected = !checkBox.isSelected();
                if(!set.getCard().getFront().equalsIgnoreCase("EDIT THIS CARD"))
                {
                    removeLineFromFile(set.getCard().getFront());
                }
                set.updateCard(termField.getText(), definitionField.getText(), checkBox.isSelected());

                File currentCourseFile = new File("userData/" + currentUser + "/" + currentUser+currentCourse+".txt");
                BufferedWriter currentCourseWriter = null;
                try {
                   currentCourseWriter = new BufferedWriter(new FileWriter(currentCourseFile,true));
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                currentCheck = checkBox.isSelected();
                try {
                    currentCourseWriter.write(termField.getText()+","+definitionField.getText()+","+currentCheck.toString()+",");
                    currentCourseWriter.close();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }

                stage.close();
            }
        });

        HBox termBox = new HBox();
        termLabel.setPadding(new Insets(5, 20, 0, 0));
        termBox.getChildren().addAll(termLabel, termField);
        termBox.setPadding(new Insets(20, 0, 20, 30));

        HBox defBox = new HBox();
        definitionLabel.setPadding(new Insets(5, 20, 0, 0));
        defBox.getChildren().addAll(definitionLabel, definitionField);
        defBox.setPadding(new Insets(20, 0, 20, 30));

        HBox submitBox = new HBox();
        submitBox.getChildren().addAll(submit);
        submitBox.setPadding(new Insets(20, 0, 20, 30));

        VBox vbox = new VBox();
        vbox.getChildren().addAll(termBox, defBox, submitBox);

        root.getChildren().addAll(vbox);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Method for the creation of a card.  Will open another stage in order to create a new card.
     * @param e
     */
    @FXML
    public void createButtonClicked(Event e){

        Stage stage = new Stage();
        stage.setTitle("Create Card");

        Pane root = new Pane();
        Scene scene = new Scene(root, 400, 300);

        Label termLabel = new Label("Term: ");
        TextField termField = new TextField();

        Label definitionLabel = new Label("Definition: ");
        TextField definitionField = new TextField();

        Button submit = new Button("Create Card");
        submit.setStyle("-fx-text-fill: white; -fx-background-color: #a6051a");

        submit.setOnAction((actionEvent) -> {
            if(!(termField.getText().isEmpty() || definitionField.getText().isEmpty())){
                set.addCard(termField.getText(), definitionField.getText(), false);
                File courseRead = new File("userData/" + currentUser + "/" + currentUser+currentCourse+".txt");
                try {
                    BufferedWriter courseFileWriter = new BufferedWriter(new FileWriter(courseRead,true));
                    courseFileWriter.write(termField.getText()+","+definitionField.getText()+","+"false,");
                    courseFileWriter.write("\n");
                    courseFileWriter.close();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                stage.close();
            }
        });

        HBox termBox = new HBox();
        termLabel.setPadding(new Insets(5, 20, 0, 0));
        termBox.getChildren().addAll(termLabel, termField);
        termBox.setPadding(new Insets(20, 0, 20, 30));

        HBox defBox = new HBox();
        definitionLabel.setPadding(new Insets(5, 20, 0, 0));
        defBox.getChildren().addAll(definitionLabel, definitionField);
        defBox.setPadding(new Insets(20, 0, 20, 30));

        HBox submitBox = new HBox();
        submitBox.getChildren().addAll(submit);
        submitBox.setPadding(new Insets(20, 0, 20, 30));

        VBox vbox = new VBox();
        vbox.getChildren().addAll(termBox, defBox, submitBox);

        root.getChildren().addAll(vbox);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Delete function for the delete button of the index cards.
     * @param e
     */
    @FXML
    public void deleteButtonClicked(Event e) {
        String nameOfCard = set.getCard().getFront();
        removeLineFromFile(nameOfCard);
        set.deleteCard(set.getCard());
    }

    @FXML
    public void flip(){
        set.flipCurrentCard();
    }

    /**
     * Is the main function for the checkboxes on each of the index cards, returns whether or not the index cards are checked as learned or unlearned.
     * @throws IOException
     */
    @FXML
    public void learned() throws IOException {
        IndexCard card = set.getCard();
        card.setLearned(checkBox.isSelected());
        String CardFront = card.getFront();
        String CardBack = card.getBack();
        removeLineFromFile(CardFront);
        File currentCourseSet = new File("userData/" + currentUser + "/" + currentUser+currentCourse+".txt");
        BufferedWriter courseSetWriter = new BufferedWriter(new FileWriter(currentCourseSet,true));
        currentCheck = card.isLearned();
        courseSetWriter.write(CardFront+","+CardBack+","+currentCheck.toString()+",");
        courseSetWriter.write("\n");
        courseSetWriter.close();

    }

    @FXML
    public void nextCard(){
        set.nextCard();
    }

    @FXML
    public void previousCard(){
        set.previousCard();
    }


    /**
     * Initializes any existing index cards and also creates the new Set for the index cards to sit in.
     * @param location
     * The location used to resolve relative paths for the root object, or
     * <tt>null</tt> if the location is not known.
     *
     * @param resources
     * The resources used to localize the root object, or <tt>null</tt> if
     * the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        File currentUserName = new File("currentUser.txt");
        File currentCourseName = new File("currentCourseSelected.txt");


        //Scanner creator to read currentUser (Helps with accessing the file)
        Scanner reader = null;
        try {
            reader = new Scanner(currentUserName);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        //Scanner creator for reading the current Course name.
        Scanner readCourseName = null;
        try{
            readCourseName = new Scanner(currentCourseName);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        // currentUser reads current username,  currentCourse reads the current course that is open.
        currentUser = reader.nextLine();
        currentCourse = readCourseName.nextLine();
        reader.close();
        readCourseName.close();

        File theExistentCourse = new File("userData/"+currentUser+"/"+currentUser+currentCourse+".txt");



        set = new Set(currentCourse, text, checkBox, counterBox);
        if(theExistentCourse.exists())
        {
            Scanner ExistentCourse = null;
            try {

                ExistentCourse = new Scanner(theExistentCourse);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            if(theExistentCourse.length() != 0)
            {
                while (ExistentCourse.hasNextLine()) {
                    String[] details = ExistentCourse.nextLine().split(",");
                    Boolean checked = Boolean.parseBoolean(details[2]);
                    set.addCard(details[0], details[1], checked);
                }
            }
            ExistentCourse.close();
        }


    }

    /**
     * Removes the String specified in the file inFile.
     * @param courseToRemove
     */
    public void removeLineFromFile(String courseToRemove) {

        try {

            File userReader = new File("currentUser.txt");
            Scanner readUser = new Scanner(userReader);
            currentUser = readUser.nextLine();
            readUser.close();


            File inFile = new File("userData/"+currentUser+"/"+currentUser+currentCourse+".txt");
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
     * Reviews all cards that are checkmarked learned
     * @throws IOException
     */
    @FXML
    public void reviewLearned() throws IOException {
        File currentCourseFile = new File("userData/"+currentUser+"/"+currentUser+currentCourse+".txt");
        Scanner courseReader = new Scanner(currentCourseFile);
        set = new Set(currentCourse, text, checkBox, counterBox);
        while(courseReader.hasNextLine())
        {
            String[] readTheLine = courseReader.nextLine().split(",");
            Boolean checker = Boolean.parseBoolean(readTheLine[2]);
            if(readTheLine[2].equals("true"))
            {
                set.addCard(readTheLine[0],readTheLine[1], checker);
            }

        }
        courseReader.close();
        deleteCardB.setVisible(false);
        checkBox.setVisible(false);
        createCard.setVisible(false);

        Stage stage = (Stage) reviewLearnedB.getScene().getWindow();
        stage.setTitle("Review Learned");
        stage.show();
    }

    /**
     * Reviews all cards that are unchecked on the learned checkbox
     * @throws IOException
     */
    @FXML
    public void reviewUnlearned() throws IOException {
        File currentCourseFile = new File("userData/"+currentUser+"/"+currentUser+currentCourse+".txt");
        Scanner courseReader = new Scanner(currentCourseFile);
        set = new Set(currentCourse, text, checkBox, counterBox);
        while(courseReader.hasNextLine())
        {
            String[] readTheLine = courseReader.nextLine().split(",");
            Boolean checker = Boolean.parseBoolean(readTheLine[2]);
            if(readTheLine[2].equals("false"))
            {
                set.addCard(readTheLine[0],readTheLine[1], checker);
            }

        }
        courseReader.close();
        deleteCardB.setVisible(false);
        checkBox.setVisible(false);
        createCard.setVisible(false);
        Stage stage = (Stage) reviewLearnedB.getScene().getWindow();
        stage.setTitle("Review Not Learned");
        stage.show();
    }

    /**
     * Reviews all cards regardless of any parameters.
     * @throws IOException
     */
    @FXML
    public void reviewAll() throws IOException
    {
        File currentCourseFile = new File("userData/"+currentUser+"/"+currentUser+currentCourse+".txt");
        Scanner courseReader = new Scanner(currentCourseFile);
        set = new Set(currentCourse, text, checkBox, counterBox);
        while(courseReader.hasNextLine())
        {
            String[] readTheLine = courseReader.nextLine().split(",");
            Boolean checker = Boolean.parseBoolean(readTheLine[2]);
            set.addCard(readTheLine[0],readTheLine[1], checker);
        }
        courseReader.close();
        deleteCardB.setVisible(false);
        checkBox.setVisible(false);
        createCard.setVisible(false);
        Stage stage = (Stage) reviewLearnedB.getScene().getWindow();
        stage.setTitle("Review All");
        stage.show();
    }
}

