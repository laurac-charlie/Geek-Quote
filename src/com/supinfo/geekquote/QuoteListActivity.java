package com.supinfo.geekquote;

import java.net.URI;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.supinfo.geekquote.model.GeekDatabase;
import com.supinfo.geekquote.model.Quote;
import com.supinfo.geekquote.model.QuoteListAdapter;

public class QuoteListActivity extends Activity {

	private ArrayList<Quote> list_quote = new ArrayList<Quote>();
	private QuoteListAdapter adapter;
	private static final int ACTIVITY_CODE = 1;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		populateList();

		Button btn = (Button) findViewById(R.id.btnAdd);
		btn.setOnClickListener(this.addListener);

		adapter = new QuoteListAdapter(this, list_quote);
		ListView list = (ListView) findViewById(R.id.listview_quote);
		list.setAdapter(adapter);
		list.setOnItemClickListener(clickListener);
	}

	public void addQuote(String strQuote) {
		Quote q = new Quote(strQuote);
		this.list_quote.add(q);
		adapter.notifyDataSetChanged();

		SQLiteDatabase db = new GeekDatabase(this).getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("textQuote", q.getStrQuote());
		values.put("dateCreation", q.getCreationDate());
		values.put("rating", q.getRating());
		db.insert(GeekDatabase.TABLE_QUOTE, null, values);
		db.close();

		/*
		 * LinearLayout layout = (LinearLayout) findViewById(R.id.main);
		 * TextView text1 = new TextView(this); text1.setText(strQuote);
		 * if(this.list_quote.size() % 2 == 0)
		 * text1.setBackgroundColor(getResources
		 * ().getColor(android.R.color.background_light)); else
		 * text1.setBackgroundColor
		 * (getResources().getColor(android.R.color.background_dark));
		 * text1.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
		 * 40));
		 * 
		 * layout.addView(text1);
		 */
	}

	private OnClickListener addListener = new OnClickListener() {
		public void onClick(View v) {
			TextView txt = (TextView) findViewById(R.id.txtquote);
			addQuote(txt.getText().toString());
			txt.setText("");
		}
	};

	private OnItemClickListener clickListener = new OnItemClickListener() {
		public void onItemClick(AdapterView<?> prent, View v, int position,
				long id) {
			Intent intent = new Intent(QuoteListActivity.this,
					QuoteActivity.class);
			intent.putExtra("theQuote",
					QuoteListActivity.this.list_quote.get(position));
			intent.putExtra("position", position);
			startActivityForResult(intent, ACTIVITY_CODE);
		}
	};

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case ACTIVITY_CODE:
			switch (resultCode) {
			case RESULT_CANCELED:
				break;
			case RESULT_OK:
				Toast t = Toast
						.makeText(this, "The quote has been saved.", 400);
				t.show();
				this.list_quote.get(data.getExtras().getInt("position"))
						.setRating(data.getExtras().getInt("rating"));
				updateQuote(this.list_quote.get(data.getExtras().getInt(
						"position")));
				break;
			default:
				break;
			}
		default:
			break;
		}
	}

	private void populateList() {
		// database
		SQLiteDatabase db = new GeekDatabase(this).getWritableDatabase();
		String[] columns = { "textQuote", "dateCreation", "rating" };

		Cursor result = db.query(GeekDatabase.TABLE_QUOTE, columns, null, null,
				null, null, null);
		Quote q = new Quote();

		result.moveToFirst();
		while (!result.isAfterLast()) {
			q.setStrQuote(result.getString(0));
			q.setCreationDate(result.getString(1));
			q.setRating(result.getInt(2));
			this.list_quote.add(q);

			result.moveToNext();
		}
		result.close();

		// web service
		JSONObject j= null;
		try {
			j = new JSONObject(sendGetRequest("http://10.0.2.2:8080/testws/rest/quotes"));
			quoteFromService(j);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
	}

	private void updateQuote(Quote q) {
		SQLiteDatabase db = new GeekDatabase(this).getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put("rating", q.getRating());
		String[] whereArgs = { q.getStrQuote(), q.getCreationDate() };

		db.update(GeekDatabase.TABLE_QUOTE, values,
				"textQuote=? and dateCreation=?", whereArgs);
		db.close();

	}

	private String sendGetRequest(String address) {
		String result = null;
		try {
			HttpClient httpClient = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(new URI(address));
			httpGet.setHeader("Accept", "application/json");
			HttpResponse response = httpClient.execute(httpGet);

			result = EntityUtils.toString(response.getEntity());
		} catch (Exception e) {
			Log.e("GeekQuote", e.getMessage(), e);
		}

		return result;
	}

	public void quoteFromService(JSONObject object) throws JSONException {
		JSONObject o = null;
		JSONArray array = object.getJSONArray("quote");
		for(int i=0; i < array.length(); i++) {
			o = (JSONObject)array.get(i);
			this.list_quote.add(new Quote(o.getString("strQuote"),o.getInt("rating")));
		}

	}

}