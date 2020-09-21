package assignment2.View;

import assignment2.Factories.DataFactory;
import assignment2.Factories.TestDAO;
import assignment2.Models.Test.Test;
import assignment2.Models.User.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

public class selectTestController {

    @FXML private ListView lvTests;

    TestDAO testDAO = DataFactory.getInstance().getTestDAO();

    User user;

    public void loadSelectTestController(User user)
    {
        this.user = user;
        loadTests();
    }

    public void takeTest()
    {
        if(lvTests.getSelectionModel().getSelectedItems().size() > 0)
        {
            try
            {
                FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("student/takeTest.fxml"));
                Parent root = fxmlLoader.load();

                takeTestController takeTestController = fxmlLoader.getController();
                takeTestController.loadTakeTestController(user, testDAO.GetTest(lvTests.getSelectionModel().getSelectedItems().get(0).toString()));
                Stage stage;
                stage=(Stage) lvTests.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle("Take Test");
                stage.show();
            }
            catch(Exception ex)
            {
                System.out.println(ex);
            }
        }
    }


    public void loadTests()
    {
        lvTests.getItems().clear();
        for (Test test: testDAO.GetAllTest())
        {
            lvTests.getItems().add(test.TestName);
        }
    }


    public void back()
    {
        try
        {
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("login.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage;
            stage=(Stage) lvTests.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Login");
            stage.show();
        }
        catch (Exception ex)
        {
            System.out.println(ex);
        }

    }


}
