import ffmpeg.FFMPEG;
import ffmpeg.Reference;
import org.jcodec.api.awt.AWTSequenceEncoder;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

public class Recorder
{

    public Recorder()
    {
        //init(fileName);
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

    private byte[] rgbToRgbaByteArray(int[] arr)
    {
        final byte[] conv = new byte[(int)(arr.length*4/3.0)];
        int r = 0;
        for(int i = 0; i < arr.length; i+=3)
        {
            conv[r] = (byte)arr[i];
            conv[r+1] = (byte)arr[i+1];
            conv[r+2] = (byte)arr[i+2];
            conv[r+3] = (byte)255;
            r += 4;
        }
        return conv;
    }

    public void stream(final Reference<InputStream> outputReference)
    {
        Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
        Robot robot = null;
        try
        {
            robot = new Robot();
            FFMPEG ffmpeg = new FFMPEG(screenRect.width, screenRect.height, 25);
            int count = 0;
            float fpsAvg = 30.0f;
            int[] buffer = new int[screenRect.width*screenRect.height*4];
            boolean notSet = true;
            System.out.println("Streaming...");
            long at = System.currentTimeMillis();
            while (count < 60)
            {
                BufferedImage image = robot.createScreenCapture(screenRect);
                image.getRGB(0, 0, screenRect.width, screenRect.height, buffer, 0, 1);
                try
                {
                    ffmpeg.appendImageToVideoStream(rgbToRgbaByteArray(buffer));
                }
                catch (final IOException pipeClosed)
                {
                    System.out.println(pipeClosed.getMessage());
                    break;
                }
                if(notSet)
                {
                    final InputStream inputStream = ffmpeg.getInputStream();
                    if(inputStream != null)
                    {
                        outputReference.setReference(inputStream);
                    }
                    else
                    {
                        outputReference.setError(true);
                    }
                    notSet = false;
                }
                fpsAvg = fpsAvg + (1000 / (System.currentTimeMillis() - at)) / 2;
                at = System.currentTimeMillis();
                count++;
            }
            while (outputReference.isClaimed())
            {
                try
                {
                    Thread.sleep(200);
                }
                catch (InterruptedException e)
                {
                }
            }
            ffmpeg.close();
            System.out.println("Stream finished!");
        }
        catch (AWTException e)
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
