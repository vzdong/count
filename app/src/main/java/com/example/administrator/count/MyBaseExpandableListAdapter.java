package com.example.administrator.count;

import android.content.ClipData;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.security.acl.Group;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by new on 2017/9/30.
 */

public class MyBaseExpandableListAdapter extends BaseExpandableListAdapter {

   // private ArrayList<Group> gData;
    private ArrayList<Map<String,String>> gData;
    //private ArrayList<ArrayList<ClipData.Item>> iData;
    private ArrayList<ArrayList<Map<String,String>>> iData;
    private Context mContext;

    public MyBaseExpandableListAdapter(ArrayList<Map<String,String>> gData, ArrayList<ArrayList<Map<String,String>>> iData, Context mContext) {
        this.gData = gData;
        this.iData = iData;
        this.mContext = mContext;
    }

    @Override
    public int getGroupCount() {
        return gData.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return iData.get(groupPosition).size();
    }

    @Override
    public Map<String,String> getGroup(int groupPosition) {
        return gData.get(groupPosition);
    }

    @Override
    public Map<String,String> getChild(int groupPosition, int childPosition) {
        return iData.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    //取得用于显示给定分组的视图. 这个方法仅返回分组的视图对象
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        ViewHolderGroup groupHolder;
        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.listview_parent, parent, false);
            //绑定控件与数据
            groupHolder = new ViewHolderGroup();
            groupHolder.group_tv_code = (TextView) convertView.findViewById(R.id.t1);
            groupHolder.group_tv_name = (TextView) convertView.findViewById(R.id.t2);
            convertView.setTag(groupHolder);
        }else{
            groupHolder = (ViewHolderGroup) convertView.getTag();
        }
        groupHolder.group_tv_code.setText(gData.get(groupPosition).get("code"));
        groupHolder.group_tv_name.setText(gData.get(groupPosition).get("name"));
        return convertView;
    }

    //取得显示给定分组给定子位置的数据用的视图
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ViewHolderItem itemHolder;
        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.listview, parent, false);
            //绑定控件与数据

            itemHolder = new ViewHolderItem();
            itemHolder.tv_code= (TextView) convertView.findViewById(R.id.t1);
            itemHolder.tv_flag = (TextView) convertView.findViewById(R.id.t2);
            itemHolder.tv_price= (TextView) convertView.findViewById(R.id.t3);
            itemHolder.tv_number = (TextView) convertView.findViewById(R.id.t4);
            itemHolder.tv_time = (TextView) convertView.findViewById(R.id.t5);
            itemHolder.tv_dealnumber = (TextView) convertView.findViewById(R.id.t0);
            convertView.setTag(itemHolder);
        }else{
            itemHolder = (ViewHolderItem) convertView.getTag();
        }

        itemHolder.tv_code.setText(iData.get(groupPosition).get(childPosition).get("code"));
        itemHolder.tv_flag.setText(iData.get(groupPosition).get(childPosition).get("flag"));
        itemHolder.tv_price.setText(iData.get(groupPosition).get(childPosition).get("price"));
        itemHolder.tv_number.setText(iData.get(groupPosition).get(childPosition).get("number"));
        itemHolder.tv_time.setText(iData.get(groupPosition).get(childPosition).get("time"));
        itemHolder.tv_dealnumber.setText(iData.get(groupPosition).get(childPosition).get("dealnumber"));
        if(itemHolder.tv_flag.getText().toString().equals("卖出")){
            itemHolder.tv_flag.setTextColor(Color.rgb(70,130,180));
        }else{
            itemHolder.tv_flag.setTextColor(Color.rgb(139, 139,122));
        }
        return convertView;
    }

    //设置子列表是否可选中
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


    private static class ViewHolderGroup{
        private TextView group_tv_code;
        private TextView group_tv_name;
    }

    private static class ViewHolderItem{

        private TextView tv_code;
        private TextView tv_flag;
        private TextView tv_price;
        private TextView tv_number;
        private TextView tv_time;
        private TextView tv_dealnumber;

    }

}