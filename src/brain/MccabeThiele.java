package brain;

import java.util.ArrayList;
import java.util.List;

import thermodynamicsModel.GammaModel;
import thermodynamicsModel.RaoultLaw;
import thermodynamicsModel.VaporPressureModel;

public class MccabeThiele {
	public static final Integer maximumPlates = 100; 
	private Double externalPressure = 760.0;
	private Double xd;
	private Double xb;
	private Double z;
	private Double q;
	private Double r;

	private VaporPressureModel vpm1;
	private VaporPressureModel vpm2;
	private GammaModel gm;

	private QLine qLine;
	private Line rectifyingLine;
	private Line strippingLine;
	//Constructor
	public MccabeThiele(Double externalPressure, Double xd, Double xb, Double z, Double q, Double r, VaporPressureModel vpm1,
			VaporPressureModel vpm2, GammaModel gm) {
		this.externalPressure = externalPressure;
		this.xd = xd;
		this.xb = xb;
		this.z = z;
		this.q = q;
		this.r = r;

		this.vpm1 = vpm1;
		this.vpm2 = vpm2;
		this.gm = gm;
		this.qLine = new QLine(q, z);
		// RectifyingLine
		Double rMin = this.rMin();
		this.rectifyingLine = new Line(r * rMin / (r * rMin + 1), xd / (r * rMin + 1));
		Double xTriple = qLine.intersection(rectifyingLine);// Triple intersection between qline and operational lines
		Double yTriple = rectifyingLine.y(xTriple);
		// StrippingLine
		Double strippingAlpha = (yTriple - xb) / (xTriple - xb);
		Double strippingBetha = yTriple - strippingAlpha * xTriple;
		this.strippingLine = new Line(strippingAlpha, strippingBetha);
	}	
	//Methods
	public Double rMin() {
		Double xi = qLine.intersectionEquilibriumLine(vpm1, vpm2, gm, externalPressure);
		Double yi = RaoultLaw.iterativeY(xi,vpm1, vpm2, gm, externalPressure);
		Double rMin = ((yi - xd) / (xi - xd)) / (1 - ((yi - xd) / (xi - xd)));
		return rMin;
	}
	public boolean testR() {
		double x1 = rectifyingLine.intersection(qLine);
		if(!(this.q == 1)) {
			if(this.rectifyingLine.nonElementarIntersectionBellow(x1, xd, gm, vpm1, vpm2, externalPressure)) {
				return true;//There is a problem
			}
			return false;//All right
		}else {
			if(this.plateList().size() >= maximumPlates) {
				return true;//There is a problem
			}
			return false;//All right
		}
	}
	public boolean testAzeotropeXb() {
		//tests if both xb and z are simultaneously above or bellow Xazeotrope
		Double Xaz = RaoultLaw.azeotropePoint(vpm1, vpm2, gm, externalPressure);
		if(!(RaoultLaw.mostVolatileComponent(z, vpm1, vpm2, gm, Xaz) == RaoultLaw.mostVolatileComponent(xb, vpm1, vpm2, gm, Xaz))) {
			return false;// all right
		}else {
			return true;//z and xb are on different regions around azeotropic point
		}
	}
	public boolean testAzeotropeXd() {
		//tests if both xd and z are simultaneously above or bellow Xazeotrope
		Double Xaz = RaoultLaw.azeotropePoint(vpm1, vpm2, gm, externalPressure);
		if(!(RaoultLaw.mostVolatileComponent(z, vpm1, vpm2, gm, Xaz) == RaoultLaw.mostVolatileComponent(xd, vpm1, vpm2, gm, Xaz))) {
			return false;// all right
		}else {
			return true;//z and xd are on different regions around azeotropic point
		}
	}
	public Double azeotropicPoint() {
		return RaoultLaw.azeotropePoint(vpm1, vpm2, gm, externalPressure);
	}
	public Integer mostVolatileOnFeed() {
		return RaoultLaw.mostVolatileComponent(z, vpm1, vpm2, gm, externalPressure);
	}
	//Next: implement Nmin method, returning the exact value and a overload plateList returning Nmin plateList;
	public List<Plate> plateList(){
		List<Plate> plateList = new ArrayList<>();
		//First stage
		Double currentY = xd;
		Line horizontalLine = new Line(0.0, currentY);
		Double currentX = horizontalLine.nonElementarIntersection(gm, vpm1, vpm2, externalPressure);
		Double currentT = RaoultLaw.iterativeY_T(currentX, vpm1, vpm2, gm, externalPressure);
		int i = 1;
		Plate currentPlate = new Plate(i, currentX, currentY, currentT);
		plateList.add(currentPlate);
		while(currentX > xb && i < maximumPlates) {
			if(!rectifyingLine.compareTo(strippingLine, currentX)) {
				currentY = rectifyingLine.y(currentX);
			} else {
				currentY = strippingLine.y(currentX);
			}
			horizontalLine = new Line(0.0, currentY);
			currentX = horizontalLine.nonElementarIntersection(gm, vpm1, vpm2, externalPressure);
			currentT = RaoultLaw.iterativeY_T(currentX, vpm1, vpm2, gm, externalPressure);
			i++;
			currentPlate = new Plate(i, currentX, currentY, currentT);
			plateList.add(currentPlate);
		}
		return plateList; 
	}
	
	//Getters
	public Double getExternalPressure() {
		return externalPressure;
	}

	public Double getXd() {
		return xd;
	}

	public Double getXb() {
		return xb;
	}

	public Double getZ() {
		return z;
	}

	public Double getQ() {
		return q;
	}

	public Double getR() {
		return r;
	}

	public VaporPressureModel getVpm1() {
		return vpm1;
	}

	public VaporPressureModel getVpm2() {
		return vpm2;
	}

	public GammaModel getGm() {
		return gm;
	}

	public QLine getqLine() {
		return qLine;
	}

	public Line getRectifyingLine() {
		return rectifyingLine;
	}

	public Line getStrippingLine() {
		return strippingLine;
	}
	public void setR(Double newR) {
		Double rMin = this.rMin();
		this.r = newR;
		this.rectifyingLine = new Line(r * rMin / (r * rMin + 1), xd / (r * rMin + 1));
		Double xTriple = qLine.intersection(rectifyingLine);// Triple intersection between qline and operational lines
		Double yTriple = rectifyingLine.y(xTriple);
		// StrippingLine
		Double strippingAlpha = (yTriple - xb) / (xTriple - xb);
		Double strippingBetha = yTriple - strippingAlpha * xTriple;
		this.strippingLine = new Line(strippingAlpha, strippingBetha);
	}
	public String toString(String substance) {
		StringBuilder sb = new StringBuilder();
		///sb.append("Project Variables:\n\nZf = "+z+"\nXd = "+xd+"\nXb = "+xb+"\nExternal pressure = "+externalPressure+" atm\nq fator = "+q+"");
		List<Plate> plateList = new ArrayList<>();
		plateList = this.plateList();
		if(plateList.size() == maximumPlates) {
			sb.append("\nWARNING! THIS APPLICATION COUNTS AND DRAWS UP TO "+maximumPlates+"th EQUILIBRIUM STAGE ONLY.\n(FOR DEFENSIVE PROGRAMMING PURPOSES)\n");
		}
		sb.append("Plate List:\nX -> "+substance+" fraction on liquid phase\nY -> "+substance+" fraction on vapor phase\nT -> Temperature on current plate\n(Plate counting starts at the top plate)\n");
		for(Plate p : plateList) {
			sb.append("\n");
			sb.append(p);
		}
		return sb.toString();
	}
}
