package com.nhpm.Models.response;

import com.nhpm.Models.NotificationModel;
import com.nhpm.Models.response.seccMembers.HouseHoldItem;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Saurabh on 17-04-2017.
 */

public class NotificationResponse extends GenericResponse implements Serializable {

        private ArrayList<NotificationModel> notificationList;

        public ArrayList<NotificationModel> getNotificationList() {
            return notificationList;
        }

        public void setNotificationResp(ArrayList<NotificationModel> notificationModelList) {
            this.notificationList = notificationModelList;
        }

        static public NotificationResponse create(String serializedData) {
            // Use GSON to instantiate this class using the JSON representation of the state
            Gson gson = new Gson();
            return gson.fromJson(serializedData, NotificationResponse.class);
        }

        public String serialize() {
            // Serialize this class into a JSON string using GSON
            Gson gson = new Gson();
            return gson.toJson(this);
        }


}
