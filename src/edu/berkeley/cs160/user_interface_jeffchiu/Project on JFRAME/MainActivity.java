package edu.berkeley.cs160.jeffchiu.prog2;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	CustomView mCustomView;
	OnTouchListener touchListener;
	int currColor = Color.BLACK;
	LinearLayout layout;
	private Paint paint = new Paint();
	private Path path = new Path();
	private Canvas canvas;
	private int whichLetter = 0;
	private boolean showImage = false;
	private String fileName = "Image";
	private ArrayList<Bitmap> images = new ArrayList<Bitmap>();
	List<Pair<Path, Integer>> pathColorPairs = new ArrayList<Pair<Path,Integer>>();


	public class CustomView extends View {

		public CustomView(Context context) {
			super(context);
			paint.setAntiAlias(true);
			paint.setColor(currColor);
			paint.setStyle(Paint.Style.STROKE);
			setDrawingCacheEnabled(true);
		}

		@Override
		protected void onDraw(Canvas canvas) {
			if (showImage) {
				canvas.drawBitmap(images.get(0), null, new Rect(0, 0, 300, 300), paint);
				canvas.drawBitmap(images.get(1), null, new Rect(0, 650, 600, 950), paint);
				canvas.drawBitmap(images.get(2), null, new Rect(0, 350, 300, 650), paint);
			} else {
				for (Pair<Path,Integer> path_clr : pathColorPairs) {
					paint.setColor(path_clr.second);
					canvas.drawPath(path_clr.first, paint);
				}
				paint.setColor(currColor);
				canvas.drawPath(path, paint);
			}
		}

	}  

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		layout = (LinearLayout) findViewById(R.id.ll);
		mCustomView = new CustomView(this);

		layout.addView(mCustomView);

		touchListener = new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				float eventX = event.getX();
				float eventY = event.getY();

				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					path.moveTo(eventX, eventY);
					break;
				case MotionEvent.ACTION_MOVE:
				case MotionEvent.ACTION_UP:
					path.lineTo(eventX, eventY);
					break;
				}
				mCustomView.invalidate();
				return true;
			}
		};  
		mCustomView.setOnTouchListener(touchListener);
		TextView directions = (TextView) findViewById(R.id.directions);
		directions.setText("Draw a C in BLUE");
	}


	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.red:
			Toast.makeText(this, "You have chosen " + getResources().getString(R.string.red) + ".",
					Toast.LENGTH_SHORT).show();
			pathColorPairs.add(new Pair(path, currColor));
			path = new Path();
			currColor = Color.RED;
			return true;
		case R.id.blue:
			Toast.makeText(this, "You have chosen " + getResources().getString(R.string.blue) + ".",
					Toast.LENGTH_SHORT).show();
			pathColorPairs.add(new Pair(path, currColor));
			path = new Path();
			currColor = Color.BLUE;
			return true;
		case R.id.yellow:
			Toast.makeText(this, "You have chosen " + getResources().getString(R.string.yellow) + ".",
					Toast.LENGTH_SHORT).show();
			pathColorPairs.add(new Pair(path, currColor));
			path = new Path();
			currColor = Color.YELLOW;
			return true;
		case R.id.black:
			Toast.makeText(this, "You have chosen " + getResources().getString(R.string.black) + " .",
					Toast.LENGTH_SHORT).show();
			pathColorPairs.add(new Pair(path, currColor));
			path = new Path();
			currColor = Color.BLACK;
			return true;


			case R.id.small:
				Toast.makeText(this, "You have changed to " + getResources().getString(R.string.small) + " stroke width.",
						Toast.LENGTH_SHORT).show();
				paint.setStrokeWidth(1f);
				return true;
			case R.id.medium:
				Toast.makeText(this, "You have changed to " + getResources().getString(R.string.medium) + " stroke width.",
						Toast.LENGTH_SHORT).show();
				paint.setStrokeWidth(5f);
				return true;
			case R.id.large:
				Toast.makeText(this, "You have changed to " + getResources().getString(R.string.large) + " stroke width.",
						Toast.LENGTH_SHORT).show();
				paint.setStrokeWidth(9f);
				return true;


			case R.id.eraser:
				Toast.makeText(this, "You have chosen " + getResources().getString(R.string.eraser) + " .",
						Toast.LENGTH_SHORT).show();
				pathColorPairs.add(new Pair(path, currColor));
				path = new Path();
				currColor = Color.WHITE;
				return true;
				

			case R.id.next:
				nextLetter();
				pathColorPairs.clear();
				return true;
				
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	public void nextLetter() {
		FileOutputStream outputStream;
		Bitmap bitmap;
		try {
			bitmap = Bitmap.createBitmap(mCustomView.getWidth(), mCustomView.getHeight(), Bitmap.Config.ARGB_8888);
			canvas = new Canvas(bitmap);
			canvas.drawColor(Color.WHITE);
			mCustomView.draw(canvas);
			outputStream = openFileOutput(fileName+whichLetter+".png", Context.MODE_PRIVATE);
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
			images.add(bitmap);
			outputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}		
		mCustomView.invalidate();
		path.reset();	

		if (whichLetter == 0) {
			whichLetter++;
		}
		if (whichLetter == 1) {
			mCustomView.invalidate();
			TextView directions = (TextView) findViewById(R.id.directions);
			directions.setText("Draw an A in RED");
			whichLetter++;
		} else if (whichLetter == 2) {
			mCustomView.invalidate();
			TextView directions = (TextView) findViewById(R.id.directions);
			directions.setText("Draw an L in YELLOW");
			whichLetter++;
		} else {
			displayResults();
		}
	}

	public void displayResults() {
		FileInputStream inputStream;
		try {
			for (int i = 0; i < 3; i++) {
				inputStream = openFileInput(fileName+i+".png");
				images.set(i, BitmapFactory.decodeStream(inputStream));
				inputStream.close();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			Toast.makeText(this, "ERROR: " + e.getMessage() + " .", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Toast.makeText(this, "ERROR: " + e.getMessage() + " .", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
		showImage = true;
		mCustomView.invalidate();
	}

}
