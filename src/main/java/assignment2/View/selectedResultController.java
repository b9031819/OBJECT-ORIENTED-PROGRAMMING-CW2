package assignment2.View;

import assignment2.Models.Test.QuestionAttempt;
import assignment2.Models.Test.Result;
import assignment2.Models.User.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class selectedResultController {

    @FXML private Label lblResult;
    @FXML private TextField txtStuName;
    @FXML private TextField txtStuYear;
    @FXML private TextField txtPercent;
    @FXML private ListView lvQuestionAttempts;

    User user;

    public void loadSelectTestController(User user, Result result)
    {
        this.user = user;
        lblResult.setText(result.TestName + " - Result");
        txtStuName.setText(result.Student.Name);
        txtStuYear.setText(Integer.toString(result.Student.Year));

        Double scorePercent = ((result.getTotalScore() / result.getMaxScore()) * 100);

        txtPercent.setText(Integer.toString((int)Math.round(scorePercent)));
        for(QuestionAttempt questionAttempt : result.QuestionAttempts)
        {
            String question = "Question : " + questionAttempt.Question.Question;
            String answer = "Answer : " + questionAttempt.AnswerGiven;
            String correct = "Correct : " + questionAttempt.Correct;
            String worth = "Worth Points : " + questionAttempt.Question.Points;
            lvQuestionAttempts.getItems().add(question + " | " + answer + " | " + correct + " | " + worth);
        }

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
            stage=(Stage) lblResult.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Teacher");
            stage.show();
        }
        catch (Exception ex)
        {
            System.out.println(ex);
        }

    }


}
