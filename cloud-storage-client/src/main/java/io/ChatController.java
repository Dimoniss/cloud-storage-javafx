package io;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ResourceBundle;

public class ChatController implements Initializable {
    public Button btnMoveToServer;
    public Button btnMoveToClient;
    private InputStream is;
    private OutputStream os;
    private byte[] buffer;

    public ListView<String> listViewClient;
    public TextField textFieldClient;
    public ListView<String> listViewServer;
    public TextField textFieldServer;

    public void sendToServer(ActionEvent event) throws IOException {
        String msg = textFieldClient.getText();
        os.write(msg.getBytes(StandardCharsets.UTF_8));
        os.flush();
        textFieldClient.clear();
    }

    public void sendToClient(ActionEvent event) {

    }

    public void initialize(URL location, ResourceBundle resources) {
        buffer = new byte[256];

        try {
            File dir = new File("./");
            listViewClient.getItems().clear();
            listViewClient.getItems().addAll(dir.list());
            Socket socket = new Socket("localhost", 8189);
            is = socket.getInputStream();
            os = socket.getOutputStream();
            Thread readThread = new Thread(() -> {
                try {
                    while (true) {
                        int read = is.read(buffer);
                        String msg = new String(buffer, 0, read);
                        Platform.runLater(() -> listViewClient.getItems().add(msg));
                    }
                } catch (Exception e){
                    System.err.println("Exception while read!!!");
                }

            });
            readThread.setDaemon(true);
            readThread.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void moveToServer(ActionEvent event) {
    }

    public void moveToClient(ActionEvent event) {
    }
}
