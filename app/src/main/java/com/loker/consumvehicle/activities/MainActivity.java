package com.loker.consumvehicle.activities;

import android.app.ProgressDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.loker.consumvehicle.ViewModel.CarDataViewModel;
import com.loker.consumvehicle.ViewModel.LoginViewModel;
import com.loker.consumvehicle.R;
import com.loker.consumvehicle.model.Car;
import com.loker.consumvehicle.model.Refill;
import com.loker.consumvehicle.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final int REGISTER_REQUEST_ADD_CAR= 1;
    public static final String  APPUSER_UID = "com.loker.consumvehicle.activities.MainActivity.UID";
    //AndroidViewModel
    private LoginViewModel consumVehiclesVM;
    private CarDataViewModel carDataViewModel;


    private User appUser;
    private List<Car> userCarList=new ArrayList<>();
    //globals to delete car (if there are not globals had to be final and dosent work)
    private Car carToDelete;

    private RecyclerView carsListRV;
    private AdapterCarsList carsListAdapter;

    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        consumVehiclesVM = ViewModelProviders.of(this).get(LoginViewModel.class);
        carDataViewModel = ViewModelProviders.of(this).get(CarDataViewModel.class);

        consumVehiclesVM.getCurrenUser().observe(this, new Observer<User>() {
            @Override
            public void onChanged(@Nullable User currentUser) {
                if (currentUser == null) {
                    // Not signed in, launch the Sign In activity
                    startActivity(new Intent(MainActivity.this, SignInActivity.class));
                    finish();
                } else {
                        //set username
                        appUser = currentUser;
                        View headerNavigation = navigationView.getHeaderView(0);
                        TextView tvUsername = headerNavigation.findViewById(R.id.userName);
                        tvUsername.setText(appUser.getEmail());

                }
            }
        });


        carDataViewModel.getUserCarList().observe(this,new Observer<List<Car>>() {
            @Override
            public void onChanged(@Nullable List<Car> cars) {
                userCarList = cars;
                carsListAdapter.setCarList(userCarList);
                Log.d("getUserCarList:Main",userCarList.toString());
            }
        });


        carsListRV = findViewById(R.id.llista);
        carsListRV.setHasFixedSize(true);
        carsListRV.setLayoutManager(new LinearLayoutManager(this));

        carsListAdapter=new AdapterCarsList(this,userCarList);
        carsListRV.setAdapter(carsListAdapter);


        ItemTouchHelper carListHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                carToDelete = userCarList.get(viewHolder.getAdapterPosition());
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle(getString(R.string.confirmar_borrar_Title_Dialog))
                        .setMessage(getString(R.string.confirmar_borrar_Message_Dialog)+" "+carToDelete.getCarName()+"?")
                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                carsListAdapter.notifyDataSetChanged();
                            }
                        })
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                carDataViewModel.deleteCar(carToDelete).observe(MainActivity.this, new Observer<Boolean>() {
                                    @Override
                                    public void onChanged(@Nullable Boolean aBoolean) {
                                        if(aBoolean!=null) {
                                            //if delete operation is successful
                                            if (aBoolean) {
                                                Toast.makeText(getBaseContext(), "!! " + carToDelete.getCarName() + " deteted !!", Toast.LENGTH_SHORT).show();
                                                carDataViewModel.resetRemoveSuccess();
                                                Log.d("remove Car", "userCarList" + userCarList.size());
                                            } else {
                                                Toast.makeText(getBaseContext(), "Some error ocurred during deletion", Toast.LENGTH_SHORT).show();
                                                carsListAdapter.notifyDataSetChanged();
                                            }
                                        }

                                    }
                                });

                            }
                        })
                        .setIcon(android.R.drawable.ic_menu_delete)
                        .show();

            }
        });

        carListHelper.attachToRecyclerView(carsListRV);


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newCar = new Intent(MainActivity.this,AddCarActivity.class);
                newCar.putExtra(APPUSER_UID,appUser.getUID());
                startActivityForResult(newCar,REGISTER_REQUEST_ADD_CAR);
            }
        });


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        // Initialize Firebase Auth
        /*mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();*/

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }else if(id == R.id.sign_out_menu){
                consumVehiclesVM.signOut();
                //Auth.GoogleSignInApi.signOut(mGoogleApiClient);
                appUser = null;
                startActivity(new Intent(this, SignInActivity.class));
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_tools) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REGISTER_REQUEST_ADD_CAR){
            if(resultCode == RESULT_OK){
                final Car newCar = new Car();
                newCar.setCarName(data.getStringExtra("carName"));
                newCar.setInicialKm(data.getFloatExtra("inicialKms",0));
                newCar.setUID(appUser.getUID());
                carDataViewModel.addCar(newCar).observe(this, new Observer<Boolean>() {
                    @Override
                    public void onChanged(@Nullable Boolean success) {
                        if(success!=null) {
                            if (success) {
                                Toast.makeText(MainActivity.this, "Car added", Toast.LENGTH_SHORT).show();
                                //prepare the boolean variable to the next change
                                carDataViewModel.resetAddCarSuccess();
                                Log.d("addedCar", "userCarList.size()=" + userCarList.size());
                            } else {
                                Toast.makeText(MainActivity.this, "Error Writting the document", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
            }
        }
    }
}
