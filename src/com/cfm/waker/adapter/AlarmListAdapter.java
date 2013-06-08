package com.cfm.waker.adapter;

import java.util.List;

import com.cfm.waker.R;

import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

public class AlarmListAdapter extends PagerAdapter{
	
	private List<View> viewList;
	
	public AlarmListAdapter(List<View> viewList){
		this.viewList = viewList;
	}
	
	@Override
	public void destroyItem(ViewGroup viewPager, int position, Object object){
		((ViewPager) viewPager).removeView(viewList.get(position));
	}
	
	@Override
	public void finishUpdate(View view){
		
	}
	
	@Override
	public Object instantiateItem(View view, int position){
		((ViewPager) view).addView(viewList.get(position), 0);
		return viewList.get(position);
	}

	@Override
	public int getCount() {
		return viewList.size();
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		// TODO Auto-generated method stub
		return view == object;
	}
	
	@Override
	public void restoreState(Parcelable parcelable, ClassLoader classLoader){
		
	}
	
	@Override
	public Parcelable saveState(){
		return null;
	}
	
	@Override
	public void startUpdate(View view){
		
	}

}
