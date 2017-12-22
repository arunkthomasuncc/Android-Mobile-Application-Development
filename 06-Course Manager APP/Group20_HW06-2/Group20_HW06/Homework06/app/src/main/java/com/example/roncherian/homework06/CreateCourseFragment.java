package com.example.roncherian.homework06;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CreateCourseFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class CreateCourseFragment extends MenuBarFragments implements InstructorListRecyclerViewAdapter.ResultsInterface{

    private OnFragmentInteractionListener mListener;

    private User loggedInUser;

    private RealmResults<Instructor> instructors;

    private InstructorObj instructor = null;

    private String loggedInUsername = "";

    private boolean isInViewMode = false;

    private boolean instructorsPresentForUser = false;

    private int position = -1;

    Courses userCourse;

    String creditHoursToDisplay = "";

    boolean displayInstr = true;

    Realm realm;

    String day="", hours="", minutes="", amPmString="", semesterString="", creditHours="", courseTitleString="";
    int semesterInt = 0, amPmInt=0, dayInt=0;

    public CreateCourseFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        getActivity().setTitle("Create Course");
        View view =  inflater.inflate(R.layout.fragment_create_course, container, false);

        Bundle bundle = this.getArguments();
        if (bundle!=null){
            final String userId = bundle.getString(MainActivity.USERNAME, "");
            isInViewMode = bundle.getBoolean(MainActivity.IS_VIEW_MODE, false);
            if(isInViewMode)
                getActivity().setTitle("Course Details");
            position = bundle.getInt(MainActivity.COURSE_CLICKED_POSITION, -1);
            loggedInUsername = userId;
            realm = Realm.getDefaultInstance();
            realm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    loggedInUser = realm.where(User.class).equalTo("username", userId).findFirst();
                    instructors = realm.where(Instructor.class).equalTo("username",userId).findAll();
                    if (instructors.size()>0){
                        instructorsPresentForUser = true;
                    } else {
                        instructorsPresentForUser = false;
                    }
                }
            }, new Realm.Transaction.OnSuccess() {
                @Override
                public void onSuccess() {

                    try {
                        Button createButton = (Button)getActivity().findViewById(R.id.buttonCreateCourse);
                        if (instructorsPresentForUser){
                            createButton.setEnabled(true);
                        }
                        else {
                            createButton.setEnabled(false);
                        }
                    }catch (Exception e){
                        return;
                    }

                }
            }, new Realm.Transaction.OnError() {
                @Override
                public void onError(Throwable error) {

                }
            });
            /*try {
                loggedInUser = realm.where(User.class).equalTo("username", userId).findFirst();
                instructors = realm.where(Instructor.class).findAll();
                // do something with the person ...
            } finally {
                realm.close();
            }*/
        }

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //realm.close();
    }

    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);



        mRecyclerView=(RecyclerView)getActivity().findViewById(R.id.recyclerViewInstructorList);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL,false);
        mRecyclerView.setLayoutManager(mLayoutManager);


        // specify an adapter (see also next example)
        //ingredientList.add("");
        mAdapter = new InstructorListRecyclerViewAdapter(getActivity(),CreateCourseFragment.this, false, null,true);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.notifyDataSetChanged();




        final Spinner spinnerDay = (Spinner) getActivity().findViewById(R.id.spinnerDay);
        List<String> list = new ArrayList<String>();
        list.add("Select");
        list.add("Monday");
        list.add("Tuesday");
        list.add("Wednesday");
        list.add("Thursday");
        list.add("Friday");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDay.setAdapter(dataAdapter);


        spinnerDay.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0: day = "";
                        break;
                    case 1: day = "Mon";
                        break;
                    case 2: day = "Tue";
                        break;
                    case 3: day = "Wed";
                        break;
                    case 4: day = "Thu";
                        break;
                    case 5: day = "Fri";
                        break;
                    default:day="";
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        final EditText editTextHours = (EditText) getActivity().findViewById(R.id.editTextHours);


        editTextHours.addTextChangedListener(new MyEditTextValidator(editTextHours) {
            @Override
            public void validate(TextView textView, String text) {

                try {
                    Integer textInt = Integer.parseInt(text);
                    if (textInt == null){
                        editTextHours.setText("");
                    } else if (textInt<1 || textInt>12){
                        editTextHours.setText("");
                    }
                    hours = editTextHours.getText().toString();
                }catch (Exception e){

                }
            }
        });

        final EditText editTextMinutes = (EditText) getActivity().findViewById(R.id.editTextMinutes);

        editTextMinutes.addTextChangedListener(new MyEditTextValidator(editTextMinutes) {
            @Override
            public void validate(TextView textView, String text) {

                try {
                    Integer textInt = Integer.parseInt(text);
                    if (textInt == null){
                        editTextMinutes.setText("");
                    } else if (textInt<0 || textInt>59){
                        editTextMinutes.setText("");
                    }
                    minutes = editTextMinutes.getText().toString();
                }catch (Exception e){

                }
            }
        });

        final Spinner spinneramPm = (Spinner)getActivity().findViewById(R.id.spinnerAmPm);
        List<String> amPmList = new ArrayList<String>();
        amPmList.add("AM");
        amPmList.add("PM");

        ArrayAdapter<String> amPmDataAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, amPmList);
        amPmDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinneramPm.setAdapter(amPmDataAdapter);

        spinneramPm.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i==0){
                    amPmString = "AM";
                } else if (i==1){
                    amPmString = "PM";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        final Spinner spinnerSemester = (Spinner)getActivity().findViewById(R.id.spinnerSemester);
        final List<String> semesterList = new ArrayList<String>();
        semesterList.add("SELECT");
        semesterList.add("FALL");
        semesterList.add("SPRING");
        semesterList.add("SUMMER");

        ArrayAdapter<String> semesterDataAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, semesterList);
        semesterDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSemester.setAdapter(semesterDataAdapter);

        spinnerSemester.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i==1){
                    semesterString = "FALL";
                } else if (i==2){
                    semesterString = "SPRING";
                } else if (i==3){
                    semesterString = "SUMMER";
                } else {
                    semesterString = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        final RadioGroup radioGroupCreditHours = (RadioGroup)getActivity().findViewById(R.id.radioGroup);
        radioGroupCreditHours.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i==R.id.radioButton1){
                    creditHours="1";
                }else if (i==R.id.radioButton2){
                    creditHours="2";
                }else if (i==R.id.radioButton3){
                    creditHours="3";
                }
            }
        });

        final EditText courseTitle = (EditText)getActivity().findViewById(R.id.editTextTitleName);


        final Button createCourse = (Button)getActivity().findViewById(R.id.buttonCreateCourse);
        createCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (courseTitle.getText().toString().length()==0){
                    Toast.makeText(getActivity(),"Enter a Course Title",Toast.LENGTH_SHORT).show();
                    return;
                } else if (instructor==null){
                    Toast.makeText(getActivity(),"Choose an instructor",Toast.LENGTH_SHORT).show();
                    return;
                } else if(day.isEmpty()){
                    Toast.makeText(getActivity(),"Choose Day",Toast.LENGTH_SHORT).show();
                    return;
                } else if(hours.isEmpty()){
                    Toast.makeText(getActivity(),"Choose Hours of Time",Toast.LENGTH_SHORT).show();
                    return;
                } else if(minutes.isEmpty()){
                    Toast.makeText(getActivity(),"Choose Minutes of Time",Toast.LENGTH_SHORT).show();
                    return;
                } else if(amPmString.isEmpty()){
                    Toast.makeText(getActivity(),"Choose Am/PM",Toast.LENGTH_SHORT).show();
                    return;
                } else if(semesterString.isEmpty()){
                    Toast.makeText(getActivity(),"Choose Semester",Toast.LENGTH_SHORT).show();
                    return;
                } else if(creditHours.isEmpty()){
                    Toast.makeText(getActivity(),"Choose Credit Hours",Toast.LENGTH_SHORT).show();
                    return;
                }
                realm = Realm.getDefaultInstance();

                realm.executeTransactionAsync(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {

                        Courses courses = realm.createObject(Courses.class, UUID.randomUUID().toString());
                        courses.setCourseTitle(courseTitle.getText().toString());
                        courses.setCreditHours(creditHours);
                        courses.setDay(day);
                        courses.setAmPm(amPmString);
                        courses.setMinutes(editTextMinutes.getText().toString());
                        courses.setHours(editTextHours.getText().toString());
                        //courses.setDate(day+" "++":"+editTextMinutes.getText().toString()+" "+amPmString);
                        Instructor instructor1 = realm.where(Instructor.class).equalTo("id",instructor.getId()).findFirst();
                        courses.setInstructor(instructor1);
                        courses.setUsername(MainActivity.LOGGED_IN_USERNAME);
                        if (null != instructor && !isInViewMode){
                            courses.getInstructor().setSelected(instructor.isSelected);
                        }

                        courses.setSemester(semesterString);

                        RealmResults<Courses> coursesRealmResults = realm.where(Courses.class).equalTo("username",MainActivity.LOGGED_IN_USERNAME).findAll();
                        RealmList<Courses> coursesRealmList = new RealmList<Courses>();
                        coursesRealmList.addAll(coursesRealmResults);

                        User loggedInUser = realm.where(User.class).equalTo("username", loggedInUsername).findFirst();
                        if (null != loggedInUser)
                            loggedInUser.courses = coursesRealmList;
                        onButtonPressed(loggedInUser.getUsername());
                    }
                }, new Realm.Transaction.OnSuccess() {
                    @Override
                    public void onSuccess() {

                    }
                });
            }
        });

        final Button resetCourseButton = (Button)getActivity().findViewById(R.id.buttonResetCourse);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setCancelable(false).setTitle("Reset all items").
                setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        courseTitle.setText("");
                        spinnerDay.setSelection(0);
                        spinneramPm.setSelection(0);
                        spinnerSemester.setSelection(0);
                        editTextHours.setText("");
                        editTextMinutes.setText("");
                        radioGroupCreditHours.clearCheck();


                        mAdapter = new InstructorListRecyclerViewAdapter(getActivity(),CreateCourseFragment.this, false, null,true);
                        mRecyclerView.setAdapter(mAdapter);

                        mAdapter.notifyDataSetChanged();

                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).create();

        final AlertDialog alertDialogSimpleDialog = alertDialogBuilder.create();

        resetCourseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialogSimpleDialog.show();
            }
        });

        if (isInViewMode){

            realm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    loggedInUser = realm.where(User.class).equalTo("username", loggedInUsername).findFirst();
                    userCourse = loggedInUser.getCourses().get(position);
                    Instructor ins = userCourse.getInstructor();
                    if (ins!=null){
                        instructor = new InstructorObj();
                        instructor.setLastName(ins.getLastName());
                        instructor.setFirstName(ins.getFirstName());
                        instructor.setWebsite(ins.getWebsite());
                        instructor.setEmail(ins.getEmail());
                        instructor.setId(ins.getId());
                        instructor.setSelected(ins.isSelected());
                        instructor.setImageArray(ins.getImageArray());
                    } else {
                        displayInstr = false;
                    }



                    hours = userCourse.getHours();
                    minutes = userCourse.getMinutes();
                    switch (userCourse.getDay()) {

                        case "Mon":
                            dayInt=1;
                            break;
                        case "Tue":
                            dayInt=2;
                            break;
                        case "Wed":
                            dayInt=3;
                            break;
                        case "Thu":
                            dayInt=4;
                            break;
                        case "Fri":
                            dayInt=5;
                            break;

                    }

                    switch (userCourse.getAmPm()) {
                        case "AM":
                            amPmInt = 0;
                            break;
                        case "PM":
                            amPmInt=1;
                    }

                    switch (userCourse.getSemester()) {
                        case "FALL":
                            semesterInt=1;
                            break;
                        case "SPRING":
                            semesterInt=2;
                            break;
                        case "SUMMER":
                            semesterInt=3;
                            break;
                    }
                    courseTitleString = userCourse.getCourseTitle();
                    creditHoursToDisplay = userCourse.getCreditHours();
                }
            }, new Realm.Transaction.OnSuccess() {
                @Override
                public void onSuccess() {
                    spinnerDay.setSelection(dayInt);
                    spinneramPm.setSelection(amPmInt);
                    spinnerSemester.setSelection(semesterInt);
                    createCourse.setVisibility(View.INVISIBLE);
                    resetCourseButton.setVisibility(View.INVISIBLE);
                    courseTitle.setText(courseTitleString);
                    editTextHours.setText(hours);
                    editTextMinutes.setText(minutes);
                    switch (creditHoursToDisplay){
                        case "1": radioGroupCreditHours.check(R.id.radioButton1);
                            break;
                        case "2": radioGroupCreditHours.check(R.id.radioButton2);
                            break;
                        case "3": radioGroupCreditHours.check(R.id.radioButton3);
                            break;
                    }

                    mAdapter = new InstructorListRecyclerViewAdapter(getActivity(),CreateCourseFragment.this, isInViewMode, instructor, displayInstr);
                    mRecyclerView.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();
                }
            });

            courseTitle.setClickable(false);
            courseTitle.setFocusable(false);
            spinneramPm.setClickable(false);
            spinneramPm.setFocusable(false);
            spinneramPm.setEnabled(false);
            spinnerDay.setEnabled(false);
            spinnerSemester.setEnabled(false);

            editTextHours.setClickable(false);
            editTextMinutes.setClickable(false);
            editTextHours.setFocusable(false);
            editTextMinutes.setFocusable(false);
            spinnerDay.setClickable(false);
            for (int i=0; i<radioGroupCreditHours.getChildCount(); i++){
                radioGroupCreditHours.getChildAt(i).setEnabled(false);
            }
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(String username) {
        if (mListener != null) {
            mListener.onCreateCourseButtonPressed(username);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void navigateIntent(String url) {

    }


    @Override
    public void itemClicked(String instructorId, boolean isSelected) {

        final String instrId= instructorId;
        realm = Realm.getDefaultInstance();

        final boolean isInstructorSelected = isSelected;
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                Instructor instructor1 = realm.where(Instructor.class).equalTo("id",instrId).findFirst();
                //loggedInUser = realm.where(User.class).equalTo("username",loggedInUsername).findFirst();
                //userCourse = loggedInUser.getCourses().
                //if (!isInViewMode)
                    //instructor1.setSelected(isInstructorSelected);
                instructor = new InstructorObj();
                instructor.setLastName(instructor1.getLastName());
                instructor.setFirstName(instructor1.getFirstName());
                instructor.setWebsite(instructor1.getWebsite());
                instructor.setSelected(isInstructorSelected);
                instructor.setId(instructor1.getId());
                instructor.setEmail(instructor1.getEmail());
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                //mAdapter.notifyDataSetChanged();
            }
        });

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onCreateCourseButtonPressed(String username);
    }
}
