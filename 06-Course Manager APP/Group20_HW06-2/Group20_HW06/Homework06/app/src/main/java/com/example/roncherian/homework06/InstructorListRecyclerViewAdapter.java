package com.example.roncherian.homework06;

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
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Created by roncherian on 03/11/17.
 */

public class InstructorListRecyclerViewAdapter extends RecyclerView.Adapter<InstructorListRecyclerViewAdapter.ViewHolder>  {



    static InstructorListRecyclerViewAdapter.ResultsInterface mNavigate;

    private static RadioButton lastChecked = null;
    private static int lastCheckedPos = 0;

    ArrayList<InstructorObj> mData = new ArrayList<>();
    static Context context;
    static boolean inViewMode = false;

    private static boolean displayInstructor = true;
    public InstructorListRecyclerViewAdapter(Context context, InstructorListRecyclerViewAdapter.ResultsInterface resultsInterface, final boolean isViewMode, InstructorObj instructorObj, boolean displayInstr) {

        this.inViewMode = isViewMode;
        lastChecked = null;
        this.context=context;
        this.mNavigate = resultsInterface;
        this.displayInstructor = displayInstr;
        if (inViewMode && instructorObj!=null){
            mData.add(instructorObj);
            notifyDataSetChanged();
        } else {

            Realm realm = Realm.getDefaultInstance();

            realm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {


                    RealmResults<Instructor>data = realm.where(Instructor.class).equalTo("username",MainActivity.LOGGED_IN_USERNAME).findAll();

                    for (Instructor instructor: data) {
                        InstructorObj instructorObj = new InstructorObj();
                        instructorObj.setEmail(instructor.getEmail());
                        instructorObj.setFirstName(instructor.getFirstName());
                        instructorObj.setId(instructor.getId());
                        instructorObj.setLastName(instructor.getLastName());
                        instructorObj.setImageArray(instructor.getImageArray());
                        if (inViewMode){
                            instructorObj.setSelected(instructor.isSelected());
                        }else {
                            instructorObj.setSelected(false);
                        }
                        boolean addIns = true;
                        instructorObj.setWebsite(instructor.getWebsite());

                        mData.add(instructorObj);
                    }
                }
            }, new Realm.Transaction.OnSuccess() {
                @Override
                public void onSuccess() {
                    notifyDataSetChanged();
                }
            });
        }



        //this.mData = mData;

    }
    @Override
    public InstructorListRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v =  LayoutInflater.from(parent.getContext()).inflate(R.layout.instructor_list_layout, parent, false);
        // set the view's size, margins, paddings and layout parameters

        InstructorListRecyclerViewAdapter.ViewHolder vh = new InstructorListRecyclerViewAdapter.ViewHolder(v);
        return vh;

    }

    @Override
    public void onBindViewHolder(InstructorListRecyclerViewAdapter.ViewHolder holder, final int position) {

        if (inViewMode){
            holder.instructorSelected.setEnabled(false);
        }
        if (null == mData || mData.size() == 0 || displayInstructor==false){
            holder.isCourseEmpty = true;
            holder.textViewInstructorName.setVisibility(View.VISIBLE);
            holder.textViewInstructorName.setText("You haven’t added any instructor yet, please add at least one instructor to continue.");
            holder.imageViewInstructor.setVisibility(View.INVISIBLE);
            holder.imageViewInstructor.getLayoutParams().height = 25;
            holder.instructorSelected.setVisibility(View.INVISIBLE);
        } else {

            InstructorObj instructor=mData.get(position);
            holder.instructor=mData.get(position);
            holder.textViewInstructorName.setText(instructor.getFirstName()+" "+instructor.getLastName()+"");
            holder.imageViewInstructor.getLayoutParams().height = 200;
            holder.imageViewInstructor.getLayoutParams().width = 200;
            holder.isCourseEmpty = false;


            holder.textViewInstructorName.setVisibility(View.VISIBLE);
            holder.imageViewInstructor.setVisibility(View.VISIBLE);
            holder.instructorSelected.setVisibility(View.VISIBLE);
            holder.instructorSelected.setChecked(mData.get(position).isSelected());
            holder.instructorSelected.setTag(new Integer(position));

            if (holder.instructor!=null && holder.instructor.getImageArray()!=null){
                BitmapFactory.Options options = new BitmapFactory.Options();
                Bitmap bitmap = BitmapFactory.decodeByteArray(holder.instructor.getImageArray(), 0, holder.instructor.getImageArray().length, options);
                holder.imageViewInstructor.setImageBitmap(bitmap);
            }


            holder.instructorSelected.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    RadioButton cb = (RadioButton)v;
                    int clickedPos = ((Integer)cb.getTag()).intValue();

                    if(cb.isChecked())
                    {
                        if(lastChecked != null)
                        {
                            lastChecked.setChecked(false);
                        }

                        lastChecked = cb;
                        lastCheckedPos = clickedPos;
                    }
                    else
                        lastChecked = null;

                    mData.get(clickedPos).setSelected(cb.isChecked());
                    if (mNavigate!=null){
                        mNavigate.itemClicked(mData.get(clickedPos).getId(), cb.isChecked());
                    }

                }
            });

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
        RadioButton instructorSelected;
        InstructorObj instructor;

        boolean isCourseEmpty;

        public ViewHolder(View itemView) {
            super(itemView);

            textViewInstructorName=(TextView)itemView.findViewById(R.id.textViewInstructorName);
            imageViewInstructor=(ImageView)itemView.findViewById(R.id.imageViewInstructorPhoto);
            instructorSelected = itemView.findViewById(R.id.checkBox);


        }

    }

    public interface ResultsInterface {
        void navigateIntent(String url);
        void itemClicked(String instructorId, boolean isSelected);
    }
}
