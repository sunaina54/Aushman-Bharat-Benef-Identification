package com.customComponent.Networking;




/**
 * INetworkResponseListener. Created on 19-09-2015.
 */
public interface INetworkResponseHandler<T> {

    void onNetworkResponse(NetworkRequestType type, NetworkResponse response);
}
