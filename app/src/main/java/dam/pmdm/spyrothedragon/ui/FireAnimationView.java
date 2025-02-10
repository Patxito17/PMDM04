package dam.pmdm.spyrothedragon.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import java.util.Random;

public class FireAnimationView extends View {
    private Paint flamePaint;
    private Path flamePath;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private boolean isAnimating = false;
    private Random random = new Random();

    public FireAnimationView(Context context) {
        super(context);
        init();
    }

    private void init() {
        flamePaint = new Paint();
        flamePaint.setStyle(Paint.Style.FILL);
        flamePaint.setAntiAlias(true);

        flamePath = new Path();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();
        int height = getHeight();

        if (width == 0 || height == 0) return; // Evita errores en medidas 0

        // Crear un gradiente que simula fuego
        LinearGradient gradient = new LinearGradient(0, height, 0, 0,
                new int[]{Color.RED, Color.YELLOW, Color.TRANSPARENT},
                new float[]{0.2f, 0.6f, 1f},
                Shader.TileMode.CLAMP);

        flamePaint.setShader(gradient);

        // Dibujar una llama con forma aleatoria
        flamePath.reset();
        flamePath.moveTo(width / 2f, height);
        flamePath.quadTo(width * 0.2f, height * 0.5f + random.nextInt(20), width / 2f, height * 0.1f + random.nextInt(30));
        flamePath.quadTo(width * 0.8f, height * 0.5f + random.nextInt(20), width / 2f, height);

        canvas.drawPath(flamePath, flamePaint);
    }

    public void startAnimation() {
        if (isAnimating) return;
        isAnimating = true;
        handler.post(animationRunnable);
    }

    public void stopAnimation() {
        isAnimating = false;
        handler.removeCallbacks(animationRunnable);
    }

    private final Runnable animationRunnable = new Runnable() {
        @Override
        public void run() {
            if (!isAnimating) return;
            invalidate(); // Redibujar para que la llama cambie
            handler.postDelayed(this, 100); // Actualizar cada 100ms
        }
    };
}