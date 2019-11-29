import java.net.URL;
import java.util.*;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.effect.*;
import javafx.stage.*;
import javafx.util.Duration;
import java.io.*;
import javafx.scene.control.Label;
import javafx.application.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.*;
import javafx.scene.image.ImageView;
import javafx.animation.*;
import javafx.scene.image.Image;
import javafx.scene.shape.*;
import javafx.animation.Transition.*;

public class levelScreenController implements Initializable {
    @FXML
    private Pane rootPane;
    @FXML
    private Label timeCounter;
    @FXML
    private Label sunCounter;
    @FXML
    private GridPane tileGrid;
    private int flag;
    private String chosenPlant;
    private ArrayList<ImageView> availablePlants;
    @FXML
    private ImageView shovel;
    @FXML
    private VBox sideBar;
    @FXML
    public static Stage inGameMenu;
    public int sunCounterVal=125;

    //Initialize game screen
    private Thread sunThread;
    private Thread zombieThread;
    private Thread timerThread;
    private void sideBarAdd(String arg){
        ImageView x= new ImageView();
        x.setImage(new Image(getClass().getResourceAsStream(String.format("./public/%s.jpg",arg))));
        x.setFitWidth(180);
        x.setFitHeight(100);
        int pos= availablePlants.size();
        availablePlants.add(x);
        x.setOnMouseEntered((MouseEvent e1) -> {
            if(chosenPlant==null || chosenPlant.compareTo(arg)!=0){
                x.setEffect(new Glow(0.2));
            }
        });
        x.setOnMouseExited((MouseEvent e2) -> {
            if(chosenPlant==null || chosenPlant.compareTo(arg)!=0){
                x.setEffect(null);
            }
        });
        x.setOnMousePressed((MouseEvent e3) ->{
            for(int i=0; i<availablePlants.size(); i++){
                if(i!=pos){
                    availablePlants.get(i).setEffect(null);
                }
                else{
                    availablePlants.get(i).setEffect(new Glow(0.5));
                }
            }
            shovel.setEffect(null);
            chosenPlant=new String(arg);
        });
        sideBar.getChildren().addAll(x);
    }
    private void addShovelEvents(){
        shovel.setOnMouseEntered((MouseEvent e1) -> {
            if(chosenPlant==null || chosenPlant.compareTo("shovel")!=0){
                shovel.setEffect(new Glow(0.2));
            }
        });
        shovel.setOnMouseExited((MouseEvent e2) -> {
            if(chosenPlant==null || chosenPlant.compareTo("shovel")!=0){
                shovel.setEffect(null);
            }
        });
        shovel.setOnMousePressed((MouseEvent e3) ->{
            for(int i=0; i<availablePlants.size(); i++){
                availablePlants.get(i).setEffect(null);
            }
            shovel.setEffect(new Glow(0.5));
            chosenPlant=new String("shovel");
        });
    }
    public void sunAppear(){
        Random x= new Random();
        int i= 1+x.nextInt(9);
        int j= x.nextInt(5);
        Sun toAppear= new Sun(25);
        for(Node node: tileGrid.getChildren()){
            if(GridPane.getColumnIndex(node)==i && GridPane.getRowIndex(node)==j){
                StackPane temp= (StackPane) node;
                toAppear.playTransition();
                temp.getChildren().addAll(toAppear.getSun());
            }
        }
    }
    public void zombieAppear(){
        NormalZombie nz=new NormalZombie();
        //ImageView zombie= new ImageView();
        // zombie.setImage(new Image(getClass().getResourceAsStream("./public/zombie.gif")));
        // zombie.setFitWidth(84.18);
        // zombie.setFitHeight(96);
        // TranslateTransition tt= new TranslateTransition(Duration.millis(40000), zombie);
        // tt.setByX(-880);
        // tt.setByY(0);
        // tt.setFromX(1210);
        // tt.setFromY(190+92*j);
        // tt.setCycleCount(1);
        // tt.play();
        // zombie.toFront();
        nz.playTransition();
        rootPane.getChildren().addAll(nz.getZombie());
    }
    public void addAllBasicEventHandlers(){
        for(int i=0; i<5; i++){
            for(int j=0; j<11; j++){
                StackPane container=new StackPane();
                if(j==0){
                    ImageView x= new ImageView();
                    x.setImage(new Image(getClass().getResourceAsStream("./public/Lawnmower.png")));
                    x.setFitWidth(76.18);
                    x.setFitHeight(88);
                    container.getChildren().addAll(x);
                }
                container.setOnMousePressed((MouseEvent e1) ->{
                    if(container.getChildren().size()>0 && (chosenPlant==null || chosenPlant.compareTo("shovel")!=0)){
                        ImageView temp;
                        int flag=0;
                        for(int k=0; k<container.getChildren().size(); k++){
                            temp= (ImageView)container.getChildren().get(k);
                            if(temp.getImage()==Sun.getImage()){
                                container.getChildren().remove(temp);
                                sunCounterVal+=Sun.getToAdd();
                                sunCounter.setText(String.format("%d",sunCounterVal));
                                flag=1;
                                break;
                            }
                        }
                        if(flag==1){
                            for(int k=0; k<container.getChildren().size(); k++){
                                temp=(ImageView)container.getChildren().get(k);
                                if(temp.getImage()==SunFlower.getImage()){
                                    Sun toAdd=new Sun(15);
                                    container.getChildren().addAll(toAdd.getSun());
                                    Sun.getSunProviderTimeline(toAdd).playFromStart();
                                }
                            }
                        }
                    }
                    else if(chosenPlant!=null && chosenPlant.compareTo("")!=0 && chosenPlant.compareTo("shovel")!=0 && container.getChildren().size()==0 && GridPane.getColumnIndex(container)%10!=0){
                        // ImageView x= new ImageView();
                        // x.setImage(new Image(getClass().getResourceAsStream(String.format("./public/%s.gif",chosenPlant))));
                        // x.setFitWidth(76.18);
                        // x.setFitHeight(88);
                        // container.getChildren().addAll(x);
                        Plants x;
                        if(chosenPlant!=null && chosenPlant.compareTo("")!=0 && chosenPlant.compareTo("shovel")!=0 && container.getChildren().size()==0 && GridPane.getColumnIndex(container)%10!=0){
                            if(chosenPlant.compareTo("PeaShooter")==0){
                                x=new PeaShooter();
                                container.getChildren().addAll(x.getPlant());
                            }
                            else if(chosenPlant.compareTo("SunFlower")==0){
                                x=new SunFlower();
                                SunProvider temp= (SunProvider) x;
                                container.getChildren().addAll(x.getPlant());
                                temp.getSun().setLayoutX(0);
                                temp.getSun().setLayoutY(0);
                                container.getChildren().addAll(temp.getSun());
                                temp.playTimeline();
                            }
                            else if(chosenPlant.compareTo("Wallnut")==0){
                                x=new Wallnut();
                                container.getChildren().addAll(x.getPlant());
                            }
                            else{
                                x=new CherryBlaster();
                                container.getChildren().addAll(x.getPlant());
                            }
                            // container.getChildren().addAll(x.getPlant());
                        }
                    }
                    else if(chosenPlant!=null && chosenPlant.compareTo("shovel")==0){
                        if(GridPane.getColumnIndex(container)%10!=0){
                            if(container.getChildren().size()>0){
                                ImageView temp;
                                for(int k=0; k<container.getChildren().size(); k++){
                                    temp= (ImageView)container.getChildren().get(k);
                                    if(temp.getImage()!=Sun.getImage()){
                                        container.getChildren().remove(temp);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                });
                tileGrid.add(container,j,i);
            }
        }
    }
    public void initializeGameScreen(){
        availablePlants=new ArrayList<ImageView>();
        sideBarAdd("PeaShooter");
        sideBarAdd("SunFlower");
        sideBarAdd("Wallnut");
        sideBarAdd("CherryBomb");
        addShovelEvents();
        addAllBasicEventHandlers();
    }
    public String addTime(String timeNow){
        String[] time=timeNow.split(":",2);
        int minutes=Integer.parseInt(time[0]);
        int seconds=Integer.parseInt(time[1]);
        seconds++;
        if(seconds==60){
            seconds=0;
            minutes++;
        }
        if(seconds<10){
            if(minutes<10){
                return String.format("0%d:0%d",minutes,seconds);
            }
            return String.format("%d:0%d",minutes,seconds);
        }
        else{
            if(minutes<10){
                return String.format("0%d:%d",minutes,seconds);
            }
            return String.format("%d:%d",minutes,seconds);
        }
    }
    public void initializeThreads(){
        sunThread= new Thread(() -> {
            try{
                while(true){
                    Thread.sleep(20000);
                    Platform.runLater(() ->{
                        sunAppear();
                    }
                    );
                }
            }
            catch(InterruptedException e){
                System.exit(0);
            }
        }
        );
        zombieThread= new Thread(() -> {
            try{
                while(true){
                    Thread.sleep(5000);
                    Platform.runLater(() ->{
                        zombieAppear();
                    }
                    );
                }
            }
            catch(InterruptedException e){
                System.exit(0);
            }
        }
        );
        timerThread= new Thread(() -> {
            try{
                while(true){
                    Thread.sleep(1000);
                    Platform.runLater(() -> {
                        timeCounter.setText(addTime(timeCounter.getText()));
                    });
                }
            }
            catch(InterruptedException e){
                System.exit(0);
            }
        });
    }
    public void startThreads(){
        sunThread.start();
        zombieThread.start();
        timerThread.start();
    }
    //
    @Override
    public void initialize(URL url, ResourceBundle rb){
        initializeGameScreen();
        initializeThreads();
        startThreads();
    }


    @FXML
    private void mouseEntered(MouseEvent e){
        ImageView button= (ImageView) e.getSource();
        button.setEffect(new Glow(0.3));
    }
    @FXML
    private void mouseExited(MouseEvent e){
        ImageView button= (ImageView) e.getSource();
        button.setEffect(null);
    }
    @FXML
    private void MenuMouseClick(MouseEvent e){
        inGameMenu=new Stage();
        FXMLLoader loader1= new FXMLLoader(getClass().getResource("InGameMenu.fxml"));
        Parent root1=null;
        try{
            root1=loader1.load();
        }
        catch(IOException e1){
            e1.printStackTrace();
            System.exit(0);
        }
        inGameMenu.setScene(new Scene(root1,600,400));
        inGameMenu.initStyle(StageStyle.UNDECORATED);
        inGameMenu.show();
    }
}
