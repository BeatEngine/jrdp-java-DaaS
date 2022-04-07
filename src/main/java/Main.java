import ffmpeg.Reference;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class Main
{

    public static void main(final String[] args)
    {
        final Recorder recorder = new Recorder();
        final Reference<InputStream> inputStreamReference = new Reference<>();
        final Thread streamer = new Thread(()->{
            recorder.stream(inputStreamReference);
        });
        streamer.start();
        final InputStream videoStream;
        while (inputStreamReference.isWaiting())
        {
            try
            {
                Thread.sleep(200);
            }
            catch (InterruptedException e)
            {
            }
        }
        if(inputStreamReference.isError())
        {
            System.out.printf("Stream null!");
            return;
        }
        inputStreamReference.setClaimed(true);
        videoStream = inputStreamReference.getReference();
        try
        {
            System.out.println("writing to output...");
            FileOutputStream fout = new FileOutputStream("streamToFile.mp4");
            int av = videoStream.available();
            while (av > 0)
            {
                fout.write(videoStream.readNBytes(av));
                av = videoStream.available();
            }
            inputStreamReference.setClaimed(false);
            System.out.println("end!");
            streamer.join();
        }
        catch (final Exception e)
        {
            inputStreamReference.setClaimed(false);
            e.printStackTrace();
        }
    }

}
