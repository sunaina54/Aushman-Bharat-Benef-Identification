package com.nhpm.fragments;

import android.app.AlertDialog;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.customComponent.utility.ProjectPrefrence;
import com.nhpm.LocalDataBase.DatabaseHelpers;
import com.nhpm.LocalDataBase.dto.MemberRequest;
import com.nhpm.Models.response.seccMembers.SeccMemberItem;
import com.nhpm.Models.response.seccMembers.SeccMemberResponse;
import com.nhpm.Models.response.verifier.VerifierLocationOLD;
import com.nhpm.Models.response.verifier.VerifierLoginResponse1;
import com.nhpm.R;
import com.nhpm.Utility.AppConstant;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CompleteVerifiedFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CompleteVerifiedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CompleteVerifiedFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private String TAG="Offilne Updated Member";
    private RecyclerView offlineList;
    private Button sendAllBT;
    private Context context;
    private TextView centerTextTV;
    private ArrayList<SeccMemberItem> memberList;
    private DatabaseHelpers dbHelper;
    private MemberRequest memberRequest;
    private SeccMemberResponse seccMemberResponse;
    private VerifierLoginResponse1 loginResponse;

    public CompleteVerifiedFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PartialVerifiedFragment.
     */
    // TODO: Rename and change types and number of parameters

    public static CompleteVerifiedFragment newInstance(String param1, String param2) {
        CompleteVerifiedFragment fragment = new CompleteVerifiedFragment();
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
        View view=inflater.inflate(R.layout.fragment_complete_verified, container, false);
        setupScreen(view);
        return view;
    }
    private void setupScreen(View view){
        context=getActivity();
        dbHelper = DatabaseHelpers.getInstance(context);
        loginResponse= VerifierLoginResponse1.create(ProjectPrefrence.
                getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.VERIFIER_CONTENT, context));
       memberList= getSeccMemberList();

        sendAllBT=(Button)view.findViewById(R.id.sendAllBT);
        offlineList=(RecyclerView)view.findViewById(R.id.offlineList);
        offlineList.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        offlineList.setLayoutManager(layoutManager);
        offlineList.setItemAnimator(new DefaultItemAnimator());
        MemberAdapter adapter=new MemberAdapter(context,memberList);
        offlineList.setAdapter(adapter);
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
            Log.d("jkagkj","LocationStr : "+seccStr);
            if(seccStr.equalsIgnoreCase(locStr)){
                memberList.add(item1);
            }
        }
        return memberList;
    }
    private class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.MyViewHolder> {

        View view;
        AlertDialog dialog;
        private ArrayList<SeccMemberItem> dataSet;
        private Context context;
        private TextView text;
        public class MyViewHolder extends RecyclerView.ViewHolder   {
            RelativeLayout itemlay;
            TextView cname,idd;
            ImageView next;
            TextView nameTV,genderTV,fatherNameTV,addressTV,houseNoTV;
            Button sendBT;
            public MyViewHolder(View itemView) {
                super(itemView);
                this.houseNoTV = (TextView) itemView.findViewById(R.id.houseNoTV);
                this.genderTV = (TextView) itemView.findViewById(R.id.genderTV);
                this.nameTV = (TextView) itemView.findViewById(R.id.nameTV);
                this.fatherNameTV=(TextView)itemView.findViewById(R.id.fatherNameTV);
                // this.addressTV = (TextView) itemView.findViewById(R.id.addressTV);
                this.sendBT=(Button)itemView.findViewById(R.id.send);

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
                    .inflate(R.layout.complete_verified_member_item, parent, false);

            //view.setOnClickListener(MainActivity.myOnClickListener);

            MyViewHolder myViewHolder = new MyViewHolder(view);
            return myViewHolder;
        }


        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

            holder.houseNoTV.setText(dataSet.get(listPosition).getAhlslnohhd());
            holder.nameTV.setText(dataSet.get(listPosition).getName());
            holder.fatherNameTV.setText(dataSet.get(listPosition).getFathername());
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
