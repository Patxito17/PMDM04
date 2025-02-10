package dam.pmdm.spyrothedragon.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import dam.pmdm.spyrothedragon.R;

public class RoundedRectangleView extends View {
    private Paint paint; // Fondo
    private Paint borderPaint; // Borde
    private RectF rect;
    private float cornerRadius;
    private float borderWidth; // Grosor del borde

    public RoundedRectangleView(Context context) {
        super(context);
        init(context);
    }

    public RoundedRectangleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public RoundedRectangleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(ContextCompat.getColor(context, R.color.orange_transparent)); // Color del fondo por defecto

        borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        borderPaint.setStyle(Paint.Style.STROKE); // Estilo de trazo para el borde
        borderPaint.setColor(ContextCompat.getColor(context, R.color.orange)); // Color del borde
        borderWidth = 5f; // Grosor del borde por defecto
        borderPaint.setStrokeWidth(borderWidth);

        rect = new RectF();
        cornerRadius = 25f; // Radio de los bordes redondeados por defecto
    }

    public void setRectangleColor(int color) {
        paint.setColor(color);
        invalidate(); // Redibujar la vista
    }

    public void setBorderColor(int color) {
        borderPaint.setColor(color);
        invalidate(); // Redibujar la vista
    }

    public void setBorderWidth(float width) {
        borderWidth = width;
        borderPaint.setStrokeWidth(borderWidth);
        invalidate();
    }

    public void setCornerRadius(float radius) {
        cornerRadius = radius;
        invalidate();
    }

    public void setRectangleBounds(float left, float top, float right, float bottom) {
        rect.set(left, top, right, bottom);
        invalidate();
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRoundRect(rect, cornerRadius, cornerRadius, paint);
        canvas.drawRoundRect(rect, cornerRadius, cornerRadius, borderPaint);
    }
}