package assignment2.View;

import assignment2.Factories.DataFactory;
import assignment2.Factories.QuestionDAO;
import assignment2.Factories.TestDAO;
import assignment2.Factories.UserDAO;
import assignment2.Models.Question.Question;
import assignment2.Models.Test.Test;
import assignment2.Models.User.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class addTestController {

    @FXML private TextField txtTestName;

    @FXML private ListView lvQuestionBank;
    @FXML private ListView lvQuestionsInTest;

    QuestionDAO questionDAO = DataFactory.getInstance().getQuestionDAO();
    TestDAO testDAO = DataFactory.getInstance().getTestDAO();

    User user;

    public void loadAddTestController(User user)
    {
        this.user  = user;
        loadQuestionBank();
    }

    public void loadQuestionBank()
    {
        lvQuestionBank.getItems().clear();
        for (Question question: questionDAO.GetAllQuestions())
        {
            lvQuestionBank.getItems().add("Question : " + question.Question + " | Type : " + question.getType().toString() + " | Tags : " + question.Tags.toString());
        }
    }

    public void addQuestions()
    {
        for (Object x :lvQuestionBank.getSelectionModel().getSelectedItems())
        {
            lvQuestionsInTest.getItems().add(x);
            lvQuestionBank.getItems().remove(x);
            if(lvQuestionBank.getItems().size() == 0) return;
        }
    }

    public void removeQuestions()
    {
        for (Object x :lvQuestionsInTest.getSelectionModel().getSelectedItems())
        {
            lvQuestionBank.getItems().add(x);
            lvQuestionsInTest.getItems().remove(x);
            if(lvQuestionsInTest.getItems().size() == 0) return;
        }
    }

    public void saveTest()
    {
        List<Question> questions = new ArrayList<>();

        for (Object x :lvQuestionsInTest.getItems())
        {
            questions.add(questionDAO.GetQuestion(x.toString().split(" \\| ")[0].replace("Question : ", "")));
        }

        Test test = new Test(0, txtTestName.getText(), questions);
        testDAO.AddTest(test);
        dialog("New Test added");
        back();
    }

    public void back()
    {
        try
        {
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("teacher/teacher.fxml"));
            Parent root = fxmlLoader.load();
            teacherController teacherController = fxmlLoader.getController();
            teacherController.loadTeacherController(user);
            Stage stage;
            stage=(Stage) txtTestName.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Teacher");
            stage.show();
        }
        catch (Exception ex)
        {
            System.out.println(ex);
        }

    }

    public void dialog(String content)
    {
        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        VBox dialogVbox = new VBox(20);
        dialogVbox.getChildren().add(new Text(content));
        Scene dialogScene = new Scene(dialogVbox, 100, 100);
        dialog.setScene(dialogScene);
        dialog.show();
    }



}
