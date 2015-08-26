package com.ezdi.cac.bean.hl7;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.ezdi.cac.constant.table.EncounterWorkListMapTableConstant;

@Entity
@Table(name = EncounterWorkListMapTableConstant.TABLE_NAME)
public class EncounterWorkListMapBean {

    @Id
    @GeneratedValue
    @Column(name = EncounterWorkListMapTableConstant.ID)
    private Integer id;

    @Column(name = EncounterWorkListMapTableConstant.ENCOUNTER_Id)
    private Integer encounterId;

    @Column(name = EncounterWorkListMapTableConstant.WORKLIST_ID)
    private Integer workListId;

    @Column(name = EncounterWorkListMapTableConstant.PRIORITY)
    private String priority;

    @Column(name = EncounterWorkListMapTableConstant.ROLE_ID)
    private Integer roleId;

    @Column(name = EncounterWorkListMapTableConstant.ROLE_NAME)
    private String roleName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = EncounterWorkListMapTableConstant.ENCOUNTER_Id, nullable = false, insertable = false, updatable = false)
    private EncounterBean encounterWorkListMapEncounterInformation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = EncounterWorkListMapTableConstant.WORKLIST_ID, nullable = false, insertable = false, updatable = false)
    private WorkListBean encounterWorkListMapWorkList;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getEncounterId() {
        return encounterId;
    }

    public void setEncounterId(Integer encounterId) {
        this.encounterId = encounterId;
    }

    public Integer getWorkListId() {
        return workListId;
    }

    public void setWorkListId(Integer workListId) {
        this.workListId = workListId;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public EncounterBean getEncounterWorkListMapEncounterInformation() {
        return encounterWorkListMapEncounterInformation;
    }

    public void setEncounterWorkListMapEncounterInformation(EncounterBean encounterWorkListMapEncounterInformation) {
        this.encounterWorkListMapEncounterInformation = encounterWorkListMapEncounterInformation;
    }

    public WorkListBean getEncounterWorkListMapWorkList() {
        return encounterWorkListMapWorkList;
    }

    public void setEncounterWorkListMapWorkList(WorkListBean encounterWorkListMapWorkList) {
        this.encounterWorkListMapWorkList = encounterWorkListMapWorkList;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}

