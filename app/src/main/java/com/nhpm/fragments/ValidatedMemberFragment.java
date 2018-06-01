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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.customComponent.utility.ProjectPrefrence;
import com.nhpm.LocalDataBase.DatabaseHelpers;
import com.nhpm.LocalDataBase.dto.MemberRequest;
import com.nhpm.Models.response.seccMembers.SeccMemberItem;
import com.nhpm.Models.response.seccMembers.SeccMemberResponse;
import com.nhpm.Models.response.verifier.VerifierLocationOLD;
import com.nhpm.NHSMasterDao;
import com.nhpm.NHSMasterDaoImpl;
import com.nhpm.R;
import com.nhpm.ReqRespModels.NhsDataList;
import com.nhpm.Utility.AppConstant;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ValidatedMemberFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ValidatedMemberFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ValidatedMemberFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private String TAG="Offilne Updated Member";
    private RecyclerView offlineList;
    private Button sendAllBT;
    private Context context;
    private TextView centerTextTV;
    private ArrayList<NhsDataList> memberList;
    private DatabaseHelpers dbHelper;
    private MemberRequest memberRequest;
    private VerifierLocationOLD verifierLoc;
    private SeccMemberResponse seccMemberResponse;
    private ArrayList<SeccMemberItem> seccMemberList;


    public ValidatedMemberFragment() {
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
    public static ValidatedMemberFragment newInstance(String param1, String param2) {
        ValidatedMemberFragment fragment = new ValidatedMemberFragment();
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
        View view=inflater.inflate(R.layout.fragement_error_layout, container, false);
        setupScreen(view);
        return view;
    }
    private void setupScreen(View view){
        context=getActivity();
        dbHelper = DatabaseHelpers.getInstance(context);
      /*  memberRequest= MemberRequest.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.MASTER_LOC_CONTENT, context));
        Log.d(TAG, "Member Request : " + memberRequest.getStateCode() + " " + memberRequest.getDistrictCode());
      */
        loadSeccFamilyMembers();

       // getSECCDataForSearchSpinner();
        sendAllBT=(Button)view.findViewById(R.id.sendAllBT);
        // prepareData();
        offlineList=(RecyclerView)view.findViewById(R.id.offlineList);
        offlineList.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        offlineList.setLayoutManager(layoutManager);
        offlineList.setItemAnimator(new DefaultItemAnimator());
        MemberAdapter adapter=new MemberAdapter(context,seccMemberList);
        offlineList.setAdapter(adapter);
      /*  MemberAdapter
                //adapter = new MemberAdapter(getApplicationContext(), nhsDataLists);
        offlineList.setAdapter(adapter);
*/
    }

   private class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.MyViewHolder> {

        View view;
        AlertDialog dialog;
        private ArrayList<SeccMemberItem> dataSet;
        Context context;
        private TextView text;
        public class MyViewHolder extends RecyclerView.ViewHolder   {
           TextView nameTV,houseNoTV,aadhaarTV;
            LinearLayout errorMsgLayout;
            public MyViewHolder(View itemView) {
                super(itemView);
                this.houseNoTV = (TextView) itemView.findViewById(R.id.houseNoTV);
                this.nameTV = (TextView) itemView.findViewById(R.id.memberNametTV);
                this.aadhaarTV = (TextView) itemView.findViewById(R.id.aadhaarNoTV);
                errorMsgLayout=(LinearLayout)itemView.findViewById(R.id.errorMsgLayout);
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
                    .inflate(R.layout.row_error_layout, parent, false);

            //view.setOnClickListener(MainActivity.myOnClickListener);

            MyViewHolder myViewHolder = new MyViewHolder(view);
            return myViewHolder;
        }


        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

            holder.houseNoTV.setText(dataSet.get(listPosition).getAhlslnohhd());
            holder.nameTV.setText(dataSet.get(listPosition).getName());
            if(listPosition==0){
                holder.errorMsgLayout.setVisibility(View.GONE);
            }
           // holder.aadhaarTV.setText(dataSet.get(listPosition).getAadhaarNo());
           /* if(dataSet.get(listPosition).getGenderidNpr().equalsIgnoreCase("0")) {
                holder.genderTV.setText("Male");
            }else{
                holder.genderTV.setText("Female");
            }*/
           /* holder.addressTV.setText(dataSet.get(listPosition).getAddressline1()+"," +
                    ""+dataSet.get(listPosition).getAddressline1());
*/        }

        @Override
        public int getItemCount() {
            return dataSet.size();
        }
        public void clearDataSource() {

            dataSet.clear();
            notifyDataSetChanged();
        }
    }

    private ArrayList<NhsDataList> getSECCDataForSearchSpinner(){
        NHSMasterDao dao=new NHSMasterDaoImpl();
        memberList=new ArrayList<>();
        memberList=dao.getMembers1(memberRequest,dbHelper);
        Log.d("Home Activity", " Member list size : " + memberList.size());
        /*TreeSet<NhsDataList> treeSet = new TreeSet<NhsDataList>(new Comparator<NhsDataList>(){
            public int compare(NhsDataList o1, NhsDataList o2) {
                // return 0 if objects are equal in terms of your properties
                if (o1.getAhlslnohhd().trim().equalsIgnoreCase(o2.getAhlslnohhd().trim())){
                    return 0;
                }
                return 1;
            }
        });
        treeSet.addAll(memberList1);
        memberList.addAll(treeSet);
        NhsDataList defaultList=new NhsDataList();
        defaultList.setAhlslnohhd("Select Head of the family");*/
        // memberList.add(0,defaultList);
        return memberList;
    }

    private void loadSeccFamilyMembers(){
        verifierLoc= VerifierLocationOLD.create(ProjectPrefrence.getSharedPrefrenceData(
                AppConstant.PROJECT_PREF, AppConstant.SELECTED_BLOCK, context));
        seccMemberResponse= SeccMemberResponse.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF,
                AppConstant.SECC_MEMBER_CONTENT, context));
        seccMemberList=new ArrayList<>();
        for(SeccMemberItem item : seccMemberResponse.getSeccMemberList()){
            String locStr=verifierLoc.getStateCode()+verifierLoc.getDistrictCode()+verifierLoc.getTehsilCode()
                    +verifierLoc.getVillTownCode()+verifierLoc.getWardCode()+verifierLoc.getBlockCode();
            String seccStr=item.getStatecode()+item.getDistrictcode()+item.getTehsilcode()+item.getTowncode()+item.getWardid()+
                    item.getAhlblockno();
            if(locStr.equalsIgnoreCase(seccStr)){
                seccMemberList.add(item);
            }
        }
    }
}
