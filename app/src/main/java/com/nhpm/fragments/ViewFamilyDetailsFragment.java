package com.nhpm.fragments;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.customComponent.CustomAlert;
import com.customComponent.CustomAsyncTask;
import com.customComponent.TaskListener;
import com.customComponent.utility.CustomHttp;
import com.nhpm.CameraUtils.CommonUtilsImageCompression;
import com.nhpm.CameraUtils.squarecamera.CameraActivity;
import com.nhpm.LocalDataBase.DatabaseHelpers;
import com.nhpm.Models.FamilyMemberModel;
import com.nhpm.Models.request.FamilyDetailsItemModel;
import com.nhpm.Models.request.GetMemberDetail;
import com.nhpm.Models.request.PersonalDetailItem;
import com.nhpm.Models.request.PrintCardItem;
import com.nhpm.Models.response.DocsListItem;
import com.nhpm.Models.response.FamilyDetailResponse;
import com.nhpm.Models.response.GenericResponse;
import com.nhpm.Models.response.GovernmentIdItem;
import com.nhpm.Models.response.PersonalDetailResponse;
import com.nhpm.R;
import com.nhpm.Utility.AppConstant;
import com.nhpm.Utility.AppUtility;
import com.nhpm.activity.CollectDataActivity;
import com.nhpm.activity.FamilyMemberEntryActivity;
import com.nhpm.activity.ViewMemberDataActivity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViewFamilyDetailsFragment extends Fragment {
    private View view;
    private FragmentTransaction fragmentTransection;
    String familyResponse;
    private GetMemberDetail getMemberDetail;
    private CustomAsyncTask customAsyncTask;
    private FragmentManager fragmentManager;
    private GenericResponse genericResponse;
    private FamilyAdapter adapter;
    private Context context;
    private ArrayList<GovernmentIdItem> govtIdStatusList;
    private Spinner govtIdSP;
    private TextView beneficiaryNameTV;
    private GovernmentIdItem item;
    private Button captureImageBT;
    private Bitmap captureImageBM;
    private ViewMemberDataActivity activity;
    private DocsListItem beneficiaryListItem;
    private String voterIdImg;
    private ImageView beneficiaryPhotoIV;
    private int CAMERA_PIC_REQUEST = 0;
    private Button previousBT;
    private ImageView addIV;
    private PersonalDetailItem personalDetailItem;
    private LinearLayout addFamilyMemberLL;
    private RecyclerView memberRecycle;
    private ArrayList<FamilyMemberModel> familyMembersList;
    private FamilyDetailsItemModel familyDetailsItemModel;
    private FamilyDetailResponse familyDetailResponse;
    private EditText govtIdET;
    private Button getFamilyScoreBT, nextBT;
    public static String FAMILY_DETAIL = "familyDetail";
    public static String INDEX = "Index";
    private LinearLayout scoreLL;
    private PrintCardItem printCardItem;
    private TextView familyMatchScoreTV;
    private TextView benefcisryNameTV;


    public ViewFamilyDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_view_family_details, container, false);
        context = getActivity();
        setupScreen(view);
        return view;

    }

    private void setupScreen(View view) {

        fragmentManager = getActivity().getSupportFragmentManager();
        familyDetailResponse = activity.getMemberDetailItem.getFamilyDetailsItem();

        govtIdSP = (Spinner) view.findViewById(R.id.govtIdSP);
        govtIdET = (EditText) view.findViewById(R.id.govtIdET);
        getFamilyScoreBT = (Button) view.findViewById(R.id.getFamilyScoreBT);
        //prepareGovernmentIdSpinner();
        memberRecycle = (RecyclerView) view.findViewById(R.id.memberRecycle);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        memberRecycle.setLayoutManager(layoutManager);
        memberRecycle.setItemAnimator(new DefaultItemAnimator());
        addFamilyMemberLL = (LinearLayout) view.findViewById(R.id.addFamilyMemberLL);
        beneficiaryNameTV = (TextView) view.findViewById(R.id.beneficiaryNameTV);
        captureImageBT = (Button) view.findViewById(R.id.captureImageBT);
        familyMatchScoreTV = (TextView) view.findViewById(R.id.familyMatchScoreTV);
        beneficiaryPhotoIV = (ImageView) view.findViewById(R.id.beneficiaryPhotoIV);
        benefcisryNameTV= (TextView) view.findViewById(R.id.beneficiaryNameTV);
        scoreLL = (LinearLayout) view.findViewById(R.id.scoreLL);


        if(familyDetailResponse!=null){
            benefcisryNameTV.setText(activity.getMemberDetailItem.getPersonalDetail().getBenefName());
            if(familyDetailResponse.getIdType()!=null &&
                    !familyDetailResponse.getIdType().equalsIgnoreCase("")){
                prepareGovernmentIdSpinner();
            }

            if(familyDetailResponse.getIdNumber()!=null &&
                    !familyDetailResponse.getIdNumber().equalsIgnoreCase("")){
                govtIdET.setText(familyDetailResponse.getIdNumber());
            }

            if(familyDetailResponse.getIdImage()!=null
                    && !familyDetailResponse.getIdImage().equalsIgnoreCase("")){
                beneficiaryPhotoIV.setImageBitmap(AppUtility.
                        convertStringToBitmap(familyDetailResponse.getIdImage()));
            }

            if(familyDetailResponse.getFamilyMatchScore()!=null){
                familyMatchScoreTV.setText(familyDetailResponse.getFamilyMatchScore()+"%");
            }

            if(familyDetailResponse.getFamilyMemberModels()!=null
                    && familyDetailResponse.getFamilyMemberModels().size()>0){
                refreshList(familyDetailResponse.getFamilyMemberModels());
            }
        }



        addIV = (ImageView) view.findViewById(R.id.addIV);

        previousBT = (Button) view.findViewById(R.id.previousBT);
        previousBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.personalDetailsLL.setBackground(context.getResources().getDrawable(R.drawable.arrow_yellow));
                activity.familyDetailsLL.setBackground(context.getResources().getDrawable(R.drawable.arrow));
                Fragment fragment = new ViewPersonalDetailsFragment();
                CallFragment(fragment);

            }
        });

        nextBT = (Button) view.findViewById(R.id.nextBT);
    }



    public void CallFragment(Fragment fragment) {
        if (fragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragContainer, fragment);
            fragmentTransaction.commit();
        }
    }

    private void prepareGovernmentIdSpinner() {
        govtIdStatusList = AppUtility.prepareGovernmentIdSpinnerList();
        ArrayList<String> spinnerList = new ArrayList<>();
        for (GovernmentIdItem item : govtIdStatusList) {
            spinnerList.add(item.status);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, R.layout.custom_drop_down, R.id.textView, spinnerList);
        govtIdSP.setAdapter(adapter);

        govtIdSP.setSelection(1);

    }

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        activity = (ViewMemberDataActivity) context;
        getMemberDetail = activity.getMemberDetailItem;

    }

    private void deleteDir(File file) {

        if (file.isDirectory()) {
            String[] children = file.list();
            for (String child : children) {
                if (child.endsWith(".jpg") || child.endsWith(".jpeg"))
                    new File(file, child).delete();
            }
            file.delete();
        }
        Intent mediaScannerIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri fileContentUri = Uri.fromFile(file);
        mediaScannerIntent.setData(fileContentUri);
        context.sendBroadcast(mediaScannerIntent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_PIC_REQUEST) {
               /* Uri imageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),"govtIdPhoto" +".jpg"));
                try {
                    captureImageBM = MediaStore.Images.Media.getBitmap(this.getContentResolver(),imageUri); //(Bitmap)imageUri;//data.getExtras().get("data");
                } catch (IOException e) {
                    e.printStackTrace();
                }*/
            Uri fileUri = Uri.fromFile(new File(DatabaseHelpers.DELETE_FOLDER_PATH,
                    context.getString(R.string.squarecamera__app_name) + "/photoCapture/IMG_12345.jpg"));
            Uri compressedUri = Uri.fromFile(new File(CommonUtilsImageCompression.compressImage(fileUri.getPath(), context, "/photoCapture")));
            //  captureImageBM=(Bitmap)data.getExtras().get("data");
            try {
                captureImageBM = MediaStore.Images.Media.getBitmap(context.getContentResolver(), compressedUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            // Log.d(TAG," Bitmap Size : "+image.getAllocationByteCount());
            voterIdImg = AppUtility.convertBitmapToString(captureImageBM);
            updateScreen(voterIdImg);
        }

        if (requestCode == AppConstant.FAMILY_MEMBER_REQUEST_CODE_VALUE) {
            if (resultCode == Activity.RESULT_OK) {

                // Utility.scrollToEnd(scrollView);
                //  Toast.makeText(context, "Result Recieved", Toast.LENGTH_LONG).show();
                FamilyMemberModel item = (FamilyMemberModel) data.getSerializableExtra(AppConstant.FAMILY_MEMBER_RESULT_CODE_NAME);
                String index = data.getStringExtra(INDEX);
                if (item != null) {
                    if (index != null) {
                        familyMembersList.set(Integer.parseInt(index), item);
                        refreshList(familyMembersList);
                    } else {
                        familyMembersList.add(item);
                        refreshList(familyMembersList);
                    }
                }
                scoreLL.setVisibility(View.GONE);
                if (familyMembersList.size() > 0) {
                    scoreLL.setVisibility(View.VISIBLE);
                }
                refreshList(familyMembersList);
            }
        }
    }

    private void refreshList(ArrayList<FamilyMemberModel> familyMembersList) {
        if (familyMembersList != null) {
            adapter = new FamilyAdapter(context, familyMembersList);
            memberRecycle.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }

    private class FamilyAdapter extends RecyclerView.Adapter<FamilyAdapter.MyViewHolder> {

        View view;
        private ArrayList<FamilyMemberModel> dataSet;


        public class MyViewHolder extends RecyclerView.ViewHolder {
            RelativeLayout menuLayout;
            ImageView settings;
            TextView nameTV;


            public MyViewHolder(final View itemView) {
                super(itemView);
                nameTV = (TextView) itemView.findViewById(R.id.nameTV);
                settings = (ImageView) itemView.findViewById(R.id.settingsIV);
                settings.setVisibility(View.GONE);
                menuLayout = (RelativeLayout) itemView.findViewById(R.id.menuLayoutRL);
                menuLayout.setVisibility(View.GONE);
            }
        }


        public FamilyAdapter(Context context, ArrayList<FamilyMemberModel> data) {
            this.dataSet = data;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.member_data_row, parent, false);
            MyViewHolder myViewHolder = new MyViewHolder(view);
            return myViewHolder;
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {


            final FamilyMemberModel item = dataSet.get(listPosition);
            holder.menuLayout.setVisibility(View.GONE);
            holder.settings.setVisibility(View.GONE);
            holder.nameTV.setText(item.getName());


        }

        @Override
        public int getItemCount() {
            return dataSet.size();
        }


    }


    private void updateScreen(String idImage) {
        try {
            if (idImage != null) {
                beneficiaryPhotoIV.setImageBitmap(AppUtility.convertStringToBitmap(idImage));
            } else {
//                if (seccItem.getGovtIdPhoto() != null && !seccItem.getGovtIdPhoto().equalsIgnoreCase("")) {
//
//                } else {
                beneficiaryPhotoIV.setImageBitmap(null);
//                }
            }
        } catch (Exception e) {

        }
    }

    void editDelete(RelativeLayout menuLayout, final ImageView settings, final FamilyMemberModel item1, final int index) {
        settings.setVisibility(View.VISIBLE);

        menuLayout.setVisibility(View.VISIBLE);
        menuLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(context, settings);
                popup.getMenuInflater()
                        .inflate(R.menu.menu_edit, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.edit:
                                Intent theIntent = new Intent(context, FamilyMemberEntryActivity.class);
                                theIntent.putExtra(AppConstant.FAMILY_MEMBER_RESULT_CODE_NAME, item1);
                                theIntent.putExtra(INDEX, index + "");
                                startActivityForResult(theIntent, AppConstant.FAMILY_MEMBER_REQUEST_CODE_VALUE);
                                break;
                            case R.id.delete:
                                alertWithOk(context, "Do yow want to delete member", familyMembersList, index);
                              /*  familyMembersList.remove(index);
                                refreshList(familyMembersList);*/
                                break;

                        }
                        return true;
                    }
                });
                popup.show();

            }
        });
    }

    private void alertWithOk(Context mContext, String msg, final ArrayList<FamilyMemberModel> familyMembersList, final int index) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(mContext.getResources().getString(com.customComponent.R.string.Alert));
        builder.setMessage(msg)
                .setCancelable(false)
                .setPositiveButton(mContext.getResources().getString(com.customComponent.R.string.OK), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do things
                        familyMembersList.remove(index);
                        refreshList(familyMembersList);

                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }


}
