package com.github.liyibo1110.stable.elephant.handler;

import java.util.List;
import java.util.Map;

import com.github.liyibo1110.stable.elephant.entity.ColumnsInfo;

/**
 * 查询原始数据后，后续处理基础接口
 * @author liyibo
 *
 * @param <T>
 */
public interface AfterHandler {

	public boolean handler(List<Map<String, Object>> datas, ColumnsInfo columnsInfo);
}
