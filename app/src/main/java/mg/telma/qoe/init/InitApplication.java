package mg.telma.qoe.init;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import mg.telma.qoe.injection.ApplicationComponent;
import mg.telma.qoe.injection.ApplicationModule;
import mg.telma.qoe.injection.DaggerApplicationComponent;


public class InitApplication extends Application {
    ApplicationComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        initRealm();
        appComponent = DaggerApplicationComponent.builder().applicationModule(new ApplicationModule(this)).build();
        appComponent.inject(this);
    }

    private void initRealm() {
        Realm.init(this);
        Realm.setDefaultConfiguration(new RealmConfiguration.Builder()
                .name("DB_QOE")
                .build());
    }

    public ApplicationComponent getApplicationComponent() {
        return appComponent;
    }
}
