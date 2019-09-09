package org.gobiiproject.gobiimodel.config;

import org.gobiiproject.gobiimodel.security.Decrypter;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Text;

public class ConfigEncrypt {
	@Text
	private String value;
	@Attribute(required=false)
	private boolean encrypt;
	@Attribute(required=false)
	private String scramble;

	public String getValue(){
		if(!encrypt){
			return value;
		}
		return Decrypter.decrypt(value,scramble);
	}
	public ConfigEncrypt(String value){
		this(value,false);
	}
	public ConfigEncrypt(String value, boolean encrypt){
		this(value,encrypt,null);
	}
	public ConfigEncrypt(String value, boolean encrypt, String scramble){
		this.value=value;
		this.encrypt=encrypt;
		this.scramble=scramble;
	}
}