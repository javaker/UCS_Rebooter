package UCSRebooter;

import javafx.scene.control.Button;

import java.io.*;

import java.net.InetAddress;
import java.net.Socket;


/**
 * Class for main logic
 *
 * @author Vyacheslav Y.
 * @version 2.0
 */
public class Logic {
    /**
     * Server parameters
     */
    static final String ipAddress = "10.10.3.61";
    static final int port = 212;

    /**
     * Just ICMP request
     */
    public boolean checkPing() {
        try {
            InetAddress inet = InetAddress.getByName(ipAddress);
            return inet.isReachable(5000);
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Telnet on tcp port services
     */
    public boolean checkTelnet() {

        Socket s1;
        InputStream is;

        try {
            s1 = new Socket(ipAddress, port);
            is = s1.getInputStream();
            DataInputStream dis = new DataInputStream(is);

            if (dis != null) {
                dis.close();
                s1.close();
                return true;
            } else {
                dis.close();
                s1.close();
                return false;
            }

        } catch (IOException e) {
            System.out.println("Telnet connect fail");
            return false;

        }

    }

    /**
     * Reboot server from windows cmd
     */
    public void shutdownServer() {
        String[] commands = {"shutdown", "-r", "-m", ipAddress, "-t", "0"};

        String line = null;

        cmd(commands, line);
    }

    /**
     * Reset virtual machine from windows cmd
     */
    public void resetServer() {
        String[] commands = {"\"C:\\Perl\\Bin\\perl.exe\"", "\"C:\\Program Files (x86)\\VMware\\VMware vSphere CLI\\bin\\vmware-cmd.pl\"", "-H", "10.10.2.13", "-U", "sa-ucs-rebooter", "-P", "6CwQTyF9uj", "/vmfs/volumes/Vol2-VMWare/s-ucs01.demetra.local/s-ucs01.demetra.local.vmx", "reset", "hard"};

        String line = null;

        cmd(commands, line);
    }

    /**
     * Exec outer commands with read information from cmd
     */
    public void cmd(String[] commands, String line) {
        try {
            Process cmd = Runtime.getRuntime().exec(commands);
            cmd.waitFor();

            BufferedReader outReader = new BufferedReader(new InputStreamReader(cmd.getInputStream(), "cp866"));
            while ((line = outReader.readLine()) != null) {
                System.out.println("CMD Success: " + line);
            }
            outReader.close();

            BufferedReader errReader = new BufferedReader(new InputStreamReader(cmd.getErrorStream(), "cp866"));
            while ((line = errReader.readLine()) != null) {
                System.out.println("CMD Error: " + line);
            }
            errReader.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *  Method for pause after reboot/reset
     */
    public void pause(int time) {
        try {
            Thread.sleep(time * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     *  Enable/Disable button in GUI
     */
    public void enableButton(Button button) {
        button.setDisable(false);
    }

    public void disableButton(Button button) {
        button.setDisable(true);
    }

}
