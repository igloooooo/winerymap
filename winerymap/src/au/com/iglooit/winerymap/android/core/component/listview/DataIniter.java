package au.com.iglooit.winerymap.android.core.component.listview;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataIniter {

	public static List<Map<String, Object>> initValue(int pageStart,
			int pageSize) {
		Map<String, Object> map;
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < pageSize; i++) {
			map = new HashMap<String, Object>();
			map.put("title", "标题" + pageStart);
			map.put("subtext", "内容" + pageStart);
			++pageStart;
			list.add(map);
		}
		return list;
	}

	public static List<Map<String, Object>> initValue(int pageStart,
			int pageSize, List<Map<String, Object>> data) {
		Map<String, Object> map;
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < pageSize; i++) {
			map = new HashMap<String, Object>();
			map.put("title", "标题" + pageStart);
			map.put("subtext", "内容" + pageStart);
			++pageStart;
			list.add(map);
		}
		return list;
	}
}