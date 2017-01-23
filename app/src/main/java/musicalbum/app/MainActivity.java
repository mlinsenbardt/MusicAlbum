package musicalbum.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;


import java.io.File;
import java.util.ArrayList;

import static android.os.Environment.getExternalStorageDirectory;

public class MainActivity extends Activity {
    private static final int CAMERA_REQUEST = 1888;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private ImageView imageView;
    public SQLiteHelper dbhelper;

    private static int TAKE_PICTURE = 1;

    private String mURI;

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

                ArrayList<String> dirList = dbhelper.getPaths();
                String images_path = dirList.get(position);
                File imageDir = new File(images_path);
                File[] imageFiles = imageDir.listFiles();
                if(imageFiles.length == 0){
                    Toast.makeText(getApplicationContext(), "No images in Album!", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(getApplicationContext(), ViewAlbumActivity.class);
                intent.putExtra(android.content.Intent.EXTRA_TEXT, images_path);
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
            File folder = new File(getExternalStorageDirectory()+"/MusicAlbums/" + "/" + title);
            if (folder.isDirectory())
            {
                String[] children = folder.list();
                for (int i = 0; i < children.length; i++)
                {
                    new File(folder, children[i]).delete();
                }
                folder.delete();
            }
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
            case R.id.refresh_button:
                this.onResume();
            default:
                return super.onOptionsItemSelected(item);

        }
    }
    private void showNewDialog(){

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.activity_dialogue);
        dialog.setTitle("New Album");

        Button save = (Button) dialog.findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText editText1 = (EditText) dialog.findViewById(R.id.title);
                EditText editText2 = (EditText) dialog.findViewById(R.id.composer);
                EditText editText3 = (EditText) dialog.findViewById(R.id.notes);

                dbhelper = new SQLiteHelper(getApplicationContext());
                Toast.makeText(getApplicationContext(),"Pushed button save",Toast.LENGTH_SHORT).show();
                String title;
                String composer;
                String notes;

                if(editText1.getText().toString() !=null){

                    title = editText1.getText().toString();
                    composer = editText2.getText().toString();
                    notes = editText3.getText().toString();

                 /* Create new folder under sdcard/.... */
                    File folder = new File(getExternalStorageDirectory()+"/MusicAlbums/" + "/" + title);
                    if(!folder.exists()) {
                        folder.mkdirs();
                        Toast.makeText(getApplicationContext(), "Folder created:" + folder, Toast.LENGTH_LONG).show();
                    }

                    else{
                        Toast.makeText(getApplicationContext(), "Folder:" + folder + " already exists. Update available.", Toast.LENGTH_LONG).show();
                    }

                    String path;
                    path = folder.getPath();
                    Toast.makeText(getApplicationContext(), "Folder: " + path, Toast.LENGTH_LONG).show();

                    dbhelper.insert(title, composer, notes, path);

                    /*Run camera app and store the pic data in created folder */

                    String imgName = System.currentTimeMillis() + ".jpg";
                    Uri myUri = Uri.fromFile(new File(folder + "/" + imgName));
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                        mURI = folder.toString();
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, myUri);
                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                    }


                }
                else{
                    Toast.makeText(getApplicationContext(), "Please enter title", Toast.LENGTH_SHORT).show();
                }

                dialog.dismiss();
            }
        });

        Button cancel = (Button) dialog.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getApplicationContext(),"Pushed button cancel",Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        dialog.show();
    }

//    @Override
//    public void onRestoreInstanceState(Bundle savedInstanceState) {
//        mTextView.setText(savedInstanceState.getString(TEXT_VIEW_KEY));
//    }
//
//    // invoked when the activity may be temporarily destroyed, save the instance state here
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("URI",mURI);
    }

    @Override
    protected void onActivityResult(final int requestCode,
                                    final int resultCode,
                                    final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
//            Bundle extras = data.getExtras();
//            Bitmap imageBitmap = (Bitmap) extras.get("data");
            String folder = mURI;
            Toast.makeText(getApplicationContext(), "picture: " + folder + " stored", Toast.LENGTH_LONG).show();
            //TODO:Now we need to parse the URI for the folder, create a new file in that folder and launch another camera intent
            String imgName = System.currentTimeMillis() + ".jpg";
            Uri myUri = Uri.fromFile(new File(folder + "/" + imgName));
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                mURI = folder.toString();
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, myUri);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }

    }
    private void showEditDialog(final String title, String composer, String notes){

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.activity_dialogue);
        dialog.setTitle("Update Album");

        Button update = (Button) dialog.findViewById(R.id.save);
        update.setText("Update");

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText editText1 = (EditText) dialog.findViewById(R.id.title);
                EditText editText2 = (EditText) dialog.findViewById(R.id.composer);
                EditText editText3 = (EditText) dialog.findViewById(R.id.notes);

                dbhelper = new SQLiteHelper(getApplicationContext());
                Toast.makeText(getApplicationContext(), "Pushed button update", Toast.LENGTH_SHORT).show();
                String newTitle;
                String composer;
                String notes;

                if (editText1.getText().toString() != null) {

                    newTitle = editText1.getText().toString();
                    composer = editText2.getText().toString();
                    notes = editText3.getText().toString();

                    dbhelper.update(title, newTitle, composer, notes);
                    File oldfile = new File(getExternalStorageDirectory()+"/MusicAlbums/" + "/" + title);
                    File newfile = new File(getExternalStorageDirectory()+"/MusicAlbums/" + "/" + newTitle);
                    oldfile.renameTo(newfile);
                } else {
                    Toast.makeText(getApplicationContext(), "Please enter title", Toast.LENGTH_SHORT).show();
                }

                dialog.dismiss();
            }
        });

        Button cancel = (Button) dialog.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getApplicationContext(),"Pushed button cancel",Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        dialog.show();

    }
}
