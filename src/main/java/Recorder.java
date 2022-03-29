import org.jcodec.api.awt.AWTSequenceEncoder;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

public class Recorder
{

    public Recorder(final String fileName)
    {
        init(fileName);
    }

    Rectangle rectangle;
    AWTSequenceEncoder encoder;
    Timer timerCount;

    boolean isRecording = false;
    File f;

    private void init(String fileName)
    {
        rectangle = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());

        //create a new file
        f = new File(fileName + ".mp4");
        try
        {
            // initialize the encoder
            encoder = AWTSequenceEncoder.createSequenceEncoder(f, 24 / 8);
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }

    private java.util.List<BufferedImage> imageList = new ArrayList<BufferedImage>();

    public static void makeVideoFromImages(List<BufferedImage> imageList, File file) throws IOException
    {

        AWTSequenceEncoder sequenceEncoder = AWTSequenceEncoder.createSequenceEncoder(file, 25);
        System.out.println("Encoding...");
        for (int i = 0; i < imageList.size(); i++)
        {
            sequenceEncoder.encodeImage(imageList.get(i));
            System.out.printf(".");
            if (i % 100 == 0)
            {
                System.out.println("");
            }
        }
        sequenceEncoder.finish();

    }

    public void startRecording()
    {
        Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
        Robot robot = null;
        try
        {
            robot = new Robot();
            File file = new File("outputVideo.mp4");

            System.out.println("Recording...");
            int count = 0;
            int frames = 30 * 10;

            float fpsAvg = 30.0f;
            long at = System.currentTimeMillis();
            while (count < 60)
            {
                BufferedImage image = robot.createScreenCapture(screenRect);
                imageList.add(image);
                count++;
                fpsAvg = fpsAvg + (1000 / (System.currentTimeMillis() - at)) / 2;
                at = System.currentTimeMillis();
            }
            makeVideoFromImages(imageList, file);
        }
        catch (AWTException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }


    public void stopRecording()
    {
        try
        {
            encoder.finish();// finish  encoding
            System.out.println("encoding finished!");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
