package musicalbum.app;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import static android.app.AlertDialog.*;

public class DialogueActivity extends ActionBarActivity {
    private static final int CAMERA_REQUEST = 1888;
    private ImageView imageView;


    private EditText editText1;
    private EditText editText2;
    private EditText editText3;
    public SQLiteHelper dbhelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialogue);

        Builder builder = new Builder(this);
        builder.setMessage(R.string.new_album);
        dbhelper = new SQLiteHelper(this);
        editText1 = (EditText) findViewById(R.id.title);
        editText2 = (EditText) findViewById(R.id.composer);
        editText3 = (EditText) findViewById(R.id.notes);

        Button save = (Button) findViewById(R.id.save);
        save.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getApplicationContext(),"Pushed button save",Toast.LENGTH_SHORT).show();
                String title;
                String composer;
                String notes;

                if(editText1.getText().toString() !=null){

                title = editText1.getText().toString();
                composer = editText2.getText().toString();
                notes = editText3.getText().toString();

                dbhelper.insert(title, composer, notes);
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                //this.finish();
                }

            }

        });

        Button cancel = (Button) findViewById(R.id.cancel);
        cancel.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Pushed button cancel",Toast.LENGTH_SHORT).show();
            }
        });
        this.finish();
    }
}
