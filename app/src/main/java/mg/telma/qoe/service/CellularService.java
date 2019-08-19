package mg.telma.qoe.service;

import java.util.List;

import mg.telma.qoe.dao.CellularDao;
import mg.telma.qoe.model.Cellular;

public class CellularService {
    private CellularDao cellularDao;

    public CellularService(CellularDao cellularDao) {
        this.cellularDao = cellularDao;
    }

    public void save(Cellular cellular) {
        cellularDao.storeOrUpdate(cellular);
    }


    public Cellular getById(Integer id) {
        Cellular cellular = cellularDao.findById(id);
        return cellular;
    }


    public List<Cellular> findAll() {
        return null;
    }
}
