package com.panter.panterapp;

import android.app.ProgressDialog;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.panter.panterapp.logic.MenuObject;
import com.panter.panterapp.util.AppConstants;
import com.panter.panterapp.util.AppController;
import com.panter.panterapp.util.MenuObjectAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity implements ActionBar.OnNavigationListener {

    private MenuObjectAdapter menuArrayAdapter;

    private ArrayList<MenuObject> menuObjects;

    // Progress dialog
    private ProgressDialog pDialog;

    /**
     * The serialization (saved instance state) Bundle key representing the
     * current dropdown position.
     */
    private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);

        makeJsonRequest();

        // Set up the action bar to show a dropdown list.
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

        MenuObject placeMenu = new MenuObject();
        placeMenu.setName("Empty");
        placeMenu.setMenuID("");
        placeMenu.setIconURL("");
        placeMenu.setLinkURL("");

        menuObjects = new ArrayList<>();

        menuObjects.add(placeMenu);

        // Specify a ArrayAdapter to populate the dropdown list.
        menuArrayAdapter =
                new MenuObjectAdapter(
                        MainActivity.this,
                        R.layout.menu_item,
                        menuObjects);

        invalidateOptionsMenu();

        // Set up the dropdown list navigation in the action bar.
        actionBar.setListNavigationCallbacks(menuArrayAdapter, this);
    }

    /**
     * Method to make json request
     * */
    private void makeJsonRequest() {

        showPDialog();

        JsonArrayRequest request = new JsonArrayRequest(AppConstants.URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //Log.d(AppConstants.TAG, response.toString());

                        try {
                            // Parsing json array response
                            // loop through each json object

                            MenuObject menuObject = new MenuObject();
                            menuObjects.clear();

                            for (int i = 0; i < response.length(); i++) {

                                JSONObject menu = (JSONObject) response
                                        .get(i);

                                String menuID = menu.getString(AppConstants.TAG_MENU_ID);
                                String name = menu.getString(AppConstants.TAG_NAME);
                                String iconURL = menu.getString(AppConstants.TAG_ICON_URL);
                                String linkURL = menu.getString(AppConstants.TAG_LINK_URL);

                                menuObject.setMenuID(menuID);
                                menuObject.setName(name);
                                menuObject.setIconURL(iconURL);
                                menuObject.setLinkURL(linkURL);

                                menuObjects.add(menuObject);

                                Log.d(AppConstants.TAG, "MenuObject: " + menuObject.getMenuID() + menuObject.getName());
                                Log.d(AppConstants.TAG, "MenuObject: " +  menuObject.getMenuID() + menuObjects.get(i).getName());

                            }System.out.println("Prueba " + menuObjects.size());

                            menuArrayAdapter.clear();

                            for (int i = 0; i < menuObjects.size(); i++) {

                                menuArrayAdapter.add(menuObjects.get(i));
                                menuArrayAdapter.notifyDataSetChanged();

                            }

                            // Set up the dropdown list navigation in the action bar.
                            getSupportActionBar().setListNavigationCallbacks(menuArrayAdapter, MainActivity.this);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(),
                                    "Error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }

                        hidePDialog();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(AppConstants.TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
                hidePDialog();
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(request);
    }

    private void showPDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidePDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        // Restore the previously serialized current dropdown position.
        if (savedInstanceState.containsKey(STATE_SELECTED_NAVIGATION_ITEM)) {
            getSupportActionBar().setSelectedNavigationItem(
                    savedInstanceState.getInt(STATE_SELECTED_NAVIGATION_ITEM));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        // Serialize the current dropdown position.
        outState.putInt(STATE_SELECTED_NAVIGATION_ITEM,
                getSupportActionBar().getSelectedNavigationIndex());
    }


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

    @Override
    public boolean onNavigationItemSelected(int position, long id) {
        // When the given dropdown item is selected, show its contents in the
        // container view.
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();
        return true;
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }

}
