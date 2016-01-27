option = {
	title: {
		text: 'Figure Y(i)',
	},
	tooltip: {
		position: 'right',
		triggerOn: 'mousemove',
		enterable: true,
		alwaysShowContent: false,
		hideDelay: 500,
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
	    left: '25%',
		y: '10%',
	},
	xAxis: {
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
		min: range[10],
		max: range[11],
		calculable: false,
		show: false,
    },
	series: [{
		name: 'Info',
		type: 'heatmap',
		data: yValue,
		label: {
			normal: {
				show: true
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