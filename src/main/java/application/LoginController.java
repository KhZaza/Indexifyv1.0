package application;


import com.sun.org.apache.xerces.internal.xs.StringList;
import edu.sjsu.yazdankhah.crypto.util.PassUtil;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.*;
import java.io.File;
import java.io.IOException;
import java.util.ResourceBundle;
import java.net.URL;
import java.util.*;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import application.CourseController;




public class LoginController {

    @FXML
    private Button reCancel2;
    @FXML
    private Button cancelButton;
    @FXML
    public Button loginButton;
    @FXML
    private Label loginMessageLabel;
    @FXML
    private Label resetMessageLabel;
    @FXML
    public ImageView brandingImageView;
    @FXML
    public ImageView lockImageView;
    @FXML
    private TextField usernameTextField;
    @FXML
    private PasswordField enterPasswordField;
    @FXML
    private Button resetPassword;
    @FXML
    private Button logoutButton;
    @FXML
    public String usernameSaved;
    @FXML
    public String passwordSaved;
    @FXML
    public  String qAns;
    @FXML
    private TextField usernameReset;
    @FXML
    private TextField securityAnswer;
    @FXML
    public TextField brandNewPass;
    @FXML
    public Button reConfirm;
    @FXML
    public Label securityWRONG;
    @FXML
    public Label UserWRONG;
    @FXML
//    public Label currentUser;

    public String saveCurrentUser  ;






    /**
     * Action function for the login button.  Allows the button to run the validate functions and login success functions.
     * @throws FileNotFoundException
     */

    public void loginButtonAction(ActionEvent event) throws IOException {
        File file1 = new File("currentUser.txt");
        if (usernameTextField.getText().isEmpty() || enterPasswordField.getText().isEmpty()) {
            loginMessageLabel.setText("Please enter username and password!");
        }
        else {
            if (validateLogin(usernameTextField.getText(), enterPasswordField.getText())) {
                BufferedWriter buff1 = new BufferedWriter(new FileWriter(file1));
                Stage stage = (Stage) loginButton.getScene().getWindow();
                usernameSaved = usernameTextField.getText();
                setCurrentUser(usernameSaved);
                passwordSaved = enterPasswordField.getText();
                buff1.write(saveCurrentUser);
                buff1.close();
                loginSuccess(event);
                stage.close();
            }
            else {
                loginMessageLabel.setText("Username or Password does not exist.");
            }
        }
    }


    /**
     * Cancels stage/closes windows.
     * @param event
     */
    public void cancelButtonAction(ActionEvent event) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    /**
     * Checks for the success of the login and also brings open the courses if it succeeds.
     * @param event
     */
    @FXML
    public void loginSuccess(ActionEvent event) {
        Parent root;
        try {
            root = FXMLLoader.load(Main.class.getResource("courseViewer.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Course Viewer");
            stage.setScene(new Scene(root, 1024, 768));
            stage.setResizable(false);
            stage.show();



        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Makes sure that the account exists
     * @return Boolean: If account exists then true, if not then false.
     * @throws FileNotFoundException
     */
    public boolean validateLogin(String username, String password) throws FileNotFoundException {
        File database = new File("users.TXT");
        Scanner readDatabase = new Scanner(database);

        while(readDatabase.hasNextLine())
        {
            String[] details = readDatabase.nextLine().split(",");
            usernameSaved = details[0];
            passwordSaved = details[1];
            PassUtil passUtil = new PassUtil();
            passwordSaved = passUtil.decrypt(passwordSaved);

            if(username.equals(usernameSaved) && password.equals(passwordSaved))
            {
                readDatabase.close();
                return true;
            }
        }
        readDatabase.close();
        return false;
    }



    public boolean validateSignup(String username) throws IOException {
        File database = new File("users.TXT");
        Scanner readDatabase = new Scanner(database);


        while(readDatabase.hasNextLine())
        {
            String[] details = readDatabase.nextLine().split(",");
            usernameSaved = details[0];
            if (usernameSaved.equals(username))
            {
                readDatabase.close();
                return false;
            }
        }
        readDatabase.close();
        return true;
    }

    /**
     * Resets the password of an existing account
     */
    public void resetPassword()
    {
        Parent root;
        try {
            root = FXMLLoader.load(Main.class.getResource("resetPassword.fxml"));
            Stage stage2 = (Stage) resetPassword.getScene().getWindow();
            stage2.close();
            Stage stage = new Stage();
            stage.setTitle("Reset password");
            stage.setScene(new Scene(root, 650, 400));
            stage.setResizable(false);
            stage.show();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Performs the sign-up function which is currently saving into flat files.
     * @throws IOException
     */
    @FXML
    protected void signUp() throws IOException {

        Stage stage = new Stage();
        stage.setTitle("Sign Up");

        Pane root = new Pane();
        Scene scene = new Scene(root, 500, 300);

        Label usernameLabel = new Label("Username: ");
        TextField usernameField = new TextField();

        Label passwordLabel = new Label("Password: ");
        PasswordField passwordField = new PasswordField();

        Label securityQuestion = new Label("Security Question: What city were you born in? ");
        TextField question = new TextField();

        Label usernameExists = new Label("Error please Try again");
        usernameExists.setVisible(false);

        Button submit = new Button("Submit");
        submit.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                String username = usernameField.getText();
                String password = passwordField.getText();
                PassUtil passUtil = new PassUtil();
                password = passUtil.encrypt(password);
                String qAns = question.getText();
                try {

                    if(validateSignup(username) && !usernameField.getText().isEmpty() && !passwordField.getText().isEmpty() && !question.getText().isEmpty() )
                    {
                        FileWriter myWriter = new FileWriter("users.TXT", true);
                        myWriter.write(username + ",");
                        myWriter.write(password + ",");
                        myWriter.write(qAns + ",");
                        myWriter.write("\n");
                        myWriter.close();
                        stage.close();

                    }
                    else
                    {
                        usernameExists.setVisible(true);
                    }

                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }







            }
        });

        HBox userBox = new HBox();
        userBox.getChildren().addAll(usernameLabel, usernameField,usernameExists);
        userBox.setPadding(new Insets(10, 10, 10, 10));

        HBox passBox = new HBox();
        passBox.getChildren().addAll(passwordLabel, passwordField);
        passBox.setPadding(new Insets(10, 10, 10, 10));

        HBox quesBox = new HBox();
        quesBox.getChildren().addAll(securityQuestion, question);
        quesBox.setPadding(new Insets(10, 10, 10, 10));

        VBox vbox = new VBox();
        vbox.getChildren().addAll(userBox, passBox, quesBox, submit);

        root.getChildren().addAll(vbox);
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    public void logoutButton()
    {
        Parent root;
        try
        {
            root = FXMLLoader.load(Main.class.getResource("login.fxml"));
            Stage stage = (Stage) logoutButton.getScene().getWindow();
            stage.setTitle("Indexify");
            stage.setScene(new Scene(root, 530, 400));
            stage.setResizable(false);
            stage.show();

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    @FXML
    public void resetCancel() throws IOException {
        Stage stage2 = (Stage) reCancel2.getScene().getWindow();
        stage2.close();
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 530, 400);
        stage.setTitle("Indexify");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public boolean validateReset(String username, String qAns) throws FileNotFoundException {
        File database = new File("users.TXT");
        Scanner readDatabase = new Scanner(database);

        while(readDatabase.hasNextLine())
        {
            String[] details = readDatabase.nextLine().split(",");
            String username2 = details[0];
            String qAns2 = details[2];
            if (username2.equals(username) && qAns2.equals(qAns))
            {
                readDatabase.close();
                return true;
            }

        }
        readDatabase.close();
        return false;
    }

    /**
     * Is the main function for the reset password button, writes in the new password into the file and replaces the old one.
     * @throws IOException
     */
    public void resetConfirm() throws IOException {


        String username = usernameReset.getText();
        String password = brandNewPass.getText();
        String qAns = securityAnswer.getText();



        if (validateReset(username, qAns) && !brandNewPass.getText().isEmpty()) {


            removeLineFromFile(username, "users.TXT");

            BufferedWriter myWriter = new BufferedWriter(new FileWriter("users.TXT", true));
            try {
                myWriter.write(username + ",");
                myWriter.write(password + ",");
                myWriter.write(qAns + ",");
                myWriter.write("\n");
                myWriter.close();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            System.out.println(username + " " + password + " " + qAns);


            Stage stage2 = (Stage) reCancel2.getScene().getWindow();
            stage2.close();
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("login.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 530, 400);
            stage.setTitle("Indexify");
            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();
        }

        else{
            resetMessageLabel.setText("Invalid Please try again!");
        }
    }

    /**
     * Is used in order to delete data from the flat file so that new data can be written in its place
     */
    public void removeLineFromFile(String username, String fileName)
    {
        try {

            File inFile = new File(fileName);

            if (!inFile.isFile())
            {
                System.out.println("Parameter is not an existing file");
                return;
            }

            //Construct the new file that will later be renamed to the original filename.
            File tempFile = new File(inFile.getAbsolutePath() + ".tmp");
            Scanner scanFile = new Scanner(inFile);
            PrintWriter pw = new PrintWriter(new FileWriter(tempFile));
            //Read from the original file and write to the new
            //unless content matches data to be removed.
            while (scanFile.hasNextLine())
            {
                String[] details = scanFile.nextLine().split(",");
                if(!username.equals(details[0]))
                {
                    pw.println(details[0] + "," + details[1] + "," + details[2] + ",");
                    pw.flush();
                }
            }

            pw.close();
            scanFile.close();

            inFile.delete();
            //Rename the new file to the filename the original file had.
            tempFile.renameTo(inFile);

            pw.close();
            scanFile.close();

        }
        catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void setCurrentUser(String s)
    {
        saveCurrentUser = s;
    }
}