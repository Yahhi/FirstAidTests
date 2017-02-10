package ru.na_uglu.firstaidtests;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 10.02.2017.
 */

public class firstAidTestsAdapter extends BaseAdapter {
    private final Activity context;
    private firstAidTests[] testsToShow;

    public firstAidTestsAdapter(Activity context, firstAidTests[] tests) {
        this.context = context;
        testsToShow = tests;
    }

    @Override
    public int getCount() {
        return testsToShow.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;

        if (convertView == null) {
            /**
             * At very first time when the List View row Item control's
             * instance is created it will be store in the convertView as a
             * ViewHolder Class object for the reusability purpose
             **/
            convertView = LayoutInflater.from(context).inflate(R.layout.list_of_tests_item, null);
            viewHolder = new ViewHolder(convertView, R.id.star1, R.id.testName, R.id.testDescription);
            convertView.setTag(viewHolder);
        } else {
            /**
             * Once the instance of the row item's control it will use from
             * already created controls which are stored in convertView as a
             * ViewHolder Instance
             * */
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.txtTitle.setText(testsToShow[position].name);
        viewHolder.txtDescription.setText(testsToShow[position].description);
        //viewHolder.img.setImageResource();

        return convertView;
    }
    public class ViewHolder {
        public final ImageView img;
        public final TextView txtTitle;
        public final TextView txtDescription;

        public ViewHolder(View view, int imgId, int txtTitleId, int txtDescId) {
            img = (ImageView) view.findViewById(imgId);
            txtTitle = (TextView) view.findViewById(txtTitleId);
            txtDescription = (TextView) view.findViewById(txtDescId);
        }
    }
}
