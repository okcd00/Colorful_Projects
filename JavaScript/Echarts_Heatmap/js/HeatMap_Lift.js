option = {
	title: {
		text: 'Figure Lift(i,j)'
	},
	tooltip: {
		position: 'right',
		triggerOn: 'mousemove',
		enterable: true,
		formatter:function (params,ticket,callback) {
		    var str = params.value;
		    var x = parseInt(str[0]) + 1;
		    var y = parseInt(str[1]) + 1;
		    var v = str[2];
		    var onstr = "";
        var res = '[Information]'+'<br/>';
        res+='Position:('+ x + ',' + y + ')' + ' Value: ' + v +'<br/>';
        res+='online: '+ online[y-1] +'<br/>';
        res+='offline: '+ offline[x-1] +'<br/>';
        setTimeout(function (){callback(ticket, res);}, 200);
        return 'loading...';
        }
	},
	animation: true,
	grid: {
	    bottom: '20%',
	    left: '10%',
		//height: '60%',
		y: '10%',
	},
	xAxis: {
		//position：'top',
		type: 'category',
		fontSize: 20,
		data: x_Axis
	},
	yAxis: {
		type: 'category',
		inverse: true,
		fontSize: 20,
		data: y_Axis
	},
	visualMap: {
		min: range[4],
		max: range[5],
		calculable: true,
		precision: 4,
		orient: 'horizontal',
		left: 'center',
		bottom: '5%'
	},
	series: [{
		name: 'Info',
		type: 'heatmap',
		data: lift,
		label: {
			normal: {
				show: false
			}
		},
		itemStyle: {
			emphasis: {
				shadowBlur: 20,
				shadowColor: 'rgba(0, 0, 0, 0.5)'
			}
		}
	}]
};