package assignment2.View;

import assignment2.Factories.DataFactory;
import assignment2.Factories.UserDAO;
import assignment2.Models.User.Student;
import assignment2.Models.User.User;
import assignment2.Models.User.UserType;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class loginController {

    @FXML private Button btnLogin;
    @FXML private Button primaryButton;
    @FXML private TextField txtLogin;

    private UserDAO userDAO = DataFactory.getInstance().getUserDAO();

    @FXML
    private void btnLogin(ActionEvent event)
    {
        if(txtLogin.getText().equalsIgnoreCase("sysadmin"))
        {
            User user = new User(0,"sysadmin", UserType.TEACHER);
            openTeacherView(user);
            return;
        }

        for (User user: userDAO.GetAllUsers())
        {
            if(user.Name.equalsIgnoreCase(txtLogin.getText()))
            {
                if(user.getType() == UserType.STUDENT)
                {
                    openStudentView((Student)user);
                    return;
                }
                else
                {
                    openTeacherView(user);
                    return;
                }
            }
        }

        dialog("User not found - Contact Teacher");
    }

    public void dialog(String content)
    {
        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        VBox dialogVbox = new VBox(20);
        dialogVbox.getChildren().add(new Text(" " + content));
        Scene dialogScene = new Scene(dialogVbox, 200, 50);
        dialog.setScene(dialogScene);
        dialog.show();
    }

    @FXML
    private void handleCloseMenuItemAction(ActionEvent event) {
        Platform.exit();
    }

    private void openTeacherView(User user)
    {
        try
        {
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("teacher/teacher.fxml"));
            Parent root = fxmlLoader.load();

            teacherController teacherController = fxmlLoader.getController();
            teacherController.loadTeacherController(user);
            Stage stage;
            stage=(Stage) txtLogin.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Teacher");
            stage.show();
        }
        catch(Exception ex)
        {
            System.out.println(ex);
            ex.printStackTrace();
        }

    }

    private void openStudentView(Student student)
    {
        try
        {
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("student/selectTest.fxml"));
            Parent root = fxmlLoader.load();

            selectTestController selectTestController = fxmlLoader.getController();
            selectTestController.loadSelectTestController(student);
            Stage stage;
            stage=(Stage) txtLogin.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Select Test");
            stage.show();
        }
        catch(Exception ex)
        {
            System.out.println(ex);
        }

    }
}
