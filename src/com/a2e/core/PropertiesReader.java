package com.a2e.core;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class PropertiesReader {

	/*
	 * Your AWS Access Key ID, as taken from the AWS Your Account page.
	 */
	private static final String AWS_ACCESS_KEY_ID = "AWS_ACCESS_KEY_ID";

	/*
	 * Your AWS Secret Key corresponding to the above ID, as taken from the AWS
	 * Your Account page.
	 */
	private static final String AWS_SECRET_KEY = "AWS_SECRET_KEY";

	/**
	 * Your AWS associate tag
	 */
	private static final String AWS_ASSOCIATE_TAG = "AWS_ASSOCIATE_TAG";

	final String accessKeyId;
	final String secretKey;
	final String associateTag;

	private static PropertiesReader KetProps = null;
	private static boolean firstThread = true;

	protected PropertiesReader() {
//		Properties props;
//		try {
//			props = new Properties();
//			props.load(new FileReader(new File("my.properties")));
//		} catch (FileNotFoundException e) {
//			throw new RuntimeException(e);
//		} catch (IOException e) {
//			throw new RuntimeException(e);
//		}

		accessKeyId = "AKIAJ4XXU2E4XE45XRTA"; //props.getProperty(AWS_ACCESS_KEY_ID);
		secretKey = "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";//props.getProperty(AWS_SECRET_KEY);
		associateTag ="myeye-21";// props.getProperty(AWS_ASSOCIATE_TAG);
	}

	public String getAccessKeyId() {
		return accessKeyId;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public String getAssociateTag() {
		return associateTag;
	}

	public static PropertiesReader getInstance() {
		if (KetProps == null) {
			KetProps = new PropertiesReader();

		}
		return KetProps;
	}

}
