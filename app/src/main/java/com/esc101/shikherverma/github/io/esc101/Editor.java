package com.esc101.shikherverma.github.io.esc101;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.esc101.shikherverma.github.io.esc101.MainActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

public class Editor extends ActionBarActivity {

    EditText code;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        code = (EditText) findViewById(R.id.editText);
        code.setHint("//Click here to start entering your code");
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    public void launchRun (View view) {
        /*//add the ndk tiny c compiler here
        MainActivity.shell("/data/data/com.esc101.shikherverma.github.io.esc101/file.sh");
        String output = MainActivity.shell("/data/data/com.esc101.shikherverma.github.io.esc101/myprogram");
        alertbox("Output",output,true);*/
    }

    public void launchSave (View view) {
        //code to save file to sd card at sdcard/TutorDroid
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(Editor.this);
        alertDialog.setTitle("File Name");
        alertDialog.setMessage("Input file name to be saved");
        //adb.setIcon(android.R.drawable.ic_dialog_alert);//add icon file later
        // Use an EditText view to get user input.
        final EditText input = new EditText(this);
        alertDialog.setView(input);
        alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                String fileName = input.getText().toString();
                try{
                    File TutorDroidDirectory = new File("/sdcard/TutorDroid/");
                    TutorDroidDirectory.mkdirs();
                    File myFile = new File(TutorDroidDirectory, fileName);
                    myFile.createNewFile();
                    FileOutputStream fOut = new FileOutputStream(myFile);
                    OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
                    myOutWriter.append(code.getText());
                    myOutWriter.close();
                    fOut.close();
                    Toast.makeText(getBaseContext(),fileName+" Saved",Toast.LENGTH_SHORT).show();
                }
                catch (Exception e) {
                    Toast.makeText(getBaseContext(), e.getMessage(),Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }
        });

        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.create();
        alertDialog.show();

    }

    public void launchClear (View view) {
        //clear screen
        code.setText("");
    }

    public void launchRedo (View view) {
        //redo the last undo
    }

    public void launchUndo (View view) {
        //undo the last word delete or paste or cut or copy or the last redo
    }

    public void alertbox(String title,String message,boolean buttonBoolean)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setTitle(title);


        if(buttonBoolean)
        {
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User clicked OK button
                }
            });

            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User clicked cancel button
                }
            });
        }
        AlertDialog alert =builder.create();
        alert.show();
    }

}
