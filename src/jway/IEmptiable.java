package com.jway;

/**
 * 
 * @author jhson
 *
 * <pre>
 * 메모리 누수 방지를 위해
 * 객체 내부 멤버 변수들을 비우는
 * empty 메서드를 이용할 필요가 있는
 * 객체를 위한 인터페이스.
 * </pre>
 */
public interface IEmptiable {
	/**
	 * 
	 * <pre>
	 * 내부의 모든 멤버들을 비운다.
	 * </pre>
	 */
	public void empty();
}
