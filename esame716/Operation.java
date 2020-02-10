public class Operation{
	private String op;
	private boolean opset;
	private double op1;
	private boolean op1set;
	private double op2;
	private boolean op2set;
	
	public Operation(){
		op = null;
		op1 = 0.0;
		op2 = 0.0;
		opset=false;
		op1set=false;
		op2set=false;
	}
	
	public void reset(){
		op = null;
		op1 = 0.0;
		op2 = 0.0;
		opset=false;
		op1set=false;
		op2set=false;
	}
	
	public boolean setOp(String ope){
		if(checkOp(ope)){
			this.op=ope;
			opset=true;
		}
			
		return checkOp(ope);
	}
	
	private boolean checkOp(String ope){
		return (ope.equals("+") || ope.equals("-") || ope.equals("*") || ope.equals("/"));
	}
	
	public void setOp1(double value){
		op1=value;
		op1set=true;
	}
	
	public boolean setOp2(double value){
		if(this.op.equals("/") && value==0.0)
			return false;
		op2=value;
		op2set=true;
		return true;
	}
	
	public boolean issetOp(){
		return opset;
	}
	public boolean issetOp1(){
		return op1set;
	}
	public boolean issetOp2(){
		return op2set;
	}
	
	public double calculate(){
		double value;
		switch(op){
			case "+": value=op1+op2; break;
			case "-": value=op1-op2; break;
			case "*": value=op1*op2; break;
			case "/": value=op1/op2; break;
			default: value = 0.0;
		}
		return value;
	}
}
