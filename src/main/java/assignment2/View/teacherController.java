package assignment2.View;

import assignment2.Factories.*;
import assignment2.Models.Question.Question;
import assignment2.Models.Test.Result;
import assignment2.Models.Test.Test;
import assignment2.Models.User.Student;
import assignment2.Models.User.Teacher;
import assignment2.Models.User.User;
import assignment2.Models.User.UserType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class teacherController {

    @FXML private TextField txtStuName;
    @FXML private TextField txtStuDOB;
    @FXML private TextField txtStuYear;
    @FXML private TextField txtStuClass;
    @FXML private TextField txtTeaName;
    @FXML private TextField txtResultYear;
    @FXML private TextField txtQFilter;

    @FXML private ListView lvQuestions;
    @FXML private ListView lvTests;
    @FXML private ListView lvResults;

    @FXML private Label lblLogin;

    private QuestionDAO questionDAO = DataFactory.getInstance().getQuestionDAO();
    private TestDAO testDAO = DataFactory.getInstance().getTestDAO();
    private ResultDAO resultDAO = DataFactory.getInstance().getResultDAO();
    private UserDAO userDAO = DataFactory.getInstance().getUserDAO();

    User user;

    public void loadTeacherController(User user)
    {
        lblLogin.setText("Logged in as : " + user.Name);
        this.user  = user;
        filterQuestions();
        loadTests();
        loadResults();
    }

    public void filterQuestions()
    {
        lvQuestions.getItems().clear();
        for (Question question: questionDAO.GetAllQuestions())
        {
            if(txtQFilter.getText().equalsIgnoreCase(""))
            {
                lvQuestions.getItems().add("Question : " + question.Question + " | Type : " + question.getType().toString() + " | Tags : " + question.Tags.toString());
            }
            else
            {
                for (String tag: question.Tags)
                {
                    if(tag.toLowerCase().contains(txtQFilter.getText().toLowerCase()))
                    {
                        lvQuestions.getItems().add("Question : " + question.Question + " | Type : " + question.getType().toString() + " | Tags : " + question.Tags.toString());
                    }
                }
            }

        }
    }

    public void deleteQuestion()
    {
        var questionSelected = questionDAO.GetQuestion(lvQuestions.getSelectionModel().getSelectedItems().get(0).toString().split(" \\| ")[0].replace("Question : ", ""));
        QuestionDAO.DeleteQuestion(questionSelected);
        dialog("Quesiton deleted");
        filterQuestions();
    }

    public void deleteTest()
    {
        var testSelected = lvTests.getSelectionModel().getSelectedItems().get(0).toString().replace("Test Name : ", "");
        testDAO.DeleteTest(testSelected);
        dialog("Test deleted");
        loadTests();
    }

    public void searchResults()
    {
        if(txtResultYear.getText().equalsIgnoreCase(""))
        {
            loadResults();
        }
        else
        {
            loadResults(Integer.parseInt(txtResultYear.getText()));
        }
    }

    public void selectResult()
    {
        try
        {
            String testName = lvResults.getSelectionModel().getSelectedItem().toString().split(" \\| ")[0].replace("Test Name : ", "").trim();
            String studentName = lvResults.getSelectionModel().getSelectedItem().toString().split(" \\| ")[1].replace("Student : ", "").trim();
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("teacher/selectedResult.fxml"));
            Parent root = fxmlLoader.load();
            selectedResultController selectedResultController = fxmlLoader.getController();
            selectedResultController.loadSelectTestController(user, resultDAO.GetResult(testName, studentName));
            Stage stage;
            stage=(Stage) lblLogin.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Result");
            stage.show();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public void loadTests()
    {
        lvTests.getItems().clear();
        for (Test test: testDAO.GetAllTest())
        {
            lvTests.getItems().add("Test Name : " + test.TestName);
        }
    }

    public void loadResults()
    {
        lvResults.getItems().clear();
        for (Result result: resultDAO.GetAllResults())
        {
            lvResults.getItems().add("Test Name : " + result.TestName + " | Student : " + result.Student.Name + "  | Year : " + result.Student.Year);
        }
    }

    public void loadResults(int Year)
    {
        lvResults.getItems().clear();
        for (Result result: resultDAO.GetAllResults())
        {
            if(result.Student.Year == Year)
                lvResults.getItems().add("Test Name : " + result.TestName + " | Student  : " + result.Student.Name + "  | Year : " + result.Student.Year);
        }
    }

    public void addQuestion()
    {
        try
        {
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("teacher/addQuestion.fxml"));
            Parent root = fxmlLoader.load();
            addQuestionController addQuestionController = fxmlLoader.getController();
            addQuestionController.loadAddQuestionController(user);
            Stage stage;
            stage=(Stage) lblLogin.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Add Question");
            stage.show();
        }
        catch (Exception ex)
        {
            System.out.println(ex);
        }
    }

    public void addTest()
    {
        try
        {
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("teacher/addTest.fxml"));
            Parent root = fxmlLoader.load();
            addTestController addTestController = fxmlLoader.getController();
            addTestController.loadAddTestController(user);
            Stage stage;
            stage=(Stage) lblLogin.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Add Test");
            stage.show();
        }
        catch (Exception ex)
        {
            System.out.println(ex);
        }
    }

    public void openSelectedResultView()
    {
        try
        {
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("teacher/selectedResult.fxml"));
            Parent root = fxmlLoader.load();
            selectedResultController selectedResultController = fxmlLoader.getController();
            selectedResultController.loadSelectTestController(user,null);
            Stage stage;
            stage=(Stage) lblLogin.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Result");
            stage.show();
        }
        catch (Exception ex)
        {
            System.out.println(ex);
        }
    }

    public void back()
    {
        try
        {
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("login.fxml"));
            Parent root = fxmlLoader.load();
            loginController loginController = fxmlLoader.getController();
            Stage stage;
            stage=(Stage) lblLogin.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Login");
            stage.show();
        }
        catch (Exception ex)
        {
            System.out.println(ex);
        }

    }

    public void addStudent()
    {
        Student student = new Student(0, txtStuName.getText(), UserType.STUDENT, txtStuDOB.getText(), txtStuClass.getText(), Integer.parseInt(txtStuYear.getText()));
        userDAO.AddUser(student);
        dialog("Student Added!");
        /*Student student = new Student(txtStuName.getText(), UserType.STUDENT, txtStuDOB.getText(), txtStuClass.getText(), Integer.parseInt(txtStuYear.getText()));
        UserDAO.AddUser(student);
        txtStuName.clear();
        txtStuDOB.clear();
        txtStuYear.clear();
        txtStuClass.clear();
        */
    }

    public void addTeacher()
    {
        Teacher teacher = new Teacher(0, txtTeaName.getText(), UserType.TEACHER);
        userDAO.AddUser(teacher);
        txtTeaName.clear();
        dialog("Teacher Added!");
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
