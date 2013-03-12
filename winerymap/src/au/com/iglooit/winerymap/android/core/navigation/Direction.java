package au.com.iglooit.winerymap.android.core.navigation;

import java.util.List;

/**
 *
 * @author Administrator
 *
 */
public class Direction {
	private List<Route> routes;
	private String status;
	public List<Route> getRoutes() {
		return routes;
	}
	public void setRoutes(List<Route> routes) {
		this.routes = routes;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
}
