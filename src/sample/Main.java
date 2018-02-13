package sample;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.event.EventHandler;
import javafx.scene.layout.AnchorPane;
import sample.csv.FileUtils;

import java.io.File;

import static sample.csv.FileUtils.getDevInfo;
import static sample.csv.FileUtils.getPath;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        try {
            AnchorPane root = new AnchorPane();
            Label label = new Label("");
            Button button = new Button();

            label.setLayoutX(100);
            label.setLayoutY(100);
            label.setPrefWidth(1000);

            button.setLayoutX(100);
            button.setLayoutY(150);
            button.setText("开始统计手机传感器");

            button.setOnAction((event) -> {

                try {
                    //label.setText("Path=="+getPath());
                    //getDevInfo();
                    // get current dir
//                    File file = new File(System.getProperty("user.dir"));
//                    // get parent dir
//                    String parentPath = file.getParent();
//                    File file2 = new File("D:\\a\\s");
//                    System.out.println(parentPath);
//                    System.out.println(file2.getPath());
                    String exception ="111";

                    File file1 = new File(new File(System.getProperty("user.dir")).getParent()+"/data");
                    if (file1.mkdirs()) {
                        System.out.println("多级层文件夹创建成功！创建后的文件目录为：" + file1.getPath() + ",上级文件为:" + file1.getParent());
                    }else {
                         exception = getDevInfo();
                    }


                    label.setText(exception);
                } catch (Exception e) {
                    label.setText("统计异常");
                }
            });

            ObservableList<Node> children = root.getChildren();
            children.add(label);
            children.add(button);

            Scene scene = new Scene(root, 400, 400);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}
