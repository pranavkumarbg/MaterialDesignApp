package com.rhcloud.phpnew_pranavkumar.materialdesignapp;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class NavigationDrawerFragment extends Fragment {

    public  static  final String PREF_FILE_NAME="testpref";
    public  static  final String KEY_USER_LEARNED_DRAWER="user_learned_drawer";
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mdrawerLayout;
    private boolean mUserLearnedDrawer;
    private boolean mFromSavedInstanceState;
    private View view;
    private RecyclerView recyclerView;
    private MyAdapter myAdapter;

    public NavigationDrawerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        mUserLearnedDrawer=Boolean.valueOf(readFromPreferences(getActivity(),KEY_USER_LEARNED_DRAWER,"false"));

        if(savedInstanceState!=null)
        {
            mFromSavedInstanceState=true;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout=inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
        // Inflate the layout for this fragment
        recyclerView= (RecyclerView) layout.findViewById(R.id.drawerlist);
        myAdapter=new MyAdapter(getActivity(),getData());
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {

                Toast.makeText(getActivity(),"item clicked"+position,Toast.LENGTH_SHORT).show();

                if(position==0)
                {
                    startActivity(new Intent(getActivity(),SubActivity.class));
                }
            }

            @Override
            public void onLongClick(View view, int position) {

                Toast.makeText(getActivity(),"item onLongclicked",Toast.LENGTH_SHORT).show();
            }
        }));
        return layout;
    }

    public static List<Info> getData()
    {
        List<Info> data= new ArrayList<>();

        int[] icons={R.drawable.icon_smile_face_1,R.drawable.icon_smile_face_2,R.drawable.icon_smile_face_3,R.drawable.icon_smile_face_4};
        String[] title={"Images","Videos","Latest","About Us"};

        for(int i=0;i<title.length && i<icons.length;i++)
        {

            Info info=new Info();
            info.iconId=icons[i];
            info.title=title[i];
            data.add(info);
        }
        return data;
    }

    public void setUp(int fragment_navigation_drawer, DrawerLayout drawerLayout, final Toolbar toolbar) {
        view=getActivity().findViewById(fragment_navigation_drawer);
        mdrawerLayout=drawerLayout;
        mDrawerToggle= new ActionBarDrawerToggle(getActivity(),drawerLayout,toolbar,R.string.drawer_open,R.string.drawer_close)
        {
            @Override
            public void onDrawerOpened(View drawerView)
            {
                super.onDrawerOpened(drawerView);
               if(!mUserLearnedDrawer)
               {
                   mUserLearnedDrawer=true;
                   saveToPreferences(getActivity(),KEY_USER_LEARNED_DRAWER,mUserLearnedDrawer+"");
               }
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView)
            {
                super.onDrawerClosed(drawerView);
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                if(slideOffset<0.6) {
                    toolbar.setAlpha(1 - slideOffset);
                }
            }
        };

        if(!mUserLearnedDrawer && !mFromSavedInstanceState)
        {
            mdrawerLayout.openDrawer(view);
        }
        mdrawerLayout.setDrawerListener(mDrawerToggle);
        mdrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });
    }

    public static void saveToPreferences(Context context, String preferenceName, String preferenceValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME,context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(preferenceName, preferenceValue);
        editor.apply();
    }


    public static String readFromPreferences(Context context, String preferenceName, String defaultValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME,context.MODE_PRIVATE);
        return sharedPreferences.getString(preferenceName, defaultValue);
    }


    class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ClickListener clickListener;
        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener)
        {
            this.clickListener=clickListener;
            gestureDetector=new GestureDetector(context,new GestureDetector.SimpleOnGestureListener(){
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    super.onLongPress(e);
                    View child=recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if(child!=null && clickListener!=null)
                    {
                        clickListener.onLongClick(child,recyclerView.getChildAdapterPosition(child));
                    }
                }
            });
        }
        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child=rv.findChildViewUnder(e.getX(), e.getY());
            if(child!=null && clickListener!=null && gestureDetector.onTouchEvent(e))
            {
                clickListener.onClick(child,rv.getChildAdapterPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

    public static interface ClickListener
    {
        public void onClick(View view,int position);
        public void onLongClick(View view,int position);

    }
}
