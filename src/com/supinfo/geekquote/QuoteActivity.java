package com.supinfo.geekquote;

import com.supinfo.geekquote.model.Quote;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

public class QuoteActivity extends Activity {

	private Quote currentQuote = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detailquote);
		Bundle extras = getIntent().getExtras();

		if(extras != null)
		{
			this.currentQuote = (Quote) extras.get("theQuote");
			TextView txtquote = (TextView) findViewById(R.id.title);
			TextView txtdate = (TextView) findViewById(R.id.date);
			RatingBar rate = (RatingBar) findViewById(R.id.rate);
			Log.d("text", Integer.toString(this.currentQuote.getRating()));
			txtquote.setText(this.currentQuote.getStrQuote());
			txtdate.setText(this.currentQuote.getCreationDate());
			rate.setRating(this.currentQuote.getRating());
		}
		
		Button submitButton = (Button) findViewById(R.id.ok);
		submitButton.setOnClickListener(new View.OnClickListener() 
		{
			public void onClick(View view) 
			{
				RatingBar rate = (RatingBar) findViewById(R.id.rate);
				getIntent().putExtra("rating", (int)rate.getRating());
				
				setResult(RESULT_OK, getIntent());
				
				if(QuoteActivity.this.currentQuote != null)
					QuoteActivity.this.currentQuote = null;
				finish();
			}
		});
		
		Button cancelButton = (Button) findViewById(R.id.cancel);
		cancelButton.setOnClickListener(new View.OnClickListener() 
		{
			public void onClick(View view) 
			{
				setResult(RESULT_CANCELED);
				QuoteActivity.this.currentQuote = null;
				finish();
			}
		});

	}
	
}
