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

    //approx threshold values for smiling/sad and eyes open/closed
    private static final double SMILING_PROB_THRESHOLD = .18;
    private static final double EYE_OPEN_PROB_THRESHOLD = .5;

    private static final float EMOJI_SCALE_FACTOR = .9f;

    //this method detects and logs the number of faces in a given bitmap and draws emoji based on facial expression
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

        //initialize the result bitmap to original picture
        Bitmap resultBitmap = pictureBitmap;

        // If there are no faces detected, show a Toast message
        if(faces.size() == 0){
            Toast.makeText(context, R.string.no_faces_message, Toast.LENGTH_SHORT).show();
        } else {
            //iterate through faces
            for( int i =0; i < faces.size(); i++) {
                Face face = faces.valueAt(i);
//                //log classification probabilities for each face in the image
//                getClassifications(face);
                //get the appropriate emoji for each face
                identifyEmoji(face);
            }
        }

        // Release the detector
        detector.release();
    }

    // Enum for all possible Emojis
    private enum Emoji {
        SMILE,
        FROWN,
        LEFT_WINK,
        RIGHT_WINK,
        LEFT_WINK_FROWN,
        RIGHT_WINK_FROWN,
        CLOSED_EYE_SMILE,
        CLOSED_EYE_FROWN
    }

    //determine the closest emoji to the expression on the face,
    //based on the person's lips; smile/sad and eyes open/closed.
    private static void identifyEmoji(Face face) {
        Log.d(LOG_TAG, "whichEmoji: smilingProb = " + face.getIsSmilingProbability());
        Log.d(LOG_TAG, "whichEmoji: leftEyeOpenProb = " + face.getIsLeftEyeOpenProbability());
        Log.d(LOG_TAG, "whichEmoji: rightEyeOpenProb = " + face.getIsRightEyeOpenProbability());


        boolean smiling = face.getIsSmilingProbability() > SMILING_PROB_THRESHOLD;
        boolean leftEyeClosed = face.getIsLeftEyeOpenProbability() < EYE_OPEN_PROB_THRESHOLD;
        boolean rightEyeClosed = face.getIsRightEyeOpenProbability() < EYE_OPEN_PROB_THRESHOLD;

        //Determine and log the appropriate emoji
        Emoji emoji;
        if(smiling) {
            if(leftEyeClosed && !rightEyeClosed) {
                emoji = Emoji.LEFT_WINK;
            } else if(rightEyeClosed && !leftEyeClosed){
                emoji = Emoji.RIGHT_WINK;
            } else if (leftEyeClosed){
                emoji = Emoji.CLOSED_EYE_SMILE;
            } else {
                emoji = Emoji.SMILE;
            }
        } else {
            if (leftEyeClosed && !rightEyeClosed) {
                emoji = Emoji.LEFT_WINK_FROWN;
            }  else if(rightEyeClosed && !leftEyeClosed){
                emoji = Emoji.RIGHT_WINK_FROWN;
            } else if (leftEyeClosed){
                emoji = Emoji.CLOSED_EYE_FROWN;
            } else {
                emoji = Emoji.FROWN;
            }
        }

        //Log the emoji
        Log.d(LOG_TAG, "Which emoji ? "+emoji.name());

    }

    //We have already enabled the classifications feature while creating a faceDetector object.
    //This method logs the classification probabilities of each eye being open and that the person is smiling
//    private static void getClassifications(Face face) {
//        //logging al possible probabilities
//        Log.d(LOG_TAG, "getClassifications: smilingProb = " + face.getIsSmilingProbability());
//        Log.d(LOG_TAG, "getClassifications: leftEyeOpenProb = "
//                + face.getIsLeftEyeOpenProbability());
//        Log.d(LOG_TAG, "getClassifications: rightEyeOpenProb = "
//                + face.getIsRightEyeOpenProbability());
//    }


}
