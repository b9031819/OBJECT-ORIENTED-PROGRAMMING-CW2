package assignment2.View;

import assignment2.Factories.DataFactory;
import assignment2.Factories.QuestionDAO;
import assignment2.Models.Question.*;
import assignment2.Models.User.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class addQuestionController {

    @FXML private Button btnSave;
    @FXML private ComboBox cbType;
    @FXML private TextField txtQuestion;
    @FXML private TextField txtAnswer;
    @FXML private TextField txtOp1;
    @FXML private TextField txtOp2;
    @FXML private TextField txtOp3;
    @FXML private TextField txtOp4;
    @FXML private TextField txtPoints;
    @FXML private TextField txtTags;
    @FXML private TextField txtYear;
    @FXML private Label lblQuestion;
    @FXML private Label lblAnswer;
    @FXML private Label lblOp1;
    @FXML private Label lblOp2;
    @FXML private Label lblOp3;
    @FXML private Label lblOp4;

    private QuestionDAO questionDAO = DataFactory.getInstance().getQuestionDAO();

    private User user;

    public void loadAddQuestionController(User user)
    {
        this.user = user;
        cbType.setValue("Simple");
        cbType.getItems().add("Simple");
        cbType.getItems().add("Multiple");
        cbType.getItems().add("Manual");
        typeChanged();
    }

    public void typeChanged()
    {
        txtQuestion.clear();
        txtAnswer.clear();
        txtOp1.clear();
        txtOp2.clear();
        txtOp3.clear();
        txtOp4.clear();

        if(cbType.getValue().toString().equalsIgnoreCase("simple"))
        {
            txtQuestion.setVisible(true);
            lblQuestion.setVisible(true);
            txtAnswer.setVisible(true);
            lblAnswer.setVisible(true);
            txtOp1.setVisible(false);
            lblOp1.setVisible(false);
            txtOp2.setVisible(false);
            lblOp2.setVisible(false);
            txtOp3.setVisible(false);
            lblOp3.setVisible(false);
            txtOp4.setVisible(false);
            lblOp4.setVisible(false);
        }

        if(cbType.getValue().toString().equalsIgnoreCase("multiple"))
        {
            txtQuestion.setVisible(true);
            lblQuestion.setVisible(true);
            txtAnswer.setVisible(true);
            lblAnswer.setVisible(true);
            txtOp1.setVisible(true);
            lblOp1.setVisible(true);
            txtOp2.setVisible(true);
            lblOp2.setVisible(true);
            txtOp3.setVisible(true);
            lblOp3.setVisible(true);
            txtOp4.setVisible(true);
            lblOp4.setVisible(true);
        }

        if(cbType.getValue().toString().equalsIgnoreCase("manual"))
        {
            txtQuestion.setVisible(true);
            lblQuestion.setVisible(true);
            txtAnswer.setVisible(false);
            lblAnswer.setVisible(false);
            txtOp1.setVisible(false);
            lblOp1.setVisible(false);
            txtOp2.setVisible(false);
            lblOp2.setVisible(false);
            txtOp3.setVisible(false);
            lblOp3.setVisible(false);
            txtOp4.setVisible(false);
            lblOp4.setVisible(false);
        }

    }

    public void btnSave()
    {
        String question = txtQuestion.getText();
        int points = Integer.parseInt(txtPoints.getText());
        List<String> tags = new ArrayList<>();
        tags.addAll(Arrays.asList(txtTags.getText().split(",")));
        tags.add("Year " + txtYear.getText());

        if(cbType.getValue().toString().equalsIgnoreCase("simple"))
        {
            String answer = txtAnswer.getText();
            SimpleQuestion simpleQuestion = new SimpleQuestion(0, question, tags, QuestionType.SIMPLE, points, answer);
            questionDAO.AddQuestion(simpleQuestion);
        }

        if(cbType.getValue().toString().equalsIgnoreCase("multiple"))
        {
            String answer = txtAnswer.getText();
            String op1 = txtOp1.getText();
            String op2 = txtOp2.getText();
            String op3 = txtOp3.getText();
            String op4 = txtOp4.getText();

            MultipleQuestion multipleQuestion = new MultipleQuestion(0, question, tags, QuestionType.MULTIPLE, points, answer, op1, op2, op3, op4);
            questionDAO.AddQuestion(multipleQuestion);
        }

        if(cbType.getValue().toString().equalsIgnoreCase("manual"))
        {
            ManualQuestion manualQuestion = new ManualQuestion(0, question, tags, QuestionType.MANUAL, points);
            questionDAO.AddQuestion(manualQuestion);
        }
        dialog("Question created");
        typeChanged();
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
            stage=(Stage) btnSave.getScene().getWindow();
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
