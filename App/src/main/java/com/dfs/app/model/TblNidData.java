package com.dfs.app.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * TBL_NID_DATA, read only.
 *
 * The nadra service owns this table and writes it during CNIC verification. App only reads the two
 * values the signup verification challenge needs, so this entity deliberately maps a subset rather
 * than the full 31 columns - fewer columns to keep in step if the DB team changes the table.
 */
@Entity
@Table(name = "TBL_NID_DATA")
public class TblNidData implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "ID")
    private java.math.BigDecimal id;

    @Column(name = "NID_NO")
    private long nidNo;

    @Column(name = "MOTHER_NAME_EN")
    private String motherNameEn;

    @Column(name = "BIRTH_PLACE_EN")
    private String birthPlaceEn;

    public java.math.BigDecimal getId() {
        return id;
    }

    public void setId(java.math.BigDecimal id) {
        this.id = id;
    }

    public long getNidNo() {
        return nidNo;
    }

    public void setNidNo(long nidNo) {
        this.nidNo = nidNo;
    }

    public String getMotherNameEn() {
        return motherNameEn;
    }

    public void setMotherNameEn(String motherNameEn) {
        this.motherNameEn = motherNameEn;
    }

    public String getBirthPlaceEn() {
        return birthPlaceEn;
    }

    public void setBirthPlaceEn(String birthPlaceEn) {
        this.birthPlaceEn = birthPlaceEn;
    }
}
