package com.panter.panterapp.util;

public interface AppConstants {

    // Web service URL that contains the menu info.
    String URL = "http://54.187.90.221/Drupal/api/v1/menus";

    // Namespace needed to call the web service.
    String NAMESPACE = "http://tempuri.org/";

    // Tag used to debug the application.
    String TAG = "PanterAppDebug";

    // JSON Node names
    String TAG_MENU_ID = "menuID";
    String TAG_NAME = "name";
    String TAG_ICON_URL = "iconURL";
    String TAG_LINK_URL = "linkURL";

}
