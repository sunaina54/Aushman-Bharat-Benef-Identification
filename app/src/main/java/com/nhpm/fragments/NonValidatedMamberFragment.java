package com.nhpm.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.customComponent.utility.ProjectPrefrence;
import com.nhpm.Models.response.seccMembers.SeccMemberItem;
import com.nhpm.Models.response.seccMembers.SeccMemberResponse;
import com.nhpm.Models.response.verifier.VerifierLocationOLD;
import com.nhpm.R;
import com.nhpm.Utility.AppConstant;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NonValidatedMamberFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NonValidatedMamberFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView memberList;
    private Context context;

    public NonValidatedMamberFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NonValidatedMamberFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NonValidatedMamberFragment newInstance(String param1, String param2) {
        NonValidatedMamberFragment fragment = new NonValidatedMamberFragment();
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
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_non_validated_mamber, container, false);
        setupScreen(view);
        return view;
    }

    private void setupScreen(View view){
        context=getActivity();
        memberList=(RecyclerView)view.findViewById(R.id.memberList);
        memberList.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        memberList.setLayoutManager(layoutManager);
        memberList.setItemAnimator(new DefaultItemAnimator());
        ArrayList<SeccMemberItem> memberItemArrayList=getSeccMemberList();
        MemberAdapter adapter=new MemberAdapter(context,memberItemArrayList);
        memberList.setAdapter(adapter);
    }

    private ArrayList<SeccMemberItem> getSeccMemberList(){
        ArrayList<SeccMemberItem> memberList=new ArrayList<>();
        SeccMemberResponse response=SeccMemberResponse.
                create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF,
                        AppConstant.SECC_MEMBER_CONTENT, context));
        VerifierLocationOLD loc= VerifierLocationOLD.create(ProjectPrefrence.
                getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.SELECTED_BLOCK, context));
        String locStr=loc.getStateCode()+""+loc.getDistrictCode()+""+loc.getVillTownCode()+""+loc.getWardCode()+""+loc.getBlockCode();
        for(SeccMemberItem item1 : response.getSeccMemberList()){
            String seccStr=item1.getStatecode()+"" +
                    ""+item1.getDistrictcode()+""+item1.getTowncode()+""+item1.getWardid()+""+item1.getAhlblockno();
            Log.d("jkagkj", "LocationStr : " + seccStr);
            if(seccStr.equalsIgnoreCase(locStr)){
                memberList.add(item1);
            }
        }
        return memberList;
    }

    private class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.MyViewHolder> {
        private ArrayList<SeccMemberItem> dataSet;
        private Context context;
        private TextView text;
        public class MyViewHolder extends RecyclerView.ViewHolder   {
            TextView nameTV,genderTV,fatherNameTV,addressTV,houseNoTV;
            TextView aadhaarNoTV,govtIdTV;
            public MyViewHolder(View itemView) {
                super(itemView);
                this.houseNoTV = (TextView) itemView.findViewById(R.id.houseNoTV);
                this.nameTV = (TextView) itemView.findViewById(R.id.memberNameTV);
                aadhaarNoTV=(TextView)itemView.findViewById(R.id.aadhaarNoTV);
                govtIdTV=(TextView)itemView.findViewById(R.id.govtIdTV);

            }

        }
        public void addAll(List<SeccMemberItem> list) {

            dataSet.addAll(list);
            notifyDataSetChanged();
        }
        public MemberAdapter(Context context, ArrayList<SeccMemberItem> data) {

            this.dataSet =data;
            this.context=context;
            this.text=text;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_not_validated_member_layout, parent, false);

            //view.setOnClickListener(MainActivity.myOnClickListener);

            MyViewHolder myViewHolder = new MyViewHolder(view);
            return myViewHolder;
        }


        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

            holder.houseNoTV.setText(dataSet.get(listPosition).getAhlslnohhd());
            holder.nameTV.setText(dataSet.get(listPosition).getName());
           // holder.fatherNameTV.setText(dataSet.get(listPosition).getFathername());
        }

        @Override
        public int getItemCount() {
            return dataSet.size();
        }
        public void clearDataSource() {

            dataSet.clear();
            notifyDataSetChanged();
        }
    }

}
