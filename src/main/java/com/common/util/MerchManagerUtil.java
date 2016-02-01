package com.common.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name="root")
@XmlSeeAlso({
	MerchItem.class
})
public class MerchManagerUtil {
	private static final Log log = LogFactory
			.getLog(MerchManagerUtil.class);
	
	@XmlElement(name="item")
	public List<MerchItem> list = new ArrayList<MerchItem>();

    /**
     * 通过文件路径来构造 商户信息管理类
     * @param fileurl xml文件路径
     */
    public MerchManagerUtil(String fileurl){
        try {
            JAXBContext context = JAXBContext.newInstance(MerchManagerUtil.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            File fl = new File(fileurl);
            MerchManagerUtil b1 = (MerchManagerUtil) unmarshaller.unmarshal(fl);
            this.list = b1.list;
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    private MerchManagerUtil(){

    }

	/***
	 * 根据服务类型和商户号查询商户密钥
	 * @param service  服务名
	 * @param merchid  商户号
	 * @return 商户密钥
	 */
	public String getSecret(String service, String merchid){
		for (int i = 0; i < list.size(); i++) {
			MerchItem field = list.get(i);
			if (field.getService().equals(service) && field.getMerchid().equals(merchid)) {
				return field.getSecret();
			}
		}
		return "";
	}
	
	public static void main(String[] args) {
		try {
            MerchManagerUtil b1 = new MerchManagerUtil("c:\\tools\\merchmanager.xml");
			log.info("b1  "+b1);
		} catch (Exception e) {
			System.err.println(e);
		}
		System.out.println("end");
	}
}

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
class MerchItem {
	@XmlAttribute
	private String service;
	@XmlAttribute
	private String merchid;
	@XmlAttribute
	private String secret;
	public MerchItem(String service, String merchid, String secret){
		this.setService(service);
		this.setMerchid(merchid);
		this.setSecret(secret);
	}
	public MerchItem(){
		
	}
	public String getService() {
		return service;
	}
	public void setService(String service) {
		this.service = service;
	}
	public String getMerchid() {
		return merchid;
	}
	public void setMerchid(String merchid) {
		this.merchid = merchid;
	}
	public String getSecret() {
		return secret;
	}
	public void setSecret(String secret) {
		this.secret = secret;
	}
}
