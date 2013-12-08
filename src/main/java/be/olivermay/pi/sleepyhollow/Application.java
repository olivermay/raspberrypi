package be.olivermay.pi.sleepyhollow;

import com.pi4j.component.switches.SwitchListener;
import com.pi4j.component.switches.SwitchState;
import com.pi4j.component.switches.SwitchStateChangeEvent;
import com.pi4j.device.piface.PiFace;
import com.pi4j.device.piface.PiFaceRelay;
import com.pi4j.device.piface.PiFaceSwitch;
import com.pi4j.device.piface.impl.PiFaceDevice;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.wiringpi.Spi;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: oliverm
 * Date: 27/11/13
 * Time: 21:37
 * To change this template use File | Settings | File Templates.
 */
public class Application {

    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("Starting!");


        final PiFace piface = new PiFaceDevice(PiFace.DEFAULT_ADDRESS, Spi.CHANNEL_0);

        piface.getSwitch(PiFaceSwitch.S1).addListener(new SwitchListener() {

            ReleaseRunnable release = new ReleaseRunnable(piface);
            Thread thread = null;

            @Override
            public void onStateChange(SwitchStateChangeEvent event) {

                if (event.getNewState() == SwitchState.OFF) {
                    piface.getOutputPin(7).high();
                    piface.getRelay(PiFaceRelay.K0).close(); // turn on relay

                    synchronized (this) {

                        if (thread != null && thread.isAlive()) {
                            release.postpone();
                        } else {
                            thread = new Thread(release);
                            thread.start();
                        }
                    }
//                    System.out.println("[SWITCH S1 PRESSED ] Turn RELAY-K0 <ON>");
                } else {
//                    System.out.println("[SWITCH S1 RELEASED] Turn RELAY-K0 <OFF>");
                }
            }
        });

    }

}
