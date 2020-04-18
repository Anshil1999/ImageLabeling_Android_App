package com.example.imagelabeling;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabel;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabeler;

import java.util.List;

public class MainActivity extends AppCompatActivity
{

    ImageView img;
    Button click,detect;
    private int camera_request=100;
    Bitmap ima_rec;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        img=findViewById(R.id.img);
        click=findViewById(R.id.click);
        detect=findViewById(R.id.detect);



        click.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {


                Intent i=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(i,camera_request);


            }
        });

        detect.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                detectLable();
            }
        });
    }
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==camera_request)

        {
            ima_rec=(Bitmap) data.getExtras().get("data");
            img.setImageBitmap(ima_rec);
        }
    }
void detectLable()
{
    FirebaseVisionImage image=FirebaseVisionImage.fromBitmap(ima_rec);
    FirebaseVisionImageLabeler labeler= FirebaseVision.getInstance().getOnDeviceImageLabeler();

    labeler.processImage(image).addOnCompleteListener(new OnCompleteListener<List<FirebaseVisionImageLabel>>()
    {
        @Override
        public void onComplete(@NonNull Task<List<FirebaseVisionImageLabel>> task)
        {

            String outD="";

            for (FirebaseVisionImageLabel labelId:task.getResult())
             {
                 outD+= (labelId.getText())+"\t"+labelId.getConfidence()+"\n";
             }
            Toast.makeText(MainActivity.this, ""+outD, Toast.LENGTH_SHORT).show();
        
        
        }
    }).addOnFailureListener(new OnFailureListener()
        {
            @Override
            public void onFailure(@NonNull Exception e)
            {
            }
        });

}



}