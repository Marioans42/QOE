package mg.telma.qoe.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

import mg.telma.qoe.enums.MainScreen;

public class MainPagerAdapter extends FragmentStatePagerAdapter {

    private final List<MainScreen> screens = new ArrayList<>();
    public MainPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return screens.get(position).getFragment();
    }

    @Override
    public int getCount() {
        return screens.size();
    }

    public void setItems(List<MainScreen> screens) {
        this.screens.clear();
        this.screens.addAll(screens);
        this.notifyDataSetChanged();
    }

    public  List<MainScreen> getItems() {
        return screens;
    }
}
