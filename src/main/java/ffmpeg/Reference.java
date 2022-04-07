package ffmpeg;

public class Reference<T>
{
    private T reference = null;
    private boolean error = false;
    private boolean waiting = true;
    private boolean claimed = false;

    public Reference()
    {

    }

    public boolean isError()
    {
        return error;
    }

    public void setError(boolean error)
    {
        this.error = error;
        if(error)
        {
            waiting = false;
        }
    }

    public boolean isClaimed()
    {
        return claimed;
    }

    public void setClaimed(boolean claimed)
    {
        this.claimed = claimed;
    }

    public boolean isWaiting()
    {
        return waiting && reference == null;
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
