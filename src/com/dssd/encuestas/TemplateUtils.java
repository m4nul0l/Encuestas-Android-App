package com.dssd.encuestas;

import java.io.File;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dssd.encuestas.datos.Encuesta;
import com.dssd.encuestas.info.DeviceInfoHelper;
import com.dssd.encuestas.templates.TwoColorDrawable;

public class TemplateUtils {
	
	public static View getContentView(Activity activity) {
		View contentView = activity.findViewById(android.R.id.content);
		if(contentView instanceof FrameLayout && ((FrameLayout)contentView).getChildCount() > 0) {
			contentView = ((FrameLayout)contentView).getChildAt(0);
		}
		return contentView;
	}
	
	public static void setDefaultBackground(Activity activity, Encuesta encuesta) {
		setTwoColorBackground(activity, null, encuesta);
	}

	public static void setTwoColorBackground(Activity activity, Encuesta encuesta) {
		setTwoColorBackground(activity, null, encuesta);
	}
	
	@SuppressWarnings("deprecation")
	public static void setTwoColorBackground(Activity activity, View contentView, Encuesta encuesta) {
		int color1 = activity.getResources().getColor(R.color.defaultColorSuperior);
		int color2 = activity.getResources().getColor(R.color.defaultColorSuperior);
		//		Color.parseColor("#DF0000"), Color.parseColor("#0000DF")
		
		if(encuesta.getColorSuperior() != null && encuesta.getColorSuperior().compareTo("") != 0) {
			color1 = Color.parseColor("#"+encuesta.getColorSuperior());
		}
		if(encuesta.getColorInferior() != null && encuesta.getColorInferior().compareTo("") != 0) {
			color2 = Color.parseColor("#"+encuesta.getColorInferior());
		}
		
		TwoColorDrawable colorDrawable = new TwoColorDrawable(color1, color2);
		
		if(contentView == null)
			contentView = getContentView(activity);
		contentView.setBackgroundDrawable(colorDrawable);
	}
	
	public static void setWidthPercentage(View view, float width) {
		setSizePercentage(view, width, -1);
	}
	public static void setHeightPercentage(View view, float height) {
		setSizePercentage(view, -1, height);
	}
	
	// TODO a borrar...
	public static void setSizePercentage(View view, View parent, float width, float height) {
		int newWidth = ViewGroup.LayoutParams.WRAP_CONTENT;
		if(width >= 0) {
			int parentWidth = parent.getWidth();
			newWidth = (int)Math.floor(width / parentWidth);
		}
		int newHeight = ViewGroup.LayoutParams.WRAP_CONTENT;
		if(height >= 0) {
			int parentHeight= parent.getHeight();
			newHeight = (int)Math.floor(width / parentHeight);
		}
		
		LayoutParams layoutParams = view.getLayoutParams();
		if(layoutParams == null)
			layoutParams = new RelativeLayout.LayoutParams(newWidth, newHeight);
		else {
			if(layoutParams instanceof RelativeLayout.LayoutParams) {
				RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams)layoutParams;
				lp.width = newWidth;
				lp.height = newHeight;
			}
		}
		view.setLayoutParams(layoutParams);
	}
	
	public static void setSizePercentage(View view, float width, float height) {
		Point newSize = getScreenPercentage(view.getContext(), width, height);
		
		LayoutParams layoutParams = view.getLayoutParams();
		if(layoutParams == null)
			layoutParams = new RelativeLayout.LayoutParams(newSize.x, newSize.y);
		else {
			if(layoutParams instanceof RelativeLayout.LayoutParams) {
				RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams)layoutParams;
				lp.width = newSize.x;
				lp.height = newSize.y;
			}
		}
		view.setLayoutParams(layoutParams);
	}
	
	public static Point getScreenPercentage(Context context, float width, float height) {
		Point screenSize = DeviceInfoHelper.getInstance(context).getDisplaySize();
		
		Point newSize = new Point(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		
		if(width >= 0) {
			int screenWidth = (screenSize.x >= screenSize.y ? screenSize.x : screenSize.y);
			newSize.x = (int)Math.floor(width * screenWidth);
		}
		if(height >= 0) {
			int screenHeight = (screenSize.x >= screenSize.y ? screenSize.y : screenSize.x);
			newSize.y = (int)Math.floor(height * screenHeight);
		}
		
		return newSize;
	}
	
	public static void setFontPercentage(TextView view, float height) {
		Point newSize = getScreenPercentage(view.getContext(), -1, height);
		
		view.setTextSize(TypedValue.COMPLEX_UNIT_PX, newSize.y);
	}
	
	public static Bitmap loadImage(Context context, String path, String image) {
		if(image != null && image.trim().length() > 0) {
			File dir = context.getDir(path, Context.MODE_PRIVATE);
			File file = new File(dir, image);
			
			Bitmap bitmap = BitmapFactory.decodeFile(file.toString());
			return bitmap;
		}
		return null;
	}
	
	public static Bitmap loadImage(Context context, String image) {
		return loadImage(context, "", image);
	}
	
	public static Bitmap loadImageRatings(Context context, String image) {
		return loadImage(context, "ratings", image);
	}
}
