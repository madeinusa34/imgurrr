package com.example.epictureapi;

import static android.os.Build.VERSION_CODES.M;

public interface AuthenticationListener  {
   void onTokenReceived(String auth_token);
}

