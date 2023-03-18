package com.example.recipeapp.Screens;

import static com.example.recipeapp.Fragments.MyRecipeFragment.recipeArrayList;
import static com.example.recipeapp.Utils.Constant.getUserId;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.recipeapp.MainActivity;
import com.example.recipeapp.R;
import com.example.recipeapp.Utils.Constant;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.UUID;

public class EditRecipeActivity extends AppCompatActivity {
    ImageView recipe_img;
    Uri imgUri=null;
    StorageReference mRef;
    private Dialog loadingDialog;
    EditText description;
    String type;
    Spinner spinner;
    String[] types = { "Morning", "Lunch", "Dinner"};
    Button btnSaveRecord;
    ArrayAdapter aa;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_recipe);
        description=findViewById(R.id.description);
        recipe_img=findViewById(R.id.recipe_img);
        loadingDialog=new Dialog(this);
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        mRef= FirebaseStorage.getInstance().getReference("recipe_images");
        recipe_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addImage();
            }
        });
        btnSaveRecord=findViewById(R.id.button);
        btnSaveRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveRecord();
            }
        });
        spinner=findViewById(R.id.spinner);
         aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,types);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spinner.setAdapter(aa);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                type = parent.getItemAtPosition(position).toString();

            } // to close the onItemSelected
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

    }

    @Override
    protected void onStart() {

        description.setText(recipeArrayList.get(Constant.INDEX).getDescription());

        Picasso.with(this)
                .load(recipeArrayList.get(Constant.INDEX).getImage())
                .placeholder(R.drawable.progress_animation)
                .fit()
                .centerCrop()
                .into(recipe_img);

        if(recipeArrayList.get(Constant.INDEX).getType().equals("Morning")){
            spinner.setSelection(0);
        }else if(recipeArrayList.get(Constant.INDEX).getType().equals("Lunch")){
            spinner.setSelection(1);
        }
        else {
            spinner.setSelection(2);
        }

        super.onStart();
    }

    public  void addImage(){
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
                                openGallery();
                            }
                        }

                        if (report.isAnyPermissionPermanentlyDenied()) {
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }

                }).check();
    }

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    public void openGallery(){
        Intent intent=new Intent(Intent.ACTION_PICK,android.provider. MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,1);
    }
    public void saveRecord(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            if(description.getText().toString().isEmpty()){
                description.setError("required");
            }else {
                upload();
            }
        }
    }
    public void upload(){
        loadingDialog.show();
        if(imgUri==null){
            DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Recipes")
                    .child(getUserId(EditRecipeActivity.this)).child(recipeArrayList.get(Constant.INDEX).getId());
            myRef.child("Type").setValue(type);
            myRef.child("Description").setValue(description.getText().toString());
            loadingDialog.dismiss();
            Toast.makeText(EditRecipeActivity.this,"record update", Toast.LENGTH_LONG).show();
            finish();
        }else {


        StorageReference storageReference = mRef.child(System.currentTimeMillis() + "." + getFileEx(imgUri));
        storageReference.putFile(imgUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!urlTask.isSuccessful()) ;
                        Uri downloadUrl = urlTask.getResult();
                        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Recipes")
                                .child(getUserId(EditRecipeActivity.this)).child(recipeArrayList.get(Constant.INDEX).getId());
                        myRef.child("Type").setValue(type);
                        myRef.child("Description").setValue(description.getText().toString());
                        myRef.child("Image").setValue(downloadUrl.toString());
                        loadingDialog.dismiss();
                        Toast.makeText(EditRecipeActivity.this,"record update", Toast.LENGTH_LONG).show();
                        finish();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        loadingDialog.dismiss();
                        Toast.makeText(EditRecipeActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                    }
                });
        }

    }

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.dialog_permission_title));
        builder.setMessage(getString(R.string.dialog_permission_message));
        builder.setPositiveButton(getString(R.string.go_to_settings), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                openSettings();
            }
        });
        builder.setNegativeButton(getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
//
    }
    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package",EditRecipeActivity.this.getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && null != data) {
            imgUri  = data.getData();
            recipe_img.setImageURI(imgUri);

        }

    }

    // get the extension of file
    private String getFileEx(Uri uri){
        ContentResolver cr=EditRecipeActivity.this.getContentResolver();
        MimeTypeMap mime=MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }
}