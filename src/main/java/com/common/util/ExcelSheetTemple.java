package com.common.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class ExcelSheetTemple {

	private List<TitleField> fields = new ArrayList<TitleField>();
	private JSONArray dataArray = new JSONArray();
	
	/***
	 * 添加标题栏数据
	 * @param field
	 * @param title
	 */
	public void addTitle(String field, String title){
		fields.add(new TitleField(field, title));
	}
	
	/***
	 * 添加值
	 * @param obj
	 */
	public void addValues(JSONObject obj){
		dataArray.add(obj);
	}
	
	/***
	 * 导出数据
	 * @param sheet
	 */
	public void explortToSheet(HSSFSheet sheet){
		for (int i = 0; i <= dataArray.size(); i++)
        {
            HSSFRow row = sheet.createRow((int)i);//创建一行
            JSONObject item = dataArray.getJSONObject(i);
            
            for (int j = 0; j < fields.size(); j++) {
            	HSSFCell cell = row.createCell(j);//创建一列
            	cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            	if (j==0) {
					cell.setCellValue(fields.get(j).getTitle());
				}else{
					String field = fields.get(j).getField();
					if (item.containsKey(field)) {
						cell.setCellValue(item.getString(field));
					}
				}
			}
        }
	}
	
	
	private class TitleField{
		private String field;
		private String title;
		
		public TitleField(String field, String title){
			this.setField(field);
			this.setTitle(title);
		}

		public String getField() {
			return field;
		}

		public void setField(String field) {
			this.field = field;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}
	}
}
