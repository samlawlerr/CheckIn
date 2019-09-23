package android.bignerdbranch.com;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;


import java.util.List;

public class CheckInListFragment extends Fragment {
    private static final String SAVED_SUBTITLE_VISIBLE = "subtitle";
    private RecyclerView mCheckInRecyclerView;
    private CrimeAdapter mAdapter;
    private boolean mSubtitleVisible;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_checkin_list, container, false);
        mCheckInRecyclerView = (RecyclerView) view
                .findViewById(R.id.checkin_recycler_view);
        mCheckInRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        if (savedInstanceState != null) {
            mSubtitleVisible = savedInstanceState.getBoolean(SAVED_SUBTITLE_VISIBLE);
        }

        updateUI();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();



        updateUI();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVED_SUBTITLE_VISIBLE, mSubtitleVisible);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_checkin_list, menu);

        MenuItem subtitleItem = menu.findItem(R.id.show_subtitle);
        if (mSubtitleVisible) {
            subtitleItem.setTitle(R.string.hide_subtitle);
        } else {
            subtitleItem.setTitle(R.string.show_subtitle);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_checkin:
                CheckIn check = new CheckIn();
                CheckInLab.get(getActivity()).addCheckIn(check);
                Intent intent = CheckInPagerActivity
                        .newIntent(getActivity(), check.getId());
                startActivity(intent);
                return true;
            case R.id.show_subtitle:
                mSubtitleVisible = !mSubtitleVisible;
                getActivity().invalidateOptionsMenu();
                updateSubtitle();
                return true;
            case R.id.help:
                intent = HelpWebPage.newIntent(getActivity(), Uri.parse("https://www.google.com"));
                startActivity(intent);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateSubtitle() {
        CheckInLab checkLab = CheckInLab.get(getActivity());
        int checkCount = checkLab.getCheckIn().size();
        String subtitle = getString(R.string.subtitle_format, checkCount);
        if (!mSubtitleVisible) {
            subtitle = null;
        }
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setSubtitle(subtitle);
    }

    private void updateUI() {
        CheckInLab checkLab = CheckInLab.get(getActivity());
        List<CheckIn> checks = checkLab.getCheckIn();
        if (mAdapter == null) {
            mAdapter = new CrimeAdapter(checks);
            mCheckInRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setChecks(checks);
            mAdapter.notifyDataSetChanged();
        }
        updateSubtitle();
    }

        private class CrimeHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        private TextView mTitleTextView;
        private TextView mDateTextView;
        private CheckIn mCheckIn;


        public CrimeHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_checkin, parent, false));
            itemView.setOnClickListener(this);
            mTitleTextView = (TextView) itemView.findViewById(R.id.check_title);
            mDateTextView = (TextView) itemView.findViewById(R.id.check_date);

        }

        public void bind(CheckIn check) {
            mCheckIn = check;
            mTitleTextView.setText(mCheckIn.getTitle());
            mDateTextView.setText(mCheckIn.getDate().toString());
        }

        @Override
        public void onClick(View view) {
            Intent intent = CheckInPagerActivity.newIntent(getActivity(), mCheckIn.getId());
            startActivity(intent);

        }
    }

    private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder> {
        private List<CheckIn> mCheckIns;
        public CrimeAdapter(List<CheckIn> checks) {
            mCheckIns = checks;
        }

        @Override
        public CrimeHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new CrimeHolder(layoutInflater, parent);


        }
        @Override
        public void onBindViewHolder(CrimeHolder holder, int position) {
            CheckIn check = mCheckIns.get(position);
            holder.bind(check);

        }

        @Override
        public int getItemCount() {
            return mCheckIns.size();
        }

        public void setChecks(List<CheckIn> checks) {
            mCheckIns = checks;
        }

    }
}
