package be.olivermay.pi.sleepyhollow;

import com.pi4j.device.piface.PiFace;
import com.pi4j.device.piface.PiFaceRelay;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 * User: oliverm
 * Date: 29/11/13
 * Time: 13:01
 * To change this template use File | Settings | File Templates.
 */
public class ReleaseRunnable implements Runnable {
    public boolean postpone;
    private PiFace piface;
    public boolean running;

    public ReleaseRunnable(PiFace piface) {
        this.piface = piface;
    }

    @Override
    public void run() {
        running = true;
//        System.out.println(Thread.currentThread() + "start");
        try {

            playSound();
            Thread.sleep(1000);
            while (postpone) {
                postpone = false;
                System.out.println("postponing");
                Thread.sleep(1000);
            }
            piface.getOutputPin(7).low();
            System.out.println(Thread.currentThread() + "done");
//            piface.getRelay(PiFaceRelay.K0).open(); // turn off relay
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } finally {
            running = false;
        }
    }

    public void postpone() {
        this.postpone = true;
        System.out.println("Postpone called");
    }

    public boolean isRunning() {
        return running;
    }


    private void playSound() {
//                Media media = new Media("scary.mp3");
//                MediaPlayer player = new MediaPlayer(media);
//                System.out.println(player);
//                player.play();

                List<File> files = Arrays.asList(new File("/var/scarysounds/").listFiles(new FileFilter() {
                    @Override
                    public boolean accept(File pathname) {
                        return pathname.getName().endsWith("mp3") || pathname.getName().endsWith("wav");
                    }
                }));

                Collections.shuffle(files);

                try {
                    String[] run = new String[]{"/usr/bin/mpg123", files.get(0).toString()};

                    System.out.println(run);
                    Process p = Runtime.getRuntime().exec(run);
                    p.waitFor();


                } catch (IOException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                } catch (InterruptedException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
    }
}
