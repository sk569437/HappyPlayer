package com.happy.model.app;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Administrator
 * 
 */
public class Category  implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 分类名
	 */
	private String mCategoryName;
	/**
	 * 分类的内容
	 */
	private List<SongInfo> mCategoryItem = new ArrayList<SongInfo>();

	public Category(String mCategroyName) {
		mCategoryName = mCategroyName;
	}

	public String getmCategoryName() {
		return mCategoryName;
	}

	public void addItem(SongInfo songInfo) {
		mCategoryItem.add(songInfo);
	}

	public List<SongInfo> getCategoryItem() {
		return mCategoryItem;
	}

	/**
	 * 根据索引获取子内容
	 * 
	 * @param pPosition
	 * @return
	 */
	public Object getItem(int pPosition) {
		if (pPosition < 0)
			return null;
		if (pPosition == 0) {
			return getmCategoryName();
		} else {
			if (mCategoryItem.size() == 0) {
				return null;
			}
			return mCategoryItem.get(pPosition - 1);
		}
	}

	/**
	 * 当前类别Item总数。Category也需要占用一个Item
	 * 
	 * @return
	 */
	public int getItemCount() {
		return mCategoryItem.size() + 1;
	}

	public int getmCategoryItemCount() {
		return mCategoryItem.size();
	}

	public List<SongInfo> getmCategoryItem() {
		return mCategoryItem;
	}

	public void setmCategoryItem(List<SongInfo> mCategoryItem) {
		this.mCategoryItem = mCategoryItem;
	}
}