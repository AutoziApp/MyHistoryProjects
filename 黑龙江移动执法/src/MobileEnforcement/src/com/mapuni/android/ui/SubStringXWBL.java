package com.mapuni.android.ui;

public class SubStringXWBL {
	
	/**
	 * @aut �μѺ� ��װ��ȡѯ�ʱ�¼ѯ�������ƺ�ִ��֤��
	 * @param SurveyPeopleNamestr
	 * @param SurveyPeopleCradCodestr
	 * @return
	 */

	
	public static String SubStringSurveyPeople(String SurveyPeopleNamestr,String SurveyPeopleCradCodestr){
		try {
			String[] SurveyPeopleName =SurveyPeopleNamestr.split(",");//username
			String[] SurveyPeopleCradCode =SurveyPeopleCradCodestr.split(",");//ִ��֤��
			StringBuilder peoBuilder = new StringBuilder();
			
				if (SurveyPeopleName.length > 0 && !SurveyPeopleName.equals("")) {
					for (int i = 0; i < SurveyPeopleName.length; i++) {
						peoBuilder.append(SurveyPeopleName[i]);
					       
						if(i<SurveyPeopleCradCode.length){
							peoBuilder.append("(");
							peoBuilder.append(SurveyPeopleCradCode[i]);
							peoBuilder.append(")��");
						}else{
							peoBuilder.append("()��");
						}
					}
			
					String peoString = peoBuilder.substring(0, peoBuilder.length() - 1);
					System.out.println("if"+peoString);;
					return peoString;
				} else {
					return "";
					
				}
				
		
		} catch (Exception e) {
			return "";
		}
		
	}
	/**
	 * @aut �μѺ� ��ȡִ��֤��
	 * @param SurveyPeopleNamestr
	 * @param SurveyPeopleCradCodestr
	 * @return
	 */
	
	public static String SubStringSurveyPeopleCradCode(String SurveyPeopleNamestr,String SurveyPeopleCradCodestr){
		String[] SurveyPeopleName =SurveyPeopleNamestr.split(",");//username
		String[] SurveyPeopleCradCode =SurveyPeopleCradCodestr.split(",");//ִ��֤��
		StringBuilder peoBuilder = new StringBuilder();
		try {
			if (SurveyPeopleName.length > 0 && !SurveyPeopleName.equals("")) {
				for (int i = 0; i < SurveyPeopleName.length; i++) {      
					if(i<SurveyPeopleCradCode.length){
						peoBuilder.append("(");
						peoBuilder.append(SurveyPeopleCradCode[i]);
						peoBuilder.append(")��");
					}else{
						peoBuilder.append("()��");
					}
				}
		
				String peoString = peoBuilder.substring(0, peoBuilder.length() - 1);
				System.out.println("if"+peoString);;
				return peoString;
			} else {
				return "";
				
			}
			
		
		} catch (Exception e) {
			return "";
		}
		
	}
	
	/**
	 * @aut �μѺ� ��װ��ȡѯ�ʱ�¼ѯ��������
	 * @param SurveyPeopleNamestr
	 * @param SurveyPeopleCradCodestr
	 * @return
	 */

	
	public static String SubStringSurveyPeopleName(String SurveyPeopleNamestr){
		try {
			String[] SurveyPeopleName =SurveyPeopleNamestr.split(",");//username
			StringBuilder peoBuilder = new StringBuilder();
			
				if (SurveyPeopleName.length > 0 && !SurveyPeopleName.equals("")) {
					for (int i = 0; i < SurveyPeopleName.length; i++) {
						peoBuilder.append(SurveyPeopleName[i]);
						peoBuilder.append("��");       
					}
			
					String peoString = peoBuilder.substring(0, peoBuilder.length() - 1);
					System.out.println("if"+peoString);;
					return peoString;
				} else {
					return "";
					
				}
				
		
		} catch (Exception e) {
			return "";
		}
		
	}

}
