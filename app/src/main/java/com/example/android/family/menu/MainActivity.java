package com.example.android.family.menu;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.widget.TextView;

import com.example.android.family.menu.animation.FlipAnimation;
import com.example.android.family.menu.db.FamilyMenuDbHelper;
import com.example.android.family.menu.model.FamilyMenu;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseToolbarActivity implements SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView rvRecyclerView;
    private List<FamilyMenu> familyMenuList = new ArrayList<FamilyMenu>();
    private RecyclerViewAdapter adapter;
    private SwipeRefreshLayout swipeToRefresh;
    private QueryData queryData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*FamilyMenu item = null;
        for (int i = 0; i < 10; i++) {
            item = new FamilyMenu();
            item.menuName = "菜名" + i;
            familyMenuList.add(item);
        }*/
        swipeToRefresh = (SwipeRefreshLayout) findViewById(R.id.swipeToRefresh);
        swipeToRefresh.setOnRefreshListener(this);
        rvRecyclerView = (RecyclerView) findViewById(R.id.rvRecyclerView);
        GridLayoutManager glm = new GridLayoutManager(this, 3);
        rvRecyclerView.setLayoutManager(glm);
        adapter = new RecyclerViewAdapter(this);
        rvRecyclerView.setAdapter(adapter);
        rvRecyclerView.addItemDecoration(new SpacesItemDecoration(3));
        rvRecyclerView.setItemAnimator(null);
        queryData = new QueryData();
        queryData.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.addDish:
                Intent intent = new Intent(this,FamilyMenuOperatorActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
        return true;
    }

    class MenuViewHolder extends RecyclerView.ViewHolder{
        TextView menuView;

        public MenuViewHolder(View itemView) {
            super(itemView);
            menuView = (TextView) itemView.findViewById(R.id.tvMenuItem);
        }

    }

    class RecyclerViewAdapter extends RecyclerView.Adapter<MenuViewHolder> {
        private Context context;
        public RecyclerViewAdapter(Context context) {
            this.context = context;
        }

        @Override
        public MenuViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.family_menu_item,parent, false);
            MenuViewHolder menuViewHolder = new MenuViewHolder(view);
            return menuViewHolder;
        }

        @Override
        public void onBindViewHolder(MenuViewHolder holder, int position) {
            final FamilyMenu familyMenu = familyMenuList.get(position);
            holder.menuView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    final FlipAnimation flipAnimation = new FlipAnimation(0, 360, view.getWidth() / 2f, view.getHeight() / 2f, view.getWidth() / 2f);
                    flipAnimation.setDuration(500);
                    flipAnimation.setFillAfter(true);
                    flipAnimation.setInterpolator(new AccelerateInterpolator());
                    flipAnimation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            ((TextView)view).setText(familyMenu.menuName);
                            view.clearAnimation();
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    view.startAnimation(flipAnimation);
                }
            });
        }

        @Override
        public int getItemCount() {
            return familyMenuList.size();
        }

    }

    class SpacesItemDecoration extends RecyclerView.ItemDecoration {
        private int space;

        public SpacesItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view,
                                   RecyclerView parent, RecyclerView.State state) {
            //outRect.left = space;
            outRect.right = space;
            outRect.bottom = space;

            // Add top margin only for the first item to avoid double space between items
            /*if (parent.getChildLayoutPosition(view) == 0) {
                outRect.top = space;
            } else {
                outRect.top = 0;
            }*/

            /*if (parent.getChildLayoutPosition(view) % 3 == 0) {
                outRect.right = 0;
            }*/
            if (parent.getChildLayoutPosition(view) % 3 == 2) {
                outRect.right = 0;
            }
        }
    }

    class QueryData extends AsyncTask<Void,List<FamilyMenu>,List<FamilyMenu>>{
        @Override
        protected List<FamilyMenu> doInBackground(Void... voids) {
            Log.d("======>","doInBackground");
            FamilyMenuDbHelper dbHelper = FamilyMenuDbHelper.getInstance(MainActivity.this);
            return dbHelper.queryData();
        }

        @Override
        protected void onPostExecute(List<FamilyMenu> familyMenus) {
            super.onPostExecute(familyMenus);
            Log.d("======>","onPostExecute");
            swipeToRefresh.setRefreshing(false);
            if (familyMenus.size() > 0) {
                familyMenuList.clear();
                familyMenuList.addAll(familyMenus);
            }
            adapter.notifyDataSetChanged();
        }
    }


    @Override
    public void onRefresh() {
        Log.d("======>","onRefresh");
        queryData = new QueryData();
        queryData.execute();
    }
}
