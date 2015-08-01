package com.tx.hsp.pingbobo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tx.hsp.pingbobo.R;

/**
 * Created by hsp on 15/7/28.
 */
public class AdapterDrawerListView extends BaseAdapter {

    private int[] drawablelist = {R.drawable.ic_home_black_24dp,R.drawable.ic_email_black_24dp,
    R.drawable.ic_chat_black_24dp,R.drawable.ic_grade_black_24dp
    ,R.drawable.ic_account_box_black_24dp,R.drawable.ic_supervisor_account_black_24dp,
    R.drawable.ic_settings_black_24dp};
    private int[] str = {R.string.drawer_home,R.string.drawer_contact,R.string.drawer_commit,
    R.string.drawer_favorite,R.string.drawer_me,R.string.drawer_signout,R.string.drawer_set};
    private LayoutInflater layoutInflater;

    public AdapterDrawerListView(Context context){
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return str.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView==null){
                convertView = layoutInflater.inflate(R.layout.fragment_navigation_drawer_adapter,null);
                holder = new ViewHolder();
                holder.imageView = (ImageView)convertView.findViewById(R.id.drawer_item_image);
                holder.textView = (TextView)convertView.findViewById(R.id.drawer_item_text);
                convertView.setTag(holder);

        }else{
                holder = (ViewHolder)convertView.getTag();
        }
            holder.textView.setText(str[position]);
            holder.imageView.setBackgroundResource(drawablelist[position]);
        return convertView;
    }

    public class ViewHolder{
        ImageView imageView;
        TextView textView;
    }
}
