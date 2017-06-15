package UCSRebooter;

import javafx.scene.control.TextField;


/**
 * Class for multithreading timer
 *
 * @author Vyacheslav Y.
 * @version 2.0
 */
public class MyTimer implements Runnable {

    TextField textField;
    Integer min;
    Integer sec;
    boolean freeze;

    /**
     * Constructor with parameters
     *
     * @param textField For display time
     * @param min       For minute
     * @param sec       For seconds
     */
    MyTimer(TextField textField, Integer min, Integer sec) {
        this.textField = textField;
        this.min = min;
        this.sec = sec;
    }

    /**
     * Self timer with change GUI textField
     *
     * @throws InterruptedException
     */
    public void timer() throws InterruptedException {
        for (int i = sec; min >= -1; i--) {
            if (min == -1) {
                textField.setText("0:0");
                freeze = true;
                break;
            }
            textField.setText(min + ":" + i);
            if (i == 1) {
                min = min - 1;
                i = sec + 1;
            }
            Thread.sleep(1000);
        }
    }

    @Override
    public void run() {
        try {
            timer();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
