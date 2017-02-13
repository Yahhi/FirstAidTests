package ru.na_uglu.firstaidtests;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by User on 10.02.2017.
 */

public class firstAidTestsAdapter extends BaseAdapter {
    private final Activity context;
    private firstAidTest[] testsToShow;

    public firstAidTestsAdapter(Activity context, firstAidTest[] tests) {
        this.context = context;
        testsToShow = tests;
    }

    @Override
    public int getCount() {
        return testsToShow.length;
    }

    @Override
    public Object getItem(int position) {
        firstAidTest aidTest = testsToShow[position];
        return aidTest;
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
            int[] imageIds = new int[] {R.id.star1, R.id.star2, R.id.star3, R.id.star4, R.id.star5};
            viewHolder = new ViewHolder(convertView, imageIds, R.id.testName, R.id.testDescription);
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
        if ((testsToShow[position].starsForUser <=5) && (testsToShow[position].starsForUser >= 0)) {
            viewHolder.setStarsRate(testsToShow[position].starsForUser);
        }

        return convertView;
    }
    public class ViewHolder {
        private static final int UNDEFINED_COUNT = -1;
        private final ImageView[] imgs = new ImageView[5];
        public final TextView txtTitle;
        public final TextView txtDescription;
        private int starsCount = UNDEFINED_COUNT;

        public ViewHolder(View view, int[] imageIds, int txtTitleId, int txtDescId) {
            int i = 0;
            for (int imgId : imageIds) {
                imgs[i] = (ImageView) view.findViewById(imgId);
                imgs[i].setVisibility(View.INVISIBLE);
                i++;
            }
            txtTitle = (TextView) view.findViewById(txtTitleId);
            txtDescription = (TextView) view.findViewById(txtDescId);
        }

        public void setStarsRate(int starsCount) {
            this.starsCount = starsCount;
            for (int i = 4; i >= 0; i--) {
                setOneStar(i);
            }
        }

        private void setOneStar(int currentStar) {
            imgs[currentStar].setVisibility(View.VISIBLE);
            if (starsCount > currentStar) {
                imgs[currentStar].setImageResource(android.R.drawable.star_on);
            } else {
                imgs[currentStar].setImageResource(android.R.drawable.star_off);
            }
        }
    }
}
