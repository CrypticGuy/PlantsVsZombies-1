import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.effect.*;
import javafx.scene.control.*;
import javafx.stage.*;
import java.io.*;
import javafx.application.*;
import javafx.event.*;
import javafx.scene.image.ImageView;
import javafx.animation.*;

public class UsernamePageController{
    @FXML
    private TextField usernameTextField;
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
    private void OKmouseClicked(MouseEvent e){
        String Username;
        Username=usernameTextField.getText();
        if(!Username.equals("")){
            User U=new User(Username);
            Game.createUser(U);
        //Game.setUsername(Username);
            LoginPageController.userNameStage.close();
            FXMLLoader loader= new FXMLLoader(getClass().getResource("screen.fxml"));
            Parent root=null;
            try{
                root=loader.load();
            }
            catch(IOException e1){
                e1.printStackTrace();
                System.exit(0);
            }
            Game.updateStage(root,1280,720);
        }   
    }
}