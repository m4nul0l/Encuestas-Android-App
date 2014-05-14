package com.dssd.encuestas;

import java.io.File;
import java.lang.reflect.Method;
import java.util.HashMap;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Build;
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
	
	static HashMap<String, Bitmap> cacheBitmaps = new HashMap<String, Bitmap>();
	
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
		
		Point screenSize = DeviceInfoHelper.getInstance(activity).getDisplaySize();
		if(screenSize.x < screenSize.y) {
			int tmp = screenSize.x;
			screenSize.x = screenSize.y;
			screenSize.y = tmp;
		}
		
		Bitmap bitmap = loadImage(activity, imagen, screenSize.x, screenSize.y);
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
	
	public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
	    // Raw height and width of image
	    final int height = options.outHeight;
	    final int width = options.outWidth;
	    int inSampleSize = 1;
	
	    if (height > reqHeight || width > reqWidth) {
	
	        final int halfHeight = height / 2;
	        final int halfWidth = width / 2;
	
	        // Calculate the largest inSampleSize value that is a power of 2 and keeps both
	        // height and width larger than the requested height and width.
	        while ((halfHeight / inSampleSize) > reqHeight
	                && (halfWidth / inSampleSize) > reqWidth) {
	            inSampleSize *= 2;
	        }
	    }
	
	    return inSampleSize;
	}	
	
	public static Bitmap loadImageResize(Context context, String path, String image, int reqWidth, int reqHeight) {
		if(image != null && image.trim().length() > 0) {
			if(cacheBitmaps.containsKey(image)) {
				return cacheBitmaps.get(image);
			}
			
			File dir = context.getDir(path, Context.MODE_PRIVATE);
			File file = new File(dir, image);
			
			// First decode with inJustDecodeBounds=true to check dimensions
		    final BitmapFactory.Options options = new BitmapFactory.Options();
		    options.inJustDecodeBounds = true;
		    BitmapFactory.decodeFile(file.toString(), options);
		    
		    // Calculate inSampleSize
		    options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
		    
		    // Decode bitmap with inSampleSize set
		    options.inJustDecodeBounds = false;
		    Bitmap bitmap = BitmapFactory.decodeFile(file.toString(), options);
		    cacheBitmaps.put(image, bitmap);
		    return bitmap;
		}
		return null;
	}
	public static Bitmap loadImageResizePercentage(Context context, String path, String image, float percWidth, float percHeight) {
		if(image != null && image.trim().length() > 0) {
			if(cacheBitmaps.containsKey(image)) {
				return cacheBitmaps.get(image);
			}
			
			File dir = context.getDir(path, Context.MODE_PRIVATE);
			File file = new File(dir, image);
			
			// First decode with inJustDecodeBounds=true to check dimensions
		    final BitmapFactory.Options options = new BitmapFactory.Options();
		    options.inJustDecodeBounds = true;
		    BitmapFactory.decodeFile(file.toString(), options);
		    
		    int reqWidth = options.outWidth;
		    int reqHeight = options.outHeight;
		    
			if(percWidth > 0) {
				Point newSize = calculateSizeFromScreenWidthPercentage(context, options.outWidth, options.outHeight, percWidth);
				reqWidth = newSize.x;
				reqHeight = newSize.y;
			}
			if(percHeight > 0) {
				Point newSize = calculateSizeFromScreenHeightPercentage(context, options.outWidth, options.outHeight, percHeight);
				reqWidth = newSize.x;
				reqHeight = newSize.y;
			}
		    
		    // Calculate inSampleSize
		    options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
		    
		    // Decode bitmap with inSampleSize set
		    options.inJustDecodeBounds = false;
		    Bitmap bitmap = BitmapFactory.decodeFile(file.toString(), options);			
		    cacheBitmaps.put(image, bitmap);
		    return bitmap;
		}
		return null;
	}
	
	public static Bitmap loadImagePercResize(Context context, String image, float reqWidth, float reqHeight) {
		return loadImageResizePercentage(context, "", image, reqWidth, reqHeight);
	}
	
	public static Bitmap loadImage(Context context, String image, int reqWidth, int reqHeight) {
		return loadImageResize(context, "", image, reqWidth, reqHeight);
	}
	
	public static Bitmap loadImageRatingsPercResize(Context context, String image, float reqWidth, float reqHeight) {
		return loadImageResizePercentage(context, "ratings", image, reqWidth, reqHeight);
	}
	
	public static Bitmap loadImageRatings(Context context, String image, int reqWidth, int reqHeight) {
		return loadImageResize(context, "ratings", image, reqWidth, reqHeight);
	}
	
	public static Point calculateSizeFromScreenWidthPercentage(Context context, int width, int height, float widthPerc) {
		if(width == 0)
			return new Point(width, height);
		
		Point size = TemplateUtils.getScreenPercentage(context, widthPerc, -1);
		int newWidth = size.x;
		int newHeight = size.x * height / width;
		//return Bitmap.createScaledBitmap(bitmap, size.x, newHeight, false);
		return new Point(newWidth, newHeight);
	}
	
	public static Bitmap resizeBitmap(Bitmap bitmap, Context context, float width) {
		Point newSize = calculateSizeFromScreenWidthPercentage(context, bitmap.getWidth(), bitmap.getHeight(), width);
		
		Bitmap scaledBitmap = Bitmap.createBitmap(newSize.x, newSize.y, Config.ARGB_8888);

		float ratioX = newSize.x / (float) bitmap.getWidth();
		float ratioY = newSize.y / (float) bitmap.getHeight();
		float middleX = newSize.x / 2.0f;
		float middleY = newSize.y / 2.0f;

		Matrix scaleMatrix = new Matrix();
		scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

		Canvas canvas = new Canvas(scaledBitmap);
		canvas.setMatrix(scaleMatrix);
		canvas.drawBitmap(bitmap, middleX - bitmap.getWidth() / 2, middleY - bitmap.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));		
		
		return scaledBitmap;
	}
	
	public static Point calculateSizeFromScreenHeightPercentage(Context context, int width, int height, float heightPerc) {
		Point size = TemplateUtils.getScreenPercentage(context, -1, heightPerc);
		int newHeight = size.y;
		int newWidth = size.y * width / height;
		
		return new Point(newWidth, newHeight);
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
		
		Bitmap bitmap = TemplateUtils.loadImagePercResize(context, imagen, width, -1);
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
	
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	public static void hideSystemUI(Activity activity) {
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
			View mDecorView = activity.getWindow().getDecorView();
		    mDecorView.setSystemUiVisibility(
		            View.SYSTEM_UI_FLAG_LAYOUT_STABLE
		            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
		            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
		            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
		            | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
		            //| View.SYSTEM_UI_FLAG_IMMERSIVE
		            );
		    
		    ActionBar actionBar = activity.getActionBar();
		    if(actionBar != null)
		    	actionBar.hide();
		}
	}
	
	public static void collapseStatusBar(Context context) {
		try {
			Object service = context.getSystemService("statusbar");
			Class<?> statusbarManager = Class
					.forName("android.app.StatusBarManager");
			
			Method collapse = null;
			try {
				collapse = statusbarManager.getMethod("collapse");
			} catch(Exception e) {
				collapse = statusbarManager.getMethod("collapsePanels");
			}
			
			if(collapse != null) {
				collapse.setAccessible(true);
				collapse.invoke(service);
			}
		} catch (Exception ex) {}
	}
	
	public static AsyncTask<Void, Void, Void> getStatusBarHiderAsyncTask(Context c) {
		final Context context = c;
		AsyncTask<Void, Void, Void> t = new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... params) {
				while(!isCancelled()) {
					collapseStatusBar(context);
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {}
				}
				
				return null;
			}
		};
		return t;
	}
}
