package com.gank.todaynews.view;

import android.os.Bundle;
import android.util.ArrayMap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gank.base.BaseTodayNewsListFramgent;
import com.gank.data.Story;
import com.gank.data.TodayNews;
import com.gank.todaynews.adapter.TodayItemAdapter;
import com.gank.todaynews.model.TodayModel;
import com.gank.todaynews.model.TodayModelImp;
import com.gank.todaynews.presenter.TodayPresenter;
import com.gank.todaynews.presenter.TodayPresenterImp;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * Created by thinkpad on 2016/4/29.
 */
public class TodayFragment extends BaseTodayNewsListFramgent implements TodayView, TodayItemAdapter.onImgCollectClickListener {
    private TodayPresenter dayGankPresenter;
    private TodayModel dayGankModel;
    private Realm realm;
    private ArrayMap<Long, Boolean> mMap = new ArrayMap<>();
    protected RealmResults<Story> results;

    public TodayFragment() {
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    protected void init() {
        dayGankModel = new TodayModelImp(getContext());
        dayGankPresenter = new TodayPresenterImp(dayGankModel, this);
        realm = Realm.getDefaultInstance();
        RealmQuery<Story> query = realm.where(Story.class);
        results = query.findAll();
        if (results != null) {
            for (int i = 0; i < results.size(); i++) {
                mMap.put(results.get(i).getmId(), true);

            }
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        mMap.clear();
        RealmQuery<Story> query = realm.where(Story.class);
        results = query.findAll();
        if (results != null) {
            for (int i = 0; i < results.size(); i++) {
                mMap.put(results.get(i).getmId(), true);
            }
        }
        todayItemAdapter.setmMap(mMap);

    }

    @Override
    protected void loadData() {
        dayGankPresenter.loadData();

    }


    @Override
    public void setupDayGankDataToView(TodayNews dayGankData) {
        adapter.setmList(dayGankData.getmStories());

    }

    @Override
    public void showProgress() {
        refreshView.setRefreshing(true);
    }

    @Override
    public void hideProgress() {
        refreshView.setRefreshing(false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (realm != null)
            realm.close();
    }

    @Override
    protected void setImgCollectListener(TodayItemAdapter adapter) {
        adapter.setListener(this);

    }

    @Override
    public void onImgClick(Story dayGankData, boolean isChecked) {
        if(isChecked) {
            if (realm != null) {
                realm.beginTransaction();
                realm.copyToRealm(dayGankData);
                realm.commitTransaction();

            }

        }else {
            realm.beginTransaction();
            RealmResults<Story> results = realm.where(Story.class).findAll();
            for (int i = 0; i < results.size(); i++) {
                if (results.get(i).getmId() == dayGankData.getmId()) {
                    results.remove(i);

                }
            }
            realm.commitTransaction();

        }

    }
}
