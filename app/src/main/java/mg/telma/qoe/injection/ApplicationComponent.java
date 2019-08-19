package mg.telma.qoe.injection;

import dagger.Component;
import mg.telma.qoe.activity.BaseActivity;
import mg.telma.qoe.init.InitApplication;

@Component(modules = {ApplicationModule.class})
public interface ApplicationComponent {
    void inject(InitApplication initApplication);
    void inject(BaseActivity baseActivity);
}
