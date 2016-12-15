package com.csf.csflibrary.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.csf.csflibrary.R;
import com.csf.csflibrary.Tools.Tools;
import com.csf.csflibrary.javaBean.StockRealtimeBean;

import org.apache.http.util.LangUtils;

import java.util.List;

/**
 * 五档适配器
 */
public class ListStockRealAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<StockRealtimeBean> listdata;
    private boolean isLandscape;
    private Context context;

    public ListStockRealAdapter(Context context, List<StockRealtimeBean> listdata, boolean isLandscape) {
        mInflater = LayoutInflater.from(context);
        this.listdata = listdata;
        this.isLandscape = isLandscape;
        this.context = context;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return listdata.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return listdata.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        ViewHolder holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.stockrealtimeitem, null);
            holder = new ViewHolder();
            holder.tv_itemtitle = (TextView) convertView.findViewById(R.id.tv_itemtitle);
            holder.tv_itmeprice = (TextView) convertView.findViewById(R.id.tv_itmeprice);
            holder.tv_itmecount = (TextView) convertView.findViewById(R.id.tv_itmecount);
            holder.layout_realtimeitem = (LinearLayout) convertView.findViewById(R.id.layout_realtimeitem);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        try {
            if (isLandscape) {
                holder.layout_realtimeitem.setPadding(0, 0, 0, Tools.dip2px(context, 10));
            } else {
            }

            final StockRealtimeBean hotNews = (StockRealtimeBean) getItem(position);
            holder.tv_itemtitle.setText(hotNews.getTitle());
            holder.tv_itmeprice.setText(hotNews.getPrice());
            holder.tv_itmecount.setText(hotNews.getCount());

        } catch (Exception e) {
            // TODO: handle exception
        }

        return convertView;

    }

    public class ViewHolder {
        TextView tv_itemtitle;
        TextView tv_itmeprice;
        TextView tv_itmecount;
        LinearLayout layout_realtimeitem;
    }
}
