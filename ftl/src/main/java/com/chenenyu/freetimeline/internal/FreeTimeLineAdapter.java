package com.chenenyu.freetimeline.internal;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chenenyu.freetimeline.FreeTimeLineConfig;
import com.chenenyu.freetimeline.FreeTimeLineElement;
import com.chenenyu.freetimeline.R;

import java.util.Collections;
import java.util.List;

/**
 * <p>
 * <a href="https://github.com/chenenyu/FreeTimeLine">FreeTimeLineAdapter</a>
 * </p>
 * Created by Cheney on 16/1/6.
 */
public class FreeTimeLineAdapter extends BaseAdapter {

    private boolean[] opened;
    private final boolean SHOW_LEFT;
    private final boolean SHOW_TOGGLE;
    private List<FreeTimeLineElement> mElements = Collections.emptyList();
    private FreeTimeLineConfig mConfig;

    public FreeTimeLineAdapter(List<FreeTimeLineElement> elements, FreeTimeLineConfig config) {
        mElements = elements;
        mConfig = config;
        opened = new boolean[mElements.size()];
        SHOW_LEFT = showLeft();
        SHOW_TOGGLE = mConfig.isShowToggle();
    }

    @Override
    public int getCount() {
        return mElements.size();
    }

    @Override
    public Object getItem(int position) {
        return mElements.size() == 0 ? null : mElements.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Context context = parent.getContext();
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.__ftl_node_row, parent, false);
        }
        TextView left = (TextView) convertView.findViewById(R.id.__ftl_row_left_text);
        left.setTextColor(mConfig.getLeftColor());
        left.setTextSize(mConfig.getLeftSize());
        ConnectorView connectorView = (ConnectorView) convertView.findViewById(R.id.__ftl_row_connector);
        connectorView.setLineColor(mConfig.getLineColor());
        connectorView.setSolidColor(mConfig.getSolidColor());
        connectorView.setSuckerColor(mConfig.getSuckerColor());
        connectorView.setHollowColor(mConfig.getHollowColor());
        TextView middle_parent = (TextView) convertView.findViewById(R.id.__ftl_row_middle_parent_text);
        middle_parent.setTextColor(mConfig.getParentColor());
        middle_parent.setTextSize(mConfig.getParentSize());
        TextView middle_child = (TextView) convertView.findViewById(R.id.__ftl_row_middle_child_text);
        middle_child.setTextColor(mConfig.getChildColor());
        middle_child.setTextSize(mConfig.getChildSize());
        ToggleView toggleView = (ToggleView) convertView.findViewById(R.id.__ftl_row_toggle);
        toggleView.setToggleColor(mConfig.getToggleColor());

        if (SHOW_LEFT) {
            left.setVisibility(View.VISIBLE);
            left.setText(mElements.get(position).getLeft());
        }

        if (position == 0) {
            connectorView.setType(mConfig.getTopType());
        } else if (position == mElements.size() - 1) {
            connectorView.setType(mConfig.getBottomType());
        } else {
            connectorView.setType(mConfig.getNodeType());
        }

        middle_parent.setText(mElements.get(position).getParent());
        if (SHOW_TOGGLE) {
            toggleView.setVisibility(View.VISIBLE);
            toggleView.setOpened(opened[position]);
            if (opened[position]) {
                middle_child.setVisibility(View.VISIBLE);
                middle_child.setText(mElements.get(position).getChild());
            } else {
                middle_child.setVisibility(View.GONE);
            }
        } else {
            toggleView.setVisibility(View.GONE);
        }

        return convertView;
    }

    private boolean showLeft() {
        for (FreeTimeLineElement element : mElements) {
            if (!TextUtils.isEmpty(element.getLeft()))
                return true;
        }
        return false;
    }

    /**
     * Expand or collapse row.
     *
     * @param position clicked item.
     */
    public void toggleRow(int position) {
        this.opened[position] = !this.opened[position];
        notifyDataSetChanged();
    }

}
