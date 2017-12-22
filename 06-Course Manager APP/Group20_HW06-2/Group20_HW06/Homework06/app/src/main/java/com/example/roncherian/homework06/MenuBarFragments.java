package com.example.roncherian.homework06;

import android.app.Fragment;
import android.app.FragmentManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

/**
 * Created by roncherian on 03/11/17.
 */

public class MenuBarFragments extends Fragment {

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_bar_layout, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int count = 0;
        switch (item.getItemId()) {
            case R.id.action_home:
                count = getFragmentManager().getBackStackEntryCount();
                //CourseManagerFragment courseManagerFragment = (CourseManagerFragment) getFragmentManager().findFragmentByTag(MainActivity.COURSE_MANAGER_FRAGMENT);
                while (count>1){
                    getFragmentManager().popBackStack();
                    count--;
                }
                //FragmentManager.popBackStack("course_manager_fragment", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                //getFragmentManager().beginTransaction().replace(R.id.containerView,courseManagerFragment,MainActivity.COURSE_MANAGER_FRAGMENT);
                return true;

            case R.id.action_instructor:
                getFragmentManager().beginTransaction().replace(R.id.containerView,new InstructorsListFragment(),"show_instructors").addToBackStack(null).commit();
                return true;

            case R.id.action_add_instructor:

                getFragmentManager().beginTransaction().replace(R.id.containerView,new AddInstructorFragment(),"add_instructor").addToBackStack(null).commit();

                return true;

            case R.id.action_logout:

                count = getFragmentManager().getBackStackEntryCount();
                while (count>0) {
                    getFragmentManager().popBackStack();
                    count--;
                }
                return true;

            case R.id.action_exit:
                getActivity().moveTaskToBack(true);
                getActivity().finish();
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
