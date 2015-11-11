package cn.cd.monster;
import cn.cd.constant.GConstant;
import cn.liwang.monster.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class SetGame extends Activity implements OnClickListener,OnCheckedChangeListener{
	private ImageView onOff;
	private RadioGroup radiog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.set_gamex);
		onOff=(ImageView)findViewById(R.id.on_off);
		radiog=(RadioGroup)findViewById(R.id.radioSet);
		onOff.setOnClickListener(this);	
		radiog.setOnCheckedChangeListener(this);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(GConstant.onOffFlag){
			onOff.setBackgroundResource(R.drawable.off);
			GConstant.onOffFlag=false;
		}else{
			onOff.setBackgroundResource(R.drawable.on);
			GConstant.onOffFlag=true;
		}
	}
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub
		if(checkedId==R.id.normalSet){
			GConstant.normalOrRandom=true;
		}else{
			GConstant.normalOrRandom=false;
		}
	}
}
