package com.konka.appupdate;

/**
 * 服务返回值实体
 * @author jan
 *
 */
public class ResponseParams 
{
	private String ResponseCode;//返回码 ：0000成功，0001失败，0003服务器异常，0004参数错误
	private String ResponseMessage;//返回消息
	private ResponseResult ResponseResult;//返回结果
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
