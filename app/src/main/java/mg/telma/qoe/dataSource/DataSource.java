package mg.telma.qoe.dataSource;

import io.realm.Realm;

public class DataSource {
    private Realm realm;

    public DataSource(Realm realm) {
        this.realm = realm;
    }
}
