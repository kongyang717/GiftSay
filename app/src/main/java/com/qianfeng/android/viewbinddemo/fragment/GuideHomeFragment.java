package com.qianfeng.android.viewbinddemo.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.qianfeng.android.viewbinddemo.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 指南页面主Fragment
 */
public class GuideHomeFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    @BindView(R.id.elv_guide_home)
    ExpandableListView mExpandableListView;
    private OnFragmentInteractionListener mListener;
    private MyAdapter mAdapter;
    private List<String> mListGroupIndex;
    private HashMap<String, List<String>> mHashMap;
    private Context mContext;

    public GuideHomeFragment() {
        // Required empty public constructor
    }

    /**
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GuideHomeFag.
     */
    // TODO: Rename and change types and number of parameters
    public static GuideHomeFragment newInstance(String param1, String param2) {
        GuideHomeFragment fragment = new GuideHomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        mListGroupIndex = new ArrayList<>();
        mHashMap = new HashMap<>();
        mAdapter = new MyAdapter();
        initData();
    }

    private void initData() {
        for (int i = 0; i < 10; i++) {
            List<String> list = new ArrayList<>();
            mHashMap.put(i + "", list);
            mListGroupIndex.add(i + "");
            for (int j = 0; j < 5; j++) {
                list.add(i + j + "");
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_guide_home, container, false);
        ButterKnife.bind(this, view);
        mExpandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return true;
            }
        });
        return view;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mExpandableListView.setAdapter(mAdapter);
        for(int i=0;i<mListGroupIndex.size();i++){
            mExpandableListView.expandGroup(i);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     *
     */
    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(Uri uri);
    }

    class MyAdapter extends BaseExpandableListAdapter {

        @Override
        public int getGroupCount() {
            return mListGroupIndex == null ? 0 : mListGroupIndex.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            if (mHashMap == null) {
                return 0;
            }
            List<String> list = mHashMap.get(mListGroupIndex.get(groupPosition));
            return list.size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return mListGroupIndex.get(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            if (mHashMap == null) {
                return 0;
            }
            List<String> list = mHashMap.get(mListGroupIndex.get(groupPosition));
            return list.get(childPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return 0;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return 0;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        class GroupViewHolder {
            @BindView(R.id.tv_guide_home_time)
            TextView tvTime;

            GroupViewHolder(View view) {
                view.setTag(this);
                ButterKnife.bind(this, view);
            }
        }
        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            GroupViewHolder childViewHolder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.elv_item_time, null);
                childViewHolder = new GroupViewHolder(convertView);
            }else{
                childViewHolder= (GroupViewHolder) convertView.getTag();
            }
            childViewHolder.tvTime.setText((String) getGroup(groupPosition));

            return convertView;
        }

        class ChildViewHolder {
            @BindView(R.id.tv_guide_home_time)
            TextView tvTime;

            ChildViewHolder(View view) {
                view.setTag(this);
                ButterKnife.bind(this, view);
            }
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            ChildViewHolder childViewHolder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.elv_item_time, null);
                childViewHolder = new ChildViewHolder(convertView);
            }else{
                childViewHolder= (ChildViewHolder) convertView.getTag();
            }
            childViewHolder.tvTime.setText((String) getChild(groupPosition,childPosition));

            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return false;
        }
    }
}
