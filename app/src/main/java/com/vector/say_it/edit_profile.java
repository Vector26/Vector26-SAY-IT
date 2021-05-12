package com.vector.say_it;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
//import com.github.dhaval2404.imagepicker.ImagePicker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link edit_profile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class edit_profile extends Fragment {


    SharedPreferences sharedPreferences;
    byte[] imageBytes;
    View v;
    Button PS;
    int flag;
    EditText email,bio;
    Uri fileuri;
    ImageView imageView;
    public Bitmap bitmap;
    JSONObject data2;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public edit_profile() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment edit_profile.
     */
    // TODO: Rename and change types and number of parameters
    public static edit_profile newInstance(String param1, String param2) {
        edit_profile fragment = new edit_profile();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        sharedPreferences = getActivity().getSharedPreferences("com.vector.say_it", Context.MODE_PRIVATE);
    }

    public void galleryIntent()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"),100);
    }

    public void init(){
        imageView=v.findViewById(R.id.editPP);
        String uri = null;
        PS=v.findViewById(R.id.profile_save);
        email=v.findViewById(R.id.emailEdit);
        bio=v.findViewById(R.id.bioEdit);
        try {
            data2=new JSONObject(sharedPreferences.getString("Profile", ""));
            email.setText(data2.getJSONObject("Profile").getJSONObject("user").getString("email"));
            bio.setText(data2.getJSONObject("Profile").getString("Bio"));
            uri = getActivity().getString(R.string.BASE_URL)+data2.getJSONObject("Profile").getString("image");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        finally {
//            Log.i("url",uri+"Hello");
        Glide.with(getActivity())
                .load(uri)
                .placeholder(R.drawable.default_pp_shape)
                .circleCrop()
                .into(imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("msg","b4");
                galleryIntent();

                Log.i("msg","after");
                }
            });
        PS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String,String> params=new HashMap<>();
                params.put("email",email.getText().toString());
                params.put("bio",bio.getText().toString());
                RequestHandler req = new RequestHandler(false, getString(R.string.BASE_URL)+"CMS-API/", Request.Method.POST,new JSONArray().put(new JSONObject(params)), getActivity(),sharedPreferences){
                    @Override
                    public void callback(JSONObject response) {
                        super.callback(response);
                        Toast.makeText(getContext(),"Profile Updated",Toast.LENGTH_LONG).show();
                        getActivity().onBackPressed();
                    }

                    @Override
                    public void callbackError(VolleyError e) {
                        super.callbackError(e);
                        Toast.makeText(getContext(),"Something Went Wrong",Toast.LENGTH_LONG).show();
                    }
                };
            }
        });
        }
    }



    @SuppressWarnings("deprecation")
    public void onSelectFromGalleryResult(Bitmap bm) {
        Log.i("Msg","Here3");
//        bitmap = new Bitmap();
        if (bm != null) {
            //                bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), data.getData());
            bitmap = bm;
        }

//        imageView.setImageBitmap(bitmap);
        Glide
                .with(getContext())
                .load(bitmap)
                .circleCrop() // this cropping technique scales the image so that it fills the requested bounds and then crops the extra.
                .into(imageView);
        String img=encodeTobase64(bitmap);
        HashMap<String,String> params=new HashMap<>();
        params.put("profile_pic",img);
        RequestHandler req = new RequestHandler(false, getString(R.string.BASE_URL)+"CMS-API/", Request.Method.POST,new JSONArray().put(new JSONObject(params)), getActivity(),sharedPreferences) {

            @Override
            public void callbackError(VolleyError e) {
                super.callbackError(e);
//                Log.i("msg",new JSONObject(params).toString());
                Toast.makeText(getContext(),"Profile Update Unsuccessfull",Toast.LENGTH_LONG).show();
            }

            @Override
            public void callback(JSONObject response) {
                super.callback(response);
                Log.i("msg","MI hereeeee");
                Toast.makeText(getActivity(),"Profile Updated Successfully",Toast.LENGTH_LONG).show();
            }
        };


//        Log.v("TAG","base64"+img);//checking in log base64 string to image cropeed image found

    }
    public String encodeTobase64(Bitmap image) {
//        Bitmap immagex=Bitmap.createScaledBitmap(image, imageView.getWidth(), imageView.getHeight(), true);
        Bitmap immagex=image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.JPEG  , 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b,Base64.DEFAULT);
//        Log.v("msg",imageEncoded);
        return imageEncoded;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.i("Msg","Here");
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), data.getData());
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (resultCode == Activity.RESULT_OK && flag==0){
            flag=1;
            doCrop(getImageUri(getContext(),bitmap));
            Log.i("Msg","Here2");
//            onSelectFromGalleryResult(data);
        }
        if (requestCode == 1 && flag==1) {
            if (data != null) {
                Bundle extras = data.getExtras();
                Bitmap bitmap= extras.getParcelable("data");
                onSelectFromGalleryResult(bitmap);
                flag=0;
            }
        }
    }

    private void doCrop(Uri picUri) {
        try {

            Intent cropIntent = new Intent("com.android.camera.action.CROP");

            cropIntent.setDataAndType(picUri, "image/*");
            cropIntent.putExtra("crop", "true");
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            cropIntent.putExtra("outputX", 128);
            cropIntent.putExtra("outputY", 128);
            cropIntent.putExtra("return-data", true);
            startActivityForResult(cropIntent, 1);
        }
        // respond to users whose devices do not support the crop action
        catch (ActivityNotFoundException anfe) {
            // display an error message
            String errorMessage = "Whoops - your device doesn't support the crop action!";
            Toast toast = Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v=inflater.inflate(R.layout.fragment_edit_profile, container, false);
        flag=0;
        init();
        // Inflate the layout for this fragment
        return v;
    }
}