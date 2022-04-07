import org.jcodec.common.io.SeekableByteChannel;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

public class VideoStream implements SeekableByteChannel
{
    private ByteArrayOutputStream stream;
    private long position = 0L;
    private long size = 0L;
    private boolean open = true;
    public VideoStream(final ByteArrayOutputStream stream)
    {
        this.stream = stream;
    }

    @Override
    public long position() throws IOException
    {
        return position;
    }

    @Override
    public SeekableByteChannel setPosition(long l) throws IOException
    {
        position = l;
        return this;
    }

    @Override
    public long size() throws IOException
    {
        return size;
    }

    @Override
    public SeekableByteChannel truncate(long l) throws IOException
    {
        return this;
    }

    @Override
    public int read(ByteBuffer dst) throws IOException
    {
        return 0;
    }

    @Override
    public int write(ByteBuffer src) throws IOException
    {
        final byte[] tmp = new byte[src.limit()];
        ByteBuffer byteBuffer = src.get(tmp);
        size += tmp.length;
        stream.write(tmp);
        return (int)size;
    }

    @Override
    public boolean isOpen()
    {
        return open;
    }

    @Override
    public void close() throws IOException
    {
        stream.close();
        open = false;
    }
}
