package com.nhpm.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.customComponent.utility.ProjectPrefrence;
import com.nhpm.Models.response.verifier.VerifierLocationItem;
import com.nhpm.Models.response.verifier.VerifierLoginResponse;
import com.nhpm.R;
import com.nhpm.Utility.AppConstant;
import com.nhpm.activity.BlockDetailActivity;
import com.nhpm.activity.DownloadedListActvity;
import com.nhpm.activity.SearchActivityWithHouseHold;

import java.util.ArrayList;


public class WithoutSeccDataOfflineFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private VerifierLoginResponse verifierDetail;
    private Button procedWithRsby;
    private Context context;
    private DownloadedListActvity activity;
    private RecyclerView recycleView;
    private CustomAdapter adapter;


    public WithoutSeccDataOfflineFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WithoutSeccDataOfflineFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WithoutSeccDataOfflineFragment newInstance(String param1, String param2) {
        WithoutSeccDataOfflineFragment fragment = new WithoutSeccDataOfflineFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_without_secc_data_downloading, container, false);
        context = getActivity();
        setupScreen(view);

        return view;
    }

    public void setupScreen(View view) {
        recycleView = (RecyclerView) view.findViewById(R.id.recycleView);
        recycleView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
        recycleView.setLayoutManager(mLayoutManager);
        verifierDetail = VerifierLoginResponse.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF,
                AppConstant.VERIFIER_CONTENT, context));
        if (verifierDetail != null && verifierDetail.getLocationList() != null) {
            adapter = new CustomAdapter(verifierDetail.getLocationList());
            recycleView.setAdapter(adapter);
        }
    }


    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);

        activity = (DownloadedListActvity) context;

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }


    private class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {
        private ArrayList<VerifierLocationItem> mDataset;

        // Provide a suitable constructor (depends on the kind of dataset)
        public CustomAdapter(ArrayList<VerifierLocationItem> myDataset) {
            mDataset = myDataset;
        }

        public void add(int position, VerifierLocationItem item) {
            mDataset.add(position, item);
            notifyItemInserted(position);
        }

        public void remove(String item) {
            int position = mDataset.indexOf(item);
            mDataset.remove(position);
            notifyItemRemoved(position);
        }

        public void updateData(ArrayList<VerifierLocationItem> itemList) {
            mDataset.clear();
            mDataset.addAll(itemList);
            notifyDataSetChanged();
        }

        // Create new1 views (invoked by the layout manager)
        @Override
        public CustomAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {
            // create a new1 view
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.location_list, parent, false);
            // set the view's size, margins, paddings and layout parameters
            CustomAdapter.ViewHolder vh = new CustomAdapter.ViewHolder(v);
            return vh;
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(CustomAdapter.ViewHolder holder, int position) {
            final VerifierLocationItem item = mDataset.get(position);
            //  holder.blockCodeTV.setText(loc.getBlockCode());
            if (item != null) {

                if (item.getStateName() != null) {
                    holder.stateNameTV.setText(item.getStateName());
                }
                if (item.getDistrictName() != null) {
                    holder.distTV.setText(item.getDistrictName());
                }
                if (item.getTehsilName() != null) {
                    holder.tehsilTV.setText(item.getTehsilName());
                }
                if (item.getVtName() != null) {
                    holder.vtNameTV.setText(item.getVtName());
                }
                if (item.getWardCode() != null) {
                    holder.wardTV.setText(item.getWardCode());
                }
                if (item.getBlockCode() != null) {
                    holder.ebTV.setText(item.getBlockCode());
                }
            }
            holder.proceedBT.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,
                            AppConstant.SELECTED_BLOCK, item.serialize(), context);
                    //  ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.homeNavigation, AppConstant.downloadActivityNavigation, context);
                    ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.dataDownloaded, "N", context);
                    Intent theIntent = new Intent(context, SearchActivityWithHouseHold.class);
                    startActivity(theIntent);
                    activity.leftTransition();
                }
            });
        }

        @Override
        public int getItemCount() {
            return mDataset.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView stateNameTV, distTV, tehsilTV, vtNameTV, wardTV, ebTV;
            LinearLayout ebLinearLayout, wardLinearLayout;
            Button proceedBT;

            public ViewHolder(View alertView) {
                super(alertView);
                proceedBT = (Button) alertView.findViewById(R.id.proceedBT);
                stateNameTV = (TextView) alertView.findViewById(R.id.stateNameTV);
                distTV = (TextView) alertView.findViewById(R.id.distNameTV);
                tehsilTV = (TextView) alertView.findViewById(R.id.tehsilNameTV);
                vtNameTV = (TextView) alertView.findViewById(R.id.vtNameTV);
                wardTV = (TextView) alertView.findViewById(R.id.wardCodeTV);
                ebTV = (TextView) alertView.findViewById(R.id.ebTV);
                ebLinearLayout = (LinearLayout) alertView.findViewById(R.id.ebLinearLayout);
                wardLinearLayout = (LinearLayout) alertView.findViewById(R.id.wardLinearLayout);
            }
        }
    }
}