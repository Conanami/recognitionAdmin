package com.web.dto;

import org.apache.commons.csv.CSVRecord;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name="root")
public class DataZBStructure {
	private static final Log log = LogFactory
			.getLog(DataZBStructure.class);
	
	@XmlElement(name="item")
	public List<FieldFormat> list = new ArrayList<>();
	
	public void read() {
		if (this.list.size()==0) {
			try {
				JAXBContext context = JAXBContext.newInstance(DataZBStructure.class);
				Unmarshaller unmarshaller = context.createUnmarshaller();
				DataZBStructure obj = (DataZBStructure) unmarshaller.unmarshal(Thread
						.currentThread().getContextClassLoader()
						.getResourceAsStream("zb_structure.xml"));
				this.list = obj.list;
			} catch (Exception e) {
				log.error(e);
			}
		}
	}
	
	public List<String> getTitles(){
		List<String> ss = new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
			ss.add(this.list.get(i).getTitle());
		}
		return ss;
	}
	
	public List<String> getFields(){
		List<String> ss = new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
			ss.add(this.list.get(i).getField());
		}
		return ss;
	}
	
	/**
	 * 获取对象 的第 N 个 属性字段
	 * @param index
	 * @return
	 */
	public String getField(int index){
		return list.get(index).getField();
	}
	
	/**
	 * 根据表头获取 存储字段名称
	 * @param title
	 * @return
	 */
	public String getFieldWithTitle(String title){
		for (int j = 0; j < list.size(); j++) {
			if (title.equals(this.list.get(j).getTitle())) {
				return this.list.get(j).getField();
			}
		}
		return "";
	}
	
	/**
	 * 从csv记录获取表头对应字段
	 * @param rd
	 * @return
	 */
	public List<String> getFieldsWithCSVRecord(CSVRecord rd){
		List<String> fields = new ArrayList<>();
		for (int i = 0; i < rd.size(); i++) {
			String title = rd.get(i);
			for (int j = 0; j < list.size(); j++) {
				if (title.equals(this.list.get(j).getTitle())) {
					fields.add(this.list.get(j).getField());
				}
			}
		}
		return fields;
	}


    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlRootElement
    static class FieldFormat{
        @XmlAttribute
        private String field;
        @XmlAttribute
        private String title;

        public FieldFormat(String field, String title){
            this.setField(field);
            this.setTitle(title);
        }
        public FieldFormat(){

        }

        /**
         * @return the field
         */
        public String getField() {
            return field;
        }
        /**
         * @param field the field to set
         */
        public void setField(String field) {
            this.field = field;
        }
        /**
         * @return the title
         */
        public String getTitle() {
            return title;
        }
        /**
         * @param title the title to set
         */
        public void setTitle(String title) {
            this.title = title;
        }

    }
	
}
