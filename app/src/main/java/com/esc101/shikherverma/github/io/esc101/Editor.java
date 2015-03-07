package com.esc101.shikherverma.github.io.esc101;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;

public class Editor extends ActionBarActivity
{

	EditText code;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_editor);
		code = (EditText) findViewById(R.id.editText);
		/*code.addTextChangedListener(watch);*/
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		try
		{
			readMyProgram();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
    /*TextWatcher watch = new TextWatcher()
    {
        @Override
        public void afterTextChanged(Editable arg0) {
            // TODO Auto-generated method stub
        }
        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3)
        {
            // TODO Auto-generated method stub
        }
        @Override
        public void onTextChanged(CharSequence s, int a, int b, int c)
        {
            // TODO Auto-generated method stub
            try
            {
                doSaveToInternal();
            } catch (IOException e)
            {
                e.printStackTrace();
            }

            try {
                doIndent();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };*/

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

	public void launchRun(View view) throws IOException
	{
		launchSave(view);
		MainActivity.shell("chmod 777 /data/data/com.esc101.shikherverma.github.io.esc101/files/file.sh");
		MainActivity.shell("chmod 777 /data/data/com.esc101.shikherverma.github.io.esc101/files/myprogram.c");
		MainActivity.shell("/data/data/com.esc101.shikherverma.github.io.esc101/files/file.sh");
		String s = MainActivity.shell("cat /data/data/com.esc101.shikherverma.github.io.esc101/files/file.stdout");
		if (s == "")
		{
			getInputAndExecute();
		}
		else
		{
			alertbox("Output Box", "ERROR\nplease review your program", false);
		}
	}

	public void getInputAndExecute()
	{
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(Editor.this);
		alertDialog.setTitle("Input Box");
		alertDialog.setMessage("Please enter input. leave empty for no input");
		//adb.setIcon(android.R.drawable.ic_dialog_alert);//add icon file later
		// Use an EditText view to get user input.
		final EditText input = new EditText(this);
		alertDialog.setView(input);
		alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int whichButton)
			{
				String inputtxt = input.getText().toString();
				String[] inputarray = inputtxt.split("\n");
				alertbox("Output Box", execute(inputarray), false);
			}
		});
		alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
			}
		});
		alertDialog.create();
		alertDialog.show();
	}

	public String execute(String[] cmds)
	{
		long startTime = System.nanoTime();
		try
		{
			Process p = Runtime.getRuntime().exec("/data/data/com.esc101.shikherverma.github.io.esc101/files/myprogram");
			DataOutputStream os = new DataOutputStream(p.getOutputStream());
			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
			// Reads stdout.
			// NOTE: You can write to stdin of the command using
			//       process.getOutputStream()
			int read;
			char[] buffer = new char[4096];
			StringBuffer output = new StringBuffer();
			if(System.nanoTime() - startTime > 100000000)
			{
				p.destroy();
				return "time limit exceeded , request time out , bla bla bla bla took too much time probably an error. Advice: rerunning the code wont solve this";
			}
			if(cmds.length!=0)
			for (String tmpCmd : cmds)
			{
				if(System.nanoTime() - startTime > 100000000)
				{
					p.destroy();
					return "time limit exceeded , request time out , bla bla bla bla took too much time probably an error. Advice: rerunning the code wont solve this";
				}os.writeBytes(tmpCmd + "\n");
				os.flush();
				while ((read = reader.read(buffer)) > 0)
				{
					if(System.nanoTime() - startTime > 100000000)
					{
						p.destroy();
						return "time limit exceeded , request time out , bla bla bla bla took too much time probably an error. Advice: rerunning the code wont solve this";
					}	output.append(buffer, 0, read);
				}
			}
			else
				while ((read = reader.read(buffer)) > 0)
				{
					if(System.nanoTime() - startTime > 100000000)
					{
						p.destroy();
						return "time limit exceeded , request time out , bla bla bla bla took too much time probably an error. Advice: rerunning the code wont solve this";
					}	output.append(buffer, 0, read);
				}
			reader.close();
			// Waits for the command to finish.
			p.waitFor();

			return output.toString();
		} catch (IOException e)
		{
			throw new RuntimeException(e);
		} catch (InterruptedException e)
		{
			throw new RuntimeException(e);
		}
	}

	public void launchSaveSD(View view)
	{
		//code to save file to sd card at sdcard/TutorDroid
		final AlertDialog.Builder alertDialog = new AlertDialog.Builder(Editor.this);
		alertDialog.setTitle("File Name");
		alertDialog.setMessage("Input file name to be saved");
		//adb.setIcon(android.R.drawable.ic_dialog_alert);//add icon file later
		// Use an EditText view to get user input.
		final EditText input = new EditText(this);
		alertDialog.setView(input);
		alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int whichButton)
			{
				String fileName = input.getText().toString();
				try
				{
					File TutorDroidDirectory = new File("/sdcard/TutorDroid/");
					TutorDroidDirectory.mkdirs();
					File myFile = new File(TutorDroidDirectory, fileName);
					myFile.createNewFile();
					FileOutputStream fOut = new FileOutputStream(myFile);
					OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
					myOutWriter.append(code.getText());
					myOutWriter.close();
					fOut.close();
					Toast.makeText(getBaseContext(), fileName + " Saved", Toast.LENGTH_SHORT).show();
				} catch (Exception e)
				{
					Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
				}
				dialog.dismiss();
			}
		});

		alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss();
			}
		});
		alertDialog.create();
		alertDialog.show();

	}

	public void launchClear(View view)
	{
		//clear screen
		code.setText("");
	}

	public void launchRedo(View view)
	{
		//redo the last undo
	}

	public void launchUndo(View view)
	{
		//undo the last word delete or paste or cut or copy or the last redo
	}

	public void alertbox(String title, String message, boolean buttonBoolean)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(message)
				.setTitle(title);
		if (buttonBoolean)
		{
			builder.setPositiveButton("Ok", new DialogInterface.OnClickListener()
			{
				public void onClick(DialogInterface dialog, int id)
				{
					// User clicked OK button
				}
			});

			builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
			{
				public void onClick(DialogInterface dialog, int id)
				{
					// User clicked cancel button
				}
			});
		}
		AlertDialog alert = builder.create();
		alert.show();
	}

	public void doIndent() throws IOException
	{
		MainActivity.shell("/data/data/com.esc101.shikherverma.github.io.esc101/files/indent /data/data/com.esc101.shikherverma.github.io.esc101/files/myprogram.c");
		readMyProgram();
	}

	public void launchSave(View view) throws IOException
	{
		showpopup("program saved");
		FileOutputStream fos = openFileOutput("myprogram.c", Context.MODE_PRIVATE);
		Writer out = new OutputStreamWriter(fos);
		out.write(code.getText().toString());
		out.close();
	}

	public void readMyProgram() throws IOException
	{
		code.setText(MainActivity.shell("cat /data/data/com.esc101.shikherverma.github.io.esc101/files/myprogram.c"));
	}

	private void showpopup(CharSequence txt)
	{
		Context context = getApplicationContext();
		int duration = Toast.LENGTH_SHORT;
		Toast toast = Toast.makeText(context, txt, duration);
		toast.show();
	}
}
