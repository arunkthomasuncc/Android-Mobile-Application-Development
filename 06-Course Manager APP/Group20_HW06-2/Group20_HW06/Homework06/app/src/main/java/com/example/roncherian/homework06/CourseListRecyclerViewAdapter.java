package com.example.roncherian.homework06;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.sql.Array;
import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmList;

/**
 * Created by roncherian on 03/11/17.
 */

public class CourseListRecyclerViewAdapter extends RecyclerView.Adapter<CourseListRecyclerViewAdapter.ViewHolder> {



    static ResultsInterface mNavigate;

    ArrayList<CourseObj> mData;
    static Context context;
    public CourseListRecyclerViewAdapter(Context context, ResultsInterface resultsInterface) {

        Realm realm = Realm.getDefaultInstance();
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                User user = realm.where(User.class).equalTo("username", MainActivity.LOGGED_IN_USERNAME).findFirst();

                mData = new ArrayList<>();
                for (Courses course: user.getCourses()) {
                    CourseObj courseObj = new CourseObj();
                    courseObj.setAmPm(course.getAmPm());
                    courseObj.setCourseTitle(course.getCourseTitle());
                    courseObj.setCreditHours(course.getCreditHours());
                    courseObj.setDay(course.getDay());
                    courseObj.setHours(course.getHours());
                    courseObj.setId(course.getId());

                    courseObj.setMinutes(course.getMinutes());
                    courseObj.setSemester(course.getSemester());
                    courseObj.setUsername(course.getUsername());

                    Instructor instructor = course.getInstructor();
                    InstructorObj instructorObj = new InstructorObj();
                    if (instructor!=null){
                        instructorObj.setEmail(instructor.getEmail());
                        instructorObj.setId(instructor.getId());
                        instructorObj.setWebsite(instructor.getWebsite());
                        instructorObj.setFirstName(instructor.getFirstName());
                        instructorObj.setLastName(instructor.getLastName());
                        instructorObj.setImageArray(instructor.getImageArray());
                        courseObj.setInstructor(instructorObj);
                    }


                    mData.add(courseObj);
                }
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                notifyDataSetChanged();
            }
        });

        this.mData = null;
        this.context=context;
        this.mNavigate = resultsInterface;
    }
    @Override
    public CourseListRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v =  LayoutInflater.from(parent.getContext()).inflate(R.layout.course_view_layout, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v);
        return vh;

    }

    @Override
    public void onBindViewHolder(CourseListRecyclerViewAdapter.ViewHolder holder, int position) {

        if (null == mData ||mData.size() == 0){
            holder.isCourseEmpty = true;
            holder.textViewCourseTitle.setVisibility(View.INVISIBLE);
            holder.textViewInstructorName.setText("No Courses");
            holder.textViewCourseTime.setVisibility(View.INVISIBLE);
            holder.imageViewInstructor.setVisibility(View.INVISIBLE);
        } else {
            CourseObj courses=mData.get(position);
            holder.myCourse = mData.get(position);
            //  holder.noteData=mData.get(position);
            if (courses.getInstructor()!=null){
                holder.textViewInstructorName.setText(courses.getInstructor().getFirstName()+" "+courses.getInstructor().getLastName()+"");

                holder.textViewCourseTime.setText(courses.getDay()+" " +courses.getHours()+":"+courses.getMinutes()+" "+courses.getAmPm());

                BitmapFactory.Options options = new BitmapFactory.Options();
                Bitmap bitmap = BitmapFactory.decodeByteArray(courses.getInstructor().getImageArray(), 0, courses.getInstructor().getImageArray().length, options);
                holder.imageViewInstructor.setImageBitmap(bitmap);
            }

            holder.textViewCourseTitle.setText(courses.getCourseTitle()+"");


            holder.isCourseEmpty = false;


            holder.textViewCourseTitle.setVisibility(View.VISIBLE);
            holder.textViewInstructorName.setVisibility(View.VISIBLE);
            holder.textViewCourseTime.setVisibility(View.VISIBLE);
            holder.imageViewInstructor.setVisibility(View.VISIBLE);
            holder.position = holder.getLayoutPosition();

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

        TextView textViewCourseTitle;
        ImageView imageViewInstructor;
        TextView textViewInstructorName;
        TextView textViewCourseTime;

        CourseObj myCourse;
        boolean isCourseEmpty;

        int position;

        public ViewHolder(final View itemView) {
            super(itemView);

            textViewCourseTitle=(TextView)itemView.findViewById(R.id.textViewCourseTitle);
            textViewInstructorName=(TextView)itemView.findViewById(R.id.textViewInstructor);
            textViewCourseTime=(TextView)itemView.findViewById(R.id.textViewTime);
            imageViewInstructor=(ImageView)itemView.findViewById(R.id.imageViewInstructorImage);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("demo","Clickeddddd");
                    if (null != mNavigate && myCourse!=null){
                        mNavigate.courseItemPressed(position);
                    }
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    Log.d("demo","long clickedd");
                    if (mNavigate!=null && myCourse!=null){
                        mNavigate.courseItemLongClicked(myCourse.getId());
                    }
                    return true;
                }
            });


        }

    }

    public interface ResultsInterface {
        void courseItemLongClicked(String courseId);
        void courseItemPressed(int position);
    }
}
