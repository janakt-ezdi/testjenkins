package com.ezdi.cac.bean.hl7;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


import com.ezdi.cac.constant.table.EncoderEditsTableConstant;


@Entity
@Table(name = EncoderEditsTableConstant.TABLE_NAME)
public class EncoderEditsBean {
	
	
		@Id
		@GeneratedValue
		@Column(name = EncoderEditsTableConstant.EDITS_ID)
		private Integer editsId;

		@Column(name = EncoderEditsTableConstant.CODEMAPBYUSER_ID)
		private Integer codeMapByUserID;

}
