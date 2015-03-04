package com.example.parsingjson;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;


public class MainActivity extends ListActivity {

	private Context context;
	private static String url = "http://docs.blackberry.com/sampledata.json";
	
	private static final String VTYPE = "Type";
	private static final String VCOLOR = "Color";
	private static final String FUEL = "Fuel";
	private static final String TREAD = "Tread";
	
	ArrayList<HashMap<String, String>> jsonlist = new ArrayList<HashMap<String,String>>();
	
	ListView lv;
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        new ProgressTask(MainActivity.this).execute();
    }
    
    private class ProgressTask extends AsyncTask<String, Void, Boolean> {
    	
    	private ProgressDialog dialog;
    	
    	private ListActivity activity;
    	
    	private Context context;
    	
    	public ProgressTask(ListActivity activity) {
    		this.activity = activity;
    		context = activity;
    		dialog = new ProgressDialog(context);
		}

    	@Override
    	protected void onPreExecute() {
    		this.dialog.setMessage("Progress start");
    		this.dialog.show();
    	}
    	
    	@Override
    	protected void onPostExecute(Boolean result) {
    		if(dialog.isShowing()){
    			dialog.dismiss();
    		}
    		
			ListAdapter adapter = new SimpleAdapter(context, jsonlist,
					R.layout.list_item, new String[] { VTYPE, VCOLOR, FUEL,
							TREAD }, new int[] { R.id.vehicleType,
							R.id.vehicleColor, R.id.fuel, R.id.tread_type });
			
			setListAdapter(adapter);
			
			lv = getListView();
    	}
    	
		@Override
		protected Boolean doInBackground(String... params) {
			
			JSONParser jParser = new JSONParser();
			
			JSONArray json = jParser.getJSONFromUrl(url);
			
			for(int i = 0; i < json.length(); i++) {
				try {
					JSONObject c = json.getJSONObject(i);
					String vtype = c.getString("vehicleType");
					String vcolor = c.getString("vehicleColor");
					String vfuel = c.getString("fuel");
					String vtread = c.getString("treadType");
					
					HashMap<String, String> map = new HashMap<String, String>();
					
					map.put(VTYPE, vtype);
					map.put(VCOLOR, vcolor);
					map.put(FUEL, vfuel);
					map.put(TREAD, vtread);
					jsonlist.add(map);
					
				} catch(JSONException e) {
					e.printStackTrace();
				}
			}
			
			return false;
		}
    }
}
