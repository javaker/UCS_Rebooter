package UCSRebooter;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;

/**
 * Class for multithreading reboot procedure
 *
 * @author Vyacheslav Y.
 * @version 2.4
 */
public class Reboot implements Runnable {

    Button rebootButton;
    Button resetButton;

    TextField textStatus;
    TextField textLoadServer;
    TextField textLoadService;
    TextField textServer;
    TextField textServices;

    boolean off;

    MyTimer timerLoadOS;
    Thread t1;

    MyTimer timerLoadServices;
    Thread t2;


    Reboot(Button rebootButton, Button resetButton, TextField textStatus, TextField textLoadServer, TextField textServer, TextField textServices, TextField textLoadService) {
        this.rebootButton = rebootButton;
        this.resetButton = resetButton;
        this.textStatus = textStatus;
        this.textLoadServer = textLoadServer;
        this.textLoadService = textLoadService;
        this.textServer = textServer;
        this.textServices = textServices;
    }


    public void reboot() {

        Logic logic = new Logic();
        createTimer();

        logic.disableButton(rebootButton);

        textStatus.setText("Завершение работы ОС...");
        textServer.setText("Fail");
        textServices.setText("Fail");

        logic.shutdownServer();

        while (true) {  //wait shutdown

            logic.pause(6);

            if (!logic.checkPing()) {   //wait ping fail
                off = true;

            }

            if (off)

                t1.start();    //exec timerLoadOS
            textStatus.setText("Ждем загрузки ОС...");
            while (true) {

                if (logic.checkPing()) {
                    textStatus.setText("Ждем запуск служб UCS...");
                    textServer.setText("Ok");
                    t1.interrupt(); //stop timerLoadOS
                    t2.start(); //start timerServices
                    while (true) {

                        if (logic.checkTelnet()) {
                            t2.interrupt(); //stop timerServices
                            textStatus.setText("Проверьте работу UCS Premiera");
                            textServices.setText("Ok");
                            break;
                        }
                        if (timerLoadServices.freeze) {
                            textStatus.setText("Сервер завис, нажмите Сброс");
                            logic.enableButton(resetButton);
                            break;
                        }
                        logic.pause(10);
                    }
                    break;
                }
                if (timerLoadOS.freeze) {
                    textStatus.setText("Сервер завис, нажмите Сброс");
                    logic.enableButton(resetButton);
                    break;
                }
                logic.pause(5);
            }
            break;
        }
    }

    /**
     * Create multithreading timers
     */
    public void createTimer() {
        timerLoadOS = new MyTimer(textLoadServer, 0, 59);
        t1 = new Thread(timerLoadOS);

        timerLoadServices = new MyTimer(textLoadService, 2, 59);
        t2 = new Thread(timerLoadServices);
    }


    @Override
    public void run() {
        reboot();
    }
}
