package com.example.demo01.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demo01.R;
import com.moxun.tagcloudlib.view.TagsAdapter;

import java.util.List;

public class TagCloudAdapter extends TagsAdapter {
    private List<String> list;

    public TagCloudAdapter(List<String> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public View getView(final Context context, final int position, ViewGroup parent) {
        View inflate = View.inflate(context, R.layout.mm, null);
        TextView viewById = inflate.findViewById(R.id.text);
        viewById.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, ""+list.get(position), Toast.LENGTH_SHORT).show();
            }
        });
        viewById.setText(getItem(position)+"");
        return inflate;
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getPopularity(int position) {
        return 1;
    }

    @Override
    public void onThemeColorChanged(View view, int themeColor) {

    }
}
