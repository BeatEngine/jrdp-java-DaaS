package ffmpeg;

public class Reference<T>
{
    private T reference = null;

    public Reference()
    {

    }

    public Reference(final T reference)
    {
        this.reference = reference;
    }

    public void setReference(final T reference)
    {
        this.reference = reference;
    }

    public T getReference()
    {
        return reference;
    }
}
