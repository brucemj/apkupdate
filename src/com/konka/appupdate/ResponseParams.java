package com.konka.appupdate;

/**
 * ���񷵻�ֵʵ��
 * @author jan
 *
 */
public class ResponseParams 
{
	private String ResponseCode;//������ ��0000�ɹ���0001ʧ�ܣ�0003�������쳣��0004��������
	private String ResponseMessage;//������Ϣ
	private ResponseResult ResponseResult;//���ؽ��
	public ResponseResult getResponseResult() {
		return ResponseResult;
	}
	public void setResponseResult(ResponseResult responseResult) {
		ResponseResult = responseResult;
	}
	public String getResponseCode() {
		return ResponseCode;
	}
	public void setResponseCode(String responseCode) {
		ResponseCode = responseCode;
	}
	public String getResponseMessage() {
		return ResponseMessage;
	}
	public void setResponseMessage(String responseMessage) {
		ResponseMessage = responseMessage;
	}

}
