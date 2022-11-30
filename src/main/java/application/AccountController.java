package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Comparator;
import java.util.Scanner;
import java.util.stream.Stream;

public class AccountController {

    @FXML
    Button confirmPasswordB;
    @FXML
    Button confirmSecurityB;
    @FXML
    Button cancelB;
    @FXML
    Button deleteAccountB;
    @FXML
    TextField oldpass;
    @FXML
    TextField newpass;
    @FXML
    TextField cityname;
    @FXML
    Label errorMessageLabel;

    @FXML
    Label cityErrorMessageLabel;

    String currentQans;
    @FXML
    Button logoutButton;


    /**
     * Closes the account settings page.
     */
    @FXML
    public void cancelButton() {
        try {
            Parent root = (Parent) FXMLLoader.load(Main.class.getResource("courseViewer.fxml"));
            Stage stage = (Stage) this.cancelB.getScene().getWindow();
            stage.setTitle("Course Viewer");
            stage.setScene(new Scene(root, 1024, 768));
            stage.setResizable(false);
            stage.show();
        } catch (IOException var5) {
            var5.printStackTrace();
        }

    }

    /**
     * Validates whether or not an account exists
     *
     * @param oldpassw
     * @return Returns true if the password matches the one in the User database.
     * @throws FileNotFoundException
     */
    public boolean validatespass(String oldpassw) throws FileNotFoundException {
        File database = new File("users.TXT");
        File readUsername = new File("currentUser.txt");
        Scanner readDatabase = new Scanner(database);
        Scanner readUser = new Scanner(readUsername);
        String currentUserName = readUser.nextLine();
        while (readDatabase.hasNextLine()) {
            String currentLine = readDatabase.nextLine();
            String[] details = currentLine.split(",");
            String usernameOfAccount = details[0];
            String realOldPass = details[1];
            if (currentUserName.contains(usernameOfAccount)) {
                if (realOldPass.equals(oldpassw)) {
                    currentQans = details[2];
                    readDatabase.close();
                    return true;
                }
            }

        }
        readDatabase.close();
        readUser.close();
        return false;

    }

    /**
     * Is the main function for the change of the password.  Changes the password in the file.
     *
     * @throws IOException
     */

    public void changePass() throws IOException {
        //read from file the new pass
        String oldPassword = oldpass.getText();
        String newPassword = newpass.getText();

        File usersFile = new File("users.TXT");


        File currentUserFile = new File("currentUser.TXT");
        Scanner currentUserReader = new Scanner(currentUserFile);
        String usernameOfCurrent = currentUserReader.nextLine();
        currentUserReader.close();

        if (validatespass(oldPassword)) {
            removeLineFromFile(usernameOfCurrent);

            BufferedWriter passwordRewriter = new BufferedWriter(new FileWriter(usersFile, true));
            Scanner passwordReader = new Scanner(usersFile);

            passwordRewriter.write(usernameOfCurrent + ",");
            passwordRewriter.write(newPassword + ",");
            passwordRewriter.write(currentQans + ",");
            passwordRewriter.write("\n");

            passwordRewriter.close();
            passwordReader.close();


            System.out.println("Change password");
        } else {
            errorMessageLabel.setText("Invalid Please try again!");
            System.out.println("Password is not Correct");
        }


    }

    /**
     * Is the main function for the button that changes the security answer for the account.
     *
     * @throws IOException
     */
    public void changeSecurityAnswer() throws IOException {
        File currentUserFile = new File("currentUser.TXT");
        File usersFile = new File("users.TXT");

        Scanner currentUserReader = new Scanner(currentUserFile);
        String usernameOfCurrent = currentUserReader.nextLine();
        currentUserReader.close();

        String passwordTosave = "";
        Scanner savePasswordHere = new Scanner(usersFile);
        while (savePasswordHere.hasNextLine()) {
            String[] details = savePasswordHere.nextLine().split(",");
            if (details[0].equals(usernameOfCurrent)) {
                passwordTosave = details[1];
                System.out.println(passwordTosave);
            }
        }
        savePasswordHere.close();
        removeLineFromFile(usernameOfCurrent);

        String newAnswer = cityname.getText();
        if (cityname.getText().isEmpty()) {
            cityErrorMessageLabel.setText("Invalid Please Fill City Name");
        } else {
            BufferedWriter qAnsRewriter = new BufferedWriter(new FileWriter(usersFile, true));
            qAnsRewriter.write(usernameOfCurrent + ",");
            qAnsRewriter.write(passwordTosave + ",");
            qAnsRewriter.write(newAnswer + ",");
            qAnsRewriter.write("\n");
            qAnsRewriter.close();

        }


    }

    /**
     * Removes information saved to the database about an account.
     *
     * @param courseToRemove
     */
    public void removeLineFromFile(String courseToRemove) {

        try {

            File userReader = new File("currentUser.txt");
            Scanner readUser = new Scanner(userReader);
            String currentUser = readUser.nextLine();
            readUser.close();


            File inFile = new File("users.txt");
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


        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    /**
     * The function for the deletion of an account. Removes all existence of an account.
     *
     * @throws FileNotFoundException
     */
    public void deleteAccount() throws IOException {

        File current = new File("currentUser.txt");
        Scanner readUser = new Scanner(current);
        String currentUserLogged = readUser.next();
        File checkIfFileExists = new File(currentUserLogged + ".txt");
        File deleteDirectory2 = new File("userData/" + currentUserLogged);

        if (deleteDirectory2.exists()) {
            deleteDirectory(deleteDirectory2);
            deleteDirectory2.delete();
        }
        if (checkIfFileExists.exists()) {
            checkIfFileExists.delete();
        }
        removeLineFromFile(currentUserLogged);

        Parent root;
        try {
            File file1 = new File("currentUser.txt");

            BufferedWriter buff1 = new BufferedWriter(new FileWriter(file1));
            buff1.write("");
            buff1.close();
            root = FXMLLoader.load(Main.class.getResource("login.fxml"));
            Stage stage = (Stage) deleteAccountB.getScene().getWindow();
            stage.setTitle("Indexify");
            stage.setScene(new Scene(root, 530, 400));
            stage.setResizable(false);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Is a function created in order to delete the files out of the userData directory in the case that the account is deleted.
     * @param file
     */
    public static void deleteDirectory(File file) {

        File[] list = file.listFiles();
        if (list != null) {
            for (File temp : list) {
                System.out.println("Visit " + temp);
                deleteDirectory(temp);
                temp.delete();
            }
        }



    }
}
