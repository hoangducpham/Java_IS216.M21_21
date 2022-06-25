/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.Date;

/**
 *
 * @author An Phan
 */
public class PhieuKham {
    private int MaPK;
    private int MaBN;
    private String liDoKham;
    private String NgayKham;

    public PhieuKham() {
    }

    public PhieuKham(int MaPK, int MaBN, String liDoKham, String NgayKham) {
        this.MaPK = MaPK;
        this.MaBN = MaBN;
        this.liDoKham = liDoKham;
    }

    public int getMaPK() {
        return MaPK;
    }

    public void setMaPK(int MaPK) {
        this.MaPK = MaPK;
    }

    public int getMaBN() {
        return MaBN;
    }

    public void setMaBN(int MaBN) {
        this.MaBN = MaBN;
    }

    public String getLiDoKham() {
        return liDoKham;
    }

    public void setLiDoKham(String liDoKham) {
        this.liDoKham = liDoKham;
    }

    public String getNgayKham() {
        return NgayKham;
    }

    public void setNgayKham(String NgayKham) {
        this.NgayKham = NgayKham;
    }
    
    
    

}


