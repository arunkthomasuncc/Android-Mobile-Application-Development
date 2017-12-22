package com.example.roncherian.homework06;

import android.app.Fragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

/**
 * Created by roncherian on 07/11/17.
 */

public class ExitMenuFragment extends Fragment {
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_bar_exit_layout, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int count = 0;
        switch (item.getItemId()) {

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
