package com.alok.clt.activities;
  
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.alok.clt.R;
import com.alok.clt.util.LicenseHandler;

public class RegistrationActivity extends Activity {
	EditText keyinp;
	EditText schoolinp;
	SharedPreferences pref;
	Editor ed;
	@Override
    public void onCreate(Bundle savedInstanceState) 
	{
        super.onCreate(savedInstanceState);
        this.setTitle("Registration");
        setContentView(R.layout.activity_registration);
        pref = getSharedPreferences("CLTprefs", Context.MODE_PRIVATE);
        ed = pref.edit();
        if(pref.getBoolean("status", false))chooseFile();
        TextView t = (TextView)findViewById(R.id.mac);
        keyinp = (EditText)findViewById(R.id.keyinp);
        schoolinp = (EditText)findViewById(R.id.schoolinp);
        keyinp.setInputType(InputType.TYPE_CLASS_PHONE);
        t.append("\n\n"+LicenseHandler.getID(getApplicationContext()));
    }
	
	public void submit(View v)
	{
		
		String key = keyinp.getText().toString();
		String school = schoolinp.getText().toString();
		if(LicenseHandler.checkLicense(key, getApplicationContext()))
			keyAccepted(school);
		else
			createDialogue();
	}
	
	private void createDialogue()
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Please check the key you entered")
		       .setCancelable(false)
		       .setNeutralButton("Retry", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		        	   keyinp.setText("");
		                dialog.cancel();
		           }
		       });
		AlertDialog alert = builder.create();
		alert.show();
	}
	
	private void keyAccepted(String school)
	{
		ed.putBoolean("status", true);
		ed.putString("school",school);
		ed.commit();
		chooseFile();
	}
	
	private void chooseFile()
	{
		Intent i = new Intent(this,FileChooserActivity.class);
		startActivity(i);
	}
}
