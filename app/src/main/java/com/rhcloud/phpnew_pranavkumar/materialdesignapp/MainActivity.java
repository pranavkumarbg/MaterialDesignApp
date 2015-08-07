package com.rhcloud.phpnew_pranavkumar.materialdesignapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.rhcloud.phpnew_pranavkumar.materialdesignapp.tabs.SlidingTabLayout;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import it.neokree.materialtabs.MaterialTab;
import it.neokree.materialtabs.MaterialTabHost;
import it.neokree.materialtabs.MaterialTabListener;

public class MainActivity extends AppCompatActivity implements MaterialTabListener {

    private Toolbar toolbar;
    private SlidingTabLayout tabHost;
    private ViewPager viewPager;
    private MaterialTabHost materialTab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.app_bar);


        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        NavigationDrawerFragment drawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);

        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), toolbar);

        materialTab = (MaterialTabHost) findViewById(R.id.materialTabHost);
        //tabHost = (SlidingTabLayout) findViewById(R.id.tabs);
        viewPager = (ViewPager) findViewById(R.id.pager);
        MyPagerAdapter myAdapter = new MyPagerAdapter(getSupportFragmentManager());
        //viewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        viewPager.setAdapter(myAdapter);
        // tabHost.setCustomTabView(R.layout.custom_tab_view, R.id.textViewtab);

        viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {

                                              @Override
                                              public void onPageSelected(int position) {
                                                  materialTab.setSelectedNavigationItem(position);
                                              }
                                          }
        );

        for (int i = 0; i < myAdapter.getCount(); i++) {
            materialTab.addTab(
                    materialTab.newTab()
                            .setText(myAdapter.getPageTitle(i))
                            .setTabListener(this)
            );
        }
        // tabHost.setDistributeEvenly(true);
//        tabHost.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
//            @Override
//            public int getIndicatorColor(int position) {
//                return getResources().getColor(R.color.accent);
//            }
//        });
//       tabHost.setBackgroundColor(getResources().getColor(R.color.primary));
//        tabHost.setSelectedIndicatorColors(getResources().getColor(R.color.accentColor));
//        tabHost.setViewPager(viewPager);
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
        if (id == R.id.navigate) {
            startActivity(new Intent(this, SubActivity.class));

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(MaterialTab materialTab) {

        viewPager.setCurrentItem(materialTab.getPosition());

    }

    @Override
    public void onTabReselected(MaterialTab materialTab) {

    }

    @Override
    public void onTabUnselected(MaterialTab materialTab) {

    }

    class MyPagerAdapter extends FragmentPagerAdapter {
        private String tabs[] = new String[]{"TAB1", "TAB2", "TAB3"};
        private String[] ntabs = getResources().getStringArray(R.array.tabs);

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }


        @Override
        public Fragment getItem(int position) {
            //MyFragment myFragment=MyFragment.getInstance(position);
            MyFragment myFragment = MyFragment.getInstance(position);
            MyFragment1 myFragment1=MyFragment1.getInstance(position);
            MyFragment2 myFragment2=MyFragment2.getInstance(position);
            Log.d("position", "position" + position);

            if (position == 0) {
                Log.d("frag", "frag0");
                return new MyFragment();
            } else if (position == 1) {
                Log.d("frag", "frag1");
                return new MyFragment1();
            } else if (position == 2) {
                Log.d("frag", "frag2");
                return new MyFragment2();
            }

            return null;
            //return myFragment;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            String s = Integer.toString(position);
            Log.i("pos:getpagetitle", s);
            SpannableString spannableString = new SpannableString("");
            return ntabs[position];
        }

        @Override
        public int getCount() {
            return 3;
        }
    }

    public static class MyFragment extends Fragment {
        private TextView textView;

        public static MyFragment getInstance(int position) {
            // MyFragment myFragment=new MyFragment();
            MyFragment myFragment = new MyFragment();


            return myFragment;

        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            // View view=inflater.inflate(R.layout.fragment_my, container, false);
            View layout = inflater.inflate(R.layout.fragment_my_first, container, false);

            textView=(TextView)layout.findViewById(R.id.textView2);

            textView.setText("hi");


            return layout;
        }
    }


    public static class MyFragment1 extends Fragment {
        private TextView textView;
        RecyclerView mRecyclerView;
        RecyclerView.LayoutManager mLayoutManager;
        GridAdapter mAdapter;
        JSONObject jsonobject;
        JSONArray jsonarray;
        String b;
        String json;
        // MyAdapter myAdapter;
        private ArrayList<FeedItem> feedItemList = new ArrayList<FeedItem>();

        public static MyFragment1 getInstance(int position) {
            // MyFragment myFragment=new MyFragment();
            MyFragment1 myFragment1 = new MyFragment1();


            return myFragment1;

        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            // View view=inflater.inflate(R.layout.fragment_my, container, false);
            View layout = inflater.inflate(R.layout.fragment_my, container, false);

            //textView=(TextView)layout.findViewById(R.id.pos);

            new DownloadJSON().execute();

            mRecyclerView = (RecyclerView) layout.findViewById(R.id.recycler_view);
            mRecyclerView.setHasFixedSize(true);

            // The number of Columns
            mLayoutManager = new GridLayoutManager(getActivity(), 2);
            // StaggeredGridLayoutManager mLayoutManager1 = new StaggeredGridLayoutManager(2,1);
            mRecyclerView.setLayoutManager(mLayoutManager);

            //textView.setText("hi 2");


            return layout;
        }

        private class DownloadJSON extends AsyncTask<String, String, String> {


            @Override
            protected void onPreExecute() {
                super.onPreExecute();

            }

            @Override
            public String doInBackground(String... params) {


                //  String json = JSONfunctions.getJSONfromURL("http://newjson-pranavkumar.rhcloud.com/GridViewJson");

                OkHttpClient okHttpClient = new OkHttpClient();
                Request request = new Request.Builder().url("http://newjson-pranavkumar.rhcloud.com/GridViewJson")
                        .build();


                try {
                    Response response = okHttpClient.newCall(request).execute();

                    json = response.body().string();


                    JSONObject reader = new JSONObject(json);
                    jsonarray = reader.getJSONArray("images");

                    for (int i = 0; i < jsonarray.length(); i++) {

                        jsonobject = jsonarray.getJSONObject(i);

                        FeedItem item = new FeedItem();

                        item.setThumbnail(jsonobject.optString("image"));
                        feedItemList.add(item);

                    }
                } catch (JSONException e) {
                    Log.e("Error", e.getMessage());
                    e.printStackTrace();
                } catch (Exception e) {

                }
                return json;
            }

            @Override
            protected void onPostExecute(String args) {

                if (args != null && !args.isEmpty()) {

                    mAdapter = new GridAdapter(getActivity(), feedItemList);

                    mRecyclerView.setAdapter(mAdapter);
                    // Toast.makeText(getApplicationContext(),args,Toast.LENGTH_LONG).show();
                    mAdapter.SetOnItemClickListener(new GridAdapter.OnItemClickListener() {

                        @Override
                        public void onItemClick(View v, int position) {
                            // do something with position
                            Toast.makeText(getActivity(),"u clicked on pos:"+position,Toast.LENGTH_LONG).show();
                        }
                    });
                }
                else
                {
                    Toast.makeText(getActivity(),"no internet or server is down",Toast.LENGTH_LONG).show();

                }


            }
        }

    }

    public static class MyFragment2 extends Fragment {
        private TextView textView;

        public static MyFragment2 getInstance(int position) {
            // MyFragment myFragment=new MyFragment();
            MyFragment2 myFragment = new MyFragment2();


            return myFragment;

        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            // View view=inflater.inflate(R.layout.fragment_my, container, false);
            View layout = inflater.inflate(R.layout.fragment_my_third, container, false);

            textView=(TextView)layout.findViewById(R.id.textView3);

            textView.setText("hi 3");


            return layout;
        }
    }


}
