package musicalbum.app;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends ActionBarActivity {
    private static final int CAMERA_REQUEST = 1888;
    private ImageView imageView;
    public SQLiteHelper dbhelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ListView listview = (ListView) findViewById(R.id.listView);
        registerForContextMenu(listview);
    }

    @Override
    protected void onResume() {
        super.onResume();
        dbhelper = new SQLiteHelper(this);

        final ListView listview = (ListView) findViewById(R.id.listView);
        ArrayList<String> values = new ArrayList<String>();
        values.add(0,"You do not have any albums.");

        //I think we can retreive the list of "albums" and populate the list
        //here.
        ArrayList<String> list = dbhelper.getTitles();

        if(list.size()==0){

            list = values;
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), ViewAlbumActivity.class);
                intent.putExtra(android.content.Intent.EXTRA_TEXT, Integer.toString(position));
                startActivity(intent);
            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        super .onCreateContextMenu(menu, v, menuInfo);
            menu.setHeaderTitle("Album");
            menu.add(0, v.getId(), 0, "Remove");
            menu.add(0, v.getId(), 0, "Edit");
            menu.add(0, v.getId(), 0, "Show");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        dbhelper = new SQLiteHelper(this);
        ArrayList<String> titleList = dbhelper.getTitles();
        String title = titleList.get(info.position);
        ArrayList<String> composerList = dbhelper.getComposers();
        String composer = composerList.get(info.position);
        ArrayList<String> notesList = dbhelper.getNotes();
        String notes = notesList.get(info.position);
        if(item.getTitle()=="Remove")
        {
            //dbhelper = new SQLiteHelper(this);
            dbhelper.remove_item(title);
            Toast.makeText(getApplicationContext(), title, Toast.LENGTH_SHORT).show();
        }
        else if(item.getTitle()=="Edit")
        {
            Toast.makeText(getApplicationContext(), composer, Toast.LENGTH_SHORT).show();
            Toast.makeText(getApplicationContext(), notes, Toast.LENGTH_SHORT).show();
            showEditDialog(title, composer, notes);
        }
        else
        {
            return false;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {


            case R.id.new_album_button:

                Toast.makeText(getApplicationContext(), "New Album button", Toast.LENGTH_SHORT).show();
                showNewDialog();

                /*builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });*/
                //dialog.dismiss();

                /*Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);*/
                return true;
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }
    private void showNewDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("New Album");

        final EditText editText1 = new EditText(this);
        final EditText editText2 = new EditText(this);
        final EditText editText3= new EditText(this);

        editText1.setWidth(210);
        editText1.setHeight(100);
        editText1.setHint("title");

        editText2.setWidth(210);
        editText2.setHeight(700);
        editText2.setHint("composer");

        editText3.setWidth(210);
        editText3.setHeight(1300);
        editText3.setHint("notes");

        RelativeLayout layout = new RelativeLayout(this);

        layout.addView(editText1);
        layout.addView(editText2);
        layout.addView(editText3);
        builder.setView(layout);

        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which){

                //dialog.dismiss();
                //Toast.makeText(getApplicationContext(), "Pushed button save", Toast.LENGTH_SHORT).show();
                String title;
                String composer;
                String notes;
                if(editText1.getText().toString() !=null){

                    Toast.makeText(getApplicationContext(), "Album saved", Toast.LENGTH_SHORT).show();
                    title = editText1.getText().toString();
                    composer = editText2.getText().toString();
                    notes = editText3.getText().toString();

                    dbhelper.insert(title, composer, notes);
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                    dialog.dismiss();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Please enter title", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(),"Pushed button cancel",Toast.LENGTH_SHORT).show();
                //dialog.dismiss();
            }
        });

        builder.show();
    }

    private void showEditDialog(final String title, String composer, String notes){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Album");

        final EditText editText1 = new EditText(this);
        final EditText editText2 = new EditText(this);
        final EditText editText3= new EditText(this);

        editText1.setWidth(210);
        editText1.setHeight(100);
        editText1.setHint("title");
        editText1.setText(title);

        editText2.setWidth(210);
        editText2.setHeight(200);
        editText2.setHint("composer");
        editText2.setText(composer);

        editText3.setWidth(210);
        editText3.setHeight(300);
        editText3.setHint("notes");
        editText3.setText(notes);

        RelativeLayout layout = new RelativeLayout(this);

        layout.addView(editText1);
        layout.addView(editText2);
        layout.addView(editText3);
        builder.setView(layout);

        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which){

                //dialog.dismiss();
                //Toast.makeText(getApplicationContext(), "Pushed button save", Toast.LENGTH_SHORT).show();
                String newTitle;
                String composer;
                String notes;
                if(editText1.getText().toString() !=null){

                    Toast.makeText(getApplicationContext(), "Album updated", Toast.LENGTH_SHORT).show();
                    newTitle = editText1.getText().toString();
                    composer = editText2.getText().toString();
                    notes = editText3.getText().toString();

                    dbhelper.update(title, newTitle, composer, notes);
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                    dialog.dismiss();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Please enter title", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(),"Pushed button cancel",Toast.LENGTH_SHORT).show();
                //dialog.dismiss();
            }
        });

        builder.show();
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(photo);
        }
    }

}
