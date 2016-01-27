var tmp = [];
for(var i = 1; i <= 100; i++){
	tmp.push(i);
}
var x_Axis = tmp;
var y_Axis = tmp;

for(var i = 0; i < Arr_size[0]; i++){
    for(var j = 0; j < 15; j++){
        var tstr = '<a href="https://www.baidu.com/s?wd=####" class="color" target="_blank" >####</a>';
        online[i][j] = tstr.replace(/####/g, online[i][j]);
    }
}

for(var i = 0; i < Arr_size[1]; i++){
    for(var j = 0; j < 15; j++){
        var tstr = '<a href="https://www.baidu.com/s?wd=####" class="color" target="_blank" >####</a>';
        offline[i][j] = tstr.replace(/####/g, offline[i][j]);
    }
}

w = w.map(function (item) {
	return [item[1], item[0], item[2] || '-'];
});

p = p.map(function (item) {
	return [item[1], item[0], item[2] || '-'];
});

lift = lift.map(function (item) {
	return [item[1], item[0], item[2] || '-'];
});

v = v.map(function (item) {
	return [item[1], item[0], item[2] || '-'];
});

xValue = xValue.map(function (item) {
	return [item[1], item[0], item[2] || '-'];
});

yValue = yValue.map(function (item) {
	return [item[1], item[0], item[2] || '-'];
});

zValue = zValue.map(function (item) {
	return [item[1], item[0], item[2] || '-'];
});