package ffmpeg;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FFMPEG
{
    final private String applicationPath;
    private Process process;

    private boolean running = false;

    private final String resolutionFormat;

    private final int fps;

    public FFMPEG(final int width, final int height, final int fps)
    {
        final String os = System.getProperty("os.name");
        if(os.toLowerCase().contains("windows"))
        {
            applicationPath = "./ffmpeg/windows/bin/ffmpeg.exe";
        }
        else
        {
            applicationPath = "./ffmpeg/linux/bin/ffmpeg";
        }
        resolutionFormat = width + "x" + height;
        this.fps = fps;
    }

    /*
    *
    Anytime you update your bitmap with new pixel information, you can write that as a new frame by sending that bitmap as input parameter to the above function eg makeVideoFrame (my_new_frame_BMP);.

    Your pipe's Process must start with these arguments:

    -y -f rawvideo -pix_fmt argb -s 800x600 -r 25 -i - ....etc

    Where...

        -f rawvideo -pix_fmt argb means accept uncompressed RGB data.

        -s 800x600 and -r 25 are example input width & height, r sets frame rate meaning FFmpeg must encode this amount of images per one second of output video.

    The full setup looks like this:

    -y -f rawvideo -pix_fmt argb -s 800x600 -r 25 -i - -c:v libx264 -profile:v baseline -level:v 3 -b:v 2500 -an out_vid.h264

    If you get blocky video output try setting two output files...

    -y -f rawvideo -pix_fmt argb -s 800x600 -r 25 -i - -c:v libx264 -profile:v baseline -level:v 3 -b:v 2500 -an out_tempData.h264 out_vid.h264

    This will output a test h264 video file which you can later put inside an MP4 container.
    The audio track -i someTrack.mp3 is optional.

    -i myH264vid.h264 -i someTrack.mp3 outputVid.mp4

    *
    * */

    private void runIfNot()
    {
        if(!running)
        {
            try
            {
                process = Runtime.getRuntime().exec(applicationPath + " -y -f rawvideo -pix_fmt rgb -s "+ resolutionFormat +" -r " +fps+ " -i - -c:v libx264 -profile:v baseline -level:v 3 -b:v "+ fps*100 + " -an out_vid.h264");
            }
            catch (final IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    public void appendImageToVideoStream(final byte[] rgbDataFrame_1920x1080) throws IOException
    {
        runIfNot();
        process.getOutputStream().write(rgbDataFrame_1920x1080);
    }

    public InputStream getInputStream()
    {
        return process.getInputStream();
    }

}
