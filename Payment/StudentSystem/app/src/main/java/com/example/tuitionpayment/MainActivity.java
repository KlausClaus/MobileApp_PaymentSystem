package com.example.tuitionpayment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * MainActivity is the main entry point of the application after login.
 * It initializes the navigation bar, requests necessary permissions, and handles fragment navigation.
 */
public class MainActivity extends AppCompatActivity {
    // Permissions required for external storage access
    private static String[] PERMISSIONS_STORAGE = {android.Manifest.permission.READ_EXTERNAL_STORAGE,// Read external storage
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE};// Write external storage
    private static int REQUEST_PERMISSION_CODE = 3;

    /**
     * Called when the activity is starting. Sets up the navigation bar, action bar, and permissions.
     *
     * @param savedInstanceState the previously saved state of the activity, if any
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setBackgroundDrawable(getDrawable(R.drawable.shape_action_bar_bg));
        getSupportActionBar().setTitle("");

        getPermission(MainActivity.this);
        // Get the BottomNavigationView from the layout
        BottomNavigationView navView = findViewById(R.id.nav_view);

        // Configure navigation and bottom menu linking
        // IDs in the bottom menu items and navigation layout must match
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_user)
                .build();
        // Create a NavController for the fragment container
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        // Setup action bar with the NavController
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        // Link bottom navigation with the NavController
        NavigationUI.setupWithNavController(navView, navController);

        Intent intent = getIntent();
        int id = intent.getIntExtra("fragment_flag", -1);
        String picurl= intent.getStringExtra("picurl");
        System.out.println(id);
        System.out.println(picurl);
        if (id == 2) {
            navController.navigate(R.id.navigation_home);
        }



    }

    /**
     * Requests the necessary permissions for the application.
     *
     * @param obj the activity from which permissions are requested
     */
    public void getPermission(Activity obj){
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            for (int i = 0 ; i < PERMISSIONS_STORAGE.length ; i++){
                if (ActivityCompat.checkSelfPermission(obj,
                        PERMISSIONS_STORAGE[i])!= PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(obj, PERMISSIONS_STORAGE, REQUEST_PERMISSION_CODE);
                }
            }
        }
    }

    /**
     * Hides the navigation bar and status bar for an immersive experience.
     */
    public void hideNav() {
        Window window = getWindow();
        window.getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LOW_PROFILE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }


//    @Override
//    protected void onNewIntent(Intent intent) {
//        intent = getIntent();
//        String a = intent.getStringExtra("back");
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        if (Integer.parseInt(a)==2){
//            navController.navigate(R.id.navigation_user);
//        }
//        System.out.println("===================================="+a);
//    }

}
