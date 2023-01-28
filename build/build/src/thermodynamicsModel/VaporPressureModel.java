package thermodynamicsModel;

public interface VaporPressureModel {
	
	public Double temperature(Double pressure);
	public Double pressure(Double temperature);
}
