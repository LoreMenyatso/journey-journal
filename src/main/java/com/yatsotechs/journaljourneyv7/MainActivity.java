package com.yatsotechs.journaljourneyv7;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton FloatingActionButton;

    // creating variables for fab, firebase database,
    // progress bar, list, adapter,firebase auth,
    // recycler view and relative layout.
    private FirebaseDatabase firebaseDatabase;
    private FirebaseDatabase firebaseDatabase1;

    StorageReference storageReference;

    private DatabaseReference databaseReference;
    private DatabaseReference databaseReference1;

    private RecyclerView journeyRV;

    private ProgressBar loadingPB;

    private FirebaseAuth mAuth;
    //private ProgressBar loadingPB;

    private ArrayList<Model> modelArrayList;

    private JourneyRVAdapter journeyRVAdapter;

    private RelativeLayout homeRL;

    private String journeyID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton = findViewById(R.id.floatingActionButton);
        //initialize
        journeyRV = findViewById(R.id.idRVJourneys);

        loadingPB = findViewById(R.id.idPBLoading);

        homeRL = findViewById(R.id.idRLBSheet);
        //loadingPB
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseDatabase1 = FirebaseDatabase.getInstance();

        mAuth = FirebaseAuth.getInstance();

        modelArrayList = new ArrayList<>();

        //on below line we are getting database reference.
        databaseReference = firebaseDatabase.getReference("Journeys");
        databaseReference1 = firebaseDatabase1.getReference("Journeys");
        //reference for our storage db
        storageReference = FirebaseStorage.getInstance().getReference("images1");

        // on below line adding a click listener for our floating action button.
        FloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, AddActivity.class));
            }
        });

        // on below line initializing our adapter class.
        journeyRVAdapter = new JourneyRVAdapter(modelArrayList, this, this::onJourneyClick);
        //setting layout manager to recycler view on below line
        journeyRV.setLayoutManager(new LinearLayoutManager(this));
        // setting adapter to recycler view on below line.
        journeyRV.setAdapter(journeyRVAdapter);
        //on below line calling a method to fetch courses from database
        getJourneys();
    }

    private void getJourneys() {
        //on below line we are clearing our list
        modelArrayList.clear();
        //on below line we are calli g add child event listener methods to read the data
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // on below line we are hiding our progress bar.
                loadingPB.setVisibility(View.GONE);

                //then add snap shot to our array list on below line
                modelArrayList.add(snapshot.getValue(Model.class));
                //notify our adapter that data has changed
                journeyRVAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                //this method is called when new child is added
                // we are notifying our adapter and making progress bar
                // visibility as gone.
                loadingPB.setVisibility(View.GONE);
                journeyRVAdapter.notifyDataSetChanged();

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                // notifying our adapter when child is removed.
                journeyRVAdapter.notifyDataSetChanged();
                loadingPB.setVisibility(View.GONE);

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // notifying our adapter when child is moved.
                loadingPB.setVisibility(View.GONE);
                journeyRVAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), " " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }

        //
    private void onJourneyClick(int position) {
        // call a method to to open RVItem on new activity
        displayBottomSheet(modelArrayList.get(position));


    }

    public void displayBottomSheet (Model model){

        //on below line we are creating our bottom sheet dialog
        final BottomSheetDialog bottomSheetDialog1 = new BottomSheetDialog(this, R.style.BottomSheetDialogTheme);

        //on below line we are inflating our layout fulw for our bottom sheet
        View layout = LayoutInflater.from(this).inflate(R.layout.bottom_sheet_layout, homeRL);

        //setting content view for bottom sheet on below line
        bottomSheetDialog1.setContentView(layout);

        //setting content to bottom sheet
        bottomSheetDialog1.setCancelable(false);
        bottomSheetDialog1.setCanceledOnTouchOutside(true);

        //calling a method to display our bottom sheet
        bottomSheetDialog1.show();

        //initiating text and image view inside the bottom sheet
        // and initializing them with the IDs

        TextView journeyNameTV = layout.findViewById(R.id.idTVJourneyName);
        TextView journeyDescriptionTV = layout.findViewById(R.id.idTVJourneyDesc);
        TextView journeyLocationTV = layout.findViewById(R.id.idTVLocation);

        EditText journeyNameEdt = layout.findViewById(R.id.idEdtTitleName);
        EditText journeyDescriptionEdt = layout.findViewById(R.id.idEdtTextdescript);
        EditText journeyLocationEdt = layout.findViewById(R.id.idEdtViewLocation);

        if (model != null) {
            //set data to the views
            journeyNameTV.setText(model.getJourneyName());
            journeyDescriptionTV.setText(model.getJourneyDescription());
            journeyLocationTV.setText(model.getJourneyLocation());
        }
        //RBLSheet buttons
        TextView updateBtn = layout.findViewById(R.id.idBtnEditJourney);
        TextView saveBtn = layout.findViewById(R.id.IDbtnSave);
        TextView deleteBtn = layout.findViewById(R.id.idBtnDeleteDetails);
        TextView shareBtn = layout.findViewById(R.id.idBtnShare);

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Fill and hit save to update changes ", Toast.LENGTH_SHORT).show();
                //hiding the text views in order
                //to display the edit text views
                journeyNameTV.setVisibility(View.GONE);
                journeyDescriptionTV.setVisibility(View.GONE);
                journeyLocationTV.setVisibility(View.GONE);

                //showing the edit text input
                journeyNameEdt.setVisibility(View.VISIBLE);
                journeyDescriptionEdt.setVisibility(View.VISIBLE);
                journeyLocationEdt.setVisibility(View.VISIBLE);

                saveBtn.setVisibility(View.VISIBLE);
                updateBtn.setVisibility(View.GONE);

                if (model != null) {
                    //set existing data to the edit texts
                    journeyNameEdt.setText(model.getJourneyName());
                    journeyDescriptionEdt.setText(model.getJourneyDescription());
                    journeyLocationEdt.setText(model.getJourneyLocation());
                    journeyID = model.getJourneyId();
                }
            }
        });
        //reference
        //databaseReference = firebaseDatabase.getReference("Journeys").child(journeyID);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // show our loading PB once save button is hit
                loadingPB.setVisibility(View.VISIBLE);

                    //get data from our edit text views
                    String journeyName = journeyNameEdt.getText().toString();
                    String journeyDescription = journeyDescriptionEdt.getText().toString();
                    String journeyLocation = journeyLocationEdt.getText().toString();

                    //on below line we are creating
                    // passing a data using key and value pair
                    Map<String, Object> map = new HashMap<>();
                    map.put("journeyName", journeyName);
                    map.put("journeyDescription", journeyDescription);
                    map.put("journeyLocation", journeyLocation);
                    map.put("journeyId", journeyID);

                    //Toast.makeText(getApplicationContext(), "saved", Toast.LENGTH_SHORT).show();

                    databaseReference1.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_SHORT).show();
                            //databaseReference1.updateChildren(map);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //call delete method
                deleteJourney();
            }
        });

        //sharing recycler view item data with other apps
        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //share content
                Intent sendIntent1 = new Intent(Intent.ACTION_SEND);
                //saving it all in one intent
                sendIntent1.putExtra(Intent.EXTRA_TEXT, "Title:"+model.getJourneyName()+"\n" +
                                                               "Description:"+model.getJourneyDescription()+"\n"+
                                                                "Location:"+model.getJourneyLocation() );


                sendIntent1.setType("text/plain");
                //startActivity(sendIntent1);

                    // Show the Sharesheet this cannot get data directly from R.string folder
                startActivity(Intent.createChooser(sendIntent1, "Share with other apps"));

            }
        });

    }

    private void deleteJourney() {
        // on below line calling a method to delete the course
        databaseReference.removeValue();
        // displaying a toast message on below line.
        Toast.makeText(this, "Deleted..", Toast.LENGTH_SHORT).show();

        startActivity(new Intent(MainActivity.this, MainActivity.class));
    }

    //creating my menu in the action bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        //on below line we are inflating our menu
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    //this class add theme chage and logout logic to the menu items
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        //adding a click listener for an option selected on our menu
        int id = item.getItemId();
        switch (id) {
            case R.id.idLogOut:
                // show toast to user
                Toast.makeText(getApplicationContext(),R.string.log_out, Toast.LENGTH_SHORT).show();
                //signing out the user with the firebase authenticator
                mAuth.signOut();
                //on below line we are opening login activity
                Intent i = new Intent(MainActivity.this, Login.class);
                startActivity(i);
                this.finish();
                return true;

            case R.id.theme1:
                // show toast to user
                Toast.makeText(getApplicationContext(),R.string.theme_changed, Toast.LENGTH_SHORT).show();

                default:
                return super.onOptionsItemSelected(item);
        }
    }



}















