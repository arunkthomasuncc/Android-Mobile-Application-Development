package com.example.roncherian.homework06;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by roncherian on 04/11/17.
 */

public class InstructorDetailListingRVAdapter extends RecyclerView.Adapter<InstructorDetailListingRVAdapter.ViewHolder>  {



    static InstructorDetailListingRVAdapter.ResultsInterface mNavigate;

    private static CheckBox lastChecked = null;
    private static int lastCheckedPos = 0;

    ArrayList<InstructorObj> mData = new ArrayList<>();
    static Context context;
    public InstructorDetailListingRVAdapter(Context context, InstructorDetailListingRVAdapter.ResultsInterface resultsInterface) {

        Realm realm = Realm.getDefaultInstance();

        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                RealmResults<Instructor> data = realm.where(Instructor.class).equalTo("username",MainActivity.LOGGED_IN_USERNAME).findAll();
                for (Instructor instructor: data) {
                    InstructorObj instructorObj = new InstructorObj();
                    instructorObj.setEmail(instructor.getEmail());
                    instructorObj.setFirstName(instructor.getFirstName());
                    instructorObj.setId(instructor.getId());
                    instructorObj.setLastName(instructor.getLastName());
                    instructorObj.setSelected(instructor.isSelected());
                    instructorObj.setWebsite(instructor.getWebsite());
                    instructorObj.setImageArray(instructor.getImageArray());
                    mData.add(instructorObj);

                }
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                notifyDataSetChanged();
            }
        });

        //this.mData = mData;
        this.context=context;
        this.mNavigate = resultsInterface;
    }
    @Override
    public InstructorDetailListingRVAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v =  LayoutInflater.from(parent.getContext()).inflate(R.layout.course_view_layout, parent, false);
        // set the view's size, margins, paddings and layout parameters

        InstructorDetailListingRVAdapter.ViewHolder vh = new InstructorDetailListingRVAdapter.ViewHolder(v);
        return vh;

    }

    @Override
    public void onBindViewHolder(InstructorDetailListingRVAdapter.ViewHolder holder, final int position) {

        holder.time.setVisibility(View.INVISIBLE);
        if (null == mData ||mData.size() == 0){
            holder.isCourseEmpty = true;
            holder.textViewInstructorName.setVisibility(View.INVISIBLE);
            holder.imageViewInstructor.setVisibility(View.INVISIBLE);
            holder.textViewInstructorEmail.setText("No Instructors Added");
        } else {
            InstructorObj instructor=mData.get(position);
            holder.instructor=mData.get(position);
            holder.textViewInstructorName.setText(instructor.getFirstName()+" "+instructor.getLastName()+"");
            holder.textViewInstructorEmail.setText(instructor.getEmail());
            holder.isCourseEmpty = false;
            holder.textViewInstructorName.setVisibility(View.VISIBLE);
            holder.imageViewInstructor.setVisibility(View.VISIBLE);
            if (holder.instructor!=null && holder.instructor.getImageArray()!=null){
                BitmapFactory.Options options = new BitmapFactory.Options();
                Bitmap bitmap = BitmapFactory.decodeByteArray(holder.instructor.getImageArray(), 0, holder.instructor.getImageArray().length, options);
                holder.imageViewInstructor.setImageBitmap(bitmap);
            }

            holder.position = position;
        }

    }

    @Override
    public int getItemCount() {
        if (null == mData || mData.size() == 0){
            return  1;
        } else {
            return mData.size();
        }

    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageViewInstructor;
        TextView textViewInstructorName;
        TextView textViewInstructorEmail;
        TextView time;
        InstructorObj instructor;
        int position;

        boolean isCourseEmpty;

        public ViewHolder(View itemView) {
            super(itemView);

            textViewInstructorName=(TextView)itemView.findViewById(R.id.textViewCourseTitle);
            imageViewInstructor=(ImageView)itemView.findViewById(R.id.imageViewInstructorImage);
            textViewInstructorEmail = (TextView) itemView.findViewById(R.id.textViewInstructor);
            time = (TextView)itemView.findViewById(R.id.textViewTime);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mNavigate!=null && instructor!=null){
                        mNavigate.instructorItemClicked(instructor.getId());
                    }
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    Log.d("demo","long clickedd");
                    if (mNavigate!=null && instructor!=null){
                        mNavigate.instructorItemLongClicked(instructor.getId());
                    }
                    return true;
                }
            });
        }

    }

    public interface ResultsInterface {
        void instructorItemLongClicked(String instructorId);
        void instructorItemClicked(String instructorId);
    }
}
