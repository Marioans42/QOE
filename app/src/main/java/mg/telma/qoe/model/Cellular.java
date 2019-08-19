package mg.telma.qoe.model;

import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Cellular extends RealmObject implements Serializable {
    @PrimaryKey
    private Integer id;
    private Integer cellId;
    private String  imei;
    private String imsi;
    private String mcc;
    private String mnc;
    private String lac;
    private String networkName;

    public Cellular(String imei, String imsi, String mcc, String mnc, String lac, String networkName) {
        this.imei = imei;
        this.imsi = imsi;
        this.mcc = mcc;
        this.mnc = mnc;
        this.lac = lac;
        this.networkName = networkName;
    }

    public Cellular() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCellId() {
        return cellId;
    }

    public void setCellId(Integer cellId) {
        this.cellId = cellId;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getImsi() {
        return imsi;
    }

    public void setImsi(String imsi) {
        this.imsi = imsi;
    }

    public String getMcc() {
        return mcc;
    }

    public void setMcc(String mcc) {
        this.mcc = mcc;
    }

    public String getMnc() {
        return mnc;
    }

    public void setMnc(String mnc) {
        this.mnc = mnc;
    }

    public String getLac() {
        return lac;
    }

    public void setLac(String lac) {
        this.lac = lac;
    }

    public String getNetworkName() {
        return networkName;
    }

    public void setNetworkName(String networkName) {
        this.networkName = networkName;
    }
}
