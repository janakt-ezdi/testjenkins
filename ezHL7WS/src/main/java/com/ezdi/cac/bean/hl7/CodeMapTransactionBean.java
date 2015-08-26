package com.ezdi.cac.bean.hl7;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.ezdi.cac.constant.table.CodeMapTransactionTableConstant;

@Entity
@Table(name = CodeMapTransactionTableConstant.TABLE_NAME)
public class CodeMapTransactionBean {
	
	@Id
	@GeneratedValue
	@Column(name = CodeMapTransactionTableConstant.CODEMAPTRANS_ID)
	private Integer codeMapTransId;

	@Column(name = CodeMapTransactionTableConstant.CODEMAP_ID)
	private Integer codeMapId;

	public Integer getCodeMapTransId() {
		return codeMapTransId;
	}

	public void setCodeMapTransId(Integer codeMapTransId) {
		this.codeMapTransId = codeMapTransId;
	}

	public Integer getCodeMapId() {
		return codeMapId;
	}

	public void setCodeMapId(Integer codeMapId) {
		this.codeMapId = codeMapId;
	}

	
	

}
