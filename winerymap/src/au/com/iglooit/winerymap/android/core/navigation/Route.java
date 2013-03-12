package au.com.iglooit.winerymap.android.core.navigation;
import java.util.List;

/**
 * goolge direction api
 * @author Administrator
 *
 */
public class Route {
	private String copyrights;
    private Bounds bounds;
    private Polyline overview_polyline;
    private String summary;
    private List<String> warnings;
    private List<String> waypoint_order;
	private List<Leg> legs;
	
	
    
	public String getCopyrights() {
		return copyrights;
	}
	public void setCopyrights(String copyrights) {
		this.copyrights = copyrights;
	}

	public Bounds getBounds() {
		return bounds;
	}
	public void setBounds(Bounds bounds) {
		this.bounds = bounds;
	}
    

	public Polyline getOverview_polyline() {
		return overview_polyline;
	}
	public void setOverview_polyline(Polyline overview_polyline) {
		this.overview_polyline = overview_polyline;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public List<String> getWarnings() {
		return warnings;
	}
	public void setWarnings(List<String> warnings) {
		this.warnings = warnings;
	}
	public List<String> getWaypoint_order() {
		return waypoint_order;
	}
	public void setWaypoint_order(List<String> waypoint_order) {
		this.waypoint_order = waypoint_order;
	}
	public List<Leg> getLegs() {
		return legs;
	}
	public void setLegs(List<Leg> legs) {
		this.legs = legs;
	}


	class GeoPointChar {
		private String lng;
		private String lat;
		public String getLng() {
			return lng;
		}
		public void setLng(String lng) {
			this.lng = lng;
		}
		public String getLat() {
			return lat;
		}
		public void setLat(String lat) {
			this.lat = lat;
		}		
	}
	
	
	class TextValue {
		private String text;
		private int value;
		public String getText() {
			return text;
		}
		public void setText(String text) {
			this.text = text;
		}
		public int getValue() {
			return value;
		}
		public void setValue(int value) {
			this.value = value;
		}
		
		
	}
	
	public class Polyline {
		private String points;

		public String getPoints() {
			return points;
		}

		public void setPoints(String points) {
			this.points = points;
		}
		
	}
	
	public class Bounds {
	    private GeoPointChar northeast;
	    private GeoPointChar southwest;
		public GeoPointChar getNortheast() {
			return northeast;
		}
		public void setNortheast(GeoPointChar northeast) {
			this.northeast = northeast;
		}
		public GeoPointChar getSouthwest() {
			return southwest;
		}
		public void setSouthwest(GeoPointChar southwest) {
			this.southwest = southwest;
		}
	    
	}
	
	class BaseSetp {
		protected  TextValue distance;
		protected  TextValue duration;
		protected GeoPointChar end_location;
		protected GeoPointChar start_location;
		public TextValue getDistance() {
			return distance;
		}
		public void setDistance(TextValue distance) {
			this.distance = distance;
		}
		public TextValue getDuration() {
			return duration;
		}
		public void setDuration(TextValue duration) {
			this.duration = duration;
		}
		public GeoPointChar getEnd_location() {
			return end_location;
		}
		public void setEnd_location(GeoPointChar end_location) {
			this.end_location = end_location;
		}
		public GeoPointChar getStart_location() {
			return start_location;
		}
		public void setStart_location(GeoPointChar start_location) {
			this.start_location = start_location;
		}
		
		
	}
	
	class Setp extends BaseSetp {
		private String html_instructions;
		private String travel_mode;
		private Polyline polyline;
		public String getHtml_instructions() {
			return html_instructions;
		}
		public void setHtml_instructions(String html_instructions) {
			this.html_instructions = html_instructions;
		}
		public String getTravel_mode() {
			return travel_mode;
		}
		public void setTravel_mode(String travel_mode) {
			this.travel_mode = travel_mode;
		}
		public Polyline getPolyline() {
			return polyline;
		}
		public void setPolyline(Polyline polyline) {
			this.polyline = polyline;
		}
		
		
	}
	

	
	class Leg extends BaseSetp {
		private String end_address;
		private String start_address;
		private List<String> via_waypoint;
		private List<Setp> steps;
		public String getEnd_address() {
			return end_address;
		}
		public void setEnd_address(String end_address) {
			this.end_address = end_address;
		}
		public String getStart_address() {
			return start_address;
		}
		public void setStart_address(String start_address) {
			this.start_address = start_address;
		}
		public List<String> getVia_waypoint() {
			return via_waypoint;
		}
		public void setVia_waypoint(List<String> via_waypoint) {
			this.via_waypoint = via_waypoint;
		}
		public List<Setp> getSteps() {
			return steps;
		}
		public void setSteps(List<Setp> steps) {
			this.steps = steps;
		}
        
		
		
	}
	
	
}
