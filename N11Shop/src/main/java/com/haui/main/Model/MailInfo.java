package com.haui.main.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MailInfo {
	String from;
	String to;
	String[] cc;
	String[] bcc;
	String subject;
	String body;
	String[] attachment;
	public MailInfo(String to, String subject, String body) {
		this.from = "Nhom 11 Shop Quan ao <lemanhcuong0406@gmail.com>";
		this.to = to;
		this.subject = subject;
		this.body = body;
	}
}
