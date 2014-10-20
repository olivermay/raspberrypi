package be.olivermay.pi.sleepyhollow;

import com.pi4j.component.relay.RelayState;
import com.pi4j.component.switches.SwitchListener;
import com.pi4j.component.switches.SwitchState;
import com.pi4j.component.switches.SwitchStateChangeEvent;
import com.pi4j.device.piface.PiFace;
import com.pi4j.device.piface.PiFaceRelay;
import com.pi4j.device.piface.PiFaceSwitch;
import com.pi4j.device.piface.impl.PiFaceDevice;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.PinMode;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
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

    private static final int INPUT_PIN = 5;

    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("Starting!");

        final PiFace piface = new PiFaceDevice(PiFace.DEFAULT_ADDRESS, Spi.CHANNEL_0);

        piface.getInputPin(INPUT_PIN).setMode(PinMode.DIGITAL_INPUT);

        for (int i = 5; i <= 5; i++) {
            piface.getInputPin(i).setMode(PinMode.DIGITAL_INPUT);
//            System.out.println(piface.getInputPin(i).getPullResistance());
            //piface.getInputPin(i).setPullResistance(PinPullResistance.OFF);
            final int port = i;
            piface.getInputPin(i).addListener(new GpioPinListenerDigital() {
                @Override
                public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
                    System.out.println("Input on pin " + port + "changed to " + event.getState());
                    piface.getOutputPin(7).setState(event.getState());
                }
            });
        }

//        int i = 0;
//
       while (true) {
            System.out.println("Input: " + piface.getInputPin(INPUT_PIN).getState());
            Thread.sleep(50);
//            if (i++ % 100 == 0) {
//                piface.getOutputPin(7).setState(PinState.HIGH);
//            }
//            if (i % 100 == 50) {
//                piface.getOutputPin(7).setState(PinState.LOW);
//            }
        }
//
    }

}
