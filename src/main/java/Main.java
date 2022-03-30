import ffmpeg.Reference;

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
        final InputStream videoStream;
        while (inputStreamReference.getReference() == null)
        {

        }
        videoStream = inputStreamReference.getReference();

        try
        {
            streamer.join();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

}
