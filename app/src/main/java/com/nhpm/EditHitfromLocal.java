package com.nhpm;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.nhpm.LocalDataBase.DatabaseHelpers;
import com.nhpm.Networking.AddMemberServiceClient;
import com.nhpm.Networking.BaseServiceClient;
import com.nhpm.Networking.INetworkResponseHandler;
import com.nhpm.Networking.NetworkRequestType;
import com.nhpm.Networking.NetworkResponse;
import com.nhpm.Networking.UpdateMemberServiceClient;
import com.nhpm.ReqRespModels.AddMemberResponse;
import com.nhpm.ReqRespModels.NhsDataList;
import com.nhpm.ReqRespModels.UpdateRequest;
import com.nhpm.Utility.AppUtility;

import java.util.ArrayList;

/**
 * Created by psqit on 6/14/2016.
 */
public class EditHitfromLocal extends IntentService {

   DatabaseHelpers dbHelper;
    Context context;
    public EditHitfromLocal() {
       super("com.FieldVerifiy.EditHitfromLocal");

    }

    @Override
    protected void onHandleIntent(Intent intent) {
       context=this;
        dbHelper = DatabaseHelpers.getInstance(this);
        if(AppUtility.isNetworkAvailable(this)) {
            /*sendPendingAddData();
            sendPendingEditData();*/
        }
}

    private void sendPendingEditData()
    {
        ArrayList<NhsDataList> pendinglist;
        final MetaDataDao metaDataDao=new MetaDataDaoImpl();

        pendinglist = metaDataDao.getPendingEdits(dbHelper);
        if(pendinglist.size()>0) {
            for (final NhsDataList item : pendinglist) {
                UpdateRequest updateRequest = new UpdateRequest(item.getRemoteid(), item.getNameNpr(), item.getRelnameNpr(), item.getFathernmNpr(), item.getMothernmNpr(), item.getAadhaarNo(), item.getPhoneRespondent(), item.getOccunameNpr(), item.getIncomesourceUrban(), "", item.getDobNpr(), item.getGenderidNpr(), item.getMstatusidNpr());

                try {
                    UpdateMemberServiceClient client = new UpdateMemberServiceClient(updateRequest);

                    client.fireNetworkRequest(context, new INetworkResponseHandler() {
                        @Override
                        public void onNetworkResponse(NetworkRequestType type, NetworkResponse response) {




                            if (response.isSuccessful()) {
                                //    loginResponse = (LoginResponse) response;

                               /* item.setStatus("true");
                                metaDataDao.updateMetaDataFlag(item, dbHelper);*/
                                metaDataDao.delete(item.getId(),dbHelper);

                             /*   Toast.makeText(getApplicationContext(), "Member details updated successfully",
                                        Toast.LENGTH_SHORT).show();*/
                            } else {
                                //showHideProgressDialog(false);

                               /* Toast.makeText(getApplicationContext(), "There is some server issue",
                                        Toast.LENGTH_SHORT).show();*/

                            }


                        }
                    });
                } catch (BaseServiceClient.ValidationException e) {
                    e.printStackTrace();
                }
            }
        }
        else {
            /*Toast.makeText(getApplicationContext(), "Member details are already synced with server.",
                    Toast.LENGTH_SHORT).show();*/
            Log.d("Pending size", pendinglist.size() + "");
        }

        //send transaction pending data
    }


    private void sendPendingAddData()
    {
        ArrayList<NhsDataList> pendinglist;
        final AddMetaDataDao metaDataDao=new AddMetaDataDaoImpl();

        pendinglist = metaDataDao.getPendingAdds(dbHelper);
        if(pendinglist.size()>0) {
            for (final NhsDataList item : pendinglist) {

                try {
                    AddMemberServiceClient client = new AddMemberServiceClient(item);
                    client.fireNetworkRequest(context, new INetworkResponseHandler() {
                        @Override
                        public void onNetworkResponse(NetworkRequestType type, NetworkResponse response) {





                            if (response.isSuccessful()) {

                                //    loginResponse = (LoginResponse) response;
                                AddMemberResponse addMemberResponse = (AddMemberResponse) response;
                                Log.d("ID FROM SERVER",addMemberResponse.id);
                                metaDataDao.delete(item.getId(),dbHelper);
                                String localid=item.getRemoteid();
                                item.setId(localid);
                                item.setRemoteid(addMemberResponse.id);
                                item.setTin(addMemberResponse.tin);
                                NHSMasterDao nhsMasterDao = new NHSMasterDaoImpl();
                                nhsMasterDao.update(item, dbHelper);
                              /*  item.setStatus("true");
                                item.setRemoteid(addMemberResponse.id);
                                item.setTin(addMemberResponse.tin);
                                Log.d("LOCAL DB",item.serialize());*/

                                //  metaDataDao.updateMetaData(item, dbHelper);


                              /*  NHSMasterDao nhsMasterDao = new NHSMasterDaoImpl();
                                nhsMasterDao.save(item, dbHelper);*/
                              /*  Toast.makeText(getApplicationContext(), "Member details updated successfully",
                                        Toast.LENGTH_SHORT).show();*/
                            } else {
                                //showHideProgressDialog(false);

                              /*  Toast.makeText(getApplicationContext(), "There is some server issue",
                                        Toast.LENGTH_SHORT).show();*/

                            }


                        }
                    });
                } catch (BaseServiceClient.ValidationException e) {
                    e.printStackTrace();
                }


            }
        }
        else {
           /* Toast.makeText(getApplicationContext(), "Member details are already synced with server.",
                    Toast.LENGTH_SHORT).show();*/
            Log.d("Pending size", pendinglist.size() + "");
        }

        //send transaction pending data
    }



}
