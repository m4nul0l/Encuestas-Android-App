package com.dssd.encuestas;

import java.io.File;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dssd.encuestas.datos.Encuesta;
import com.dssd.encuestas.info.DeviceInfoHelper;

public class TemplateUtils {
	
	static final float GLOBAL_TEXT_SIZE = 0.0735f;
	
	public static View getContentView(Activity activity) {
		View contentView = activity.findViewById(android.R.id.content);
		if(contentView instanceof FrameLayout && ((FrameLayout)contentView).getChildCount() > 0) {
			contentView = ((FrameLayout)contentView).getChildAt(0);
		}
		return contentView;
	}
	
	public static void setDefaultBackground(Activity activity, Encuesta encuesta) {
		setImageBackground(activity, null, encuesta);
	}
	
	/*
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
	}*/
	
	@SuppressWarnings("deprecation")
	public static void setImageBackground(Activity activity, View contentView, Encuesta encuesta) {
		String imagen = encuesta.getImagenFondo();
		Bitmap bitmap = loadImage(activity, imagen);
		if(bitmap != null) {
			BitmapDrawable bd = new BitmapDrawable(activity.getResources(), bitmap);
			bd.setFilterBitmap(true);
			bd.setGravity(Gravity.FILL);
			
			if(contentView == null)
				contentView = getContentView(activity);
			contentView.setBackgroundDrawable(bd);
		}
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
	
	public static void setTextColor(TextView view, Encuesta encuesta) {
		String colorFuente = encuesta.getColorFuente();
		if(colorFuente != null && colorFuente.compareTo("") != 0) {
			int color = Color.parseColor("#"+colorFuente);
			view.setTextColor(color);
		}
	}
	
	public static Bitmap loadImage(Context context, String path, String image) {
		if(image != null && image.trim().length() > 0) {
			File dir = context.getDir(path, Context.MODE_PRIVATE);
			File file = new File(dir, image);
			
			Options options = new BitmapFactory.Options();
		    options.inScaled = false;			
			Bitmap bitmap = BitmapFactory.decodeFile(file.toString(), options);
			
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
	
	public static Bitmap resizeBitmap(Bitmap bitmap, Context context, float width) {
		Point size = TemplateUtils.getScreenPercentage(context, width, -1);
		int newWidth = size.x;
		int newHeight = size.x * bitmap.getHeight() / bitmap.getWidth();
		//return Bitmap.createScaledBitmap(bitmap, size.x, newHeight, false);
		
		Bitmap scaledBitmap = Bitmap.createBitmap(newWidth, newHeight, Config.ARGB_8888);

		float ratioX = newWidth / (float) bitmap.getWidth();
		float ratioY = newHeight / (float) bitmap.getHeight();
		float middleX = newWidth / 2.0f;
		float middleY = newHeight / 2.0f;

		Matrix scaleMatrix = new Matrix();
		scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

		Canvas canvas = new Canvas(scaledBitmap);
		canvas.setMatrix(scaleMatrix);
		canvas.drawBitmap(bitmap, middleX - bitmap.getWidth() / 2, middleY - bitmap.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));		
		
		return scaledBitmap;
	}
	
	public static Bitmap resizeBitmapH(Bitmap bitmap, Context context, float height) {
		Point size = TemplateUtils.getScreenPercentage(context, -1, height);
		int newHeight = size.y;
		int newWidth = size.y * bitmap.getWidth() / bitmap.getHeight();
		
		Bitmap scaledBitmap = Bitmap.createBitmap(newWidth, newHeight, Config.ARGB_8888);
		
		float ratioX = newWidth / (float) bitmap.getWidth();
		float ratioY = newHeight / (float) bitmap.getHeight();
		float middleX = newWidth / 2.0f;
		float middleY = newHeight / 2.0f;

		Matrix scaleMatrix = new Matrix();
		scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

		Canvas canvas = new Canvas(scaledBitmap);
		canvas.setMatrix(scaleMatrix);
		canvas.drawBitmap(bitmap, middleX - bitmap.getWidth() / 2, middleY - bitmap.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));		
		
		return scaledBitmap;
	}
	
	public static void setLogoEmpresa(Context context, Encuesta encuesta, ImageView iv, float width) {
		String imagen = encuesta.getLogo();
		Bitmap bitmap = TemplateUtils.loadImage(context, imagen);
		if(bitmap != null) {
			bitmap = TemplateUtils.resizeBitmap(bitmap, context, width);
			
			iv.setImageBitmap(bitmap);
			iv.setBackgroundColor(Color.TRANSPARENT);
		}
	}
	
	public static Typeface getFontRokkittRegular(Context context) {
		Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/Rokkitt-Regular.ttf");
		return typeface;
	}
	
	public static Typeface getFontRokkittBold(Context context) {
		Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/Rokkitt-Bold.ttf");
		return typeface;
	}
}
