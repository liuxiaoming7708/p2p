package com.payease.p2p.base.event;

import lombok.Getter;

import org.springframework.context.ApplicationEvent;

import com.payease.p2p.base.domain.RealAuth;

/**
 * 实名认证审核通过的消息(事件)
 * 
 * @author Administrator
 * 
 */
@Getter
public class RealAuthSuccessEvent extends ApplicationEvent {

	/**
	 * 事件关联的对象
	 */
	private RealAuth realAuth;

	public RealAuthSuccessEvent(Object source, RealAuth realAuth) {
		super(source);
		this.realAuth = realAuth;
	}

}
