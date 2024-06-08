package com.yatsotechs.journaljourneyv7;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class AddActivity extends AppCompatActivity {

    //declarations
    private EditText journeyNameEdt, journeyDescriptionEdt, journeyLocationEdt;
    private Button  btnbrowse;

    private FloatingActionButton sendDatabtn;

    ProgressBar progressBar;

    //for processing image data

    private ImageView imgView;
    private Uri FilePathUri;
    StorageReference storageReference;

    int Image_Request_Code = 7;

    private String journeyId;

    //create variable for firebase database
    FirebaseDatabase firebaseDatabase;

    //create database reference
    DatabaseReference databaseReference;

    //create a variable t=for our object class
    Model model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        getSupportActionBar().hide();

        //initializing our edt and btn
        journeyNameEdt = findViewById(R.id.idEdtJourneyName);
        journeyDescriptionEdt = findViewById(R.id.idEdtJourneyDescription);
        journeyLocationEdt = findViewById(R.id.idEdtJourneyLocation);

        // for myn image view
        imgView = findViewById(R.id.image_view);

        //get instance of our firebase database
        firebaseDatabase = FirebaseDatabase.getInstance();

        //reference for our storage db
        storageReference = FirebaseStorage.getInstance().getReference("images1");

        //get reference of our database
        databaseReference = firebaseDatabase.getReference("Journeys");

        //initializing our object
        //class variable
        model = new Model();

        sendDatabtn = findViewById(R.id.idBtnSendData);

        btnbrowse = findViewById(R.id.btnbrowse);

        //browse imagery
        btnbrowse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Image"), Image_Request_Code);

            }
        });

        //adding an onClick listener for sending data
        sendDatabtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(FilePathUri != null & journeyNameEdt !=null & journeyDescriptionEdt != null & journeyLocationEdt != null){
                String journey = journeyNameEdt.getText().toString();
                String description = journeyDescriptionEdt.getText().toString();
                String location = journeyLocationEdt.getText().toString();
                String url = FilePathUri.getPath();
                journeyId = journey;

                //below line is for checking weather the edittext files are empty or not
                Model model = new Model(journeyId, journey, description, location,url);

                    startActivity(new Intent(AddActivity.this, MainActivity.class));
                addDataFirebase(journey, description, location);

                UploadImage();}
                else{
                    Toast.makeText(getApplicationContext(), "Please make selection or input text", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Image_Request_Code && resultCode == RESULT_OK && data != null && data.getData() != null){


            imgView.setVisibility(View.VISIBLE);

            FilePathUri = data.getData();

            try{
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), FilePathUri);
                imgView.setImageBitmap(bitmap);
            }
            catch (IOException e){
                e.printStackTrace();
            }

        }
    }

    public String GetFileExtension (Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }


    public void UploadImage(){

        if (FilePathUri != null) {
            StorageReference storageReference1 = storageReference.child(System.currentTimeMillis()+ "."+ GetFileExtension(FilePathUri));
            storageReference1.putFile(FilePathUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Toast.makeText(getApplicationContext(), "Image uploaded", Toast.LENGTH_SHORT).show();


                }
            });
        }else{
            Toast.makeText(AddActivity.this, "Please Select Image or Add Image Name", Toast.LENGTH_SHORT).show();
        }
    }




    private void addDataFirebase (String journey, String description, String location){
        // these below lines are used to set data in our object class
        model.setJourneyName(journey);
        model.setJourneyDescription(description);
        model.setJourneyLocation(location);

        //we are to use an add value event listener method
        //this is called with database reference
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //inside here we are setting our object class to our database reference
                //database reference will send data to firebase

                databaseReference.child(journeyId).setValue(model);

                //databaseReference.setValue(model);

                //if successful show toast
                Toast.makeText(AddActivity.this, "data added", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //if failed show toast
                Toast.makeText(AddActivity.this, "Failed to add data"+ error, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
