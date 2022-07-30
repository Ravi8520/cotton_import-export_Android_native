package com.ecotton.impex.api;


public interface ApiCallback {
      void success(String responseData);
      void failure(String responseData);
}
