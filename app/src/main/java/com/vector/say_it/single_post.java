package com.vector.say_it;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.bumptech.glide.Glide;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link single_post#newInstance} factory method to
 * create an instance of this fragment.
 */
public class single_post extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    View v;
    TextView username,content,likes,comments,date;
    ImageView profile_pic;
    String url,uri;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    commentsVeiw comments_veiw;
    JSONArray commentArray;
    Button like,comment;
    public int id,post_id,isLiked;
    SharedPreferences sharedPreferences;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public single_post() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment single_post.
     */
    // TODO: Rename and change types and number of parameters
    public static single_post newInstance(String param1, String param2) {
        single_post fragment = new single_post();
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
    }

    public void refreshpost(){
        url=getString(R.string.BASE_URL)+"CMS-API/Feed?id="+post_id;
        RequestHandler req=new RequestHandler(false,url, Request.Method.GET,null,getActivity(),sharedPreferences){
            @Override
            public void callback(JSONObject response) {
                try {
                    post_id = (int) response.getInt("id");
                    likes.setText(response.getInt("likes")+"");
//            holder.likes.setText("Hello");
                    isLiked= response.getInt("isLiked");
                    if( isLiked==1){
                         like.setBackground(context.getDrawable(R.drawable.ic_heart));
                    }
                    comments.setText( response.getJSONArray("comments").length()+"");
                    content.setText( response.getString("content"));
                    commentArray=response.getJSONArray("comments");
                    comments_veiw.setFeed(commentArray);
                    comments_veiw.notifyDataSetChanged();
                    String[] Date= response.getString("date_posted").split("T");
                    String[] Time=Date[1].split(":");
                     date.setText(Time[0]+":"+Time[1]+"   "+Date[0]);
                     like.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            YoYo.with(Techniques.BounceIn)
                                    .duration(400)
                                    .playOn(v);
                        }
                    });
                     id = (int)  response.getJSONObject("profile").getInt("id");
//             User.setText("id->>"+ id+"  "+ post_id+"<<-post_id");
                     username.setText( response.getJSONObject("profile").getJSONObject("user").getString("username"));
                    uri= response.getJSONObject("profile").getString("image");
                    Log.i("PP",uri);
                    Glide.with(context)
                            .load(uri)
                            .placeholder(R.drawable.default_pp_shape)
                            .circleCrop()
                            .into(profile_pic);
                }
                catch (JSONException e){
                    e.getStackTrace();
                    Log.i("Msg","Here");
                }
            }
        };
    }

    public void init(){
        sharedPreferences = getActivity().getSharedPreferences("com.vector.say_it", Context.MODE_PRIVATE);
        post_id=Integer.parseInt(getActivity().getIntent().getStringExtra("post_id"));
        username=v.findViewById(R.id.username);
        content=v.findViewById(R.id.bio);
        likes=v.findViewById(R.id.likes);
        comments=v.findViewById(R.id.comments);
        profile_pic=v.findViewById(R.id.profile_pic);
        date=v.findViewById(R.id.email);
        like=v.findViewById(R.id.Like);
        comment=v.findViewById(R.id.Comment);
        recyclerView= v.findViewById(R.id.commentList);
        layoutManager = new LinearLayoutManager(getActivity());
        commentArray=new JSONArray();
        comments_veiw=new commentsVeiw(commentArray,getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(comments_veiw);
        refreshpost();
    }

//    public void like(Context context,JSONObject js){
//        Log.i("Liked","Liked");
//        String url=context.getString(R.string.BASE_URL)+"/CMS-API/Feed/like";
//        JSONObject postdata=new JSONObject();
//        JSONArray ja=new JSONArray();
//        try{
//            postdata.put("id",post_id+"");
//            ja.put(postdata);
//        }
//        catch (JSONException e){
//            e.getStackTrace();
//        }
//        RequestHandler req=new RequestHandler(false,url, Request.Method.POST,ja,context,sharedPreferences){
//            @Override
//            public void callback(JSONObject response) {
//                int s;
//                Log.i("Orig",response.toString());
//                Log.i("DataVar",response.toString());
//                s=Integer.parseInt(likes.getText().toString())+1;
//                Log.i("S",s+"");
//                likes.setText(s+"");
//                try{
////                        Log.i("Action","Actually Liked");
//                    if(response.getString("Message").toString().equals("Liked")){
//                        like.setBackground(context.getDrawable(R.drawable.ic_heart));
//                        Log.i("Action","Actually Liked");
//                        Toast.makeText(context,"Liked",Toast.LENGTH_SHORT);
//                    }
//                    else if(response.getString("Message").toString().equals("Disliked")){
//                        s=Integer.parseInt(likes.getText().toString())-2;
//                        likes.setText(s+"");
//                        Log.i("S",s+"");
//                        like.setBackground(context.getDrawable(R.drawable.ic_heartdislike));
//                        Log.i("Action","Actually DisLiked");
////                            Toast.makeText(context,"Diliked",Toast.LENGTH_SHORT);
//                    }
//                    else{
//                        Log.i("Action",response.getString("Message"));
//                    }
//                }
//                catch (JSONException e){
//                    Toast.makeText(context,"Liked",Toast.LENGTH_SHORT);
//                    s = Integer.parseInt(likes.getText().toString())-1;
//                    likes.setText(s+"");
//                    e.getStackTrace();
//                }
//            }
//        };
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v=inflater.inflate(R.layout.fragment_single_post, container, false);
        init();
        return v;
    }
}