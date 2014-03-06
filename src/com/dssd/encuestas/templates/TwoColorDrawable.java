package com.dssd.encuestas.templates;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;

public class TwoColorDrawable extends Drawable {
	
	int color1, color2;

	public TwoColorDrawable(int color1, int color2) {
		this.color1 = color1;
		this.color2 = color2;
	}

	@Override
	public void draw(Canvas canvas)	 {
		int height = getBounds().height();
	    int width = getBounds().width();
	    
	    RectF rect = new RectF(0.0f, 0.0f, width, height/2);
	    Paint p = new Paint();
	    p.setColor(color1);
	    canvas.drawRoundRect(rect, 0, 0, p);
	    
	    rect = new RectF(0.0f, height/2, width, height);
	    p.setColor(color2);
	    canvas.drawRoundRect(rect, 0, 0, p);
	}

	@Override
	public int getOpacity() {
		return 255;
	}

	@Override
	public void setAlpha(int alpha) {
	}

	@Override
	public void setColorFilter(ColorFilter cf) {
	}
}
