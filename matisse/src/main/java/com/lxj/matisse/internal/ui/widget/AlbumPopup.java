package com.lxj.matisse.internal.ui.widget;

import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.lxj.matisse.R;
import com.lxj.matisse.internal.utils.UIUtils;
import com.lxj.matisse.ui.MatisseActivity;

/**
 * Description:
 * Create by lxj, at 2019/3/18
 */
public class AlbumPopup  {
    ListView listView;
    FrameLayout popupContainer;
    CursorAdapter adapter;
    AdapterView.OnItemClickListener itemClickListener;
    public AlbumPopup(final MatisseActivity activity, CursorAdapter adapter, AdapterView.OnItemClickListener itemClickListener) {
        this.adapter = adapter;
        this.itemClickListener = itemClickListener;
        popupContainer = activity.findViewById(R.id.popupContainer);
        listView = activity.findViewById(R.id.listView);
        listView.setAdapter(this.adapter);
        listView.setOnItemClickListener(itemClickListener);
        popupContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        popupContainer.post(new Runnable() {
            @Override
            public void run() {
                popupContainer.setTranslationY(-popupContainer.getMeasuredHeight());
                ViewGroup.LayoutParams params = listView.getLayoutParams();
                params.height = Math.max(params.height, UIUtils.dp2px(activity, 360));
                listView.setLayoutParams(params);
            }
        });
    }

    public void show(){
        popupContainer.animate().translationY(0).setDuration(300).setInterpolator(new FastOutSlowInInterpolator()).start();
    }

    public void dismiss(){
        popupContainer.animate().translationY(-popupContainer.getMeasuredHeight()).setDuration(200).start();
    }

}
