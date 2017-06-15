package UCSRebooter;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.io.IOException;

/**
 * @author Vyacheslav Y.
 * @version 2.0
 */
public class Controller {

    public TextField textService;
    public TextField textServer;
    public TextField textStatus;
    public TextField textLoadServer;
    public TextField textLoadServices;

    public Button rebootButton;
    public Button resetButton;


    Logic logic;

    /**
     * Thread for multithreading
     */
    Thread t3;
    Thread t4;

    /**
     * Autostart check on load app
     */
    @FXML
    private void initialize() {
        try {
            checkSRV();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Cold reset server procedure
     */
    public void reset() {
        t4 = new Thread(new Reset(resetButton, textStatus, textLoadServer, textLoadServices, textServer, textService));
        t4.start();
    }

    /**
     * Reboot server procedure
     */
    public void reboot() {
        t3 = new Thread(new Reboot(rebootButton, resetButton, textStatus, textLoadServer, textServer, textService, textLoadServices));
        t3.start();
    }


    /**
     * Check services and server
     *
     * @throws IOException
     * @throws InterruptedException
     */
    public void checkSRV() throws IOException, InterruptedException {

        logic = new Logic();

        if (logic.checkPing()) {
            textServer.setText("Ok");
            if (logic.checkTelnet())
                textService.setText("Ok");
            else
                textService.setText("Fail");
            textStatus.setText("Нажмите кнопку \"Перезагрузка\"");
            logic.enableButton(rebootButton);

        } else {
            textServer.setText("Fail");
            textService.setText("Fail");
            logic.enableButton(resetButton);
            textStatus.setText("Сервер завис, нажмите Сброс");
        }
    }
}
