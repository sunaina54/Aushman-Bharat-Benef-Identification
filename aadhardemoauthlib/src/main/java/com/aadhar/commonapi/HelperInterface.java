package com.aadhar.commonapi;

public interface HelperInterface {
	void handlerFunction(final byte rawImage[], final int imageHeight,
                         final int imageWidth, final int status, final String errorMessage,
                         final boolean complete, final byte isoData[], final int quality,
                         final int finalNFIQ);
}
