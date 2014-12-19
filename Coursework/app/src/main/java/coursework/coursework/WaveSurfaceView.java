package coursework.coursework;

import android.content.Context;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by Nicholas on 11/12/2014.
 */
public class WaveSurfaceView extends SurfaceView implements SurfaceHolder.Callback
{

    private SurfaceHolder holder;
    WaveDrawThread drawThread =  null;

    public WaveSurfaceView(Context context)
    {
        super(context);
        holder = getHolder();
        holder.addCallback(this);
        setFocusable(true);
    }

    public WaveDrawThread getThread()
    {
        return  drawThread;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

            drawThread = new WaveDrawThread(getHolder(), this);
            drawThread.setRunning(true);
            drawThread.start();;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        drawThread.setSurfaceSize(width,height);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        drawThread.setRunning(false);
        while (retry)
        {
            try
            {
                drawThread.join();
                retry = false;
                Log.d("MyTag","ThreadStopped");
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }
}
