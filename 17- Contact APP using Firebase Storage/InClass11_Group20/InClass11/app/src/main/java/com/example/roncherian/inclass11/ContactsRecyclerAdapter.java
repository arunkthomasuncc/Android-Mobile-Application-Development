package com.example.roncherian.inclass11;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by roncherian on 20/11/17.
 */

public class ContactsRecyclerAdapter extends RecyclerView.Adapter<ContactsRecyclerAdapter.ViewHolder>{

    OnClick onClick;

    static ArrayList<Contact> mData;
    static String userId;

    ContactActivity currentContext;
    // Context context;

    static MainActivity mainActivity;

    public ContactsRecyclerAdapter(ArrayList<Contact> mData, OnClick context, String loggedInUserId, ContactActivity mContext) {
        this.mData = mData;
        this.onClick = context;
        this.userId = loggedInUserId;
        this.currentContext = mContext;
    }


    @Override
    public ContactsRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_list_layout, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ContactsRecyclerAdapter.ViewHolder holder, int position) {


        Contact contactObj = mData.get(position);
        holder.contact = contactObj;
        holder.name.setText(contactObj.getFirstName()+" "+contactObj.getLastName());
        holder.phone.setText(contactObj.getPhone());
        holder.email.setText(contactObj.getEmail());
        holder.position = position;
        Picasso.with(this.currentContext).load(contactObj.getImageUrl()).into(holder.contactPhoto);

    }

    @Override
    public int getItemCount() {

        return mData.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView email;
        TextView phone;
        ImageView contactPhoto;
        ImageButton deleteContact, editContact;


        int position;
        Contact contact;

        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView)itemView.findViewById(R.id.textViewNameValue);
            email = (TextView)itemView.findViewById(R.id.textViewEmailValue);
            phone = (TextView)itemView.findViewById(R.id.textViewPhoneValue);
            contactPhoto = (ImageView)itemView.findViewById(R.id.imageViewContactPhoto);
            editContact = (ImageButton)itemView.findViewById(R.id.imageButtoEditContactProfile);
            deleteContact = (ImageButton)itemView.findViewById(R.id.imageButtonDeleteContactProfile);

            editContact.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("demo","Clicked on edit contact");
                    if (onClick!=null){
                        onClick.onEditContactButtonClicked(position,contact);
                    }
                }
            });

            deleteContact.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("demo","Clicked on delete contact");
                    if (onClick!=null){
                        onClick.OnDeleteItemClicked(position,contact);
                    }
                }
            });


        }
    }

    public interface OnClick{
        public void onEditContactButtonClicked(int position, Contact contact);
        public void OnDeleteItemClicked(int position, Contact contact);
    }



}