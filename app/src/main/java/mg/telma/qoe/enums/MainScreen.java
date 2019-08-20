package mg.telma.qoe.enums;

import androidx.fragment.app.Fragment;

import mg.telma.qoe.R;
import mg.telma.qoe.ui.fragment.InternetFragment;

public enum MainScreen {

    INTERNET(R.id.nav_internet, R.drawable.ic_internet, R.string.app_name, new InternetFragment());

    private int menuItemId;
    private int menuItemIconId;
    private int titleStringId;
    private Fragment fragment;

    MainScreen(int menuItemId, int menuItemIconId, int titleStringId, Fragment fragment) {
        this.menuItemId = menuItemId;
        this.menuItemIconId = menuItemIconId;
        this.titleStringId = titleStringId;
        this.fragment = fragment;
    }

    public int getMenuItemId() {
        return menuItemId;
    }

    public void setMenuItemId(int menuItemId) {
        this.menuItemId = menuItemId;
    }

    public int getMenuItemIconId() {
        return menuItemIconId;
    }

    public void setMenuItemIconId(int menuItemIconId) {
        this.menuItemIconId = menuItemIconId;
    }

    public int getTitleStringId() {
        return titleStringId;
    }

    public void setTitleStringId(int titleStringId) {
        this.titleStringId = titleStringId;
    }

    public Fragment getFragment() {
        return fragment;
    }

    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }

    public static MainScreen getMainScreenForMenuItem(int menuItemId) {
        for(MainScreen item: MainScreen.values()) {
            if(item.menuItemId == menuItemId)
                return item;
        }
        return null;
    }

}
