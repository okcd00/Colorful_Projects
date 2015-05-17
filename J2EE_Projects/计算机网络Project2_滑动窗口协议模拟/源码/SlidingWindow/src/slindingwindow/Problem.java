package slindingwindow;
import java.io.Serializable;
public class Problem implements Serializable{
	//定义信道中是否出现问题，以及问题的种类
		enum problemType{noProblem,lost,wrong,delay};
}
