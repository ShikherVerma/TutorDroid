package com.esc101.shikherverma.github.io.esc101;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

//import android.view.Menu;//I have commented out the menu create function for now.

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //check if files in data/data/ are alright or not
        try {
            createDirectoryIfNeeded();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_main, menu);
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
    }
    */

    public void launchTextEditor (View view) {
        startActivity(new Intent(getApplicationContext(), Editor.class));
    }

    public void launchTutorial (View view) {
        //start a list type activity here
    }

    public void launchCourseDetails (View view) {
        //don't start a new activity here , just change the layout to a text screen
    }

    public void launchSettings (View view) {
        //start a list type activity or whatever activity is suited for settings page, just google once
    }

    public void launchHelp (View view) {
        //don't start a new activity here , just change the layout to a text screen
    }

    private void createDirectoryIfNeeded() throws Exception
    {
        File theDir = new File("/data/data/com.esc101.shikherverma.github.io.esc101/tcclibs");
        // if the directory does not exist, copy assets
        if (!theDir.exists())
        {
            //replace this toast with some solid alert box with round and round animation
            showpopup("Setting up Tiny C Compiler");
            copyFileFromAssets("binaries.zip","/data/data/com.esc101.shikherverma.github.io.esc101/binaries.zip");
            copyFileFromAssets("libraries.zip","/data/data/com.esc101.shikherverma.github.io.esc101/libraries.zip");
            unzip("/data/data/com.esc101.shikherverma.github.io.esc101/binaries.zip","/data/data/com.esc101.shikherverma.github.io.esc101/");
            unzip("/data/data/com.esc101.shikherverma.github.io.esc101/libraries.zip","/data/data/com.esc101.shikherverma.github.io.esc101/");
            shell("chmod 777 /data/data/com.esc101.shikherverma.github.io.esc101/buzybox");
            shell("chmod 777 /data/data/com.esc101.shikherverma.github.io.esc101/tcc");
            shell("chmod 777 /data/data/com.esc101.shikherverma.github.io.esc101/indent");
            shell("chmod 777 /data/data/com.esc101.shikherverma.github.io.esc101/file.sh");
            shell("rm /data/data/com.esc101.shikherverma.github.io.esc101/binaries.zip");
            shell("rm /data/data/com.esc101.shikherverma.github.io.esc101/libraries.zip");
            showpopup("Done configuring");
        }
        else
        {
            shell("chmod 777 /data/data/com.esc101.shikherverma.github.io.esc101/file.sh");
            showpopup("testing testing");
            shell("/data/data/com.esc101.shikherverma.github.io.esc101/file.sh");
            String output=shell("/data/data/com.esc101.shikherverma.github.io.esc101/myprogram");
            alertbox("Outbox",output,true);
        }
    }

    public void copyFileFromAssets( String file, String dest) throws Exception
    {
        AssetManager assetManager = getAssets();
        InputStream in = null;
        OutputStream fout = null;
        int count = 0;

        try
        {
            in = assetManager.open(file);
            fout = new FileOutputStream(new File(dest));

            byte data[] = new byte[1024];
            while ((count = in.read(data, 0, 1024)) != -1)
            {
                fout.write(data, 0, count);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (in != null)
            {
                try {
                    in.close();
                } catch (IOException e)
                {
                }
            }
            if (fout != null)
            {
                try {
                    fout.close();
                } catch (IOException e) {
                }
            }
        }
    }

    public static String shell(String command)
    {
        try {
            //remember the command should be of the style "/system/bin/ls /sdcard" .i.e. full paths
            Process process = Runtime.getRuntime().exec(command);
            // Reads stdout.
            // NOTE: You can write to stdin of the command using
            //       process.getOutputStream().
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));
            int read;
            char[] buffer = new char[4096];
            StringBuffer output = new StringBuffer();
            while ((read = reader.read(buffer)) > 0) {
                output.append(buffer, 0, read);
            }
            reader.close();
            // Waits for the command to finish.
            process.waitFor();

            return output.toString();
        } catch (IOException e) {

            throw new RuntimeException(e);

        } catch (InterruptedException e) {

            throw new RuntimeException(e);
        }
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

    private void showpopup(CharSequence txt)
    {
        Context context = getApplicationContext();
        CharSequence text = "Setting up tiny C compiler";
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, txt, duration);
        toast.show();
    }

    /**
     * Unzip a zip file.  Will overwrite existing files.
     *
     * @param zipFile Full path of the zip file you'd like to unzip.
     * @param location Full path of the directory you'd like to unzip to (will be created if it doesn't exist).
     * @throws IOException
     */
    public static void unzip(String zipFile, String location) throws IOException {
        int size;
        int BUFFER_SIZE=8192;
        byte[] buffer = new byte[BUFFER_SIZE];

        try {
            if ( !location.endsWith("/") ) {
                location += "/";
            }
            File f = new File(location);
            if(!f.isDirectory()) {
                f.mkdirs();
            }
            ZipInputStream zin = new ZipInputStream(new BufferedInputStream(new FileInputStream(zipFile), BUFFER_SIZE));
            try {
                ZipEntry ze = null;
                while ((ze = zin.getNextEntry()) != null) {
                    String path = location + ze.getName();
                    File unzipFile = new File(path);

                    if (ze.isDirectory()) {
                        if(!unzipFile.isDirectory()) {
                            unzipFile.mkdirs();
                        }
                    } else {
                        // check for and create parent directories if they don't exist
                        File parentDir = unzipFile.getParentFile();
                        if ( null != parentDir ) {
                            if ( !parentDir.isDirectory() ) {
                                parentDir.mkdirs();
                            }
                        }

                        // unzip the file
                        FileOutputStream out = new FileOutputStream(unzipFile, false);
                        BufferedOutputStream fout = new BufferedOutputStream(out, BUFFER_SIZE);
                        try {
                            while ( (size = zin.read(buffer, 0, BUFFER_SIZE)) != -1 ) {
                                fout.write(buffer, 0, size);
                            }

                            zin.closeEntry();
                        }
                        finally {
                            fout.flush();
                            fout.close();
                        }
                    }
                }
            }
            finally {
                zin.close();
            }
        }
        catch (Exception e)
        {}
    }

    public static void zip(String[] files, String zipFile) throws IOException {
        BufferedInputStream origin = null;
        int BUFFER_SIZE=8192;
        ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(zipFile)));
        try {
            byte data[] = new byte[BUFFER_SIZE];

            for (int i = 0; i < files.length; i++) {
                FileInputStream fi = new FileInputStream(files[i]);
                origin = new BufferedInputStream(fi, BUFFER_SIZE);
                try {
                    ZipEntry entry = new ZipEntry(files[i].substring(files[i].lastIndexOf("/") + 1));
                    out.putNextEntry(entry);
                    int count;
                    while ((count = origin.read(data, 0, BUFFER_SIZE)) != -1) {
                        out.write(data, 0, count);
                    }
                }
                finally {
                    origin.close();
                }
            }
        }
        finally {
            out.close();
        }
    }
}