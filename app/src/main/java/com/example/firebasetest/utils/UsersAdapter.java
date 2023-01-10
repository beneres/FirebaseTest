package com.example.firebasetest.utils;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.firebasetest.R;
import com.example.firebasetest.activities.ShowUserActivity;
import com.example.firebasetest.activities.User;


import java.util.List;


public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.CommentViewHolder>
{

    private Context context;
    public static User currentUser=new  User();
    private List<User> userList;
    private User userMe;

    public UsersAdapter(Context context, List<User> users) {

        this. userList = users;
        this.context=context;



    }



    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new CommentViewHolder(LayoutInflater.from(context).inflate(R.layout.item_user, parent, false));






    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {

        User user = userList.get(position);

        holder.userName.setText(user.getName());
        holder.score.setText("N/A");

        //holder.tvRank.setText();


        holder.itemView.setOnClickListener(v ->
        {
            currentUser=user;
            context.startActivity(new Intent(context, ShowUserActivity.class));
        });

    }


    @Override
  //  public int getItemCount() {
    public int getItemCount() {
        return userList.size();
    }

    class CommentViewHolder extends RecyclerView.ViewHolder {

        ImageView imgUser;
        TextView userName ,tvRank,score;

        CommentViewHolder(@NonNull View itemView) {
            super(itemView);


            userName = itemView.findViewById(R.id.tv_user_name);
            imgUser = itemView.findViewById(R.id.img_profile);
            tvRank = itemView.findViewById(R.id.tv_rank);
            score = itemView.findViewById(R.id.tv_score);




        }

    }



    public interface EditTextDialogListener {
        void onOk(String text);
    }

}
