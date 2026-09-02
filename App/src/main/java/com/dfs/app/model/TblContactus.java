package com.dfs.app.model;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;


/**
 * The persistent class for the TBL_CONTACTUS database table.
 */
@Entity
@Table(name = "TBL_CONTACTUS")
@NamedQuery(name = "TblContactus.findAll", query = "SELECT t FROM TblContactus t")
public class TblContactus implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name = "CONTACT_NO")
    private String contactNo;
    @Id
    @Column(name = "CONTACTUS_ID")
    private BigDecimal contactusId;

    private String email;

    @Column(name = "FACEBOOK_ID")
    private String facebookId;

    @Column(name = "WHATSAPP_NO")
    private String whatsappNo;

    public TblContactus() {
    }

    public String getContactNo() {
        return this.contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public BigDecimal getContactusId() {
        return this.contactusId;
    }

    public void setContactusId(BigDecimal contactusId) {
        this.contactusId = contactusId;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFacebookId() {
        return this.facebookId;
    }

    public void setFacebookId(String facebookId) {
        this.facebookId = facebookId;
    }

    public String getWhatsappNo() {
        return this.whatsappNo;
    }

    public void setWhatsappNo(String whatsappNo) {
        this.whatsappNo = whatsappNo;
    }

}