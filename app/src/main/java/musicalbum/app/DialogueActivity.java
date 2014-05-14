package musicalbum.app;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

import static android.app.AlertDialog.*;
import static android.os.Environment.getExternalStorageDirectory;

public class DialogueActivity extends ActionBarActivity {


    private EditText editText1;
    private EditText editText2;
    private EditText editText3;
    public SQLiteHelper dbhelper;

    private static int TAKE_PICTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialogue);

        dbhelper = new SQLiteHelper(this);
        editText1 = (EditText) findViewById(R.id.title);
        editText2 = (EditText) findViewById(R.id.composer);
        editText3 = (EditText) findViewById(R.id.notes);



    }
}
