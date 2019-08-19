package mg.telma.qoe.dao;

import java.util.List;

import io.realm.Realm;
import mg.telma.qoe.model.Cellular;

public class CellularDao {

    public void storeOrUpdate(final Cellular cellular) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm1) {
                realm1.insertOrUpdate(cellular);
            }
        });
        realm.close();
    }

    public Cellular findById (Integer id) {
        Realm realm = Realm.getDefaultInstance();
        Cellular cellular = realm.where(Cellular.class).equalTo("id", id).findFirst();
        realm.close();
        return cellular;
    }

    public List<Cellular> findAll () {
        Realm realm = Realm.getDefaultInstance();
        List<Cellular> list = realm.where(Cellular.class).findAll();
        return  list;
    }
}
