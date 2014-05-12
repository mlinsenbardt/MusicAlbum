package musicalbum.app;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import android.graphics.drawable.Drawable;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.Toast;
import java.util.ArrayList;


public class ViewAlbumActivity extends ActionBarActivity {

    private GestureDetectorCompat mDetector;
    private ArrayList<String> images = new ArrayList<String>();
    private int image_index;
    ImageView displayed_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_album);
        mDetector = new GestureDetectorCompat(this, new MyGestureListener());

        //Load images here
        images.add("@drawable/ic_launcher");
        images.add("@drawable/flag");
        images.add("@drawable/boat");
        image_index = 0;

        Bundle extras = getIntent().getExtras();
        String albumTitle = extras.getString(android.content.Intent.EXTRA_TEXT);
        image_index = Integer.parseInt(albumTitle);
        image_index = image_index % 3;

        displayed_image = (ImageView)findViewById(R.id.CurrentPageView);
        int imageResource = getResources().getIdentifier(images.get(image_index), null, getPackageName());
        Drawable res = getResources().getDrawable(imageResource);
        displayed_image.setImageDrawable(res);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.view_album, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        this.mDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        private static final String DEBUG_TAG = "Gestures";

        @Override
        public boolean onFling(MotionEvent event1, MotionEvent event2, float velocityX, float velocityY) {
            displayed_image = (ImageView)findViewById(R.id.CurrentPageView);
            Toast.makeText(getApplicationContext(), "Detected Fling", Toast.LENGTH_SHORT).show();

            if ((velocityX < 0) && (image_index < (images.size()-1)))
            {
                image_index = image_index + 1;
                int imageResource = getResources().getIdentifier(images.get(image_index), null, getPackageName());
                Drawable res = getResources().getDrawable(imageResource);
                displayed_image.setImageDrawable(res);
            }
            else if ((velocityX > 0) && (image_index > 0))
            {
                image_index = image_index - 1;
                int imageResource = getResources().getIdentifier(images.get(image_index), null, getPackageName());
                Drawable res = getResources().getDrawable(imageResource);
                displayed_image.setImageDrawable(res);
            }
            return true;
        }
    }

}
