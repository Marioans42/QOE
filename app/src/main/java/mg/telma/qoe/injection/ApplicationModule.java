package mg.telma.qoe.injection;

import dagger.Module;
import dagger.Provides;
import mg.telma.qoe.dao.CellularDao;
import mg.telma.qoe.init.InitApplication;
import mg.telma.qoe.service.CellularService;
import mg.telma.qoe.service.PingTestService;
import mg.telma.qoe.service.UploadTestService;

@Module
public class ApplicationModule {

    final private InitApplication application;

    public ApplicationModule(InitApplication application) {
        this.application = application;
    }

    @Provides
    CellularDao providesCellularDao() {
        return new CellularDao();
    }

    @Provides
    CellularService providesCellularService(CellularDao cellularDao) {
        return new CellularService(cellularDao);
    }


}
