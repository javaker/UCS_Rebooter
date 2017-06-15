package UCSRebooter;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;

/**
 * Class for multithreading reset procedure
 *
 * @author Vyacheslav Y.
 * @version 2.4
 */
public class Reset implements Runnable {


    Button resetButton;

    TextField textStatus;
    TextField textLoadServer;
    TextField textLoadServices;
    TextField textServices;
    TextField textServer;

    boolean off = true;

    MyTimer timerLoadOS;
    Thread t5;

    MyTimer timerLoadServices;
    Thread t6;


    Reset(Button resetButton, TextField textStatus, TextField textLoadServer, TextField textLoadServices, TextField textServer, TextField textService) {
        this.resetButton = resetButton;
        this.textStatus = textStatus;
        this.textLoadServer = textLoadServer;
        this.textLoadServices = textLoadServices;
        this.textServer = textServer;
        this.textServices = textService;
    }


    public void reset() {

        Logic logic = new Logic();
        createTimer();

        logic.disableButton(resetButton);
        textStatus.setText("Идет перезагрузка сервера");
        textServer.setText("Fail");
        textServices.setText("Fail");

        logic.resetServer();

        t5.start(); //start timerLoadOS

        while (true) {  //wait shutdown

            logic.pause(6);

            if (logic.checkPing()) {   //wait ping fail
                off = false;
            }

            if (!off) {

                textStatus.setText("Ждем загрузки ОС...");
                while (true) {
                    if (logic.checkPing()) {
                        textStatus.setText("Ждем запуск служб UCS...");
                        textServer.setText("Ok");
                        t5.interrupt(); //stop timerLoadOS
                        t6.start(); //start timerServices
                        while (true) {
                            logic.pause(6);
                            if (logic.checkTelnet()) {
                                t6.interrupt(); //stop timerServices
                                textStatus.setText("Проверьте работу UCS Premiera");
                                textServices.setText("Ok");
                                break;
                            }
                            if (timerLoadServices.freeze) {
                                textStatus.setText("Сервер завис, нажми Сброс");
                                logic.enableButton(resetButton);
                                break;
                            }
                        }
                        break;
                    }
                    if (timerLoadOS.freeze) {
                        textStatus.setText("Сервер завис, нажми Сброс");
                        logic.enableButton(resetButton);
                        textServer.setText("Fail");
                        break;
                    }
                    logic.pause(5);
                }
                break;
            }
            if (timerLoadOS.freeze) {
                textStatus.setText("Сервер завис, нажмиnt Сброс");
                logic.enableButton(resetButton);
                break;
            }
        }
    }

    /**
     * Create multithreading timers
     */
    public void createTimer() {
        timerLoadOS = new MyTimer(textLoadServer, 0, 59);
        t5 = new Thread(timerLoadOS);

        timerLoadServices = new MyTimer(textLoadServices, 2, 59);
        t6 = new Thread(timerLoadServices);
    }

    @Override
    public void run() {
        reset();
    }

}
