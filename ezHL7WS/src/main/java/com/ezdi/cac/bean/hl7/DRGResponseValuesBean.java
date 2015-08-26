package com.ezdi.cac.bean.hl7;

import com.ezdi.cac.constant.table.DRGResponseValuesTableConstant;

import javax.persistence.*;

/**
 * Created by Satyam.S on 7/10/2015.
 */
@Entity
@Table(name = DRGResponseValuesTableConstant.TABLE_NAME)
public class DRGResponseValuesBean {


    @Id
    @GeneratedValue
    @Column(name = DRGResponseValuesTableConstant.ID)
    private Integer id;

    @Column(name = DRGResponseValuesTableConstant.CODING_SYSTEM)
    private String codingSystem;


    @Column(name = DRGResponseValuesTableConstant.ENCOUNTERINDEXID)
    private Integer encounterIndexId;

    @Column(name = DRGResponseValuesTableConstant.ACTIVERECORD)
    private Integer activeRecord;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCodingSystem() {
        return codingSystem;
    }

    public void setCodingSystem(String codingSystem) {
        this.codingSystem = codingSystem;
    }

    public Integer getEncounterIndexId() {
        return encounterIndexId;
    }

    public void setEncounterIndexId(Integer encounterIndexId) {
        this.encounterIndexId = encounterIndexId;
    }

    public Integer getActiveRecord() {
        return activeRecord;
    }

    public void setActiveRecord(Integer activeRecord) {
        this.activeRecord = activeRecord;
    }
}
