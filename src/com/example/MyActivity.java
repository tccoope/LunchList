package com.example;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.*;
import android.widget.*;

public class MyActivity extends ListActivity {
    Cursor model=null;
    RestaurantAdapter adapter=null;
    RestaurantHelper helper=null;
    public final static String ID_EXTRA="com.example._ID";
    SharedPreferences prefs=null;
    private SharedPreferences.OnSharedPreferenceChangeListener prefListener=
            new SharedPreferences.OnSharedPreferenceChangeListener(){
                public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key){
                    if(key.equals("sort_order")){
                        initList();
                    }
                }
            };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        prefs=PreferenceManager.getDefaultSharedPreferences(this);
        helper=new RestaurantHelper(this);
        initList();
        prefs.registerOnSharedPreferenceChangeListener(prefListener);
    }



    @Override
    public void onDestroy(){
        super.onDestroy();

        helper.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        new MenuInflater(this).inflate(R.menu.option, menu);

        return(super.onCreateOptionsMenu(menu));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId()==R.id.add){
            startActivity(new Intent(MyActivity.this, DetailForm.class));

            return(true);
        }
        else if(item.getItemId()==R.id.prefs){
            startActivity(new Intent(MyActivity.this, EditPreferences.class));

            return(true);
        }

        return(super.onOptionsItemSelected(item));
    }
    @Override
    public void onListItemClick(ListView list, View view, int position, long id){
        Intent i=new Intent(MyActivity.this, DetailForm.class);

        i.putExtra(ID_EXTRA, String.valueOf(id));
        startActivity(i);
    }

    class RestaurantAdapter extends CursorAdapter {
        RestaurantAdapter(Cursor c){
            super(MyActivity.this, c);
        }

        @Override
        public void bindView(View row, Context ctxt, Cursor c){
            RestaurantHolder holder=(RestaurantHolder)row.getTag();

            holder.populateForm(c, helper);
        }

        @Override
        public View newView(Context ctxt, Cursor c, ViewGroup parent){
            LayoutInflater inflater=getLayoutInflater();
            View row=inflater.inflate(R.layout.row, parent, false);
            RestaurantHolder holder=new RestaurantHolder(row);

            row.setTag(holder);

            return(row);
        }
    }

    private void initList(){
        if(model!=null){
            stopManagingCursor(model);
            model.close();
        }

        model=helper.getAll(prefs.getString("sort_order", "name"));
        stopManagingCursor(model);
        adapter=new RestaurantAdapter(model);
        setListAdapter(adapter);
    }

    static class RestaurantHolder {
        private TextView name=null;
        private TextView address=null;
        private ImageView icon=null;

        RestaurantHolder(View row){
            name=(TextView)row.findViewById(R.id.title);
            address=(TextView)row.findViewById(R.id.address);
            icon=(ImageView)row.findViewById(R.id.icon);
        }

        void populateForm(Cursor c, RestaurantHelper helper){
            name.setText(helper.getName(c));
            address.setText(helper.getAddress(c));

            if(helper.getType(c).equals("sit_down")){
                icon.setImageResource(R.drawable.beer_icon);
            }
            else if (helper.getType(c).equals("take_out")){
                icon.setImageResource(R.drawable.beer_icon);
            }
            else {
                icon.setImageResource(R.drawable.beer_icon);
            }
        }
    }
}