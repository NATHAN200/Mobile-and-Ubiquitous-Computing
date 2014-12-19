package coursework.coursework;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.SystemClock;
import android.view.SurfaceHolder;

/**
 * Created by Nicholas on 11/12/2014.
 */
public class WaveDrawThread extends Thread {

    SurfaceHolder surfaceHolder;
    WaveSurfaceView surfaceView;
    Paint paintWave;
    boolean run = false;
    int canvasWidth;
    int canvasHeight;
    private float xPos = 0.0f;
    private float yPos = 0.0f;
    private int i;
    private float HalfAppletHeight;
    private float HalfAppletWidth;

    public WaveDrawThread(SurfaceHolder holder, WaveSurfaceView sView)
    {
        surfaceHolder = holder;
        surfaceView = sView;
        paintWave = new Paint();
    }

    public void doStart()
    {

    }

    public  void run()
    {
            while (run) {
                Canvas c = null;
                try {
                    c = surfaceHolder.lockCanvas(null);
                    synchronized (surfaceHolder) {
                        svDraw(c);
                    }
                } finally {
                    if (c != null) {
                        surfaceHolder.unlockCanvasAndPost(c);
                    }
                }
            }


    }

    public void setRunning(boolean b)
    {
        run = b;
    }

    public void setSurfaceSize(int width, int height)
    {
        synchronized (surfaceHolder)
        {
            canvasHeight = height;
            canvasWidth = width;
            HalfAppletHeight = canvasHeight / 2;
            HalfAppletWidth  = canvasWidth / 32;
        }
    }

    private void svDraw(Canvas canvas)
    {
        if(run) {
            canvas.save();
            canvas.restore();
            canvas.drawColor(Color.WHITE);
            paintWave.setStyle(Paint.Style.FILL);
            paintWave.setColor(Color.RED);
            drawWave(canvas, 23);
            paintWave.setColor(Color.GREEN);
            drawWave(canvas, 28);
            paintWave.setColor(Color.BLUE);
            drawWave(canvas, 33);
        }
    }

    public void drawWave(Canvas theCanvas, int period)
    {
        float xPosOld = 0.0f;
        float yPosOld = 0.0f;
        int dStart = -15;
        int sDate = 0;
        int tDate = 0;

        sDate = (int)SystemClock.currentThreadTimeMillis() + dStart;

        for (i=0;i<=30;i++)
        {
            xPos = i * HalfAppletWidth;

            tDate = sDate + i;
            yPos = (float)(-100.0f * (Math.sin((tDate%period)*2*Math.PI/period)));  //Make the function of the waves a function of time so that the waves move with time

            if ( i == 0)
                paintWave.setStyle(Paint.Style.FILL);
            else
                theCanvas.drawLine(xPosOld, (yPosOld + HalfAppletHeight), xPos, (yPos + HalfAppletHeight), paintWave);
            xPosOld = xPos;
            yPosOld = yPos;
        }
    }
}


