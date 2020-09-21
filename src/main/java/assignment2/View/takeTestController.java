package assignment2.View;

import assignment2.Factories.DataFactory;
import assignment2.Factories.ResultDAO;
import assignment2.Models.Question.MultipleQuestion;
import assignment2.Models.Question.Question;
import assignment2.Models.Question.QuestionType;
import assignment2.Models.Question.SimpleQuestion;
import assignment2.Models.Test.QuestionAttempt;
import assignment2.Models.Test.Result;
import assignment2.Models.Test.Test;
import assignment2.Models.User.Student;
import assignment2.Models.User.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class takeTestController
{

    @FXML private Label lblTest;
    @FXML private Label lblQuestionCount;
    @FXML private Label lblPoints;

    @FXML private TextArea txtQuestion;
    @FXML private TextArea txtAnswer;

    @FXML private RadioButton rbAnswerA;
    @FXML private RadioButton rbAnswerB;
    @FXML private RadioButton rbAnswerC;
    @FXML private RadioButton rbAnswerD;

    ResultDAO resultDAO = DataFactory.getInstance().getResultDAO();

    User user;
    Test test;
    Iterator<Question> questions;
    Question currentQuestion;
    int questionNumber = 0;
    int totalQuestions;
    List<QuestionAttempt> questionAttempts = new ArrayList<>();
    ToggleGroup toggleGroup = new ToggleGroup();


    public void loadTakeTestController(User user, Test test)
    {
        this.user = user;
        this.test = test;
        lblTest.setText("Test : " + test.TestName);
        questions = test.Questions.iterator();
        totalQuestions = test.Questions.size();
        loadNextQuestion();
        rbAnswerA.setToggleGroup(toggleGroup);
        rbAnswerB.setToggleGroup(toggleGroup);
        rbAnswerC.setToggleGroup(toggleGroup);
        rbAnswerD.setToggleGroup(toggleGroup);
    }

    private void loadNextQuestion()
    {
        if(questions.hasNext())
        {
            txtAnswer.clear();
            rbAnswerA.setSelected(false);
            rbAnswerB.setSelected(false);
            rbAnswerC.setSelected(false);
            rbAnswerD.setSelected(false);

            currentQuestion = questions.next();
            questionNumber ++;
            lblQuestionCount.setText("Question " + questionNumber + " of " + totalQuestions);
            lblPoints.setText("Worth " + currentQuestion.Points + " points");
            txtQuestion.setText(currentQuestion.Question);
            if(currentQuestion.getType() == QuestionType.SIMPLE || currentQuestion.getType() == QuestionType.MANUAL)
            {
                txtAnswer.setVisible(true);
                rbAnswerA.setVisible(false);
                rbAnswerB.setVisible(false);
                rbAnswerC.setVisible(false);
                rbAnswerD.setVisible(false);
            }

            if(currentQuestion.getType() == QuestionType.MULTIPLE)
            {
                txtAnswer.setVisible(false);
                if(((MultipleQuestion)currentQuestion).Option1.equalsIgnoreCase(""))
                {
                    rbAnswerA.setVisible(false);
                }
                else
                {
                    rbAnswerA.setVisible(true);
                    rbAnswerA.setText(((MultipleQuestion)currentQuestion).Option1);
                }

                if(((MultipleQuestion)currentQuestion).Option2.equalsIgnoreCase(""))
                {
                    rbAnswerB.setVisible(false);
                }
                else
                {
                    rbAnswerB.setVisible(true);
                    rbAnswerB.setText(((MultipleQuestion)currentQuestion).Option2);
                }

                if(((MultipleQuestion)currentQuestion).Option3.equalsIgnoreCase(""))
                {
                    rbAnswerC.setVisible(false);
                }
                else
                {
                    rbAnswerC.setVisible(true);
                    rbAnswerC.setText(((MultipleQuestion)currentQuestion).Option3);
                }


                if(((MultipleQuestion)currentQuestion).Option4.equalsIgnoreCase(""))
                {
                    rbAnswerD.setVisible(false);
                }
                else
                {
                    rbAnswerD.setVisible(true);
                    rbAnswerD.setText(((MultipleQuestion)currentQuestion).Option4);
                }
            }
        }
        else
        {
           dialog("End of test \n Test results submitted for teacher marking");
           Result result = new Result(0, (Student)user, questionAttempts, test.TestName);
           resultDAO.AddResult(result);
           openLoginMenu();
        }
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

    private void openLoginMenu()
    {
        try
        {
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("login.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage;
            stage=(Stage) txtQuestion.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Login");
            stage.show();
        }
        catch(Exception ex)
        {
            System.out.println(ex);
        }

    }

    public void submitAnswer()
    {
        Boolean questionCorrect = false;
        String answerGiven = "";

        if(currentQuestion.getType() == QuestionType.SIMPLE)
        {
            answerGiven = txtAnswer.getText().trim();
            if(((SimpleQuestion)currentQuestion).Answer.equalsIgnoreCase(answerGiven))
            {
                questionCorrect = true;
            }
        }

        if(currentQuestion.getType() == QuestionType.MULTIPLE)
        {
            answerGiven = ((RadioButton)toggleGroup.getSelectedToggle()).getText();
            if(((MultipleQuestion)currentQuestion).Answer.equalsIgnoreCase(answerGiven))
            {
                questionCorrect = true;
            }
        }

        QuestionAttempt questionAttempt = new QuestionAttempt(currentQuestion, answerGiven, questionCorrect);
        questionAttempts.add(questionAttempt);
        loadNextQuestion();
    }





}
