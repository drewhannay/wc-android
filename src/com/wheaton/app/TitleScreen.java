package com.wheaton.app;

import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class TitleScreen extends Activity {
	
	private EditText textBox;
	private Button searchButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		textBox = (EditText) findViewById(R.id.text_box);
		searchButton = (Button) findViewById(R.id.search_button);
		
		searchButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				//TODO Test this to make sure using toString works.
				String s = textBox.getText().toString();
				String s2 = "";
				
				if(s.equals("")){
					Toast.makeText(TitleScreen.this, "Please enter a search term.", Toast.LENGTH_SHORT).show();
					return;
				}
				for(int i = 0; i<s.length(); i++){
					if(s.charAt(i)==' ')
						s2+="+";
					else s2+=s.charAt(i);
				}
				Intent i = new Intent(TitleScreen.this, Results.class);
				i.putExtra("param", s2);
				try{
					URL stalkernet = new URL("http://intra.wheaton.edu/directory/whosnew/index.php/");
					Scanner in = new Scanner((InputStream) stalkernet.getContent());
				}catch(Exception e){
					Toast.makeText(TitleScreen.this, "Please connect to the Wheaton College network to use this app.", Toast.LENGTH_SHORT).show();
					return;
				}
				
				
				startActivity(i);
			}
		});
	}

}
