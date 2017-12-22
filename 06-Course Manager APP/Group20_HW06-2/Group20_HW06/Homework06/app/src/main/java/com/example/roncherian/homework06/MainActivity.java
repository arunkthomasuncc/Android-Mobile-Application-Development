package com.example.roncherian.homework06;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;

import io.realm.Realm;

public class MainActivity extends AppCompatActivity implements LoginFragment.OnFragmentInteractionListener, RegisterFragment.OnFragmentInteractionListener, CourseManagerFragment.OnFragmentInteractionListener,
        CreateCourseFragment.OnFragmentInteractionListener, AddInstructorFragment.OnFragmentInteractionListener,
        InstructorsListFragment.OnFragmentInteractionListener{

    static String LOGIN_FRAGMENT = "login_fragment";
    static String REGISTER_FRAGMENT = "register_fragment";
    static String COURSE_MANAGER_FRAGMENT = "course_manager_fragment";
    static String CREATE_COURSE_FRAGMENT = "create_course_fragment";
    static String VIEW_COURSE_FRAGMENT = "view_course_fragment";

    static final int CAMERA_REQUEST_CODE = 200;
    static final int GALLERY_REQUEST_CODE = 100;

    static String USERNAME = "user_name";

    static String IS_VIEW_MODE = "is_view_mode";

    static String COURSE_CLICKED_POSITION = "course_clicked_position";

    static String INSTRUCTOR_ID = "instructor_id";

    static String LOGGED_IN_USERNAME = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Realm.init(this);
        setTitle("Course Manager");
        getFragmentManager().beginTransaction().add(R.id.containerView,new LoginFragment(),LOGIN_FRAGMENT).commit();


    }

    @Override
    public void onCreateNewCourseButtonPressed(String username) {

        CreateCourseFragment createCourseFragment =  new CreateCourseFragment();
        Bundle bundle = new Bundle();
        bundle.putString(USERNAME,username);
        bundle.putBoolean(IS_VIEW_MODE,false);
        bundle.putInt(COURSE_CLICKED_POSITION,-1);
        createCourseFragment.setArguments(bundle);
        getFragmentManager().beginTransaction().replace(R.id.containerView, createCourseFragment,CREATE_COURSE_FRAGMENT).addToBackStack(null).commit();


    }

    @Override
    public void viewCourseItemPressed(int position, String username) {

        CreateCourseFragment createCourseFragment =  new CreateCourseFragment();
        Bundle bundle = new Bundle();
        bundle.putString(USERNAME,username);
        bundle.putBoolean(IS_VIEW_MODE,true);
        bundle.putInt(COURSE_CLICKED_POSITION,position);
        createCourseFragment.setArguments(bundle);
        getFragmentManager().beginTransaction().replace(R.id.containerView, createCourseFragment,VIEW_COURSE_FRAGMENT).addToBackStack(null).commit();

    }

    @Override
    public void onSignUpButtonPressed() {

        getFragmentManager().beginTransaction().replace(R.id.containerView,new RegisterFragment(),REGISTER_FRAGMENT).addToBackStack(null).commit();


    }

    @Override
    public void onRegisterUserPressed(String username){
        getFragmentManager().popBackStack();
        onLoginButtonPressed(username);
    }

    @Override
    public void onLoginButtonPressed(String username){

        LOGGED_IN_USERNAME = username;
        CourseManagerFragment courseManagerFragment =  new CourseManagerFragment();
        Bundle bundle = new Bundle();
        bundle.putString(USERNAME,username);
        courseManagerFragment.setArguments(bundle);
        getFragmentManager().beginTransaction().replace(R.id.containerView, courseManagerFragment,COURSE_MANAGER_FRAGMENT).addToBackStack(null).commit();


    }

    @Override
    public void onCreateCourseButtonPressed(String username) {

        if (getFragmentManager().getBackStackEntryCount()>0){
            getFragmentManager().popBackStack();
        }
        /*CourseManagerFragment courseManagerFragment =  new CourseManagerFragment();
        Bundle bundle = new Bundle();
        bundle.putString(USERNAME,username);
        courseManagerFragment.setArguments(bundle);
        getFragmentManager().beginTransaction().add(R.id.containerView, courseManagerFragment,COURSE_MANAGER_FRAGMENT).addToBackStack(null).commit();
*/

    }

    @Override
    public void orRegisterInstructor() {
        if (getFragmentManager().getBackStackEntryCount()>0){
            getFragmentManager().popBackStack();
        }


    }

    @Override
    public void onAddInstructorButtonPressed() {
        getFragmentManager().beginTransaction().replace(R.id.containerView,new AddInstructorFragment(),"add_instructor").addToBackStack(null).commit();
        //InstrcutorListFragment
    }

    @Override
    public void onViewInstructor(String instructorId){
        AddInstructorFragment addInstructorFragment = new AddInstructorFragment();
        Bundle bundle = new Bundle();
        bundle.putString(INSTRUCTOR_ID,instructorId);
        //bundle.putString(USERNAME,LOGGED_IN_USERNAME);
        addInstructorFragment.setArguments(bundle);
        getFragmentManager().beginTransaction().replace(R.id.containerView, addInstructorFragment, "add_instructor").addToBackStack(null).commit();
    }

    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    public final static boolean isValidWebUrl(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return Patterns.WEB_URL.matcher(target).matches();
        }
    }

    @Override
    public void onBackPressed() {

        if (getFragmentManager().getBackStackEntryCount()>0){
            getFragmentManager().popBackStack();
        }
    }
}
