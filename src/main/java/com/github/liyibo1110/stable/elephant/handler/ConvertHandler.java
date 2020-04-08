package com.github.liyibo1110.stable.elephant.handler;

/**
 * 列迁移处理器基础接口
 * @author liyibo
 *
 * @param <T>
 */
public interface ConvertHandler<T> {

	public T handler(T value);
}
