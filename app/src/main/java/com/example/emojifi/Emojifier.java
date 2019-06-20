package com.example.emojifi;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.util.SparseArray;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;


public class Emojifier {

    private static final String LOG_TAG = Emojifier.class.getSimpleName();

    //this method detects and logs the number of faces in a given bitmap.
    static void detectFaces(Context context, Bitmap pictureBitmap){
        // Create the face detector, disable tracking and enable classifications
        FaceDetector detector = new FaceDetector.Builder(context)
                .setTrackingEnabled(false)
                .setClassificationType(FaceDetector.ALL_CLASSIFICATIONS)
                .build();

        // Build the frame
        Frame frame = new Frame.Builder().setBitmap(pictureBitmap).build();

        // Detect the faces
        SparseArray<Face> faces = detector.detect(frame);

        // Log the number of faces
        Log.d(LOG_TAG, "detectFaces: number of faces = " + faces.size());

        // If there are no faces detected, show a Toast message
        if(faces.size() == 0){
            Toast.makeText(context, R.string.no_faces_message, Toast.LENGTH_SHORT).show();
        } else {
            for( int i =0; i < faces.size(); i++) {
                Face face = faces.valueAt(i);
                //log classification probabilities for each face in the image
                getClassifications(face);
            }
        }

        // Release the detector
        detector.release();
    }

    //We have already enabled the classifications feature while creating a faceDetector object.
    //This method logs the classification probabilities of each eye being open and that the person is smiling
    private static void getClassifications(Face face) {
        //logging al possible probabilities
        Log.d(LOG_TAG, "getClassifications: smilingProb = " + face.getIsSmilingProbability());
        Log.d(LOG_TAG, "getClassifications: leftEyeOpenProb = "
                + face.getIsLeftEyeOpenProbability());
        Log.d(LOG_TAG, "getClassifications: rightEyeOpenProb = "
                + face.getIsRightEyeOpenProbability());
    }


}
