package com.nhpm.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.customComponent.utility.ProjectPrefrence;
import com.nhpm.BaseActivity;
import com.nhpm.Models.response.rsbyMembers.RSBYItem;
import com.nhpm.Models.response.rsbyMembers.RSBYMemberResponse;
import com.nhpm.R;
import com.nhpm.Utility.AppConstant;

import java.util.ArrayList;
import java.util.List;

public class SearchRSBYMemberActivity extends BaseActivity {
    private TextView headerTextTV;
    private RelativeLayout searchLayout;
    private Context context;
    private RecyclerView rsbyFamilyList;
    private String TAG="SearchRSBY Member";
    private ArrayList<RSBYItem> rsbyList;
    private ImageView backIV;
    private EditText seacrhURNET;
    private Button offlineRecBT;
    private RSBYMemberResponse rsbiMemberResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rsbymember_details);
        setupScreen();
        logoutScreen();
    }
    private void setupScreen(){
        context=this;
        preparedRsbyData();
        rsbiMemberResponse=RSBYMemberResponse.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF,
                AppConstant.RSBY_CONTENT,context));
        if(rsbiMemberResponse==null){
            rsbiMemberResponse=new RSBYMemberResponse();
            rsbiMemberResponse.setRsbyList(rsbyList);
            ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.RSBY_CONTENT, rsbiMemberResponse.serialize(), context);
            rsbiMemberResponse=RSBYMemberResponse.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF,
                    AppConstant.RSBY_CONTENT,context));
        }

        headerTextTV=(TextView)findViewById(R.id.centertext);
        headerTextTV.setText("Search RSBY Family");
        backIV=(ImageView)findViewById(R.id.back);
        headerTextTV.setVisibility(View.VISIBLE);
        searchLayout=(RelativeLayout)findViewById(R.id.searchRSBY);
        rsbyFamilyList=(RecyclerView)findViewById(R.id.rsbyList);
        rsbyFamilyList.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rsbyFamilyList.setLayoutManager(layoutManager);
        rsbyFamilyList.setItemAnimator(new DefaultItemAnimator());
        offlineRecBT=(Button)findViewById(R.id.addOfflineURNBT);
        seacrhURNET=(EditText)findViewById(R.id.searchRsbyET);
        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                rightTransition();
            }
        });

        offlineRecBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             /*   RSBYItem item=new RSBYItem();
                item.setIsDownloaded(false);
                item.setURN(seacrhURNET.getText().toString());
                rsbiMemberResponse.getRsbyList().add(item);
                ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.RSBY_CONTENT, rsbiMemberResponse.serialize(), context);
                finish();*/
            }
        });
        searchLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* boolean flag=false;
                String urnText=seacrhURNET.getText().toString();
                if(urnText.trim().length()==17) {
                    ArrayList<RSBYItem> rsbyItems = new ArrayList<RSBYItem>();
                    for (RSBYItem item : rsbiMemberResponse.getRsbyList()) {
                        if (item.getURN().equalsIgnoreCase(urnText)) {
                            flag = true;
                            item.setIsDownloaded(true);
                            rsbyItems.add(item);
                        }
                    }
                    if (flag) {
                        OtherMemberAdapter adapter = new OtherMemberAdapter(context, rsbyList);
                        rsbyFamilyList.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    } else {
                        offlineRecBT.setVisibility(View.VISIBLE);
                    }
                } else {
                    CustomAlert.alertWithOk(context, "Please enter valid URN Number");
                }*/
            }
        });


    }

    private void preparedRsbyData(){
        RSBYItem item=null;
       /* rsbyList=new ArrayList<>();
                item=new RSBYItem();
                item.setURN("09430323012000774");
                item.setMemberName("Amit Singh");
                item.setFatherHusbandName("Rahul Singh");
                item.setGender("M");
        item.setIsDownloaded(true);
        rsbyList.add(item);
         item=new RSBYItem();
        item.setURN("09430323012000774");
        item.setMemberName("Amit Singh");
        item.setFatherHusbandName("Rahul Singh");
        item.setGender("M");
        item.setIsDownloaded(true);*/
        rsbyList.add(item);
    }


    private class OtherMemberAdapter extends RecyclerView.Adapter<OtherMemberAdapter.MyViewHolder> {

        View view;
        AlertDialog dialog;
        private ArrayList<RSBYItem> dataSet;
        // Context context;
        private TextView text;
        //private Activity context;
        public class MyViewHolder extends RecyclerView.ViewHolder   {
            RelativeLayout itemlay;
            TextView nameTV,idd,urnTV,verfiedTV,fatherNameTV,motherNameTV,genderTV,verifiedStatTV;
            ImageView next;
            Button eSignBT;


            public MyViewHolder(View itemView) {
                super(itemView);
                this.nameTV = (TextView) itemView.findViewById(R.id.nameTV);
                // this.idd=(TextView)itemView.findViewById(R.id.idd);
                //this.next = (ImageView) itemView.findViewById(R.id.next);
                this.urnTV=(TextView)itemView.findViewById(R.id.urnNoTV);
                fatherNameTV=(TextView)itemView.findViewById(R.id.fatherNameTV);
                verifiedStatTV=(TextView)itemView.findViewById(R.id.verifiedStatusTV);
                // motherNameTV=(TextView)itemView.findViewById(R.id.motherNameTV);
                genderTV=(TextView)itemView.findViewById(R.id.genderTV);


                /*itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent theIntent=new Intent(context,RsbyMemberDetailActivity.class);
                        theIntent.putExtra(AppConstant.SELECTED_MEMBER,dataSet.get(getPosition()));
                        startActivity(theIntent);
                    }
                });*/

            }


        }
        public void addAll(List<RSBYItem> list) {

            dataSet.addAll(list);
            notifyDataSetChanged();
        }
        public OtherMemberAdapter(Context context, ArrayList<RSBYItem> data) {

            this.dataSet = data;;
            this.text=text;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.rsby_member_item, parent, false);

            //view.setOnClickListener(MainActivity.myOnClickListener);

            MyViewHolder myViewHolder = new MyViewHolder(view);
            return myViewHolder;
        }


        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

            if(listPosition==0) {
                holder.verifiedStatTV.setTextColor(getColor(R.color.green));
                holder.verifiedStatTV.setText("Verified");
            }else{
                holder.verifiedStatTV.setTextColor(Color.RED);
                holder.verifiedStatTV.setText("Not Verified");
            }

           /* RSBYItem item=dataSet.get(listPosition);
            holder.urnTV.setText(item.getURN());
            holder.nameTV.setText(item.getMemberName());
            //  holder.idd.setText(dataSet.get(listPosition).getId());
            holder.fatherNameTV.setText(item.getFatherHusbandName());
            // holder.motherNameTV.setText(item.getMothernmNpr());
            if(item.getGender().equalsIgnoreCase("M")) {
                holder.genderTV.setText("Male");
            }else{
                holder.genderTV.setText("Female");
            }*/
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

    private void logoutScreen(){
        final ImageView settings=(ImageView)findViewById(R.id.settings);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(context, settings);
                popup.getMenuInflater()
                        .inflate(R.menu.logout_home, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.logout:
                               // logoutVerifier();
                               Intent theIntent = new Intent(context, LoginActivity.class);
                                theIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                theIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(theIntent);
                                rightTransition();
                                break;

                        }
                        return true;
                    }
                });
                popup.show();
            }
        });

    }

}
