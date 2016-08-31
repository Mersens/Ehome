package com.zzu.ehome.view;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.zzu.ehome.R;

/**
 * �������Ҫ����ˢ��ֱ����canPullDown�з���false��������Զ����غ�����ˢ��û�г�ͻ��ͨ��������β����footerviewʵ���Զ����أ�
 * ������ʹ���в�Ҫ�ٶ�footerview��
 * ������������http://blog.csdn.net/zhongkejingwang/article/details/38963177
 * @author chenjing
 * 
 */
public class PullableListView extends ListView implements Pullable
{
	public static final int INIT = 0;
	public static final int LOADING = 1;
	public static final int NO_MORE_DATA = 2;
	private OnLoadListener mOnLoadListener;
	private View mLoadmoreView;
	private ImageView mLoadingView;
	private TextView mStateTextView;
	private int state = INIT;
	private boolean canLoad = true;
	private boolean autoLoad = true;
	private boolean hasMoreData = true;
	private AnimationDrawable mLoadAnim;

	public PullableListView(Context context)
	{
		super(context);
		init(context);
	}

	public PullableListView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		init(context);
	}

	public PullableListView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		init(context);
	}

	private void init(Context context)
	{
		mLoadmoreView = LayoutInflater.from(context).inflate(R.layout.load_more,
				null);
		mLoadingView = (ImageView) mLoadmoreView.findViewById(R.id.loading_icon);
		mLoadingView.setBackgroundResource(R.drawable.loading_anim);
		mLoadAnim = (AnimationDrawable) mLoadingView.getBackground();
		mStateTextView = (TextView) mLoadmoreView.findViewById(R.id.loadstate_tv);
		mLoadmoreView.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				//�������
				if(state != LOADING && hasMoreData){
					load();
				}
			}
		});
		addFooterView(mLoadmoreView, null, false);
	}
	
	/**
	 * �Ƿ����Զ�����
	 * @param enable true���ã�false����
	 */
	public void enableAutoLoad(boolean enable){
		autoLoad = enable;
	}
	
	/**
	 * �Ƿ���ʾ���ظ���
	 * @param v true��ʾ��false����ʾ
	 */
	public void setLoadmoreVisible(boolean v){
		if(v)
			{
			if (getFooterViewsCount() == 0) {
				addFooterView(mLoadmoreView, null, false);
			}
			}
		else {
			removeFooterView(mLoadmoreView);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev)
	{
		switch (ev.getActionMasked())
		{
		case MotionEvent.ACTION_DOWN:
			// ���µ�ʱ���ֹ�Զ�����
			canLoad = false;
			break;
		case MotionEvent.ACTION_UP:
			// �ɿ����ж��Ƿ��Զ�����
			canLoad = true;
			checkLoad();
			break;
		}
		return super.onTouchEvent(ev);
	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt)
	{
		super.onScrollChanged(l, t, oldl, oldt);
		// �ڹ������ж��Ƿ������Զ���������
		checkLoad();
	}

	/**
	 * �ж��Ƿ������Զ���������
	 */
	private void checkLoad()
	{
		if (reachBottom() && mOnLoadListener != null && state != LOADING
				&& canLoad && autoLoad && hasMoreData)
			load();
	}
	
	private void load(){
		changeState(LOADING);
		mOnLoadListener.onLoad(this);
	}

	private void changeState(int state)
	{
		this.state = state;
		switch (state)
		{
		case INIT:
			mLoadAnim.stop();
			mLoadingView.setVisibility(View.INVISIBLE);
			mStateTextView.setText(R.string.more);
			break;

		case LOADING:
			mLoadingView.setVisibility(View.VISIBLE);
			mLoadAnim.start();
			mStateTextView.setText(R.string.loading);
			break;
			
		case NO_MORE_DATA:
			mLoadAnim.stop();
			mLoadingView.setVisibility(View.INVISIBLE);
			mStateTextView.setText("没有更多数据");
			break;
		}
	}

	/**
	 * ��ɼ���
	 */
	public void finishLoading()
	{
		changeState(INIT);
	}

	@Override
	public boolean canPullDown()
	{
		if (getCount() == 0)
		{
			// û��item��ʱ��Ҳ��������ˢ��
			return true;
		} else if (getFirstVisiblePosition() == 0
				&& getChildAt(0).getTop() >= 0)
		{
			// ����ListView�Ķ�����
			return true;
		} else
			return false;
	}

	public void setOnLoadListener(OnLoadListener listener)
	{
		this.mOnLoadListener = listener;
	}

	/**
	 * @return footerview�ɼ�ʱ����true�����򷵻�false
	 */
	private boolean reachBottom()
	{
		if (getCount() == 0)
		{
			return true;
		} else if (getLastVisiblePosition() == (getCount() - 1))
		{
			// �����ײ�����픲����ǵ�0����Ҳ����˵item������һ��������Զ����أ�����ֻ�ܵ������
			if (getChildAt(getLastVisiblePosition() - getFirstVisiblePosition()) != null
					&& getChildAt(
							getLastVisiblePosition()
									- getFirstVisiblePosition()).getTop() < getMeasuredHeight() && !canPullDown())
				return true;
		}
		return false;
	}

	public boolean isHasMoreData() {
		return hasMoreData;
	}

	public void setHasMoreData(boolean hasMoreData) {
		this.hasMoreData = hasMoreData;
		if (!hasMoreData) {
			changeState(NO_MORE_DATA);
		}else{
			changeState(INIT);
		}
	}

	public interface OnLoadListener
	{
		void onLoad(PullableListView pullableListView);
	}

	@Override
	public boolean canPullUp() {
		// TODO Auto-generated method stub
		return false;
	}
}
